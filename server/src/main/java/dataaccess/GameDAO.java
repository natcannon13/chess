package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {

    void clear();
    boolean isEmpty();
    GameData createGame(GameData gameData);
    GameData getGame(int ID);
    Collection<GameData> listGames();
    GameData updateGame();

}
