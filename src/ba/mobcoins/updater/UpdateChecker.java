package ba.mobcoins.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import ba.mobcoins.controllers.ConfigController;
import ba.mobcoins.utilities.Utils;


public class UpdateChecker
{

	private final JavaPlugin javaPlugin;
	private final double localPluginVersion;
	private double spigotPluginVersion;

	// Constants. Customize to your liking.
	private static final int ID = 65777; // The ID of your resource. Can be found in the resource URL.
	private static final String ERR_MSG = "&cUpdate checker failed!";
	private static final String UPDATE_MSG = ConfigController.getPrefix() + " &fA new update is available at: &bhttps://www.spigotmc.org/resources/" + ID + "/updates";
	private static final long CHECK_INTERVAL = 12_000; // In ticks.

	public UpdateChecker(final JavaPlugin javaPlugin)
	{
		this.javaPlugin = javaPlugin;
		this.localPluginVersion = Double.parseDouble(javaPlugin.getDescription().getVersion());
	}

	public void checkForUpdate()
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				// The request is executed asynchronously as to not block the main thread.
				Bukkit.getScheduler().runTaskAsynchronously(javaPlugin, () ->
				{
					// Request the current version of your plugin on SpigotMC.
					try
					{
						final HttpsURLConnection connection = (HttpsURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=" + ID).openConnection();
						connection.setRequestMethod("GET");
						spigotPluginVersion = Double.parseDouble(new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine());
					}
					catch (final IOException e)
					{
						Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', ERR_MSG));
						e.printStackTrace();
						cancel();
						return;
					}

					// Check if the requested version is the same as the one in your plugin.yml.
					if (localPluginVersion >= spigotPluginVersion)
					{
						return;
					}

					Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', UPDATE_MSG));

					// Register the PlayerJoinEvent
					Bukkit.getScheduler().runTask(javaPlugin, () -> Bukkit.getPluginManager().registerEvents(new Listener()
					{
						@EventHandler(priority = EventPriority.MONITOR)
						public void onPlayerJoin(PlayerJoinEvent event)
						{
							Player player = event.getPlayer();
							if (player.hasPermission("BAMobCoins.update") || player.isOp())
							{
								player.sendMessage(ChatColor.translateAlternateColorCodes('&', UPDATE_MSG));
							}
							else
							{
								return;
							}
						}
					}, javaPlugin));

					cancel(); // Cancel the runnable as an update has been found.
				});
			}
		}.runTaskTimer(javaPlugin, 0, CHECK_INTERVAL);
	}
}
