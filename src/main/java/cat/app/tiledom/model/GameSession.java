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

    public int getScore() { return _score; }
    public Board getBoard() { return _board; }
}
