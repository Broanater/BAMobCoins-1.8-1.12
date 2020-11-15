package ba.mobcoins.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;

import ba.mobcoins.Main;
import ba.mobcoins.logger.CustomLogger;
import ba.mobcoins.utilities.*;
import net.md_5.bungee.api.ChatColor;


public class ConfigController implements Listener
{
	private static Main plugin;
	
	private static String pluginFolderPath;
	
	private static HashMap<String, Double> dropRates = new HashMap<String, Double>();
	
	public ConfigController(Main pl)
	{
		plugin = pl;
		pluginFolderPath = "plugins/" + plugin.getName() + "/";
	}
	
	public static void reload()
	{
		File configFile = new File(plugin.getDataFolder(), "config.yml");
		if(!configFile.exists())
		{
			try
			{
				FileUtils.copyInputStreamToFile(plugin.getResource("resources/config.yml"), new File(pluginFolderPath + "config.yml"));
			}
			catch (Exception e)
			{
				CustomLogger.sendError("Failed to load default config.yml");
			}
		}
		
		plugin.reloadConfig();
		
		/* Other things to reload */
		reloadDropRates();
	}
	
	
	private static void reloadDropRates()
	{
		dropRates.clear();
		
		for (String entity : plugin.getConfig().getConfigurationSection("DropChances").getKeys(false))
		{
			EntityType entityType = null;
			
			try
			{
				entityType = EntityType.valueOf(entity);
			}
			catch (Exception e)
			{
				CustomLogger.sendError(String.format("Failed to verify entity '%s'. Skipping...", entity));
				CustomLogger.sendError(e);
			}
			
			if (entityType == null)
			{
				CustomLogger.sendError(String.format("Failed to verify entity '%s'. Skipping...", entity));
			}
			else
			{
				double rate = plugin.getConfig().getDouble("DropChances." + entity);
				
				dropRates.put(entity.toUpperCase(), rate);
			}
		}
	}
	
	public static String getPrefix()
	{
		return Utils.convertColorCodes(plugin.getConfig().getString("Options.Prefix"));
	}
	
	
	/* 
	 * --------------------------------------------------------------
	 * --------------------------------------------------------------
	 * Get Methods 
	 * --------------------------------------------------------------
	 * --------------------------------------------------------------
	 */
	public static HashMap<String, Double> getDropRates()
	{
		return dropRates;
	}
	
	public static boolean getDebug()
	{
		return plugin.getConfig().getBoolean("Debug");
	}
}
