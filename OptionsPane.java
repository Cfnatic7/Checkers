package Checker;

import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;

public class OptionsPane extends TilePane {
    private Button resetGameButton;
    private Button resetAIGameButton;
    public static int HEIGHT = 2 * CheckersApp.TILE_SIZE;

    public Button getResetGameButton() {
        return resetGameButton;
    }

    public Button getResetAIGameButton() {
        return this.resetAIGameButton;
    }

    public OptionsPane(int x, int y) {
        setPrefRows(1);
        setPrefColumns(2);
        double singleButtonWidth = (double)(CheckersApp.WIDTH * CheckersApp.TILE_SIZE + PlayerPane.WIDTH) / 2;
        resetGameButton = new Button("New game");
        resetGameButton.setMinWidth(singleButtonWidth);
        resetGameButton.setMinHeight(HEIGHT);
        resetAIGameButton = new Button("New game against computer");
        resetAIGameButton.setMinHeight(HEIGHT);
        resetAIGameButton.setMinWidth(singleButtonWidth);
        getChildren().addAll(resetGameButton, resetAIGameButton);
        relocate(x * CheckersApp.TILE_SIZE, y * CheckersApp.TILE_SIZE);
    }
}
