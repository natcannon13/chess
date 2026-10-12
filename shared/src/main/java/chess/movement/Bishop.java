package chess.movement;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Bishop extends RadialMovementRule {

    int[][] directions;
    public Bishop () {
        directions = new int[][]{
                {1, 1},
                {-1, 1},
                {-1, -1},
                {1, -1}
        };
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition) {
        return getMoves(board, piece, myPosition, directions, false);
    }
}
