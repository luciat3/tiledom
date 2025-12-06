package cat.app.tiledom.controller.state;

import cat.app.tiledom.GUI.AudioManager;
import cat.app.tiledom.GUI.UIManager;
import cat.app.tiledom.model.GameSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConfigureStateTest {
    
    private AudioManager audio;
    private UIManager ui;
    private GameSession session;
    private ConfigureState configureState;

    @BeforeEach
    void setUp() {
        audio = mock(AudioManager.class);
        ui = mock(UIManager.class);
        session = mock(GameSession.class);

        configureState = new ConfigureState(audio, ui, session);
    }

    // -------------- CAIXA BLANCA -------------------

    @Test
    void start_ReturnsSameInstance() {
        GameState next = configureState.start();
        assertSame(configureState, next, "start() ha de retornar el mateix ConfigureState");
    }

    @Test
    void restart_ReturnsSameInstance() {
        GameState next = configureState.restart();
        assertSame(configureState, next, "restart() ha de retornar el mateix ConfigureState");
    }

    @Test
    void pause_ReturnsSameInstance() {
        GameState next = configureState.pause();
        assertSame(configureState, next, "pause() ha de retornar el mateix ConfigureState");
    }

    @Test
    void configure_ReturnsSameInstance() {
        GameState next = configureState.configure();
        assertSame(configureState, next, "configure() ha de retornar el mateix ConfigureState");
    }

    @Test
    void exit_ReturnsSameInstance() {
        GameState next = configureState.exit();
        assertSame(configureState, next, "exit() ha de retornar el mateix ConfigureState");
    }

    @Test
    void resume_ShowsPlayingScreenAndReturnsPlayState() {
        GameState next = configureState.resume();

        // Efectes sobre la UI i l'àudio
        verify(ui).showScreen("playing");
        verify(audio).startMusic("play_track");
        assertTrue(next instanceof PlayState, "resume() ha de retornar un nou PlayState");
    }
}
