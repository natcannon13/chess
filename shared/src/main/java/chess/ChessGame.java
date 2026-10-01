package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor turn;
    private ChessRulesEngine rules;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        rules = new ChessRulesEngine();
        turn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition){
        ChessPiece piece = board.getPiece(startPosition);
        if(piece != null) {
            Collection<ChessMove> possibleMoves = piece.pieceMoves(board, startPosition);
            Collection<ChessMove> allValidMoves = new ArrayList<>();
            for(ChessMove move: possibleMoves){
                if(rules.canMakeMove(piece.getTeamColor(), move, board)){
                    allValidMoves.add(move);
                }
            }
            return allValidMoves;
        }
        else return null;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        if(moves == null || !moves.contains(move)) {
            throw new InvalidMoveException("Invalid Move");
        }
        else {
            TeamColor color = board.getColor(move.getStartPosition());
            if(color != turn){
                throw new InvalidMoveException("Out of Turn");
            }
            if(!rules.canMakeMove(color, move, board)){
                throw new InvalidMoveException("Move results in Check.");
            }
            board.movePiece(move);
            changeTurn();
            ChessPiece.PieceType promotion = move.getPromotionPiece();
            if(promotion != null){
                board.promotePiece(move.getEndPosition(), promotion);
            }
        }
    }

    public void changeTurn(){
        if(turn == TeamColor.BLACK){
            turn = TeamColor.WHITE;
        }
        else if(turn == TeamColor.WHITE){
            turn = TeamColor.BLACK;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        try {
            return rules.isInCheck(teamColor, board);
        }
        catch(InvalidBoardStateException e){
            System.err.println(e);
            return false;
        }
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        try{
            return rules.isInCheckmate(teamColor, board);
        }
        catch(InvalidBoardStateException e){
            System.out.println(e);
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            return false;
        }
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPosition searchPosition = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(searchPosition);
                if(piece != null && piece.getTeamColor() == teamColor){
                    if (!validMoves(searchPosition).isEmpty()){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && turn == chessGame.turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, turn);
    }
}
