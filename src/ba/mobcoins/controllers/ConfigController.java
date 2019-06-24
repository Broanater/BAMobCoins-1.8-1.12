package ba.mobcoins.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;

import ba.mobcoins.Main;
import ba.mobcoins.utilities.*;


public class ConfigController implements Listener
{
	private static Main plugin;
	
	private static String pluginFolderPath;
	
	private static HashMap<EntityType, Double> dropRates = new HashMap<EntityType, Double>();
	
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
				Utils.sendError("Failed to load default config.yml");
			}
		}
		
		plugin.reloadConfig();
		
		/* Other things to reload */
		reloadDropRates();
	}
	
	
	private static void reloadDropRates()
	{
		dropRates.clear();
		
		/* Passive Mobs */
		dropRates.put(EntityType.BAT, getBat());
		dropRates.put(EntityType.SQUID, getSquid());
		dropRates.put(EntityType.RABBIT, getRabbit());
		dropRates.put(EntityType.CHICKEN, getRabbit());
		dropRates.put(EntityType.PIG, getPig());
		dropRates.put(EntityType.SHEEP, getSheep());
		dropRates.put(EntityType.COW, getCow());
		dropRates.put(EntityType.MUSHROOM_COW, getMushroomCow());
		dropRates.put(EntityType.SNOWMAN, getSnowman());
		dropRates.put(EntityType.OCELOT, getOcelot());
		dropRates.put(EntityType.HORSE, getHorse());
		
		/* Hostile Mobs */
		dropRates.put(EntityType.ZOMBIE, getZombie());
		dropRates.put(EntityType.SKELETON, getSkeleton());
		dropRates.put(EntityType.SPIDER, getSpider());
		dropRates.put(EntityType.CAVE_SPIDER, getCaveSpider());
		dropRates.put(EntityType.CREEPER, getCreeper());
		dropRates.put(EntityType.ENDERMAN, getEnderman());
		dropRates.put(EntityType.BLAZE, getBlaze());
		dropRates.put(EntityType.SILVERFISH, getSilverfish());
		dropRates.put(EntityType.WITCH, getWitch());
		dropRates.put(EntityType.MAGMA_CUBE, getMagmaCube());
		dropRates.put(EntityType.ENDERMITE, getEndermite());
		dropRates.put(EntityType.GUARDIAN, getGuardian());
		dropRates.put(EntityType.GHAST, getGhast());
		dropRates.put(EntityType.SLIME, getSlime());
		dropRates.put(EntityType.GIANT, getGiant());
		dropRates.put(EntityType.WITHER, getWither());
		dropRates.put(EntityType.ENDER_DRAGON, getEnderDragon());
		
		/* Neutral Mobs */
		dropRates.put(EntityType.VILLAGER, getVillager());
		dropRates.put(EntityType.IRON_GOLEM, getIronGolem());
		dropRates.put(EntityType.PIG_ZOMBIE, getZombiePigman());
		dropRates.put(EntityType.WOLF, getWolf());
		
		/* Player */
		dropRates.put(EntityType.PLAYER, getPlayer());
	}
	
	
	/* 
	 * --------------------------------------------------------------
	 * --------------------------------------------------------------
	 * Get Methods 
	 * --------------------------------------------------------------
	 * --------------------------------------------------------------
	 */
	public static HashMap<EntityType, Double> getDropRates()
	{
		return dropRates;
	}
	
	
	/*
	 * --------------------------------------------------------------
	 * --------------------------------------------------------------
	 * Coin drop rates
	 * --------------------------------------------------------------
	 * --------------------------------------------------------------
	 */
	/*
	 * Passive Mobs Start
	 */
	public static double getPig()
	{
		double slot = plugin.getConfig().getDouble("DropChances.PIG");
		return slot;
	}

	public static double getBat()
	{
		double slot = plugin.getConfig().getDouble("DropChances.BAT");
		return slot;
	}

	public static double getSquid()
	{
		double slot = plugin.getConfig().getDouble("DropChances.SQUID");
		return slot;
	}

	public static double getRabbit()
	{
		double slot = plugin.getConfig().getDouble("DropChances.RABBIT");
		return slot;
	}

	public static double getCow()
	{
		double slot = plugin.getConfig().getDouble("DropChances.COW");
		return slot;
	}

	public static double getMushroomCow()
	{
		double slot = plugin.getConfig().getDouble("DropChances.MUSHROOMCOW");
		return slot;
	}

	public static double getSnowman()
	{
		double slot = plugin.getConfig().getDouble("DropChances.SNOWMAN");
		return slot;
	}

	public static double getOcelot()
	{
		double slot = plugin.getConfig().getDouble("DropChances.OCELOT");
		return slot;
	}

	public static double getHorse()
	{
		double slot = plugin.getConfig().getDouble("DropChances.HORSE");
		return slot;
	}

	public static double getSheep()
	{
		double slot = plugin.getConfig().getDouble("DropChances.SHEEP");
		return slot;
	}

	public static double getChicken()
	{
		double slot = plugin.getConfig().getDouble("DropChances.CHICKEN");
		return slot;
	}
	/*
	 * Passive Mobs End
	 */

	/*
	 * Hostile Mobs Start
	 */
	public static double getZombie()
	{
		double slot = plugin.getConfig().getDouble("DropChances.ZOMBIE");
		return slot;
	}

	public static double getSkeleton()
	{
		double slot = plugin.getConfig().getDouble("DropChances.SKELETON");
		return slot;
	}

	public static double getSpider()
	{
		double slot = plugin.getConfig().getDouble("DropChances.SPIDER");
		return slot;
	}

	public static double getCaveSpider()
	{
		double slot = plugin.getConfig().getDouble("DropChances.CAVESPIDER");
		return slot;
	}

	public static double getSilverfish()
	{
		double slot = plugin.getConfig().getDouble("DropChances.SILVERFISH");
		return slot;
	}

	public static double getMagmaCube()
	{
		double slot = plugin.getConfig().getDouble("DropChances.MAGMACUBE");
		return slot;
	}

	public static double getEndermite()
	{
		double slot = plugin.getConfig().getDouble("DropChances.ENDERMITE");
		return slot;
	}

	public static double getGuardian()
	{
		double slot = plugin.getConfig().getDouble("DropChances.GUARDIAN");
		return slot;
	}

	public static double getGhast()
	{
		double slot = plugin.getConfig().getDouble("DropChances.GHAST");
		return slot;
	}

	public static double getSlime()
	{
		double slot = plugin.getConfig().getDouble("DropChances.SLIME");
		return slot;
	}

	public static double getGiant()
	{
		double slot = plugin.getConfig().getDouble("DropChances.GIANT");
		return slot;
	}

	public static double getWither()
	{
		double slot = plugin.getConfig().getDouble("DropChances.WITHER");
		return slot;
	}

	public static double getEnderDragon()
	{
		double slot = plugin.getConfig().getDouble("DropChances.ENDERDRAGON");
		return slot;
	}

	public static double getCreeper()
	{
		double slot = plugin.getConfig().getDouble("DropChances.CREEPER");
		return slot;
	}

	public static double getEnderman()
	{
		double slot = plugin.getConfig().getDouble("DropChances.ENDERMAN");
		return slot;
	}

	public static double getBlaze()
	{
		double slot = plugin.getConfig().getDouble("DropChances.BLAZE");
		return slot;
	}

	public static double getWitch()
	{
		double slot = plugin.getConfig().getDouble("DropChances.WITCH");
		return slot;
	}
	/*
	 * Hostile Mobs End
	 */

	/*
	 * Neutral Mobs Start
	 */
	public static double getIronGolem()
	{
		double slot = plugin.getConfig().getDouble("DropChances.IRONGOLEM");
		return slot;
	}

	public static double getVillager()
	{
		double slot = plugin.getConfig().getDouble("DropChances.VILLAGER");
		return slot;
	}

	public static double getWolf()
	{
		double slot = plugin.getConfig().getDouble("DropChances.WOLF");
		return slot;
	}

	public static double getZombiePigman()
	{
		double slot = plugin.getConfig().getDouble("DropChances.PIGMAN");
		return slot;
	}
	/*
	 * Neutral Mobs End
	 */

	/*
	 * Player Start
	 */
	public static double getPlayer()
	{
		double slot = plugin.getConfig().getDouble("DropChances.PLAYER");
		return slot;
	}
	/*
	 * Player End
	 */

}
