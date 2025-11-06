package cat.app.tiledom.GUI;

import javax.swing.*;
import java.awt.*;

import cat.app.tiledom.model.Board;

public class BoardPanel extends JPanel {
        private JButton[][] buttons;
    private Board board;

    public BoardPanel(Board board) {
        this.board = board;
        int size = board.getSize();
        setLayout(new GridLayout(size, size));
        buttons = new JButton[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                JButton btn = new JButton();
                btn.setFont(new Font("Arial", Font.BOLD, 14));
                updateButton(btn, board.getTiles()[i][j]);
                buttons[i][j] = btn;
                add(btn);
            }
        }
    }

    public void updateBoard() {
        int[][] tiles = board.getTiles();
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                updateButton(buttons[i][j], tiles[i][j]);
            }
        }
        repaint();
    }

    private void updateButton(JButton btn, int value) {
        if (value == 0) {
            btn.setText("");
            btn.setBackground(Color.LIGHT_GRAY);
        } else {
            btn.setText(String.valueOf(value));
            btn.setBackground(Color.WHITE);
        }
    }

    public JButton getButton(int i, int j) {
        return buttons[i][j];
    }
}
