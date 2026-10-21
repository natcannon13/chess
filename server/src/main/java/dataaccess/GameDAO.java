package dataaccess;

import model.GameData;
import java.util.List;

public interface GameDAO {

    void clear();
    boolean isEmpty();
    GameData createGame(GameData gameData);
    GameData getGame(int id);
    List<GameData> listGames();
    void updateGame(GameData newInfo);

}
