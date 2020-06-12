/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;


public class Board {

    private static final short BLANK_TILE = 0;
    // My Board
    // Also a 1D array instead of 2D array to speed up array accessing
    private final int[] board;
    // Row length
    private final int n;
    private int manhat;
    private int hamm;
    private int blankPos;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException();
        }
        else {
            n = tiles.length;
            this.board = new int[n * n];
            manhat = 0;
            hamm = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    this.board[i * n + j] = tiles[i][j];
                    if (tiles[i][j] == BLANK_TILE) blankPos = i * n + j;
                    precalculate(i * n + j);
                }
            }
        }
    }

    private void precalculate(int idx) {
        if (this.board[idx] != BLANK_TILE) {
            int currentRow = (int) Math.floor((double) idx / n);
            int currentCol = idx % n;
            int correctRow = (int) Math.floor((double) (this.board[idx] - 1) / n);
            int correctCol = (this.board[idx] - 1) % n;
            int offset = Math.abs(correctRow - currentRow) + Math
                    .abs(correctCol - currentCol);
            if (offset > 0) {
                hamm++;
                manhat += offset;
            }
        }
    }


    // string representation of this board
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                builder.append(String.format("%2d ", this.board[i * n + j]));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hamm;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhat;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < ((n * n) - 1); i++) {
            if (this.board[i] != i + 1) {
                return false;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Board that = (Board) other;
        return Arrays.equals(this.board, that.board);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> list = new ArrayList<Board>();
        if (isTopEdge(blankPos)) {
            if (isTopLeftCorner(blankPos)) {
                list.add(new Board(exch(this.board, blankPos, blankPos + 1)));
                list.add(new Board(exch(this.board, blankPos, blankPos + n)));
            }
            else if (isTopRightCorner(blankPos)) {
                list.add(new Board(exch(this.board, blankPos, blankPos - 1)));
                list.add(new Board(exch(this.board, blankPos, blankPos + n)));
            }
            else {
                list.add(new Board(exch(this.board, blankPos, blankPos - 1)));
                list.add(new Board(exch(this.board, blankPos, blankPos + 1)));
                list.add(new Board(exch(this.board, blankPos, blankPos + n)));
            }
        }
        else if (isBottomEdge(blankPos)) {
            if (isBottomLeftCorner(blankPos)) {
                list.add(new Board(exch(this.board, blankPos, blankPos + 1)));
                list.add(new Board(exch(this.board, blankPos, blankPos - n)));
            }
            else if (isBottomRightCorner(blankPos)) {
                list.add(new Board(exch(this.board, blankPos, blankPos - 1)));
                list.add(new Board(exch(this.board, blankPos, blankPos - n)));
            }
            else {
                list.add(new Board(exch(this.board, blankPos, blankPos - 1)));
                list.add(new Board(exch(this.board, blankPos, blankPos + 1)));
                list.add(new Board(exch(this.board, blankPos, blankPos - n)));
            }
        }
        else if (isRightEdge(blankPos)) {
            list.add(new Board(exch(this.board, blankPos, blankPos - n)));
            list.add(new Board(exch(this.board, blankPos, blankPos + n)));
            list.add(new Board(exch(this.board, blankPos, blankPos - 1)));
        }
        else if (isLeftEdge(blankPos)) {
            list.add(new Board(exch(this.board, blankPos, blankPos - n)));
            list.add(new Board(exch(this.board, blankPos, blankPos + n)));
            list.add(new Board(exch(this.board, blankPos, blankPos + 1)));
        }
        else {
            list.add(new Board(exch(this.board, blankPos, blankPos - 1)));
            list.add(new Board(exch(this.board, blankPos, blankPos + 1)));
            list.add(new Board(exch(this.board, blankPos, blankPos - n)));
            list.add(new Board(exch(this.board, blankPos, blankPos + n)));
        }
        return list;
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board twin = null;
        for (int i = 0; i < n * n - 1; i++) {
            if (this.board[i] != BLANK_TILE && this.board[i + 1] != BLANK_TILE) {
                twin = new Board(exch(this.board, i, i + 1));
                break;
            }
        }
        return twin;
    }

    private boolean isTopEdge(int index) {
        if (index < n * n) {
            return index < n;
        }
        else throw new ArrayIndexOutOfBoundsException("Couldnt't find the blank tile!");
    }

    private boolean isBottomEdge(int index) {
        if (index < n * n) {
            return (index < n * n) && (index > (n * n) - n - 1);
        }
        else throw new ArrayIndexOutOfBoundsException("Couldnt't find the blank tile!");
    }

    private boolean isRightEdge(int index) {
        if (index < n * n) {
            return (index + 1) % n == 0;
        }
        else throw new ArrayIndexOutOfBoundsException("Couldnt't find the blank tile!");
    }

    private boolean isLeftEdge(int index) {
        if (index < n * n) {
            return index % n == 0;
        }
        else throw new ArrayIndexOutOfBoundsException("Couldnt't find the blank tile!");
    }

    private boolean isTopRightCorner(int index) {
        return index == n - 1;
    }

    private boolean isTopLeftCorner(int index) {
        return index == 0;
    }

    private boolean isBottomRightCorner(int index) {
        return index == (n * n) - 1;
    }

    private boolean isBottomLeftCorner(int index) {
        return index == (n * n) - n;
    }

    private int[][] exch(int[] a, int idx1, int idx2) {
        int[] cpy = Arrays.copyOf(a, a.length);
        int temp = cpy[idx1];
        cpy[idx1] = cpy[idx2];
        cpy[idx2] = temp;
        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = cpy[i * n + j];
            }
        }
        return result;
    }


    public static void main(String[] args) {
        int[][] tiles = new int[3][3];
        tiles[0][0] = 8;
        tiles[0][1] = 1;
        tiles[0][2] = 3;
        tiles[1][0] = 4;
        tiles[1][1] = 0;
        tiles[1][2] = 2;
        tiles[2][0] = 7;
        tiles[2][1] = 6;
        tiles[2][2] = 5;
        Board myBoard = new Board(tiles);
        System.out.println(myBoard.hamming());
        System.out.println(myBoard.toString());
        System.out.println(myBoard.dimension());
        System.out.println(myBoard.hamming());
        System.out.println(myBoard.manhattan());
        System.out.println(myBoard.isGoal());
        System.out.println(myBoard.equals(myBoard));
        for (Board board : myBoard.neighbors()) {
            System.out.println(board.toString());
        }
        System.out.println(myBoard.twin().toString());
    }

}
