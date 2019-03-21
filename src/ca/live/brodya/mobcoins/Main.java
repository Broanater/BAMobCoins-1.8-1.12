package ca.live.brodya.mobcoins;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

public class Main extends org.bukkit.plugin.java.JavaPlugin implements Listener
{
	HashMap<String, Integer> coins = new HashMap<String, Integer>();
	PluginDescriptionFile pdf;

	public Main(Main pl)
	{
	}

	public Main()
	{
		this.pdf = getDescription();
	}

	public void onEnable()
	{
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		System.out.print("-------------------------------");
		System.out.print("");
		System.out.print("    BAMobCoins Enabled!");
		System.out.print("");
		System.out.print("-------------------------------");
		getCommand("BAMobCoins").setExecutor(new Commands(this));
		registerEvents();
		createFile();
		loadBal();

		/*
		 * Automatic update check try { URL checkURL = new
		 * URL("https://api.spigotmc.org/legacy/update.php?resource=38676");
		 * URLConnection con = checkURL.openConnection(); String newVersion = new
		 * BufferedReader(new InputStreamReader(con.getInputStream())).readLine(); if
		 * (!getDescription().getVersion().equals(newVersion)) {
		 * System.out.print("An update was found! New version: " + newVersion +
		 * " download: https://www.spigotmc.org/resources/38676/"); } } catch
		 * (IOException e1) { e1.printStackTrace(); }
		 */
	}

	public void onDisable()
	{
		System.out.print("-------------------------------");
		System.out.print("");
		System.out.print("    BAMobCoins Disabled!");
		System.out.print("");
		System.out.print("-------------------------------");
		saveBal();
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e)
	{

		/*
		final Player p = e.getPlayer();
		 * Op automatic update check if (p.isOp()) { try { URL checkURL = new
		 * URL("https://api.spigotmc.org/legacy/update.php?resource=38676");
		 * URLConnection con = checkURL.openConnection(); String newVersion = new
		 * BufferedReader(new InputStreamReader(con.getInputStream())).readLine(); if
		 * (!getDescription().getVersion().equals(newVersion)) {
		 * p.sendMessage(Utils.getprefix() + ChatColor.GRAY +
		 * "An update was found! New version: " + newVersion +
		 * " download: https://www.spigotmc.org/resources/38676/"); } } catch
		 * (IOException e1) { e1.printStackTrace(); }
		 * 
		 * }
		 */
	}

	private void registerEvents()
	{
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new Utils(this), this);
		pm.registerEvents(new Events(this), this);
		pm.registerEvents(new CoinsAPI(this), this);
	}

	public void createFile()
	{
		File file = new File("plugins//BAMobCoins//Balances.yml");
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			} catch (IOException localIOException)
			{
			}
		}
	}

	public void saveBal()
	{
		File file = new File("plugins//BAMobCoins//Balances.yml");
		YamlConfiguration bal = YamlConfiguration.loadConfiguration(file);
		for (String UUID : this.coins.keySet())
		{
			bal.set(UUID, this.coins.get(UUID));
		}

		try
		{
			bal.save(file);
		} catch (IOException localIOException)
		{
		}
	}

	public void loadBal()
	{
		File file = new File("plugins//BAMobCoins//Balances.yml");
		YamlConfiguration bal = YamlConfiguration.loadConfiguration(file);
		for (String UUID : bal.getKeys(false))
		{
			this.coins.put(UUID, Integer.valueOf(bal.getInt(UUID)));
		}
	}
}
