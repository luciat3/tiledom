package cat.app.tiledom.GUI;

import cat.app.tiledom.model.Board;
import cat.app.tiledom.model.GameSession;
import cat.app.tiledom.model.RandomTileGenerator;
import cat.app.tiledom.controller.GameController;
import cat.app.tiledom.controller.state.PlayState;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame implements UIManager {
    private Board board;
    private BoardPanel boardPanel;
    private GameSession gameSession;
    private AudioManager audioManager;
    private PlayState playState;
    private GameController controller;

    public GameWindow(int dificultat) {
        setTitle("Tiledom");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Model
        RandomTileGenerator gen = new RandomTileGenerator(dificultat);
        board = new Board(dificultat, gen);
        gameSession = new GameSession();
        gameSession.setBoard(board);
        // Vista
        boardPanel = new BoardPanel(board);
        audioManager = new ImplementedAudioManager();

        // Controlador
        playState = new PlayState(audioManager, this, gameSession);
        controller = new GameController(board, boardPanel, playState);


        add(boardPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void showScreen(String screenName) {
    }

    @Override
    public boolean showDialog(String message) {
        JOptionPane.showMessageDialog(this, getDialogText(message), "Missatge", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    private String getDialogText(String key) {
        switch (key) {
            case "level_completed":
                return "Has completat el nivell!";
            case "no_moves_left":
                return "No queden moviments possibles!";
            case "invalid_move":
                return "Aquestes peces no es poden eliminar!";
            case "exiting":
                return "Vols sortir del joc?";
            default:
                return key;
        }
    }


    @Override
    public void updateScore(int score) {
        if (boardPanel != null) {
            boardPanel.updateScore(score);
        }
    }

}
