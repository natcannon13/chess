package chess.movement;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class King extends ChessMovementRule {

    public King () {
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        for (int i = 0; i < 8; i++){
            int vertical = 0;
            int horizontal = 0;
            switch (i){
                case 0:
                    vertical = 1;
                    horizontal = 0;
                    break;
                case 1:
                    vertical = 1;
                    horizontal = 1;
                    break;
                case 2:
                    vertical = 0;
                    horizontal = 1;
                    break;
                case 3:
                    vertical = -1;
                    horizontal = 1;
                    break;
                case 4:
                    vertical = -1;
                    horizontal = 0;
                    break;
                case 5:
                    vertical = -1;
                    horizontal = -1;
                    break;
                case 6:
                    vertical = 0;
                    horizontal = -1;
                    break;
                case 7:
                    vertical = 1;
                    horizontal = -1;
                    break;
            }
            int row = myPosition.getRow();
            int col = myPosition.getColumn();

            row += vertical;
            col += horizontal;
            ChessPosition pos = new ChessPosition(row, col);
            MoveResultState nextMove = moveResult(board, piece, pos);
            if (nextMove == MoveResultState.FREE || nextMove == MoveResultState.CAPTURE) {
                moves.add(new ChessMove(myPosition, pos, null));
            }
        }
        return moves;
    }
}
