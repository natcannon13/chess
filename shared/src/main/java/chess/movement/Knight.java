package chess.movement;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Knight extends RadialMovementRule {

    int[][] directions;
    public Knight () {
        directions = new int[][]{
                {1, 2},
                {2, 1},
                {-1, 2},
                {2, -1},
                {-1, -2},
                {-2, -1},
                {-2, 1},
                {1, -2}
        };
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition) {
        return getMoves(board, piece, myPosition, directions, true);
    }

}