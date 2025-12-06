package cat.app.tiledom.controller.state;

import cat.app.tiledom.GUI.AudioManager;
import cat.app.tiledom.GUI.UIManager;
import cat.app.tiledom.model.GameSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PauseStateTest {
    private AudioManager audio;
    private UIManager ui;
    private GameSession session;
    private PauseState pauseState;

    @BeforeEach
    void setUp() {
        audio = mock(AudioManager.class);
        ui = mock(UIManager.class);
        session = mock(GameSession.class);

        pauseState = new PauseState(audio, ui, session);
    }

    // ---------------- CAIXA BLANCA ---------------

    @Test
    void configure_ReturnsSameInstance() {
        GameState next = pauseState.configure();
        assertSame(pauseState, next, "configure() ha de retornar el mateix PauseState");
    }

    @Test
    void exit_ReturnsSameInstance() {
        GameState next = pauseState.exit();
        assertSame(pauseState, next, "exit() ha de retornar el mateix PauseState");
    }

    @Test
    void pause_ReturnsSameInstance() {
        GameState next = pauseState.pause();
        assertSame(pauseState, next, "pause() ha de retornar el mateix PauseState");
    }

    @Test
    void restart_ReturnsSameInstance() {
        GameState next = pauseState.restart();
        assertSame(pauseState, next, "restart() ha de retornar el mateix PauseState");
    }

    @Test
    void start_ReturnsSameInstance() {
        GameState next = pauseState.start();
        assertSame(pauseState, next, "start() ha de retornar el mateix PauseState");
    }

    @Test
    void resume_ShowsPlayingScreenAndReturnsPlayState() {
        GameState next = pauseState.resume();

        // comprovar resultats sobre la UI i l'àudio
        verify(ui).showScreen("playing");
        verify(audio).startMusic("play_track");
        assertTrue(next instanceof PlayState, "resume() ha de retornar un nou PlayState");
    }    
}
