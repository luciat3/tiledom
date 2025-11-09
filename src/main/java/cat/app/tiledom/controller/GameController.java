package cat.app.tiledom.controller;


import javax.swing.JOptionPane;

import cat.app.tiledom.GUI.BoardPanel;
import cat.app.tiledom.controller.state.PlayState;
import cat.app.tiledom.model.Board;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameController {
    
    private Board board;
    private BoardPanel panel;
     private PlayState playState;
    private int[] firstSelection = null;

    public GameController(Board board, BoardPanel panel, PlayState playState) {
        this.board = board;
        this.panel = panel;
        this.playState = playState;
        addListeners();
    }

    // controlar accions de l'usuari
    private void addListeners() {
        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int row = i;
                int col = j;
                panel.getButton(i, j).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleClick(row, col);
                    }
                });
            }
        }
    }

    // provar d'eliminar caselles
    void handleClick(int x, int y) {
        if (firstSelection == null) {
            firstSelection = new int[]{x, y};
            if (panel != null && panel.getButton(x, y) != null) {
                panel.getButton(x, y).setBackground(java.awt.Color.CYAN);
            }
        } else {
            int x1 = firstSelection[0], y1 = firstSelection[1];
            playState.handleMove(x1, y1, x, y);

            firstSelection = null;
            panel.updateBoard();
        }
    }

}
