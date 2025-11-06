package cat.app.tiledom.controller;


import javax.swing.JOptionPane;

import cat.app.tiledom.GUI.BoardPanel;
import cat.app.tiledom.model.Board;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameController {
    
    private Board board;
    private BoardPanel panel;
    private int[] firstSelection = null;

    public GameController(Board board, BoardPanel panel) {
        this.board = board;
        this.panel = panel;
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
            boolean validMove = board.tryMatch(x1, y1, x, y);
            if (validMove) {
                JOptionPane.showMessageDialog(null, "Parella eliminada!");
            } else {
                JOptionPane.showMessageDialog(null, "Moviment invàlid!");
            }
            firstSelection = null;
            if (panel != null) {
                panel.updateBoard();
            }
            if (validMove){
                if (board.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Has guanyat!");
                } else if (!board.hasAvailableMoves()) {
                    JOptionPane.showMessageDialog(null, "No queden moviments. Fi de la partida.");
                }
            }
        }
    }

}
