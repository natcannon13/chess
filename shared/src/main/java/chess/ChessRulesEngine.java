package chess;

public class ChessRulesEngine {

    public ChessRulesEngine(){

    }

    public boolean isInCheck(ChessGame.TeamColor color, ChessBoard board) throws InvalidBoardStateException{
        ChessPosition kingPosition = findKing(color, board);
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPosition searchPosition = new ChessPosition(i, j);
                if(checkHelper(searchPosition, kingPosition, board, color)){
                    return true;
                }
            }
        }
        return false;
    }

    private ChessPosition findKing(ChessGame.TeamColor color, ChessBoard board) throws InvalidBoardStateException{
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPosition searchPosition = new ChessPosition(i, j);
                if(board.hasPiece(searchPosition)){
                    ChessPiece piece = board.getPiece(searchPosition);
                    if(piece.getTeamColor() == color && piece.getPieceType() == ChessPiece.PieceType.KING){
                        return searchPosition;
                    }
                }
            }
        }
        throw new InvalidBoardStateException("Error! No King found.");
    }

    public boolean checkHelper(ChessPosition piecePosition, ChessPosition kingPosition, ChessBoard board, ChessGame.TeamColor color){
        if(!board.hasPiece(piecePosition)){
            return false;
        }
        if(board.getColor(piecePosition) == color){
            return false;
        }
        else{
            ChessPiece movingPiece = board.getPiece(piecePosition);
            for(ChessMove move : movingPiece.pieceMoves(board, piecePosition)){
                ChessPosition endPosition = move.getEndPosition();
                if(endPosition.equals(kingPosition)){
                    return true;
                }
            }
        }
        return false;
    }

}
