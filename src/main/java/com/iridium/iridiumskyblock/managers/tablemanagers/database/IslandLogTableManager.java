package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandLog;
import com.iridium.iridiumskyblock.database.IslandUpgrade;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class IslandLogTableManager extends TableManager<IslandLog, Integer> {

    LinkedHashMap<Integer, List<IslandLog>> islandLogById = new LinkedHashMap<>();

    public IslandLogTableManager(ConnectionSource connectionSource, Class<IslandLog> clazz, Comparator<IslandLog> comparator) throws SQLException {
        super(connectionSource, clazz, comparator);
        List<IslandLog> islandLogsStatic = getEntries();
        for (int i = 0, islandLogsSize = islandLogsStatic.size(); i < islandLogsSize; i++) {
            IslandLog log = islandLogsStatic.get(i);
            List<IslandLog> logs = islandLogById.getOrDefault(log.getIslandId(), new ArrayList<>());
            logs.add(log);
            islandLogById.put(log.getIslandId(), logs);
        }
    }

    @Override
    public void addEntry(IslandLog islandLog) {
        islandLog.setChanged(true);
        List<IslandLog> islandLogs = islandLogById.getOrDefault(islandLog.getIslandId(), new ArrayList<>());
        islandLogs.add(islandLog);
        islandLogById.put(islandLog.getIslandId(), islandLogs);
    }

    @Override
    public void delete(IslandLog islandLog) {
        List<IslandLog> islandLogs = islandLogById.getOrDefault(islandLog.getIslandId(), new ArrayList<>());
        islandLogs.remove(islandLog);
        islandLogById.put(islandLog.getIslandId(), islandLogs);
        super.delete(islandLog);
    }

    @Override
    public void clear() {
        islandLogById.clear();
        super.clear();
    }

    public LinkedHashMap<Integer, List<IslandLog>> getIslandLogById() {
        return islandLogById;
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<IslandLog> getEntries(@NotNull Island island) {
        return islandLogById.getOrDefault(island.getId(), new ArrayList<>());
    }

    public List<IslandLog> deleteDataInHashMap(Island island) {
        List<IslandLog> islandLogs = islandLogById.getOrDefault(island.getId(), new ArrayList<>());
        islandLogById.remove(island.getId());
        return islandLogs;
    }

    @Override
    public void delete(Collection<IslandLog> data) {
        super.delete(data);
    }
}
