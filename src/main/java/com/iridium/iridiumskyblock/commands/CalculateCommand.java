package com.iridium.iridiumskyblock.commands;

import com.iridium.iridiumcore.utils.StringUtils;
import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.Island;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CalculateCommand extends Command {

    /**
     * The default constructor.
     */
    public CalculateCommand() {
        super(Arrays.asList("calculate", "calc"), "Recalculate all Island Values", "", true, Duration.ofMinutes(5));
    }

    /**
     * Executes the command for the specified {@link CommandSender} with the provided arguments.
     * Not called when the command execution was invalid (no permission, no player or command disabled).
     *
     * @param sender The CommandSender which executes this command
     * @param args   The arguments used with this command. They contain the sub-command
     */
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        User user = IridiumSkyblock.getInstance().getUserManager().getUser(player);
        if (args.length > 1) {
            if (sender.hasPermission("iridiumskyblock.calculate.otherisland")) {
                int islandID = Integer.parseInt(args[1]);
                IridiumSkyblock.getInstance().getIslandManager().getIslandById(islandID).ifPresent(island -> {
                    player.sendMessage(StringUtils.color( ("%prefix% &7Recalcule de tous les blocs de l'île de " + island.getOwner().getName() + ". Patientez un instant...").replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
                    IridiumSkyblock.getInstance().getIslandManager().recalculateIsland(island, player);
                });
                return true;
            }
        }
        if (user.getIsland().isEmpty()) {
            player.sendMessage(StringUtils.color(IridiumSkyblock.getInstance().getMessages().noIsland.replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            return false;
        } else {
            player.sendMessage(StringUtils.color( "%prefix% &7Recalcule de tous les blocs de votre île. Patientez un instant...".replace("%prefix%", IridiumSkyblock.getInstance().getConfiguration().prefix)));
            Island island = user.getIsland().get();
            IridiumSkyblock.getInstance().getIslandManager().recalculateIsland(island, player);
        }
        return true;
    }

    /**
     * Handles tab-completion for this command.
     *
     * @param commandSender The CommandSender which tries to tab-complete
     * @param command       The command
     * @param label         The label of the command
     * @param args          The arguments already provided by the sender
     * @return The list of tab completions for this command
     */
    @Override
    public List<String> onTabComplete(CommandSender commandSender, org.bukkit.command.Command command, String label, String[] args) {
        // We currently don't want to tab-completion here
        // Return a new List, so it isn't a list of online players
        return Collections.emptyList();
    }

}
