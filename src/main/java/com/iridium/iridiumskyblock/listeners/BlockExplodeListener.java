package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.SettingType;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandSetting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

import java.util.Optional;

public class BlockExplodeListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getBlock().getWorld())) return;
        Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());
        if (islandOptional.isEmpty()) {
            event.setCancelled(true);
        } else {
            Island island = islandOptional.get();
            IslandSetting tntExplosion = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(island, SettingType.TNT_DAMAGE);
            if (SettingType.TNT_DAMAGE.isFeactureValue() && !tntExplosion.getBooleanValue()) {
                event.setCancelled(true);
                return;
            }
            event.blockList().removeIf(block -> !island.isInIsland(block.getLocation()));
        }
    }


}
