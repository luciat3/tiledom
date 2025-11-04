package cat.app.tiledom.model;

public class GameSession {
    private int _score;
    private int _level;

    public GameSession() {
        _score = 0;
        _level = 1;
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
}
