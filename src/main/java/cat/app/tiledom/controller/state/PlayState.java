package cat.app.tiledom.controller.state;

import cat.app.tiledom.model.Board;
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

    public void handleMove(int x1, int y1, int x2, int y2) {
        Board board = _game_session.getBoard();

        //comprovem si es poden eliminar les fitxes seleccionades
        boolean matched = board.tryMatch(x1, y1, x2, y2);

        if (matched) {
            //si s'eliminen, augmentem la puntuació
            _game_session.addScore(100);
            _ui.updateScore(_game_session.getScore());
            
            //si el taulell queda buit -> victòria
            if (board.isEmpty()) {
                _ui.showDialog("level_completed");
                _game_session.nextLevel();
            }
            //si no queden moviments -> derrota
            else if (!board.hasAvailableMoves()) {
                _audio.stopMusic();
                _ui.showDialog("no_moves_left");
                _audio.startMusic("game_over");
            }
        } else {
            //si les fitxes seleccionades no son correctes, so d'error
            _audio.startMusic("error_sound");
            _ui.showDialog("invalid_move");
        }
    }
}