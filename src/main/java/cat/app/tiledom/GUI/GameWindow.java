package cat.app.tiledom.GUI;

import cat.app.tiledom.model.Board;
import cat.app.tiledom.model.RandomTileGenerator;
import cat.app.tiledom.controller.GameController;
import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    private Board board;
    private BoardPanel boardPanel;
    private GameController controller;

    public GameWindow(int dificultat) {
        setTitle("Tiledom");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Model
        RandomTileGenerator gen = new RandomTileGenerator(dificultat);
        board = new Board(dificultat, gen);

        // Vista
        boardPanel = new BoardPanel(board);

        // Controlador
        controller = new GameController(board, boardPanel);

        add(boardPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
