package cat.app.tiledom;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import cat.app.tiledom.GUI.GameWindow;


public class TiledomApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] options = {"Fàcil (8x8)", "Intermig (10x10)", "Difícil (12x12)"};
            int dificultat = JOptionPane.showOptionDialog(
                    null,
                    "Selecciona la dificultat:",
                    "Tiledom",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            ) + 1;

            if (dificultat < 1 || dificultat > 3) System.exit(0);
            new GameWindow(dificultat);
        });
    }

}