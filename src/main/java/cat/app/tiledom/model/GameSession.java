package cat.app.tiledom.model;

public class GameSession {
    private int _score;
    private int _level;
    private Board _board;

    public GameSession() {
        _score = 0;
        _level = 1;
    }

    public void setBoard(Board board) {
        this._board = board;
    }

    public void addScore(int points) {
        _score += points;
    }

    public void nextLevel() {
        _level++;
    }

    public void reset() {
        _score = 0;
        _level = 1;
    }

    public int getScore() { return _score; }
    public int getLevel() { return _level; }
    public Board getBoard() { return _board; }
}
