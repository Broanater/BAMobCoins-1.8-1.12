package ba.mobcoins.controllers;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;

import ba.mobcoins.Main;
import ba.mobcoins.utilities.*;

public class MobNameController implements Listener
{
	private static Main plugin;
	
	private static File mobNamesFile = null;
	private static FileConfiguration mobNamesConfig = null;
	
	public MobNameController(Main pl)
	{
		plugin = pl;
	}

	public static void reload()
	{
		File langFolder = new File("plugins/BAMobCoins/lang");
		/* Lang Files */
		
		if (!langFolder.exists())
		{
			/* Create the folder */
			langFolder.mkdir();
		}
		

		File mobNames = new File("plugins/BAMobCoins/lang", "Mobs.yml");
		if (!mobNames.exists())
		{
			try
			{
				/* Copy the lang_en.yml default file */
				FileUtils.copyInputStreamToFile(plugin.getResource("resources/Mobs.yml"), mobNames);
			}
			catch (IOException e)
			{
				Utils.sendError("Failed to create Mobs.yml. Names will be defaulted to the entity name.");
			}
		}
		
		
		mobNamesFile = new File("plugins/BAMobCoins/lang", "Mobs.yml");
		
		mobNamesConfig = YamlConfiguration.loadConfiguration(mobNamesFile);
		
		if (mobNamesConfig != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(mobNamesFile);
			mobNamesConfig.setDefaults(defConfig);
		}
	}
	
	public static String getMobName(EntityType entity)
	{
		return mobNamesConfig.getString(entity.toString());
	}
}
