package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.SettingType;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandSetting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.Optional;

public class EntityExplodeListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getEntity().getWorld())) return;
        List<MetadataValue> list = event.getEntity().getMetadata("island_spawned");
        if (list.isEmpty()) return;
        int islandId = list.get(0).asInt();
        Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandById(islandId);
        if (islandOptional.isEmpty()) {
            event.setCancelled(true);
        } else {
            Island island = islandOptional.get();
            IslandSetting tntExplosion = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, SettingType.TNT_DAMAGE);
            if (SettingType.TNT_DAMAGE.isFeactureValue() && !tntExplosion.getBooleanValue()) {
                event.setCancelled(true);
            }
            event.blockList().removeIf(block -> !island.isInIsland(block.getLocation()));
        }

    }

}
