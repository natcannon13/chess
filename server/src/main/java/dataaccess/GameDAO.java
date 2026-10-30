package dataaccess;

import model.GameData;
import java.util.List;

public interface GameDAO {

    void clear() throws DataAccessException;
    boolean isEmpty() throws DataAccessException;
    GameData createGame(GameData gameData) throws DataAccessException;
    GameData getGame(int id) throws DataAccessException;
    List<GameData> listGames() throws DataAccessException;
    void updateGame(GameData newInfo) throws DataAccessException;

}
