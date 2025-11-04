package cat.app.tiledom.controller.state;

public interface GameState {
    GameState configure();
    GameState exit();
    GameState pause();
    GameState restart();
    GameState resume();
    GameState start();
}