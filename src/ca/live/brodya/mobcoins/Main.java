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

import ca.live.brodya.mobcoins.updater.UpdateChecker;

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
		createBalanceFile();
		loadBalance();
		createMessagesFile();

		/* Check if theres any updates for the plugin on spigot. */
		new UpdateChecker(this).checkForUpdate();
	}

	public void onDisable()
	{
		System.out.print("-------------------------------");
		System.out.print("");
		System.out.print("    BAMobCoins Disabled!");
		System.out.print("");
		System.out.print("-------------------------------");
		saveBalance();
	}

	private void registerEvents()
	{
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new Utils(this), this);
		pm.registerEvents(new Events(this), this);
		pm.registerEvents(new CoinsAPI(this), this);
		pm.registerEvents(new Messages(this), this);
	}

	
	/*
	 * Balance file stuff
	 */
	public void createBalanceFile()
	{
		File file = new File("plugins//BAMobCoins//Balances.yml");
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
			}
		}
	}

	public void saveBalance()
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
		}
		catch (IOException localIOException)
		{
		}
	}

	public void loadBalance()
	{
		File file = new File("plugins//BAMobCoins//Balances.yml");
		YamlConfiguration bal = YamlConfiguration.loadConfiguration(file);
		for (String UUID : bal.getKeys(false))
		{
			this.coins.put(UUID, Integer.valueOf(bal.getInt(UUID)));
		}
	}
	
	
	/*
	 * Messages file stuff.
	 */
	public void createMessagesFile()
	{
		File file = new File(this.getDataFolder(), "Messages.yml");
		if(!file.exists())
		{
			/* If it doesn't exists copy it from the jar */
			this.saveResource("Messages.yml", false);
		}
		Messages.reloadMessages();
	}
}
