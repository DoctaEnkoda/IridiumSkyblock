package com.iridium.iridiumskyblock.managers;

import com.iridium.iridiumskyblock.IridiumSkyblock;
import com.iridium.iridiumskyblock.database.User;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * Class which handles users.
 */
public class UserManager {

    /**
     * Gets a {@link User}'s info. Creates one if he doesn't exist.
     *
     * @param offlinePlayer The player who's data should be fetched
     * @return The user data
     */
    public @NotNull User getUser(@NotNull OfflinePlayer offlinePlayer) {
        User user = IridiumSkyblock.getInstance().getDatabaseManager().getUserTableManager().getUserByUUID(offlinePlayer.getUniqueId());
        if (user == null) {
            Optional<String> name = Optional.ofNullable(offlinePlayer.getName());
            user = new User(offlinePlayer.getUniqueId(), name.orElse(""));
            IridiumSkyblock.getInstance().getDatabaseManager().getUserTableManager().addEntry(user);
            if (IridiumSkyblock.getInstance().getConfiguration().debug) {
                IridiumSkyblock.getInstance().getLogger().info("Player: " + user.getName() + "\n" +
                        "UUID: " + user.getUuid() + "\n" +
                        "Event: UserManager#getUser");
            }
        }
        return user;
    }

    public User getUserByUsername(String username) {
        User user = IridiumSkyblock.getInstance().getDatabaseManager().getUserTableManager().getUserByUsername(username);
        if (user == null) {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(username);
            Optional<String> name = Optional.ofNullable(offlinePlayer.getName());
            user = new User(offlinePlayer.getUniqueId(), name.orElse(""));
            IridiumSkyblock.getInstance().getDatabaseManager().getUserTableManager().addEntry(user);
            if (IridiumSkyblock.getInstance().getConfiguration().debug) {
                IridiumSkyblock.getInstance().getLogger().info("Player: " + user.getName() + "\n" +
                        "UUID: " + user.getUuid() + "\n" +
                        "Event: UserManager#getUser");
            }
        }
        return user;
    }

    /**
     * Finds an User by his {@link UUID}.
     *
     * @param uuid The uuid of the onlyForPlayers
     * @return the User class of the onlyForPlayers
     */
    private Optional<User> getUserByUUID(@NotNull UUID uuid) {
        return IridiumSkyblock.getInstance().getDatabaseManager().getUserTableManager().getUser(uuid);
    }

}
