package Checker;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import static Checker.CheckersApp.TILE_SIZE;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class Piece extends StackPane {

    private PieceColor color;
    private PieceType type;
    private Tile board[][];
    private Player player;
    private boolean isLocked;

    private double mouseX, mouseY;
    private double oldX, oldY;

    public Player getPlayer() {
        return this.player;
    }

    public Tile[][] getBoard() {
        return this.board;
    }

    public void transformToDame() {
        this.type = PieceType.Dame;
        Ellipse ellipse = new Ellipse(TILE_SIZE * 0.3125 / 2, TILE_SIZE * 0.26 / 2);
        ellipse.setStroke(Color.WHITE);
        ellipse.setStrokeWidth(TILE_SIZE * 0.02);
        ellipse.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
        ellipse.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2);
        getChildren().addAll(ellipse);
    }

    public PieceColor getColor() {
        return color;
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    private boolean hasKillNormal() {
        int boardX = (int) (oldX / TILE_SIZE), boardY = (int) (oldY / TILE_SIZE);
        try {
            if (tryMove(boardX + 2, boardY + 2 * color.moveDir).getType() == MoveType.KILL) return true;
        } catch(Exception ignore) {}
        try {
            if (tryMove(boardX - 2, boardY + 2 * color.moveDir).getType() == MoveType.KILL) return true;
        } catch(Exception ignore) {}
        return false;
    }

    private boolean hasKillDame() {
        int boardX = (int) (oldX / TILE_SIZE), boardY = (int) (oldY / TILE_SIZE);
        try {
            if (tryMove(boardX + 2, boardY + 2).getType() == MoveType.KILL) return true;
        } catch(Exception ignore) {}
        try {
            if (tryMove(boardX - 2, boardY + 2).getType() == MoveType.KILL) return true;
        } catch(Exception ignore) {}
        try {
            if (tryMove(boardX + 2, boardY - 2).getType() == MoveType.KILL) return true;
        } catch(Exception ignore) {}
        try {
            if (tryMove(boardX - 2, boardY - 2).getType() == MoveType.KILL) return true;
        } catch(Exception ignore) {}
        return false;
    }

    public boolean hasKill() {
        if (type == PieceType.Normal) {
            return hasKillNormal();
        }
        else {
            return hasKillDame();
        }
    }

    public void lockPiece() {
        this.isLocked = true;
    }

    public void unlockPiece() {
        this.isLocked = false;
    }

    public boolean isLocked() {
        return this.isLocked;
    }

    public Piece(PieceColor color, Tile[][] board, Player player, int x, int y) {
        this.color = color;
        this.board = board;
        this.player = player;
        this.type = PieceType.Normal;
        this.isLocked = false;

        move(x, y);

        Ellipse bg = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);
        bg.setFill(Color.BLACK);

        bg.setStroke(Color.BLACK);
        bg.setStrokeWidth(TILE_SIZE * 0.03);

        bg.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
        bg.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2 + TILE_SIZE * 0.07);

        Ellipse ellipse = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);
        ellipse.setFill(color == PieceColor.RED
                ? Color.valueOf("#c40003") : Color.valueOf("#fff9f4"));

        ellipse.setStroke(Color.BLACK);
        ellipse.setStrokeWidth(TILE_SIZE * 0.03);

        ellipse.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);
        ellipse.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2);

        getChildren().addAll(bg, ellipse);

        if (!(this.player instanceof  AI)) {
            setOnMousePressed(e -> {
                mouseX = e.getSceneX();
                mouseY = e.getSceneY();
            });

            setOnMouseDragged(e -> {
                relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
            });
        }

    }

    public void move(int x, int y) {
        oldX = x * TILE_SIZE;
        oldY = y * TILE_SIZE;
        relocate(oldX, oldY);
    }

    public int toBoard(double pixel) {
        return (int)(pixel + TILE_SIZE / 2) / TILE_SIZE;
    }

    public MoveResult tryMove(int newX, int newY) throws ArrayIndexOutOfBoundsException {
        if (newX < 0 || newX > 7 || newY < 0 || newY > 7) return new MoveResult(MoveType.NONE);
        CheckersApp helper = new CheckersApp();
        if (board[newX][newY].hasPiece() || (newX + newY) % 2 == 0) {
            return new MoveResult(MoveType.NONE);
        }

        if (!this.player.getTurn()) {
            return new MoveResult(MoveType.NONE);
        }

        if (this.player.hasLockedPiece() && !this.isLocked) return new MoveResult(MoveType.NONE);

        int x0 = helper.toBoard(this.getOldX());
        int y0 = helper.toBoard(this.getOldY());
        if (this.type == PieceType.Dame) {
            if (Math.abs(newX - x0) == 1 && Math.abs(newY - y0) == 1) {
                return new MoveResult(MoveType.NORMAL);
            }
            else if (Math.abs(newX - x0) == 2 && Math.abs(newY - y0) == 2) {
                int x1 = x0 + (newX - x0) / 2;
                int y1 = y0 + (newY - y0) / 2;
                if (board[x1][y1].hasPiece() && (board[x1][y1].getPiece().getColor() != this.getColor())) {
                    return new MoveResult(MoveType.KILL, board[x1][y1].getPiece());
                }
            }
        }
        else {
            if (Math.abs(newX - x0) == 1 && newY - y0 == this.getColor().moveDir) {
                return new MoveResult(MoveType.NORMAL);
            } else if (Math.abs(newX - x0) == 2 && newY - y0 == this.getColor().moveDir * 2) {

                int x1 = x0 + (newX - x0) / 2;
                int y1 = y0 + (newY - y0) / 2;

                if (board[x1][y1].hasPiece() && board[x1][y1].getPiece().getColor() != this.getColor()) {
                    return new MoveResult(MoveType.KILL, board[x1][y1].getPiece());
                }
            }
        }

        return new MoveResult(MoveType.NONE);
    }

    public boolean hasMove() {
        int boardY = (int)(this.oldY / TILE_SIZE);
        int boardX = (int)(this.oldX / TILE_SIZE);
        if (this.color == PieceColor.RED && this.type == PieceType.Normal) {
            if (boardX + 1 < CheckersApp.WIDTH && boardY + 1 < CheckersApp.HEIGHT && this.tryMove(boardX + 1, boardY + 1).getType() != MoveType.NONE) return true;
            else if (boardX - 1 >= 0 && boardY + 1 < CheckersApp.HEIGHT && this.tryMove(boardX - 1, boardY + 1).getType() != MoveType.NONE) return true;
            else if (boardX + 2 < CheckersApp.WIDTH && boardY + 2 < CheckersApp.HEIGHT && this.tryMove(boardX + 2, boardY + 2).getType() != MoveType.NONE) return true;
            if (boardX - 2 >= 0 && boardY + 2 < 8){
                return this.tryMove(boardX - 2, boardY + 2).getType() != MoveType.NONE;
            }
        }
        else if (this.color == PieceColor.WHITE && this.type == PieceType.Normal) {
            if (boardX + 1 < CheckersApp.WIDTH && boardY - 1 >= 0 && this.tryMove(boardX + 1, boardY - 1).getType() != MoveType.NONE) return true;
            else if (boardX - 1 >= 0 && boardY - 1 >= 0 && this.tryMove(boardX - 1, boardY - 1).getType() != MoveType.NONE) return true;
            else if (boardX + 2 < CheckersApp.WIDTH && boardY - 2 >= 0 && this.tryMove(boardX + 2, boardY - 2).getType() != MoveType.NONE) return true;
            if (boardX - 2 >= 0 && boardY - 2 >= 0) {
                return this.tryMove(boardX - 2, boardY - 2).getType() != MoveType.NONE;
            }
        }
        else if (this.type == PieceType.Dame) {
            try {
                if (this.tryMove(boardX + 1, boardY + 1).getType() != MoveType.NONE) return true;
            } catch(Exception ignored) {}
            try {
                if (this.tryMove(boardX - 1, boardY + 1).getType() != MoveType.NONE) return true;
            } catch(Exception ignored) {}
            try {
                if (this.tryMove(boardX + 2, boardY + 2).getType() != MoveType.NONE) return true;
            } catch(Exception ignored) {}
            try {
                if (this.tryMove(boardX - 2, boardY - 2).getType() != MoveType.NONE) return true;
            } catch(Exception ignored) {}
            try {
                if (this.tryMove(boardX + 1, boardY - 1).getType() != MoveType.NONE) return true;
            } catch(Exception ignored) {}
            try {
                if (this.tryMove(boardX - 1, boardY - 1).getType() != MoveType.NONE) return true;
            } catch(Exception ignored) {}
            try {
                if (this.tryMove(boardX + 2, boardY - 2).getType() != MoveType.NONE) return true;
            } catch (Exception ignored) {}
            try {
                return this.tryMove(boardX - 2, boardY + 2).getType() != MoveType.NONE;
            } catch(Exception ignored) {}
        }

        return false;
    }

    public void abortMove() {
        relocate(oldX, oldY);
    }
}