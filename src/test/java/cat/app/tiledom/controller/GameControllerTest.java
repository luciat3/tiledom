package cat.app.tiledom.controller;


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

    @BeforeEach
    void setUp() {
        mockBoard = mock(Board.class);
        mockPanel = mock(BoardPanel.class);
        mockState = mock(PlayState.class);
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
}
