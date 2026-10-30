package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        initialize();
    }

    private void initialize() throws DataAccessException {
        var statement = "CREATE TABLE IF NOT EXISTS user (" +
                "username VARCHAR(255), " +
                "password VARCHAR(255), " +
                "email VARCHAR(255)" +
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
        var statement = "TRUNCATE TABLE user";
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
    public boolean isEmpty() throws DataAccessException {
        var statement = "SELECT 1 FROM user LIMIT 1";
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
    public UserData createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO user VALUES (?, ?, ?)";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, userData.password());
                preparedStatement.setString(3, userData.email());
                preparedStatement.executeUpdate();
                return new UserData(userData.username(), userData.password(), userData.email());
            }
        }
        catch(SQLException e) {
            throw new DataAccessException("Error: ");
        }
    }



    @Override
    public UserData getUser(String username) throws DataAccessException {
        var statement = "SELECT * FROM user WHERE username=?";
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                preparedStatement.setString(1, username);
                var rs = preparedStatement.executeQuery();
                if(rs.next()){
                    String name = rs.getString("username");
                    String password = rs.getString("password");
                    String email = rs.getString("email");
                    return new UserData(name, password, email);
                }
                return null;
            }
        } catch(SQLException e) {
            throw new DataAccessException("Error: ");
        }
    }
}
