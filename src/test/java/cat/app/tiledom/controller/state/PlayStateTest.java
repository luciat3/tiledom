package cat.app.tiledom.controller.state;

import cat.app.tiledom.model.Board;
import cat.app.tiledom.model.GameSession;
import cat.app.tiledom.GUI.AudioManager;
import cat.app.tiledom.GUI.UIManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlayStateTest {
    
    // Com que encara no hem implementat GUI ni Game Session, farem servir mocks
    // Comprovem que els estats funcionen correctament
    // (no caldria ja que se'ns han donat fets)

    @Test
    void configureSwitchesToConfigureState() {
        AudioManager audio = mock(AudioManager.class);
        UIManager ui = mock(UIManager.class);
        GameSession session = mock(GameSession.class);

        PlayState state = new PlayState(audio, ui, session);

        GameState next = state.configure();

        verify(ui).showScreen("configuration");
        verify(audio).stopMusic();
        assertTrue(next instanceof ConfigureState);
    }

    @Test
    void pauseSwitchesToPauseState() {
        AudioManager audio = mock(AudioManager.class);
        UIManager ui = mock(UIManager.class);
        GameSession session = mock(GameSession.class);

        PlayState state = new PlayState(audio, ui, session);

        GameState next = state.pause();

        verify(ui).showScreen("paused");
        verify(audio).stopMusic();
        assertTrue(next instanceof PauseState);
    }

    @Test
    void restartResetsGameWhenConfirmed() {
        AudioManager audio = mock(AudioManager.class);
        UIManager ui = mock(UIManager.class);
        GameSession session = mock(GameSession.class);
        when(ui.showDialog("restart")).thenReturn(true);

        PlayState state = new PlayState(audio, ui, session);
        GameState next = state.restart();

        verify(audio).stopMusic();
        verify(audio).startMusic("initial_track");
        verify(ui).showScreen("init");
        assertTrue(next instanceof InitState);
    }

    @Test
    void restartCancelsAndStaysWhenDeclined() {
        AudioManager audio = mock(AudioManager.class);
        UIManager ui = mock(UIManager.class);
        GameSession session = mock(GameSession.class);
        when(ui.showDialog("restart")).thenReturn(false);

        PlayState state = new PlayState(audio, ui, session);
        GameState next = state.restart();

        verify(audio).stopMusic();
        verify(audio).startMusic("play_track");
        assertSame(state, next);
    }
    
    @Test
    void handleMoveUpdatesScoreAndLevelWhenBoardCleared() {
        AudioManager audio = mock(AudioManager.class);
        UIManager ui = mock(UIManager.class);
        GameSession session = spy(new GameSession());
        Board board = mock(Board.class);
        // crearem la funció tryMatch per comprovar si dues peces es poden eliminar, enviant dues posicions (x,y)
        when(board.tryMatch(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(true);
        // funció isEmpty per determinar si queden peces
        when(board.isEmpty()).thenReturn(true);
        // setBoard per implementar el taulell inicialitzat a cada partida
        session.setBoard(board);

        PlayState state = new PlayState(audio, ui, session);
        // augmenta la puntació si es pot fer la jugada
        state.handleMove(0, 0, 1, 1);

        verify(ui).showDialog("level_completed");
        assertTrue(session.getScore() > 0);
    }


    // Millora dels tests
    // ----------------------- CAIXA BLANCA -----------------------
    private AudioManager audio;
    private UIManager ui;
    private GameSession gameSession;
    private Board board;
    private PlayState playState;

    @BeforeEach
    void setUp() {
        audio = mock(AudioManager.class);
        ui = mock(UIManager.class);
        gameSession = mock(GameSession.class);
        board = mock(Board.class);

        when(gameSession.getBoard()).thenReturn(board);

        playState = new PlayState(audio, ui, gameSession);
    }
    
    // moviment vàlid, el taulell queda buit -> "level_completed"
    @Test
    void handleMove_MatchedAndBoardEmpty_ShowsLevelCompleted() {
        //jugada vàlida
        when(board.tryMatch(0, 0, 0, 1)).thenReturn(true);
        when(board.isEmpty()).thenReturn(true);
        when(gameSession.getScore()).thenReturn(100);

        playState.handleMove(0, 0, 0, 1);

        //comprovacions de puntuació
        verify(gameSession).addScore(100);
        verify(ui).updateScore(100);
        verify(ui).showDialog("level_completed");
        verify(board, never()).hasAvailableMoves();
        verify(audio, never()).stopMusic();
        verify(audio, never()).startMusic("game_over");
    }

    // moviment vàlid, no queden moviments -> derrota
    @Test
    void handleMove_MatchedAndNoMovesLeft_ShowsGameOver() {
        when(board.tryMatch(1, 1, 1, 2)).thenReturn(true);
        when(board.isEmpty()).thenReturn(false);
        when(board.hasAvailableMoves()).thenReturn(false);
        when(gameSession.getScore()).thenReturn(200);

        playState.handleMove(1, 1, 1, 2);

        //comprovacions de puntuació
        verify(gameSession).addScore(100);
        verify(ui).updateScore(200);
        verify(audio).stopMusic();
        verify(ui).showDialog("no_moves_left");
        verify(audio).startMusic("game_over");
        verify(ui, never()).showDialog("level_completed");
    }

    // moviment invàlid -> so d'error i diàleg "invalid_move"
    @Test
    void handleMove_NotMatched_PlaysErrorAndShowsInvalidMove() {
        when(board.tryMatch(0, 0, 0, 1)).thenReturn(false);
        playState.handleMove(0, 0, 0, 1);

        // no s'ha d'actualitzar la puntuació
        verify(gameSession, never()).addScore(anyInt());
        verify(ui, never()).updateScore(anyInt());
        verify(audio).startMusic("error_sound");
        verify(ui).showDialog("invalid_move");
        verify(board, never()).isEmpty();
        verify(board, never()).hasAvailableMoves();
    }

    // moviment exit: usuari cancela la sortida -> es queda al mateix estat
    @Test
    void exitCancelledTestPlayState() {
        when(ui.showDialog("exiting")).thenReturn(false);
        GameState next = playState.exit();
        // s'ha d'aturar la música actual
        verify(audio).stopMusic();
        // s'ha de mostrar el diàleg de sortida
        verify(ui).showDialog("exiting");
        // tornem a la música de joc
        verify(audio).startMusic("play_track");
        assertSame(playState, next);
    }

    @Test
    void resume_ReturnsSameInstance() {
        GameState next = playState.resume();
        assertSame(playState, next, "resume() ha de retornar el mateix PlayState");
    }

    @Test
    void start_ReturnsSameInstance() {
        GameState next = playState.start();
        assertSame(playState, next, "start() ha de retornar el mateix PlayState");
    }
}
