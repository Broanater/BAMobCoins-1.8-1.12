package ba.mobcoins.controllers;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import ba.mobcoins.Main;
import ba.mobcoins.utilities.Utils;

public class BalanceController implements Listener
{
	private static Main plugin;
	
	public BalanceController(Main pl)
	{
		plugin = pl;
	}
	
	
	public static void createFile()
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
				Utils.sendError("Failed to create 'balances.yml'. Balance saving will be compromised.");
			}
		}
	}

	public static void save()
	{
		File file = new File("plugins/BAMobCoins/data/balances.yml");
		YamlConfiguration bal = YamlConfiguration.loadConfiguration(file);
		for (String UUID : plugin.coins.keySet())
		{
			bal.set(UUID, plugin.coins.get(UUID));
		}

		try
		{
			bal.save(file);
		}
		catch (Exception e)
		{
			Utils.sendError("Failed to save 'balances.yml'. Balance saving will be compromised.");
		}
	}

	public static void load()
	{
		File file = new File("plugins/BAMobCoins/data/balances.yml");
		YamlConfiguration bal = YamlConfiguration.loadConfiguration(file);
		for (String UUID : bal.getKeys(false))
		{
			plugin.coins.put(UUID, Integer.valueOf(bal.getInt(UUID)));
		}
	}
}
