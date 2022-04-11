package Checker;

import javafx.scene.Group;
import javafx.scene.Node;


import java.util.Iterator;

import static java.lang.Thread.sleep;

public class Player {
    private Group pieceGroup;
    private boolean turn;
    private boolean won;
    private boolean lost;
    private PlayerPane playerPane;
//    private final Thread timeUpdater;
    public final String playerName;

    public Player(boolean turn, String playerName, PlayerPane playerPane){
        this.playerPane = playerPane;
        this.pieceGroup = new Group();
        this.turn = turn;
        this.playerName = playerName;
//        timeUpdater = new Thread( () -> {
//            while(true) {
//                try {
//                    sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                updateTime(System.currentTimeMillis());
//            }
//        });
        won = false;
        lost = false;
    }

    public PlayerPane getPlayerPane() {
        return this.playerPane;
    }

    public void setPieceGroup(Group pieceGroup) {
        this.pieceGroup = pieceGroup;
    }

    public boolean getTurn() {
        return this.turn;
    }

    public Group getPieceGroup() {
        return this.pieceGroup;
    }

//    public String getFormattedTime() {
//        return this.formattedTime.toString();
//    }

//    public void updateTime(long currentTime) {
//
//    }

    public void turnOff() {
        this.playerPane.killTimeUpdating();
        this.turn = false;
    }

    public void turnOn() {
        this.playerPane.startTimeUpdating();
        this.turn = true;
    }

    public void youLose() {
        this.lost = true;
        try {
            this.checkAndUpdateStatus();
        } catch(Exception e) {

        }
    }

    public boolean hasLockedPiece() {
        Iterator<Node> i = getPieceGroup().getChildren().iterator();
        Piece piece;
        while(i.hasNext()) {
            piece = (Piece) i.next();
            if (piece.isLocked()) return true;
        }
        return false;
    }


    public void youWin() {
        this.won = true;
        try {
            this.checkAndUpdateStatus();
        } catch(Exception e) {

        }
    }

    public boolean isWinner() {
        return this.won;
    }

    public boolean isLost() {
        return this.lost;
    }

    private void checkAndUpdateStatus() throws InterruptedException {
        if (this.isLost()) {
            this.playerPane.killTimeUpdating();
            this.playerPane.updateText("Player " + this.playerName + " " + "lost");
        }
        else if (this.isWinner()) {
            this.playerPane.killTimeUpdating();
            this.playerPane.updateText("Player " + this.playerName + " " + "won");
        }
    }

    public boolean hasMove() {
        Iterator<Node> i = this.getPieceGroup().getChildren().iterator();
        Piece piece;
        while(i.hasNext()) {
            piece = (Piece) i.next();
            try {
                if (piece.hasMove()) return true;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void setPlayerPane(PlayerPane newPane) {
        this.playerPane = newPane;
    }

//    public void stop() throws InterruptedException {
//        timeUpdater.wait();
//    }
//
//    public void play() throws InterruptedException {
//        if (!timeUpdater.isAlive()) {
//            timeUpdater.start();
//        }
//        else {
//            timeUpdater.notifyAll();
//        }
//    }
//
//    public void updateTime() {
//
//    }
}
