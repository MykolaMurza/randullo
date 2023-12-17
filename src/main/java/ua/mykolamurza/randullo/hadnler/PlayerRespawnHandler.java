package ua.mykolamurza.randullo.hadnler;

import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.security.SecureRandom;

/**
 * @author Mykola Murza
 */
public class PlayerRespawnHandler implements Listener {
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        Location bedSpawnLocation = player.getBedSpawnLocation();
        if (bedSpawnLocation != null) {
            event.setRespawnLocation(bedSpawnLocation);
            return;
        }

        Location placeToSpawnPlayer = findPlaceToSpawnPlayer(world);
        event.setRespawnLocation(placeToSpawnPlayer);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        if (event.getPlayer().hasPlayedBefore()) {
            return;
        }

        Location placeToSpawnPlayer = findPlaceToSpawnPlayer(world);
        player.teleport(placeToSpawnPlayer);
    }

    private Location findPlaceToSpawnPlayer(World world) {
        SecureRandom random = new SecureRandom();
        double borderSize = world.getWorldBorder().getSize();
        double halfBorderSize = borderSize / 2;

        Location location = generateRandomLocation(world, random, borderSize, (int) halfBorderSize);

        checkIsLocationSafeAndMoveIfNot(location, world, location.getBlockX() >= 0);

        return location;
    }

    private Location generateRandomLocation(World world, SecureRandom random, double borderSize, int halfBorderSize) {
        double x = (int) (random.nextDouble() * borderSize) - halfBorderSize + 0.5;
        double z = (int) (random.nextDouble() * borderSize) - halfBorderSize + 0.5;
        int y = getHighestBlockAt(world, x, z);

        return new Location(world, x, y, z);
    }

    private int getHighestBlockAt(World world, double x, double z) {
        return world.getHighestBlockYAt(new Location(world, x, 0d, z), HeightMap.WORLD_SURFACE);
    }

    // isXPositive - means coordinate X was bigger than 0, and we have to remove 10 from X value if zone in unsafe.
    // We can't check this statement every time because it leads to infinite loop on values between -10 and 10.
    private void checkIsLocationSafeAndMoveIfNot(Location location, World world, boolean isXPositive) {
        Material type = location.getBlock().getType();
        while (type == Material.LAVA || type == Material.LAVA_CAULDRON) {
            location.setX(isXPositive ? location.getX() - 10 : location.getX() + 10);
            location.setY(world.getHighestBlockAt(location.getBlockX(), location.getBlockZ()).getY());
            type = location.getBlock().getType();
        }
    }
}
