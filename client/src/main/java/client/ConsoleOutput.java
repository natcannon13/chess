package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import ui.EscapeSequences;

import java.util.List;

public class ConsoleOutput {

    public static void helpPreLogin(){
        String[] commands = {
                "register <USERNAME> <PASSWORD> <EMAIL>",
                "login <USERNAME> <PASSWORD>",
                "quit",
                "help"
        };
        String[] descriptions = {
                " - creates an account",
                " - logs in to an existing account",
                " - exits the program",
                " - displays this menu"
        };

        for(int i = 0; i < commands.length; i++) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
            System.out.print(commands[i]);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            System.out.println(descriptions[i]);
        }
    }

    public static void helpPostLogin(){
        String[] commands = {
                "create <NAME>",
                "list",
                "join <ID> [WHITE|BLACK]",
                "observe <ID> [WHITE|BLACK]",
                "logout",
                "quit",
                "help"
        };
        String[] descriptions = {
                " - creates a new game",
                " - lists existing games",
                " - joins a game as a player",
                " - joins a game as an observer",
                " - logs out",
                " - exits the program",
                " - displays this menu"
        };

        for(int i = 0; i < commands.length; i++) {
            System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
            System.out.print(commands[i]);
            System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            System.out.println(descriptions[i]);
        }
    }

    public static void printPrompt(Repl.State state){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.print("[" + state.toString() + "] >>> ");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

    public static void invalidCommand(){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        System.out.println("Error: Invalid command. Run help to see a list of commands.");
    }

    public static void printError(String msg){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        System.out.println(msg);
    }

    public static void printRegister(String username){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        System.out.println("Account " + username + " successfully registered!");
    }

    public static void printLogin(String username){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        System.out.println("Login successful. Welcome, " + username);
    }

    public static void printArgumentError(){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
        System.out.println("Error: Invalid arguments. Run help to see command usage.");
    }

    public static void printLogout(){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        System.out.println("Logged out successfully.");
    }

    public static void printGameCreated(int id){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_GREEN);
        System.out.println("New game created with ID: " + id);
    }

    public static void printGameList(List<GameData> data){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        for(int i = 0; i < data.size(); i++){
            GameData gameData = data.get(i);
            System.out.println("ID: " + (i + 1) + "    Game Name: " + gameData.gameName());
            System.out.println("Players:    White: " + gameData.whiteUsername() + "    Black: " + gameData.blackUsername());
        }
    }

    public static void renderChessBoard(ChessBoard board, ChessGame.TeamColor perspective){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        if(perspective == ChessGame.TeamColor.WHITE){
            System.out.println("   a   b   c  d   e  f   g   h");
            for(int i = 7; i >= 0 ; i--){
                printRow(board, i, perspective);
            }
            System.out.println("   a   b   c  d   e  f   g   h");
        }
        else{
            System.out.println("   h   g   f  e   d  c   b   a");
            for(int i = 0; i < 8; i++){
                printRow(board, i, perspective);
            }
            System.out.println("   h   g   f  e   d  c   b   a");
        }
    }

    private static void printRow(ChessBoard board, int i, ChessGame.TeamColor perspective){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.print(i + 1 + " ");
        if(perspective == ChessGame.TeamColor.WHITE){
            for(int j = 0; j < 8; j++){
                printCharacter(board, i, j);
            }
        }
        else{
            for(int j = 7; j >= 0; j--){
                printCharacter(board, i, j);
            }
        }
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        System.out.println(" " + (i + 1));
    }

    private static void printCharacter(ChessBoard board, int i, int j){
        System.out.print(calculateBackgroundColor(i, j));
        ChessPiece piece = board.getPiece(new ChessPosition(i + 1, j + 1));
        if(piece != null) {
            System.out.print(calculatePieceColor(piece));
            System.out.print(calculatePiece(piece));
        }
        else{
            System.out.print(EscapeSequences.EMPTY);
        }
    }

    private static String calculateBackgroundColor(int i, int j){
        if((i + j) % 2 == 0){
            return EscapeSequences.SET_BG_COLOR_BLACK;
        }
        return EscapeSequences.SET_BG_COLOR_WHITE;
    }

    private static String calculatePieceColor(ChessPiece piece){
        ChessGame.TeamColor color = piece.getTeamColor();
        if(color == ChessGame.TeamColor.BLACK){
            return EscapeSequences.SET_TEXT_COLOR_BLUE;
        }
        else{
            return EscapeSequences.SET_TEXT_COLOR_RED;
        }
    }

    private static String calculatePiece(ChessPiece piece){
        switch(piece.getPieceType()){
            case PAWN -> {
                return EscapeSequences.BLACK_PAWN;
            }
            case ROOK -> {
                return EscapeSequences.BLACK_ROOK;
            }
            case KNIGHT -> {
                return EscapeSequences.BLACK_KNIGHT;
            }
            case KING -> {
                return EscapeSequences.BLACK_KING;
            }
            case QUEEN -> {
                return EscapeSequences.BLACK_QUEEN;
            }
            case BISHOP -> {
                return EscapeSequences.BLACK_BISHOP;
            }
        }
        return null;
    }

}
