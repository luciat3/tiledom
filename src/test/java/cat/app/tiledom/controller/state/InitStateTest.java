package cat.app.tiledom.controller.state;

import cat.app.tiledom.model.GameSession;
import cat.app.tiledom.GUI.AudioManager;
import cat.app.tiledom.GUI.UIManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



class InitStateTest {

    @Test
    void lifecycleMethodsReturnSameInstance() {
        AudioManager audio = mock(AudioManager.class);
        UIManager ui = mock(UIManager.class);
        GameSession session = mock(GameSession.class);

        InitState state = new InitState(audio, ui, session);

        assertSame(state, state.configure(), "configure should return the same instance");
        assertSame(state, state.exit(), "exit should return the same instance");
        assertSame(state, state.pause(), "pause should return the same instance");
        assertSame(state, state.restart(), "restart should return the same instance");
        assertSame(state, state.resume(), "resume should return the same instance");
    }

    @Test
    void startInvokesUiAndAudioAndReturnsPlayState() {
        AudioManager audio = mock(AudioManager.class);
        UIManager ui = mock(UIManager.class);
        GameSession session = mock(GameSession.class);

        InitState state = new InitState(audio, ui, session);

        GameState next = state.start();

        verify(ui).showScreen("playing");
        verify(audio).startMusic("play_track");

        assertNotNull(next, "next state should not be null");
        assertTrue(next instanceof PlayState, "start should return a PlayState instance");
    }
    
    /*
    @Test
    void exitWhenConfirmedAttemptsSystemExitAndDoesNotRestartMusic() {
        AudioManager audio = mock(AudioManager.class);
        UIManager ui = mock(UIManager.class);
        GameSession session = mock(GameSession.class);
        when(ui.showDialog("exiting")).thenReturn(true);

        InitState state = new InitState(audio, ui, session);

        SecurityManager original = System.getSecurityManager();
        SecurityManager blockingExit = new SecurityManager() {
            @Override
            public void checkPermission(java.security.Permission perm) {
                // allow everything
            }
            @Override
            public void checkExit(int status) {
                throw new SecurityException("Exit status: " + status);
            }
        };
        System.setSecurityManager(blockingExit);
        try {
            SecurityException ex = assertThrows(SecurityException.class, state::exit);
            assertTrue(ex.getMessage().contains("Exit status: 0"));
            verify(audio).stopMusic();
            verify(audio, never()).startMusic("play_track");
            verify(ui).showDialog("exiting");
        } finally {
            System.setSecurityManager(original);
        }
    }
*/
    @Test
    void exitWhenCancelledRestartsMusicAndReturnsSameInstance() {
        AudioManager audio = mock(AudioManager.class);
        UIManager ui = mock(UIManager.class);
        GameSession session = mock(GameSession.class);
        when(ui.showDialog("exiting")).thenReturn(false);

        InitState state = new InitState(audio, ui, session);

        GameState returned = state.exit();

        verify(audio).stopMusic();
        verify(audio).startMusic("play_track");
        verify(ui).showDialog("exiting");
        assertSame(state, returned, "exit should return the same instance when not confirmed");
    }
}