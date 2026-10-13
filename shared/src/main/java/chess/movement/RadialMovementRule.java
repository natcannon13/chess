package chess.movement;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class RadialMovementRule extends ChessMovementRule {

    public RadialMovementRule () {

    }

    public Collection<ChessMove> getMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition, int[][] directions, boolean singleMove) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int vertical = 0;
        int horizontal = 0;
        for (int i = 0; i < directions.length; i++) {
            vertical = directions[i][0];
            horizontal = directions[i][1];

            MoveResultState nextMove = MoveResultState.FREE;
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
                do {
                    row += vertical;
                    col += horizontal;
                    ChessPosition pos = new ChessPosition(row, col);
                    nextMove = moveResult(board, piece, pos);
                    if (nextMove == MoveResultState.FREE || nextMove == MoveResultState.CAPTURE) {
                        moves.add(new ChessMove(myPosition, pos, null));
                    }
                } while (!singleMove && nextMove == MoveResultState.FREE);
        }
        return moves;

    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition){
        return new ArrayList<ChessMove>();
    }
}
