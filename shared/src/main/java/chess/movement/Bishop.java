package chess.movement;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

public class Bishop extends ChessMovementRule {

    public Bishop () {
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        for (int i = 0; i < 4; i++){
            int vertical = 0;
            int horizontal = 0;
            switch (i){
                case 0:
                    vertical = 1;
                    horizontal = 1;
                    break;
                case 1:
                    vertical = -1;
                    horizontal = 1;
                    break;
                case 2:
                    vertical = -1;
                    horizontal = -1;
                    break;
                case 3:
                    vertical = 1;
                    horizontal = -1;
            }
            MoveResultState nextMove = MoveResultState.FREE;
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            while(nextMove == MoveResultState.FREE){
                row += vertical;
                col += horizontal;
                ChessPosition pos = new ChessPosition(row, col);
                nextMove = moveResult(board, piece, pos);
                if (nextMove == MoveResultState.FREE || nextMove == MoveResultState.CAPTURE){
                    moves.add(new ChessMove(myPosition, pos, null));
                }
            }
        }
        return moves;
    }

}
