package model;

import chess.ChessGame;

import java.util.Objects;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){
    public boolean isFull(String color){
        if(Objects.equals(color, "WHITE")){
            return whiteUsername != null;
        }
        if(Objects.equals(color, "BLACK")){
            return blackUsername != null;
        }
        return false;
    }
}
