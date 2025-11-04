package cat.app.tiledom.controller.state;

import cat.app.tiledom.model.GameSession;
import cat.app.tiledom.GUI.AudioManager;
import cat.app.tiledom.GUI.UIManager;

public class InitState implements GameState {

    private final AudioManager _audio;
    private final UIManager _ui;
    private final GameSession _game_session;
    
    public InitState(AudioManager audio, UIManager ui, GameSession game_session) {
        _audio = audio;
        _ui = ui;
        _game_session = game_session;
    }

    @Override
    public GameState configure() {
        return this;
    }

    @Override
    public GameState exit() {
        _audio.stopMusic();
        boolean confirmed = _ui.showDialog("exiting");
        if (confirmed) {
            //Exit Actions
            System.exit(0);
        }
        _audio.startMusic("play_track");
        return this;
    }

    @Override
    public GameState pause() {
        return this;
    }

    @Override
    public GameState restart() {
        return this;
    }

    @Override
    public GameState resume() {
        return this;
    }

    @Override
    public GameState start() {
        _ui.showScreen("playing");
        _audio.startMusic("play_track");
        return new PlayState(_audio, _ui, _game_session);
    }
    
}