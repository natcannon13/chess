package dataaccess;

import model.AuthData;

import java.sql.SQLException;
import java.sql.Statement;

public class SQLAuthDAO implements AuthDAO {


    public SQLAuthDAO() throws DataAccessException {
        initialize();
    }

    private void initialize() throws DataAccessException {
        var statement = "CREATE TABLE IF NOT EXISTS auth (" +
                "authToken VARCHAR(255), " +
                "username VARCHAR(255)" +
                ")";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e){
            throw new DataAccessException("Error: internal server error");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE auth";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e){
            throw new DataAccessException("Error: internal server error");
        }
    }

    @Override
    public boolean isEmpty() throws DataAccessException{
        var statement = "SELECT 1 FROM auth LIMIT 1";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement)){
                var rs = preparedStatement.executeQuery();
                return(!rs.next());
            }
        }
        catch(SQLException e){
            throw new DataAccessException("Error: internal server error");
        }
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auth VALUES (?, ?)";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                preparedStatement.setString(1, authData.authToken());
                preparedStatement.setString(2, authData.username());
                preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e){
            throw new DataAccessException("Error: internal server error");
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var statement = "SELECT * FROM auth WHERE authToken=?";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                preparedStatement.setString(1, authToken);
                var rs = preparedStatement.executeQuery();
                if(rs.next()){
                    String token = rs.getString("authToken");
                    String username = rs.getString("username");
                    return new AuthData(token, username);
                }
                return null;
            }
        }
        catch(SQLException e){
            throw new DataAccessException("Error: internal server error");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken=?";
        try(var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, authToken);
                var rs = preparedStatement.executeUpdate();
            }
        }
        catch(SQLException e){
            throw new DataAccessException("Error: internal server error");
        }
    }
}
