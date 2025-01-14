package com.iridium.iridiumskyblock.managers.tablemanagers.database;

import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandBan;
import com.iridium.iridiumskyblock.managers.tablemanagers.TableManager;
import com.j256.ormlite.support.ConnectionSource;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.*;

public class IslandBanTableManager extends TableManager<IslandBan, Integer> {

    LinkedHashMap<Integer, List<IslandBan>> islandBanById = new LinkedHashMap<>();

    public IslandBanTableManager(ConnectionSource connectionSource, Class<IslandBan> clazz, Comparator<IslandBan> comparing) throws SQLException {
        super(connectionSource, clazz, comparing);
        List<IslandBan> banList = getEntries();
        for (int i = 0, bansSize = banList.size(); i < bansSize; i++) {
            IslandBan ban = banList.get(i);
            List<IslandBan> banks = islandBanById.getOrDefault(ban.getIslandId(), new ArrayList<>());
            banks.add(ban);
            islandBanById.put(ban.getIslandId(), banks);
        }
    }

    @Override
    public void addEntry(IslandBan islandBan) {
        islandBan.setChanged(true);
        List<IslandBan> islandBans = islandBanById.getOrDefault(islandBan.getIslandId(), new ArrayList<>());
        islandBans.add(islandBan);
        islandBanById.put(islandBan.getIslandId(), islandBans);
    }

    @Override
    public void delete(IslandBan islandBan) {
        List<IslandBan> islandBans = islandBanById.getOrDefault(islandBan.getIslandId(), new ArrayList<>());
        islandBans.remove(islandBan);
        islandBanById.put(islandBan.getIslandId(), islandBans);
        super.delete(islandBan);
    }

    @Override
    public void clear() {
        islandBanById.clear();
        super.clear();
    }

    public Optional<IslandBan> getEntry(IslandBan islandBan) {
        List<IslandBan> islandBans = islandBanById.getOrDefault(islandBan.getIslandId(), new ArrayList<>());
        for (IslandBan ban : islandBans) {
            if (ban.getBannedUser() != null) {
                if (islandBan.getBannedUser() != null) {
                    if (ban.getBannedUser().getUuid().equals(islandBan.getBannedUser().getUuid())) return Optional.of(ban);
                }
            }
        }
        return Optional.empty();
    }

    public LinkedHashMap<Integer, List<IslandBan>> getIslandBanById() {
        return islandBanById;
    }

    /**
     * Gets all entries associated with an island
     *
     * @param island the specified island
     */
    public List<IslandBan> getEntries(@NotNull Island island) {
        return islandBanById.getOrDefault(island.getId(), new ArrayList<>());
    }

    public List<IslandBan> deleteDataInHashMap(Island island) {
        List<IslandBan> islandBans = islandBanById.getOrDefault(island.getId(), new ArrayList<>());
        islandBanById.remove(island.getId());
        return islandBans;
    }

    @Override
    public void delete(Collection<IslandBan> islandBans) {
        super.delete(islandBans);
    }


}
