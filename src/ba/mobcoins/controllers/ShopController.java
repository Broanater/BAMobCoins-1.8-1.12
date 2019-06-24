package ba.mobcoins.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.FileUtil;

import com.google.common.io.Files;

import net.md_5.bungee.api.ChatColor;

import ba.mobcoins.*;
import ba.mobcoins.apis.CoinsAPI;
import ba.mobcoins.models.*;
import ba.mobcoins.models.CustomItem.ItemTypes;
import ba.mobcoins.utilities.Utils;

public class ShopController implements Listener
{
	private static Main plugin;
	public static HashMap<String, CustomInventory> shopInvs = new HashMap<String, CustomInventory>();
	public static ArrayList<Category> categories = new ArrayList<Category>();

	private static File shopFile = null;
	private static FileConfiguration shopConfig = null;

	public ShopController(Main pl)
	{
		plugin = pl;
		reload();
	}

	public static void reload()
	{
		reloadShop();
		reloadCategories();
	}

	public static void reloadShop()
	{
		shopFile = new File("plugins/BAMobCoins/Shop.yml");
		/* Get the default from the jar. */
		if (!shopFile.exists())
		{
			/* If it doesn't exists copy it from the jar */
			try
			{
				FileUtils.copyInputStreamToFile(plugin.getResource("resources/Shop.yml"), new File("plugins/BAMobCoins/Shop.yml"));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		shopConfig = YamlConfiguration.loadConfiguration(shopFile);

		if (shopFile != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(shopFile);
			shopConfig.setDefaults(defConfig);
		}
	}

	public static void reloadCategories()
	{
		File categoriesFolder = new File("plugins/BAMobCoins/categories");
		if (!categoriesFolder.exists())
		{
			/* Create the folder */
			categoriesFolder.mkdir();

			try
			{
				/* Copy the Keys.yml default file */
				FileUtils.copyInputStreamToFile(plugin.getResource("resources/Keys.yml"), new File("plugins/BAMobCoins/categories/Keys.yml"));

				/* Copy the Items.yml default file */
				FileUtils.copyInputStreamToFile(plugin.getResource("resources/Items.yml"), new File("plugins/BAMobCoins/categories/Items.yml"));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}

		shopInvs.clear();
		createShopMenu();
		createCategoryShops();
	}

	public static Inventory getShopInventory(String playerUuid, String inventoryId)
	{
		CustomInventory customInv = shopInvs.get(inventoryId);

		Inventory inv = customInv.getInventory();

		if (customInv.getCoinInfoLocation() > -1)
		{
			/* Show users coins */
			inv.setItem(customInv.getCoinInfoLocation(), getDisplayItem(playerUuid));
		}

		if (customInv.getDropInfoLocation() > -1)
		{
			/* Show drop rates */
			inv.setItem(customInv.getDropInfoLocation(), getDropInfo());
		}

		return inv;
	}

	public static ArrayList<CustomItem> getShopItems(String inventoryKey)
	{
		CustomInventory customInv = shopInvs.get(inventoryKey);

		if (customInv == null)
		{
			return null;
		}
		else
		{
			return customInv.getShopItems();
		}
	}

	/* Create the categories */
	private static void createShopMenu()
	{
		Inventory inv = Bukkit.createInventory(null, getMenuSize(), getMenuTitle());

		/* Add category options */
		for (String key : shopConfig.getConfigurationSection("Shop.Categories").getKeys(false))
		{
			Material material = null;
			try
			{
				material = Material.valueOf(shopConfig.getString("Shop.Categories." + key + ".Display.Material"));
			}
			catch (Exception e)
			{
				System.out.println("[BAMobCoins] Material given for 'Shop.Categories." + key + ".Display.Material' is unknown. Skipping category.");
				continue;
			}
			int damage = shopConfig.getInt("Shop.Categories." + key + ".Display.Damage");
			int amount = shopConfig.getInt("Shop.Categories." + key + ".Display.Amount");
			String name = Utils.convertColorCodes(shopConfig.getString("Shop.Categories." + key + ".Display.Name"));
			List<String> rawLore = shopConfig.getStringList("Shop.Categories." + key + ".Display.Lore");

			ArrayList<String> lore = new ArrayList<String>();
			for (String line : rawLore)
			{
				lore.add(Utils.convertColorCodes(line));
			}

			boolean usePermission = shopConfig.getBoolean("Shop.Categories." + key + ".UsePermission");
			String permission = "";
			if (usePermission)
			{
				permission = shopConfig.getString("Shop.Categories." + key + ".Permission");
			}

			int location = shopConfig.getInt("Shop.Categories." + key + ".Slot");

			String file = shopConfig.getString("Shop.Categories." + key + ".File");

			ItemStack item = new ItemStack(material, amount, (short) damage);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(name);
			itemMeta.setLore(lore);
			item.setItemMeta(itemMeta);

			if (usePermission)
			{
				categories.add(new Category(key, item, location, file, usePermission, permission));
			}
			else
			{
				categories.add(new Category(key, item, location, file, usePermission));
			}

			inv.setItem(location, item);

		}

		shopInvs.put("MENU", new CustomInventory(getMenuTitle(), inv, getCoinInfoLocation(), getDropInfoLocation()));
	}

	private static void createCategoryShops()
	{
		File categoriesFolder = new File("plugins/BAMobCoins/categories");
		File[] categoryFiles = categoriesFolder.listFiles();

		for (File categoryFile : categoryFiles)
		{
			if (Files.getFileExtension(categoryFile.getPath()).equals("yml"))
			{
				String fileName = categoryFile.getName();
				String id = fileName.substring(0, fileName.lastIndexOf('.'));

				/* Get config file */
				FileConfiguration categoryConfig = YamlConfiguration.loadConfiguration(categoryFile);

				int size = categoryConfig.getInt("Category.Size");
				String title = Utils.convertColorCodes(categoryConfig.getString("Category.Title"));

				Inventory inv = Bukkit.createInventory(null, size, title);

				/* Add the item to be sold in the shop */
				ArrayList<CustomItem> shopItems = new ArrayList<CustomItem>();
				for (String key : categoryConfig.getConfigurationSection("Category.Items").getKeys(false))
				{
					String rawType = categoryConfig.getString("Category.Items." + key + ".Type").toUpperCase();
					ItemTypes type;
					try
					{
						type = ItemTypes.valueOf(rawType);
					}
					catch (Exception e)
					{
						System.out.println("[BAMobCoins] Type given for 'Category.Items." + key + ".Type' is unknown. Make sure you use 'COMMAND' or 'ITEM'. Skipping item.");
						continue;
					}
					
					int location = 0;
					ArrayList<Integer> fillerLocations = new ArrayList<Integer>();
					
					if (type == ItemTypes.COMMAND || type == ItemTypes.ITEM)
					{
						location = categoryConfig.getInt("Category.Items." + key + ".Slot");
					}
					else if (type == ItemTypes.FILLER)
					{
						fillerLocations = (ArrayList<Integer>) categoryConfig.getIntegerList("Category.Items." + key + ".Slot");
					}
					
					int price = categoryConfig.getInt("Category.Items." + key + ".Price");

					/* Get Display Item */
					Material material;
					try
					{
						material = Material.valueOf(categoryConfig.getString("Category.Items." + key + ".Display.Material"));
					}
					catch (Exception e)
					{
						System.out.println("[BAMobCoins] Material given for 'Category.Items." + key + ".Display.Material' is unknown. Skipping item.");
						continue;
					}
					int damage = categoryConfig.getInt("Category.Items." + key + ".Display.Damage");
					int amount = categoryConfig.getInt("Category.Items." + key + ".Display.Amount");
					String name = Utils.convertColorCodes(categoryConfig.getString("Category.Items." + key + ".Display.Name"));
					ArrayList<String> rawLore = (ArrayList<String>) categoryConfig.getStringList("Category.Items." + key + ".Display.Lore");

					ArrayList<String> lore = new ArrayList<String>();
					for (String line : rawLore)
					{
						line = line.replace("%PRICE%", String.valueOf(price));
						lore.add(Utils.convertColorCodes(line));
					}

					ItemStack displayItem = new ItemStack(material, amount, (short) damage);
					ItemMeta displayMeta = displayItem.getItemMeta();
					displayMeta.setDisplayName(name);
					displayMeta.setLore(lore);
					displayItem.setItemMeta(displayMeta);

					/* Get reward (commands or item) */
					if (type == ItemTypes.COMMAND)
					{
						ArrayList<String> commands = (ArrayList<String>) categoryConfig.getStringList("Category.Items." + key + ".Commands");
						inv.setItem(location, displayItem);
						shopItems.add(new CustomItem(type, key, displayItem, location, price, commands));
					}
					else if (type == ItemTypes.ITEM)
					{
						Material rewardMaterial = Material.valueOf(categoryConfig.getString("Category.Items." + key + ".Item.Material"));
						int rewardDamage = categoryConfig.getInt("Category.Items." + key + ".Item.Damage");
						int rewardAmount = categoryConfig.getInt("Category.Items." + key + ".Item.Amount");
						String rewardName = categoryConfig.getString("Category.Items." + key + ".Item.Name");
						ArrayList<String> rewardRawLore = (ArrayList<String>) categoryConfig.getStringList("Category.Items." + key + ".Item.Lore");

						ArrayList<String> rewardLore = new ArrayList<String>();
						for (String rewardLine : rewardRawLore)
						{
							rewardLore.add(Utils.convertColorCodes(rewardLine));
						}

						if (rewardMaterial == null)
						{
							System.out.println("[BAMobCoins] Material given for 'Category.Items." + key + ".Item.Material' is unknown. Skipping item.");
							continue;
						}

						ItemStack rewardItem = new ItemStack(rewardMaterial, rewardAmount, (short) rewardDamage);
						ItemMeta rewardMeta = rewardItem.getItemMeta();
						if (!rewardName.equalsIgnoreCase("default"))
						{
							rewardMeta.setDisplayName(Utils.convertColorCodes(rewardName));
						}
						rewardMeta.setLore(rewardLore);
						rewardItem.setItemMeta(rewardMeta);

						shopItems.add(new CustomItem(type, key, displayItem, location, price, rewardItem));
						inv.setItem(location, displayItem);
					}
					else if (type == ItemTypes.FILLER)
					{
						for (int fillerLocation : fillerLocations)
						{
							inv.setItem(fillerLocation, displayItem);
						}
					}
				}

				
				/* Back button */
				int backSlot = categoryConfig.getInt("Category.BackSlot");
				if (backSlot > -1)
				{
					inv.setItem(backSlot, getBackButton());
				}
				
				
				shopInvs.put(id, new CustomInventory(title, inv, getCoinInfoLocation(), getDropInfoLocation(), shopItems));
			}
		}
	}

	/* Helping Methods */
	public static ItemStack getBackButton()
	{
		Material material = Material.valueOf(shopConfig.getString("Shop.Back.Item.Material"));
		int damage = shopConfig.getInt("Shop.Back.Item.Damage");
		int amount = shopConfig.getInt("Shop.Back.Item.Amount");
		String name = Utils.convertColorCodes(shopConfig.getString("Shop.Back.Item.Name"));
		List<String> rawLore = shopConfig.getStringList("Shop.Back.Item.Lore");

		ArrayList<String> lore = new ArrayList<String>();
		for (String line : rawLore)
		{
			lore.add(Utils.convertColorCodes(line));
		}

		if (material == null)
		{
			System.out.println("[BAMobCoins] Material given for 'Shop.Back.Item.Material' is unknown. Skipping item.");
			return null;
		}
		else
		{
			ItemStack item = new ItemStack(material, amount, (short) damage);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(name);
			meta.setLore(lore);
			item.setItemMeta(meta);

			return item;
		}
	}

	public static String getMenuTitle()
	{
		return Utils.convertColorCodes(shopConfig.getString("Shop.Menu.Title"));
	}

	private static int getMenuSize()
	{
		return shopConfig.getInt("Shop.Menu.Size");
	}

	private static int getCoinInfoLocation()
	{
		return shopConfig.getInt("Shop.Menu.Coin_Display_Location");
	}

	private static ItemStack getDisplayItem(String playerUuid)
	{
		Material material = Material.valueOf(shopConfig.getString("Shop.Coin_Display.Item.Material"));
		int damage = shopConfig.getInt("Shop.Coin_Display.Item.Damage");
		int amount = shopConfig.getInt("Shop.Coin_Display.Item.Amount");
		String name = shopConfig.getString("Shop.Coin_Display.Item.Name");

		String updatedName = name.replace("%amount%", String.valueOf(CoinsAPI.getCoins(playerUuid)));

		ItemStack item = new ItemStack(material, amount, (short) damage);

		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(Utils.convertColorCodes(updatedName));

		item.setItemMeta(itemMeta);

		return item;
	}

	private static ItemStack getDropInfoItem()
	{
		Material material = Material.valueOf(shopConfig.getString("Shop.Drop_Info.Item.Material"));
		int damage = shopConfig.getInt("Shop.Drop_Info.Item.Damage");
		int amount = shopConfig.getInt("Shop.Drop_Info.Item.Amount");

		if (material == null)
		{
			System.out.println("[BAMobCoins] Material given for 'Shop.Drop_Info.Item.Material' is unknown. Assigning default material (BOOK).");
			material = Material.BOOK;
			damage = 0;
		}

		if (amount < 1)
		{
			System.out.println("[BAMobCoins] Amount given for 'Shop.Drop_Info.Item.Amount' is less than 1. Assigning default value (1).");
			amount = 1;
		}

		ItemStack item = new ItemStack(material, amount, (short) damage);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.convertColorCodes(shopConfig.getString("Shop.Drop_Info.Item.Name")));
		item.setItemMeta(meta);

		return item;
	}

	private static String getMainColour()
	{
		return Utils.convertColorCodes(shopConfig.getString("Shop.Drop_Info.Main_Colour"));
	}

	private static int getDropInfoLocation()
	{
		return shopConfig.getInt("Shop.Menu.Drop_Info_Location");
	}
	
	private static String getPercentFormat()
	{
		String format = shopConfig.getString("Shop.Drop_Info.Percent_Format");
		
		if (format == null)
		{
			return "&d&l%MOB% - &f%RATE%%";
		}
		else
		{
			return format;
		}
	}

	private static ItemStack getDropInfo()
	{
		ItemStack item = getDropInfoItem();
		ItemMeta meta = item.getItemMeta();

		HashMap<EntityType, Double> dropRates = ConfigController.getDropRates();

		ArrayList<String> lore = new ArrayList<String>();
		
		String mobFormat = getPercentFormat();
		/* Passive mobcoin display */
		if (dropRates.get(EntityType.BAT) > 0 || dropRates.get(EntityType.SQUID) > 0 || dropRates.get(EntityType.RABBIT) > 0 || dropRates.get(EntityType.CHICKEN) > 0 || 
				dropRates.get(EntityType.PIG) > 0 || dropRates.get(EntityType.SHEEP) > 0 || dropRates.get(EntityType.COW) > 0 || dropRates.get(EntityType.MUSHROOM_COW) > 0 || 
				dropRates.get(EntityType.SNOWMAN) > 0 || dropRates.get(EntityType.OCELOT) > 0 || dropRates.get(EntityType.HORSE) > 0)
		{
			lore.add(getMainColour() + "Passive Mobs:");
		}
		if (dropRates.get(EntityType.BAT) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.BAT));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.BAT)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.SQUID) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.SQUID));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.SQUID)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.RABBIT) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.RABBIT));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.RABBIT)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.CHICKEN) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.CHICKEN));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.CHICKEN)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.PIG) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.PIG));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.PIG)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.SHEEP) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.SHEEP));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.SHEEP)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.COW) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.COW));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.COW)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.MUSHROOM_COW) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.MUSHROOM_COW));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.MUSHROOM_COW)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.SNOWMAN) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.SNOWMAN));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.SNOWMAN)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.OCELOT) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.OCELOT));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.OCELOT)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.HORSE) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.HORSE));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.HORSE)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}

		/* Hostile mobcoin chance display */
		if (dropRates.get(EntityType.ZOMBIE) > 0 || dropRates.get(EntityType.SKELETON) > 0 || dropRates.get(EntityType.SPIDER) > 0 || dropRates.get(EntityType.CAVE_SPIDER) > 0 || 
				dropRates.get(EntityType.CREEPER) > 0 || dropRates.get(EntityType.ENDERMAN) > 0 || dropRates.get(EntityType.BLAZE) > 0 || dropRates.get(EntityType.SILVERFISH) > 0 || 
				dropRates.get(EntityType.WITCH) > 0 || dropRates.get(EntityType.MAGMA_CUBE) > 0 || dropRates.get(EntityType.ENDERMITE) > 0 || dropRates.get(EntityType.GUARDIAN) > 0 || 
				dropRates.get(EntityType.GHAST) > 0 || dropRates.get(EntityType.SLIME) > 0 || dropRates.get(EntityType.GIANT) > 0 || dropRates.get(EntityType.WITHER) > 0 || 
				dropRates.get(EntityType.ENDER_DRAGON) > 0)
		{
			lore.add(getMainColour() + "Hostile Mobs:");
		}
		if (dropRates.get(EntityType.ZOMBIE) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.ZOMBIE));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.ZOMBIE)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.SKELETON) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.SKELETON));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.SKELETON)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.SPIDER) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.SPIDER));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.SPIDER)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.CAVE_SPIDER) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.CAVE_SPIDER));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.CAVE_SPIDER)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.CREEPER) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.CREEPER));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.CREEPER)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.ENDERMAN) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.ENDERMAN));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.ENDERMAN)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.BLAZE) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.BLAZE));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.BLAZE)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.WITCH) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.WITCH));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.WITCH)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.SILVERFISH) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.SILVERFISH));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.SILVERFISH)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.MAGMA_CUBE) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.MAGMA_CUBE));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.MAGMA_CUBE)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.ENDERMITE) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.ENDERMITE));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.ENDERMITE)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.GUARDIAN) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.GUARDIAN));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.GUARDIAN)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.GHAST) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.GHAST));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.GHAST)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.SLIME) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.SLIME));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.SLIME)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.GIANT) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.GIANT));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.GIANT)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.WITCH) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.WITHER));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.WITHER)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.ENDER_DRAGON) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.ENDER_DRAGON));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.ENDER_DRAGON)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}

		/* Neutral mobcoin chance display */
		if (dropRates.get(EntityType.VILLAGER) > 0 || dropRates.get(EntityType.IRON_GOLEM) > 0 || 
				dropRates.get(EntityType.PIG_ZOMBIE) > 0 || dropRates.get(EntityType.WOLF) > 0)
		{
			lore.add(getMainColour() + "Neutral Mobs:");
		}
		if (dropRates.get(EntityType.VILLAGER) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.VILLAGER));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.VILLAGER)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.IRON_GOLEM) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.IRON_GOLEM));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.IRON_GOLEM)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.PIG_ZOMBIE) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.PIG_ZOMBIE));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.PIG_ZOMBIE)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}
		if (dropRates.get(EntityType.WOLF) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.WOLF));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.WOLF)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}

		/* Player mobcoin chance display */
		if (dropRates.get(EntityType.PLAYER) > 0)
		{
			lore.add(getMainColour() + "Players:");
		}
		if (dropRates.get(EntityType.PLAYER) > 0)
		{
			mobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.PLAYER));
			mobFormat = mobFormat.replace("%RATE%", String.valueOf(dropRates.get(EntityType.PLAYER)));
			
			lore.add(Utils.convertColorCodes(mobFormat));
		}

		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
