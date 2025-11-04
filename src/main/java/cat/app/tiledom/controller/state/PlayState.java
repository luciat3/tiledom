package cat.app.tiledom.controller.state;

import cat.app.tiledom.model.GameSession;
import cat.app.tiledom.GUI.AudioManager;
import cat.app.tiledom.GUI.UIManager;

public class PlayState implements GameState {
    private final AudioManager _audio;
    private final UIManager _ui;
    private final GameSession _game_session;

    public PlayState(AudioManager audio, UIManager ui, GameSession game_session) {
        _audio = audio;
        _ui = ui;
        _game_session = game_session;
    }

    @Override
    public GameState configure() {
        _ui.showScreen("configuration");
        _audio.stopMusic();
        return new ConfigureState(_audio, _ui, _game_session);
    }

    @Override
    public GameState exit() {
        _audio.stopMusic();
        boolean confirmed = _ui.showDialog("exiting");
        if (confirmed) {
            System.exit(0);
        }
        _audio.startMusic("play_track");
        return this;
    }

    @Override
    public GameState pause() {
        _ui.showScreen("paused");
        _audio.stopMusic();
        return new PauseState(_audio, _ui, _game_session);
    }

    @Override
    public GameState restart() {
        _audio.stopMusic();
        boolean restart = _ui.showDialog("restart");
        if (restart) {
            _game_session.reset();
            _audio.startMusic("initial_track");
            _ui.showScreen("init");
            return new InitState(_audio, _ui, _game_session);
        }
        _audio.startMusic("play_track");
        return this;
    }

    @Override
    public GameState resume() {
        return this;
    }

    @Override
    public GameState start() {
        return this;
    }
}