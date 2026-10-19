package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{

    private Map<Integer, GameData> gameDataMemory;

    public MemoryGameDAO(){
        gameDataMemory = new HashMap<>();
    }

    @Override
    public void clear() {
        gameDataMemory.clear();
    }

    @Override
    public boolean isEmpty() {
        return gameDataMemory.isEmpty();
    }

    @Override
    public GameData createGame(GameData gameData) {
        return gameDataMemory.put(gameData.gameID(), gameData);
    }

    @Override
    public GameData getGame(int ID) {
        return gameDataMemory.get(ID);
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public GameData updateGame() {
        return null;
    }
}
