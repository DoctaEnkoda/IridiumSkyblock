package com.iridium.iridiumskyblock.listeners;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import com.iridium.iridiumskyblock.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Optional;

public class PlayerTeleportListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (event.getTo() == null) return;
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getTo().getWorld())) return;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(event.getPlayer());
        Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getTo());
        if (islandOptional.isPresent()) {
            Island island = islandOptional.get();
            if (!event.getPlayer().hasPermission("iridiumskyblock.bypassban") && IridiumSkyblock.getInstance().getIslandManager().isBannedOnIsland(island, user)) {
                event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().youHaveBeenBanned
                        .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                        .replace("%owner%", island.getOwner().getName())
                        .replace("%name%", island.getName())
                ));
                event.setCancelled(true);
            } else {
                Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () ->
                                PlayerUtils.sendBorder(event.getPlayer(), island)
                        , 1);
            }
        } else {
            event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIslandFound
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            event.setCancelled(true);
        }
    }

}
