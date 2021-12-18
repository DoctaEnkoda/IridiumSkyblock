package com.iridium.iridiumskyblock.managers.tablemanagers;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.*;

/**
 * Used for handling Crud operations on a table + handling cache
 */
public class IslandTableManager extends TableManager<Island, Integer> {

    private final LinkedHashMap<Integer, Island> islandLinkedHashMap = new LinkedHashMap<>();

    public IslandTableManager(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Island.class, Comparator.comparing(Island::getId));
        int size = getEntries().size();
        for (int i = 0; i < size; i++) {
            Island island = getEntries().get(i);
            islandLinkedHashMap.put(island.getId(), island);
        }
    }

    @Override
    public void addEntry(Island island) {
        if (islandLinkedHashMap.containsValue(island)) {
            System.out.println("Alerte Tentative de Duplication d'entrée !");
            return;
        }
        islandLinkedHashMap.put(island.getId(), island);
    }

    public Optional<Island> getIsland(int id) {
        Island island = islandLinkedHashMap.get(id);
        if (island == null) return Optional.empty();
        return Optional.of(island);
    }

    public LinkedHashMap<Integer, Island> getIslandLinkedHashMap() {
        return islandLinkedHashMap;
    }
}
