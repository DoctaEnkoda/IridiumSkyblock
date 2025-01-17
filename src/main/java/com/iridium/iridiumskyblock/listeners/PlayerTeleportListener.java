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
                if (!event.getPlayer().hasPermission("iridiumskyblock.locationisland.bypass")){
                    event.setCancelled(true);
                }
            } else {
                if (event.getPlayer().hasPermission("iridiumskyblock.teleportisland.bypass") || island.isVisitable() || (user.isBypassing() || island.getMembers().contains(user))) {
                    Bukkit.getScheduler().runTaskLater(IridiumSkyblock.getInstance(), () ->
                                    PlayerUtils.sendBorder(event.getPlayer(), island)
                            , 1);
                } else {
                    if (!event.getPlayer().hasPermission("iridiumskyblock.locationisland.bypass")){
                        event.setCancelled(true);
                    }
                    event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().islandIsPrivate
                            .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)
                            .replace("%owner%", island.getOwner().getName())
                            .replace("%name%", island.getName())));
                }
            }
        } else {
            event.getPlayer().sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIslandFound
                    .replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            if (!event.getPlayer().hasPermission("iridiumskyblock.locationisland.bypass")){
                event.setCancelled(true);
            }
        }
    }

}
