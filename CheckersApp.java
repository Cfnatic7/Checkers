package Checker;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CheckersApp extends Application {

    public static final int TILE_SIZE = 70;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private final Tile[][] board = new Tile[WIDTH][HEIGHT];

    private final Group tileGroup = new Group();
    private Player playerWhite = new Player(true, "White", new PlayerPane("White", 8, 4));
    private Player playerRed = new Player(false, "Red", new PlayerPane("Red", 8, 0));
    private final OptionsPane optionsPane = new OptionsPane(0, 8);
    public static final int PLAYER_PANE_OFFSET = 0;
//    private final Group pieceGroupPlayerRed = new Group();
//    private final Group pieceGroupPlayerWhite = new Group();


    private Parent createContent() {
        optionsPane.getResetGameButton().setOnMouseClicked(event -> resetGame());
        optionsPane.getResetAIGameButton().setOnMouseClicked(event -> resetGameAI());
        Pane root = new Pane();
        root.setPrefSize((WIDTH + (int)(PlayerPane.WIDTH / TILE_SIZE)) * TILE_SIZE, (HEIGHT + 2) * TILE_SIZE);
        root.getChildren().addAll(tileGroup, playerWhite.getPieceGroup(), playerRed.getPieceGroup(), playerWhite.getPlayerPane(), playerRed.getPlayerPane(), optionsPane);



        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y + PLAYER_PANE_OFFSET);
                board[x][y] = tile;

                tileGroup.getChildren().add(tile);

                Piece piece = null;

//                if (y <= 2 && (x + y) % 2 != 0) {
//                    piece = makePiece(PieceColor.RED, x, y + PLAYER_PANE_OFFSET);
//                    playerRed.getPieceGroup().getChildren().add(piece);
//                }
//
//                if (y >= 5 && (x + y) % 2 != 0) {
//                    piece = makePiece(PieceColor.WHITE, x, y + PLAYER_PANE_OFFSET);
//                    playerWhite.getPieceGroup().getChildren().add(piece);
//                }

//                if (piece != null) {
//                    tile.setPiece(piece);
//                }
                tile.setPiece(piece);
            }
        }

        return root;
    }

    private void printBoard() {
        System.out.print(System.lineSeparator());
        System.out.print(System.lineSeparator());
        System.out.print(System.lineSeparator());
        for (int y = 0; y < 8; y++) {
            System.out.print("|");
            for (int x = 0; x < 8; x++) {
                if (board[x][y].hasPiece()) System.out.print("O");
                else if (board[x][y] == null) System.out.print("@");
                else System.out.print(" ");
                System.out.print("|");
            }
            System.out.print(System.lineSeparator());
            System.out.print("------------------");
            System.out.print(System.lineSeparator());
        }
    }

//    public MoveResult tryMove(Piece piece, int newX, int newY) {
//        System.out.printf("NewX: %d, NewY: %d, tile: %h\n", newX, newY, board[newX][newY]);
//        if (board[newX][newY - PLAYER_PANE_OFFSET].hasPiece() || (newX + newY - PLAYER_PANE_OFFSET) % 2 == 0) {
//            return new MoveResult(MoveType.NONE);
//        }
//        if ((piece.getType() == PieceType.RED && !playerRed.getTurn()) || (piece.getType() == PieceType.WHITE && !playerWhite.getTurn())) {
//            return new MoveResult(MoveType.NONE);
//        }
//
//        int x0 = toBoard(piece.getOldX());
//        int y0 = toBoard(piece.getOldY());
//
//        if (Math.abs(newX - x0) == 1 && newY - y0 == piece.getType().moveDir) {
//            return new MoveResult(MoveType.NORMAL);
//        }
//        else if (Math.abs(newX - x0) == 2 && newY - y0 == piece.getType().moveDir * 2) {
//
//            int x1 = x0 + (newX - x0) / 2;
//            int y1 = y0 + (newY - y0) / 2;
//
//            if (board[x1][y1 - PLAYER_PANE_OFFSET].hasPiece() && board[x1][y1 - PLAYER_PANE_OFFSET].getPiece().getType() != piece.getType()) {
//                return new MoveResult(MoveType.KILL, board[x1][y1 - PLAYER_PANE_OFFSET].getPiece());
//            }
//        }
//
//        return new MoveResult(MoveType.NONE);
//    }

    public int toBoard(double pixel) {
        return (int)(pixel + TILE_SIZE / 2) / TILE_SIZE;
    }

    public void resetGame() {
        playerRed.turnOff();
        playerWhite.turnOff();
        playerRed.getPieceGroup().getChildren().clear();
        playerWhite.getPieceGroup().getChildren().clear();
        if (playerRed instanceof  AI) {
            Player temp = playerRed;
            playerRed = new Player(false, "Red", temp.getPlayerPane());
            playerRed.setPieceGroup(temp.getPieceGroup());
        }
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {

                Piece piece = null;

                if (y <= 2 && (x + y) % 2 != 0) {
                    piece = makePiece(PieceColor.RED, x, y + PLAYER_PANE_OFFSET);
                    playerRed.getPieceGroup().getChildren().add(piece);
                }

                if (y >= 5 && (x + y) % 2 != 0) {
                    piece = makePiece(PieceColor.WHITE, x, y + PLAYER_PANE_OFFSET);
                    playerWhite.getPieceGroup().getChildren().add(piece);
                }
                board[x][y].setPiece(piece);
            }
        }
        playerWhite.getPlayerPane().resetTime();
        playerRed.getPlayerPane().resetTime();
        playerWhite.turnOn();
    }

    public void resetGameAI() {
        Player temp = playerRed;
        this.playerRed = new AI(false, "Red", temp.getPlayerPane(), playerWhite);
        playerRed.setPieceGroup(temp.getPieceGroup());
        playerRed.turnOff();
        playerWhite.turnOff();
        playerRed.getPieceGroup().getChildren().clear();
        playerWhite.getPieceGroup().getChildren().clear();
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {

                Piece piece = null;

                if (y <= 2 && (x + y) % 2 != 0) {
                    piece = makePiece(PieceColor.RED, x, y + PLAYER_PANE_OFFSET);
                    playerRed.getPieceGroup().getChildren().add(piece);
                }

                if (y >= 5 && (x + y) % 2 != 0) {
                    piece = makePiece(PieceColor.WHITE, x, y + PLAYER_PANE_OFFSET);
                    playerWhite.getPieceGroup().getChildren().add(piece);
                }
                board[x][y].setPiece(piece);
            }
        }
        playerWhite.getPlayerPane().resetTime();
        playerRed.getPlayerPane().resetTime();
        playerWhite.turnOn();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setOnCloseRequest(event -> {
            playerRed.turnOff();
            playerWhite.turnOff();
//            try {
//                playerRed.getPlayerPane().stopTimeUpdating();
//                playerWhite.getPlayerPane().stopTimeUpdating();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        });
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("CheckersApp");
        primaryStage.setScene(scene);
        primaryStage.show();
        printBoard();
    }

    private Piece makePiece(PieceColor color, int x, int y) {
        Piece piece;
        if (color == PieceColor.RED) {
            piece = new Piece(color, this.board, this.playerRed, x, y);
        }
        else {
            piece = new Piece(color, this.board, this.playerWhite, x, y);
        }

        if (!(piece.getPlayer() instanceof  AI)){
            piece.setOnMouseReleased(e -> {
                int newX = toBoard(piece.getLayoutX());
                int newY = toBoard(piece.getLayoutY());

                MoveResult result = null;

                if (newX < 0 || newY < 0 || newX >= WIDTH || newY >= HEIGHT) {
                    result = new MoveResult(MoveType.NONE);
                } else {
//                result = tryMove(piece, newX, newY);
                    result = piece.tryMove(newX, newY);
                }

                int x0 = toBoard(piece.getOldX());
                int y0 = toBoard(piece.getOldY());

                switch (result.getType()) {
                    case NONE -> piece.abortMove();
                    case NORMAL -> {
                        piece.move(newX, newY);
                        if ((piece.getColor() == PieceColor.RED && newY == 7) || (piece.getColor() == PieceColor.WHITE && newY == 0)) {
                            piece.transformToDame();
                        }
                        board[x0][y0 - PLAYER_PANE_OFFSET].setPiece(null);
                        board[newX][newY - PLAYER_PANE_OFFSET].setPiece(piece);
                        if (playerWhite.getTurn()) {
                            playerWhite.turnOff();
                            playerRed.turnOn();
                            if (!playerRed.hasMove()) {
                                playerRed.turnOff();
                                playerWhite.turnOff();
                                playerRed.getPlayerPane().updateText("You drew.");
                                playerWhite.getPlayerPane().updateText("You drew.");
                            }
                        } else if (playerRed.getTurn()) {
                            playerRed.turnOff();
                            playerWhite.turnOn();
                            if (!playerWhite.hasMove()) {
                                playerRed.turnOff();
                                playerWhite.turnOff();
                                playerRed.getPlayerPane().updateText("You drew.");
                                playerWhite.getPlayerPane().updateText("You drew.");
                            }
                        }
                    }
                    case KILL -> {
                        piece.move(newX, newY);
                        if ((piece.getColor() == PieceColor.RED && newY == 7) || (piece.getColor() == PieceColor.WHITE && newY == 0)) {
                            piece.transformToDame();
                        }
                        board[x0][y0 - PLAYER_PANE_OFFSET].setPiece(null);
                        board[newX][newY - PLAYER_PANE_OFFSET].setPiece(piece);
                        Piece otherPiece = result.getPiece();
                        board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY()) - PLAYER_PANE_OFFSET].setPiece(null);
                        if (otherPiece.getColor() == PieceColor.WHITE) {
                            playerWhite.getPieceGroup().getChildren().remove(otherPiece);
                        } else {
                            playerRed.getPieceGroup().getChildren().remove(otherPiece);
                        }
                        if (playerWhite.getTurn()) {
                            if (playerRed.getPieceGroup().getChildren().isEmpty()) {
                                playerWhite.youWin();
                                playerRed.youLose();
                            } else {
                                if (!piece.hasKill()) {
                                    playerWhite.turnOff();
                                    playerRed.turnOn();
                                    piece.unlockPiece();
                                } else {
                                    piece.lockPiece();
                                }
                                if (!playerRed.hasMove() && playerRed.getTurn()) {
                                    playerRed.turnOff();
                                    playerWhite.turnOff();
                                    playerRed.getPlayerPane().updateText("You drew.");
                                    playerWhite.getPlayerPane().updateText("You drew.");
                                }
                            }
                        } else if (playerRed.getTurn()) {
                            if (playerWhite.getPieceGroup().getChildren().isEmpty()) {
                                playerRed.youWin();
                                playerWhite.youLose();
                            } else {
                                if (!piece.hasKill()) {
                                    playerRed.turnOff();
                                    playerWhite.turnOn();
                                    piece.unlockPiece();
                                } else {
                                    piece.lockPiece();
                                }
                                if (!playerWhite.hasMove() && playerWhite.getTurn()) {
                                    playerRed.turnOff();
                                    playerWhite.turnOff();
                                    playerRed.getPlayerPane().updateText("You drew.");
                                    playerWhite.getPlayerPane().updateText("You drew.");
                                }
                            }
                        }
                    }
                }
                printBoard();
            });
        }

        return piece;
    }

    public static void main(String[] args) {
        launch(args);

    }
}