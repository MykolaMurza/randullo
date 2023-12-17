package ua.mykolamurza.randullo;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ua.mykolamurza.randullo.hadnler.PlayerRespawnHandler;

/**
 * Random teleport for players on respawn and for new players on first spawn.
 *
 * @author Mykola Murza
 * @version Minecraft 1.20.2
 */
public final class Randullo extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Start Randullo.");

        getServer().getPluginManager().registerEvents(new PlayerRespawnHandler(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("Stop Randullo.");
    }
}
