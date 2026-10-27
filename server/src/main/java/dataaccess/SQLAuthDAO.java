package dataaccess;

import model.AuthData;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{


    public SQLAuthDAO() throws DataAccessException {
        this.initialize();
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
            throw new DataAccessException("Error: ");
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
            throw new DataAccessException("Error: ");
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
            throw new DataAccessException("Error: ");
        }
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auth VALUES ('"
                + authData.authToken()
                + "', '"
                + authData.username()
                + "')";
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
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {

    }
}
