package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        initialize();
    }

    private void initialize() throws DataAccessException {
        var statement = "CREATE TABLE IF NOT EXISTS user (" +
                "username VARCHAR(255), " +
                "password VARCHAR(255)" +
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
        return false;
    }

    @Override
    public UserData createUser(UserData userData) throws DataAccessException {
        return null;
    }



    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }
}
