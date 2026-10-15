package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {

    void clear();
    GameData createGame();
    GameData getGame();
    Collection<GameData> listGames();
    GameData updateGame();

}
