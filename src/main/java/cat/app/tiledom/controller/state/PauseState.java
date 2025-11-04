package cat.app.tiledom.controller.state;

import cat.app.tiledom.model.GameSession;
import cat.app.tiledom.GUI.AudioManager;
import cat.app.tiledom.GUI.UIManager;

public class PauseState implements GameState {
    private final AudioManager _audio;
    private final UIManager _ui;
    private final GameSession _game_session;

    public PauseState(AudioManager audio, UIManager ui, GameSession game_session) {
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
        _ui.showScreen("playing");
        _audio.startMusic("play_track");
        return new PlayState(_audio, _ui, _game_session);
    }

    @Override
    public GameState start() {
        return this;
    }

}