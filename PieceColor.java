package Checker;

public enum PieceColor {
    RED(1), WHITE(-1);

    final int moveDir;

    PieceColor(int moveDir) {
        this.moveDir = moveDir;
    }
}
