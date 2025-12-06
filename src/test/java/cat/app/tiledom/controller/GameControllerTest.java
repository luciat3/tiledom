package cat.app.tiledom.controller;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import cat.app.tiledom.GUI.BoardPanel;
import cat.app.tiledom.controller.state.PlayState;
import cat.app.tiledom.model.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    // fem servir classes mock per veure si la vista reacciona 
    // al joc com esperavem 
    private Board mockBoard;
    private BoardPanel mockPanel;
    private GameController controller;
    private PlayState mockState;
    private JButton[][] fakeButtons;

    @BeforeEach
    void setUp() {
        mockBoard = mock(Board.class);
        mockPanel = mock(BoardPanel.class);
        mockState = mock(PlayState.class);
        //preparem un taulell 2x2 perquè addListeners realment recorri els bucles
        when(mockBoard.getSize()).thenReturn(2);

        fakeButtons = new JButton[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                fakeButtons[i][j] = new JButton();
                when(mockPanel.getButton(i, j)).thenReturn(fakeButtons[i][j]);
            }
        }
        controller = new GameController(mockBoard, mockPanel, mockState);
    }

    @Test
    void testFirstHandleClick() throws Exception {
        // comprovem que el primer click s'estigui guardant
        controller.handleClick(1, 1);
        var field = GameController.class.getDeclaredField("firstSelection");
        field.setAccessible(true);
        int[] selection = (int[]) field.get(controller);
        assertNotNull(selection);
        assertEquals(1, selection[0]);
        assertEquals(1, selection[1]);
    }

    @Test
    void testSecondHandleClickValid() {
        // comprovem que el segon click es guarda
        // y com actua quan són una parella correcta
        controller.handleClick(0, 0);
        when(mockBoard.tryMatch(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(true);
        controller.handleClick(0, 1);

        verify(mockState, times(1)).handleMove(0, 0, 0, 1);
        verify(mockPanel, times(1)).updateBoard();
    }

    @Test
    void testSecondHandleClickInvalid() throws Exception {
        // comprovem que el segon click es guarda
        // y com actua quan són una parella incorrecta
        controller.handleClick(0, 0);
        when(mockBoard.tryMatch(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(false);
        controller.handleClick(0, 1);

        verify(mockState, times(1)).handleMove(0, 0, 0, 1);

        // comprova que es reinicia la selecció
        var field = GameController.class.getDeclaredField("firstSelection");
        field.setAccessible(true);
        assertNull(field.get(controller));
    }

    @Test
    void testFirstClickHighlightsButtonAndDoesNotTriggerMove() {
        // mock extra de la vista: un botó concret del taulell
        JButton mockButton = mock(JButton.class);
        when(mockPanel.getButton(1, 1)).thenReturn(mockButton);

        // fem el primer click a la casella (1,1)
        controller.handleClick(1, 1);

        // El controlador ha de remarcar el botó seleccionat
        verify(mockButton, times(1)).setBackground(Color.CYAN);

        // En el primer click encara no s'ha de fer cap moviment de joc
        verify(mockState, never()).handleMove(anyInt(), anyInt(), anyInt(), anyInt());

        // Tampoc s'ha d'actualitzar el taulell encara
        verify(mockPanel, never()).updateBoard();
    }


    @Test
    void testVictoria() {

        // comprovem com reacciona en estat de victòria
        controller.handleClick(0, 0);
        when(mockBoard.tryMatch(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(true);
        when(mockBoard.isEmpty()).thenReturn(true);

        controller.handleClick(0, 1);

        verify(mockState, times(1)).handleMove(0, 0, 0, 1);
        verify(mockPanel).updateBoard();
    }

    @Test
    void testDerrota() {

        // comprovem com reacciona en cas de derrota
        controller.handleClick(0, 0);
        when(mockBoard.tryMatch(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(true);
        when(mockBoard.isEmpty()).thenReturn(false);
        when(mockBoard.hasAvailableMoves()).thenReturn(false);

        controller.handleClick(0, 1);

        verify(mockState, times(1)).handleMove(0, 0, 0, 1);
        verify(mockPanel).updateBoard();
    }

    // ------------------ CAIXA BLANCA -------------------
    @Test
    void testActionPerformed_WhiteBox() throws Exception {

        JButton button = fakeButtons[1][0];

        ActionListener[] listeners = button.getActionListeners();
        assertEquals(1, listeners.length, "El botó (1,0) ha de tenir un ActionListener afegit per addListeners");

        // Disparem manualment el listener, simulant un clic de l’usuari
        listeners[0].actionPerformed(
            new ActionEvent(button, ActionEvent.ACTION_PERFORMED, "testClick")
        );

        //Verifiquem que handleClick(row, col) s’ha executat
        java.lang.reflect.Field firstSelField = GameController.class.getDeclaredField("firstSelection");
        firstSelField.setAccessible(true);
        int[] firstSelection = (int[]) firstSelField.get(controller);

        assertNotNull(firstSelection, "Després del clic, firstSelection no ha de ser null");
        assertEquals(1, firstSelection[0], "La fila guardada a firstSelection ha de ser 1");
        assertEquals(0, firstSelection[1], "La columna guardada a firstSelection ha de ser 0");
    }

}
