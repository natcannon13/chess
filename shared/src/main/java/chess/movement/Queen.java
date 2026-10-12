package chess.movement;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Queen extends RadialMovementRule {

    int[][] directions;
    public Queen () {
        directions = new int[][]{
                {1, 0},
                {1, 1},
                {0, 1},
                {-1, 1},
                {-1, 0},
                {-1, -1},
                {0, -1},
                {1, -1}
        };
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition) {
        return getMoves(board, piece, myPosition, directions, false);
    }
}
