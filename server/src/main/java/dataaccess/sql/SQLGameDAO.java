package dataaccess.sql;

import chess.ChessGame;
import chess.ChessMovementRule;
import chess.ChessPiece;
import chess.movement.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.SQLException;
import java.util.List;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        initialize();
    }

    private void initialize() throws DataAccessException {
        var statement = "CREATE TABLE IF NOT EXISTS game (" +
                "gameID INT," +
                "whiteUsername VARCHAR(255), " +
                "blackUsername VARCHAR(255)," +
                "gameName VARCHAR(255)," +
                "game TEXT" +
                ")";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e){
            throw new DataAccessException("Error: ");
        }
    }

    @Override
    public void clear() throws DataAccessException{
        var statement = "TRUNCATE TABLE game";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e){
            throw new DataAccessException("Error: ");
        }
    }

    @Override
    public boolean isEmpty() throws DataAccessException{
        var statement = "SELECT 1 FROM game LIMIT 1";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                var rs = preparedStatement.executeQuery();
                return(!rs.next());
            }
        }
        catch(SQLException e){
            throw new DataAccessException("Error: ");
        }
    }

    @Override
    public GameData createGame(GameData gameData) throws DataAccessException{
        return null;
    }

    @Override
    public GameData getGame(int id) throws DataAccessException{
        return null;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException{
        return List.of();
    }

    @Override
    public void updateGame(GameData newInfo) throws DataAccessException{

    }

    public ChessGame deserialize(String gameJson){
        var deserializer = createDeserializer();
        return deserializer.fromJson(gameJson, ChessGame.class);
    }

    public String serialize(ChessGame game){
        var serializer = new Gson();
        return serializer.toJson(game);
    }

    private static Gson createDeserializer(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ChessPiece.class, (JsonDeserializer<ChessPiece>)(el, type, ctx) -> {
            ChessPiece chessPiece = null;
            if(el.isJsonObject()){
                ChessPiece.PieceType pieceType = ctx.deserialize(el.getAsJsonObject().get("type"), ChessPiece.PieceType.class);
                ChessGame.TeamColor color = ctx.deserialize(el.getAsJsonObject().get("pieceColor"), ChessGame.TeamColor.class);
                chessPiece = new ChessPiece(color, pieceType);
            }
            return chessPiece;
        });
        return gsonBuilder.create();
    }
}
