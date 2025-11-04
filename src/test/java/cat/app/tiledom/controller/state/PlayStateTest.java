package cat.app.tiledom.controller.state;

import cat.app.tiledom.model.Board;
import cat.app.tiledom.model.GameSession;
import cat.app.tiledom.GUI.AudioManager;
import cat.app.tiledom.GUI.UIManager;
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

        verify(session).reset();
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

}
