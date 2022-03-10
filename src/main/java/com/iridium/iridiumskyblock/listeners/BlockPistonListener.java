package com.iridium.iridiumskyblock.listeners;

import com.google.common.collect.ImmutableMap;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.api.IridiumSkyblockAPI;
import com.iridium.iridiumskyblock.database.Island;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;

import java.util.Map;
import java.util.Optional;

public class BlockPistonListener implements Listener {

    private static final Map<BlockFace, int[]> offsets = ImmutableMap.<BlockFace, int[]>builder()
            .put(BlockFace.EAST, new int[]{1, 0, 0})
            .put(BlockFace.WEST, new int[]{-1, 0, 0})
            .put(BlockFace.UP, new int[]{0, 1, 0})
            .put(BlockFace.DOWN, new int[]{0, -1, 0})
            .put(BlockFace.SOUTH, new int[]{0, 0, 1})
            .put(BlockFace.NORTH, new int[]{0, 0, -1})
            .build();

    @EventHandler(ignoreCancelled = true)
    public void onBlockPistonExtend(BlockPistonExtendEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getBlock().getWorld())) return;

        Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());
        if (islandOptional.isPresent()) {
            for (Block block : event.getBlocks()) {
                int[] offset = offsets.get(event.getDirection());
                if (!islandOptional.get().isInIsland(block.getLocation().add(offset[0], offset[1], offset[2]))) {
                    event.setCancelled(true);
                    return;
                }
            }
        } else {
            event.setCancelled(true);
        }

    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPistonRetract(BlockPistonRetractEvent event) {
        if (!IridiumSkyblockAPI.getInstance().isIslandWorld(event.getBlock().getWorld())) return;

        Optional<Island> islandOptional = IridiumSkyblock.getInstance().getIslandManager().getIslandViaLocation(event.getBlock().getLocation());
        if (islandOptional.isPresent()) {
            for (Block block : event.getBlocks()) {
                if (!islandOptional.get().isInIsland(block.getLocation())) {
                    event.setCancelled(true);
                    return;
                }
            }
        } else {
            event.setCancelled(true);
        }

    }

}
