package dataaccess;

import model.GameData;

import java.util.*;

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
    public GameData getGame(int id) {
        return gameDataMemory.get(id);
    }

    @Override
    public List<GameData> listGames() {
        return new ArrayList<>(gameDataMemory.values());
    }

    @Override
    public void updateGame(GameData newInfo) {
        gameDataMemory.replace(newInfo.gameID(), newInfo);
    }
}
