package ba.mobcoins;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import ba.mobcoins.updater.UpdateChecker;

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
		File configFile = new File(this.getDataFolder(), "config.yml");
		if(!configFile.exists())
		{
			try
			{
				FileUtils.copyInputStreamToFile(this.getResource("resources/config.yml"), new File("plugins/BAMobCoins/config.yml"));
			}
			catch (Exception e)
			{
				System.out.println("[BAMobCoins] Failed to copy default config.yml");
			}
		}
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		getCommand("BAMobCoins").setExecutor(new Commands(this));
		registerEvents();
		
		
		createBalanceFile();
		loadBalance();
		Messages.reloadMessages();

		/* Check if theres any updates for the plugin on spigot. */
		new UpdateChecker(this).checkForUpdate();
		

		System.out.print("-------------------------------");
		System.out.print("");
		System.out.print("    BAMobCoins Enabled!");
		System.out.print("");
		System.out.print("-------------------------------");
	}

	public void onDisable()
	{
		saveBalance();
		
		
		System.out.print("-------------------------------");
		System.out.print("");
		System.out.print("    BAMobCoins Disabled!");
		System.out.print("");
		System.out.print("-------------------------------");
	}

	private void registerEvents()
	{
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new Utils(this), this);
		pm.registerEvents(new Events(this), this);
		pm.registerEvents(new CoinsAPI(this), this);
		pm.registerEvents(new Messages(this), this);
		pm.registerEvents(new ShopController(this), this);
	}

	
	/*
	 * Balance file stuff
	 */
	public void createBalanceFile()
	{
		File folder = new File("plugins/BAMobCoins/data");
		File file = new File("plugins/BAMobCoins/data/balances.yml");
		if (!folder.exists())
		{
			folder.mkdir();
		}
		if (!file.exists())
		{
			try
			{
				file.createNewFile();
			}
			catch (Exception e)
			{
				System.out.println("[BAMobCoins] Failed to create 'balances.yml'. Balance saving will be compromised.");
			}
		}
	}

	public void saveBalance()
	{
		File file = new File("plugins/BAMobCoins/data/balances.yml");
		YamlConfiguration bal = YamlConfiguration.loadConfiguration(file);
		for (String UUID : this.coins.keySet())
		{
			bal.set(UUID, this.coins.get(UUID));
		}

		try
		{
			bal.save(file);
		}
		catch (Exception e)
		{
			System.out.println("[BAMobCoins] Failed to save 'balances.yml'. Balance saving will be compromised.");
		}
	}

	public void loadBalance()
	{
		File file = new File("plugins/BAMobCoins/data/balances.yml");
		YamlConfiguration bal = YamlConfiguration.loadConfiguration(file);
		for (String UUID : bal.getKeys(false))
		{
			this.coins.put(UUID, Integer.valueOf(bal.getInt(UUID)));
		}
	}
}
