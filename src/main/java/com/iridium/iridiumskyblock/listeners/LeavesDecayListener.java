package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.SettingType;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.IslandSetting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;

import java.util.Optional;

public class LeavesDecayListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getBlock().getWorld())) return;
        if (SettingType.LEAF_DECAY.isFeactureValue()) {
            Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());
            if (islandOptional.isEmpty()) {
                event.setCancelled(true);
            } else {
                IslandSetting leafDecaySettings = IridiumSkyblock.getInstance().getIslandManager().getIslandSetting(islandOptional.get(), SettingType.LEAF_DECAY);
                if (!leafDecaySettings.getBooleanValue()) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
