package dataaccess.sql;

import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
        catch(SQLException | DataAccessException e){
            throw new DataAccessException("Error: internal server error");
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
        catch(SQLException | DataAccessException e){
            throw new DataAccessException("Error: internal server error");
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
        catch(SQLException | DataAccessException e){
            throw new DataAccessException("Error: internal server error");
        }
    }

    @Override
    public GameData createGame(GameData gameData) throws DataAccessException{
        var statement = "INSERT INTO game VALUES (?, ?, ?, ?, ?)";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                preparedStatement.setInt(1, gameData.gameID());
                preparedStatement.setString(2, gameData.whiteUsername());
                preparedStatement.setString(3, gameData.blackUsername());
                preparedStatement.setString(4, gameData.gameName());
                preparedStatement.setString(5, serialize(gameData.game()));
                preparedStatement.executeUpdate();
            }
        } catch(SQLException | DataAccessException e){
            throw new DataAccessException("Error: internal server error");
        }
        return gameData;
    }

    @Override
    public GameData getGame(int id) throws DataAccessException{
        var statement = "SELECT * FROM game WHERE gameID=?";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                preparedStatement.setInt(1, id);
                var rs = preparedStatement.executeQuery();
                if(rs.next()){
                    int gameID = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    ChessGame game = deserialize(rs.getString("game"));
                    return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                }
                return null;
            }
        } catch(SQLException | DataAccessException e){
            throw new DataAccessException("Error: internal server error");
        }
    }

    @Override
    public List<GameData> listGames() throws DataAccessException{
        List<GameData> gameList = new ArrayList<>();
        var statement = "SELECT * FROM game";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                var rs = preparedStatement.executeQuery();
                while(rs.next()){
                    int gameID = rs.getInt("gameID");
                    String whiteUsername = rs.getString("whiteUsername");
                    String blackUsername = rs.getString("blackUsername");
                    String gameName = rs.getString("gameName");
                    ChessGame game = deserialize(rs.getString("game"));
                    gameList.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
                }
            }
            return gameList;
        } catch(SQLException | DataAccessException e){
            throw new DataAccessException("Error: internal server error");
        }
    }

    @Override
    public void updateGame(GameData newInfo) throws DataAccessException{
        var statement = "UPDATE game SET whiteUsername = ?, blackUsername = ?, game = ? WHERE gameID = ?";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, newInfo.whiteUsername());
                preparedStatement.setString(2, newInfo.blackUsername());
                preparedStatement.setString(3, serialize(newInfo.game()));
                preparedStatement.setInt(4, newInfo.gameID());
                preparedStatement.executeUpdate();
            }
        } catch(SQLException | DataAccessException e){
            throw new DataAccessException("Error: internal server error");
        }
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
