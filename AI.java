package Checker;

import javafx.application.Platform;
import javafx.scene.Node;

import java.util.Iterator;

public class AI extends Player {

    private final Player humanOpponent;
    private Thread playMoveThread;

    public AI(boolean turn, String playerName, PlayerPane playerPane, Player humanOpponent) {
        super(turn, playerName, playerPane);
        this.humanOpponent = humanOpponent;
    }

    @Override
    public void turnOn() {
        super.turnOn();
        this.playMoveThread = new Thread( () -> {
            Runnable runnable = this::makeMove;
            while (this.getTurn()) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ignored) {
                }
                Platform.runLater(runnable);
            }
        });
        this.playMoveThread.start();
    }

    @Override
    public void turnOff() {
        super.turnOff();
        if (this.playMoveThread != null) {
            this.playMoveThread.stop();
        }
    }

    public int makeKillMove() {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException ignored) {
//        }
        Iterator<Node> i = this.getPieceGroup().getChildren().iterator();
        Piece piece;
        int boardX, boardY;
        MoveResult temp;
        while(i.hasNext()) {
            piece = (Piece) i.next();
            if (piece.hasKill()) {
                boardX = (int)(piece.getOldX() / CheckersApp.TILE_SIZE);
                boardY = (int)(piece.getOldY() / CheckersApp.TILE_SIZE);
                temp = piece.tryMove(boardX + 2, boardY + 2);
                if (temp.getType() == MoveType.KILL) {
                    piece.getBoard()[boardX][boardY].setPiece(null);
//                    piece.getBoard()[boardX + 1][boardY + 1].setPiece(null);
                    Piece otherPiece = temp.getPiece();
                    otherPiece.getBoard()[otherPiece.toBoard(otherPiece.getOldX())][otherPiece.toBoard(otherPiece.getOldY())].setPiece(null);
                    humanOpponent.getPieceGroup().getChildren().remove(otherPiece);
                    piece.getBoard()[boardX + 2][boardY + 2].setPiece(piece);
                    piece.move(boardX + 2, boardY + 2);
                    if (!piece.hasKill()) {
                        piece.unlockPiece();
                        this.turnOff();
                        humanOpponent.turnOn();
                    }
                    else {
                        piece.lockPiece();
                    }
                    return 0;
                }
                temp = piece.tryMove(boardX + 2, boardY - 2);
                if (temp.getType() == MoveType.KILL) {
                    piece.getBoard()[boardX][boardY].setPiece(null);
                    Piece otherPiece = temp.getPiece();
                    otherPiece.getBoard()[otherPiece.toBoard(otherPiece.getOldX())][otherPiece.toBoard(otherPiece.getOldY())].setPiece(null);
                    humanOpponent.getPieceGroup().getChildren().remove(otherPiece);
                    piece.getBoard()[boardX + 2][boardY - 2].setPiece(piece);
                    piece.move(boardX + 2, boardY - 2);
                    if (!piece.hasKill()) {
                        piece.unlockPiece();
                        this.turnOff();
                        humanOpponent.turnOn();
                    }
                    else {
                        piece.lockPiece();
                    }
                    return 0;
                }
                temp = piece.tryMove(boardX - 2, boardY + 2);
                if (temp.getType() == MoveType.KILL) {
                    piece.getBoard()[boardX][boardY].setPiece(null);
//                    piece.getBoard()[boardX - 1][boardY + 1].setPiece(null);
                    Piece otherPiece = temp.getPiece();
                    otherPiece.getBoard()[otherPiece.toBoard(otherPiece.getOldX())][otherPiece.toBoard(otherPiece.getOldY())].setPiece(null);
                    humanOpponent.getPieceGroup().getChildren().remove(otherPiece);
                    piece.getBoard()[boardX - 2][boardY + 2].setPiece(piece);
                    piece.move(boardX - 2, boardY + 2);
                    if (!piece.hasKill()) {
                        piece.unlockPiece();
                        this.turnOff();
                        humanOpponent.turnOn();
                    }
                    else {
                        piece.lockPiece();
                    }
                    return 0;
                }
                temp = piece.tryMove(boardX - 2, boardY - 2);
                if (temp.getType() == MoveType.KILL) {
                    piece.getBoard()[boardX][boardY].setPiece(null);
//                    piece.getBoard()[boardX - 1][boardY - 1].setPiece(null);
                    Piece otherPiece = temp.getPiece();
                    otherPiece.getBoard()[otherPiece.toBoard(otherPiece.getOldX())][otherPiece.toBoard(otherPiece.getOldY())].setPiece(null);
                    humanOpponent.getPieceGroup().getChildren().remove(otherPiece);
                    piece.getBoard()[boardX - 2][boardY - 2].setPiece(piece);
                    piece.move(boardX - 2, boardY - 2);
                    if (!piece.hasKill()) {
                        piece.unlockPiece();
                        this.turnOff();
                        humanOpponent.turnOn();
                    }
                    else {
                        piece.lockPiece();
                    }
                    return 0;
                }
            }
        }
        return 1;
    }

    public int makeNormalMove() {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException ignored) { }
        Iterator<Node> i = this.getPieceGroup().getChildren().iterator();
        Piece piece;
        int boardX, boardY;
        MoveResult temp;
        while(i.hasNext()) {
            piece = (Piece) i.next();
            boardX = (int) (piece.getOldX() / CheckersApp.TILE_SIZE);
            boardY = (int) (piece.getOldY() / CheckersApp.TILE_SIZE);
            if (piece.hasMove()) {
                temp = piece.tryMove(boardX + 1, boardY + 1);
                if (temp.getType() == MoveType.NORMAL) {
                    piece.getBoard()[boardX][boardY].setPiece(null);
                    piece.getBoard()[boardX + 1][boardY + 1].setPiece(piece);
                    piece.move(boardX + 1, boardY + 1);
                    this.turnOff();
                    humanOpponent.turnOn();
                    return 0;
                }
                temp = piece.tryMove(boardX + 1, boardY - 1);
                if (temp.getType() == MoveType.NORMAL) {
                    piece.getBoard()[boardX][boardY].setPiece(null);
                    piece.getBoard()[boardX + 1][boardY - 1].setPiece(piece);
                    piece.move(boardX + 1, boardY - 1);
                    piece.unlockPiece();
                    this.turnOff();
                    humanOpponent.turnOn();
                    return 0;
                }
                temp = piece.tryMove(boardX - 1, boardY + 1);
                if (temp.getType() == MoveType.NORMAL) {
                    piece.getBoard()[boardX][boardY].setPiece(null);
                    piece.getBoard()[boardX - 1][boardY + 1].setPiece(piece);
                    piece.move(boardX - 1, boardY + 1);
                    piece.unlockPiece();
                    this.turnOff();
                    humanOpponent.turnOn();
                    return 0;
                }
                temp = piece.tryMove(boardX - 1, boardY - 1);
                if (temp.getType() == MoveType.NORMAL) {
                    piece.getBoard()[boardX][boardY].setPiece(null);
                    piece.getBoard()[boardX - 1][boardY - 1].setPiece(piece);
                    piece.move(boardX - 1, boardY - 1);
                    piece.unlockPiece();
                    this.turnOff();
                    humanOpponent.turnOn();
                    return 0;
                }
            }
        }
        return 1;
    }

    public void makeMove() {
        if (this.getPieceGroup().getChildren().isEmpty()) {
            this.turnOff();
            humanOpponent.turnOn();
            this.getPlayerPane().updateText("You lost.");
            humanOpponent.getPlayerPane().updateText("You win.");
        }
        int check = makeKillMove();
        if (check == 1) {
            check = makeNormalMove();
        }
        if (check == 1) {
            this.turnOff();
            humanOpponent.turnOff();
            this.getPlayerPane().updateText("You drew.");
            humanOpponent.getPlayerPane().updateText("You drew.");
        }
    }
}
