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
import org.bukkit.NamespacedKey;
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
import ba.mobcoins.enchants.Glow;
import ba.mobcoins.logger.CustomLogger;
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

	private static String path;

	public ShopController(Main pl)
	{
		plugin = pl;
		path = "plugins/" + plugin.getName() + "/";
		reload();
	}

	public static void reload()
	{
		reloadShop();
		reloadCategories();
	}

	public static void reloadShop()
	{
		shopFile = new File(path + "Shop.yml");
		/* Get the default from the jar. */
		if (!shopFile.exists())
		{
			/* If it doesn't exists copy it from the jar */
			try
			{
				FileUtils.copyInputStreamToFile(plugin.getResource("resources/Shop.yml"), new File(path + "Shop.yml"));
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
		File categoriesFolder = new File(path + "categories");
		if (!categoriesFolder.exists())
		{
			/* Create the folder */
			categoriesFolder.mkdir();

			try
			{
				/* Copy the Keys.yml default file */
				FileUtils.copyInputStreamToFile(plugin.getResource("resources/Keys.yml"), new File(path + "categories/Keys.yml"));

				/* Copy the Items.yml default file */
				FileUtils.copyInputStreamToFile(plugin.getResource("resources/Items.yml"), new File(path + "categories/Items.yml"));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}

		shopInvs.clear();
		createShopMenu();
		createCategories();
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

			boolean glow = false;
			try
			{
				glow = shopConfig.getBoolean("Shop.Categories." + key + ".Display.Glow");
			}
			catch (Exception e)
			{
			}

			int location = shopConfig.getInt("Shop.Categories." + key + ".Slot");

			String file = shopConfig.getString("Shop.Categories." + key + ".File");

			ItemStack item = new ItemStack(material, amount, (short) damage);
			ItemMeta itemMeta = item.getItemMeta();

			itemMeta.setDisplayName(name);
			itemMeta.setLore(lore);

			/* Enable glow effects depending on if the server is 1.8.* or a newer version. */
			if (plugin.getServer().getVersion().contains("MC: 1.8"))
			{
				CustomLogger.sendError("Glow effect is not an option for 1.8 versions of spigot.");
			}
			else
			{
				if (glow)
				{
					Glow glowEnchant = new Glow(new NamespacedKey(plugin, "GlowingEnchant"));
					itemMeta.addEnchant(glowEnchant, 1, true);
				}

			}

			if (usePermission)
			{
				categories.add(new Category(key, item, location, file, usePermission, permission));
			}
			else
			{
				categories.add(new Category(key, item, location, file, usePermission));
			}
			item.setItemMeta(itemMeta);
			
			inv.setItem(location, item);

		}

		shopInvs.put("MENU", new CustomInventory(getMenuTitle(), inv, getCoinInfoLocation(), getDropInfoLocation()));
	}

	private static void createCategories()
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
				int displayLoc = categoryConfig.getInt("Category.Coin_Display_Location");
				int dropInfoLoc = categoryConfig.getInt("Category.Drop_Info_Location");

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
						System.out
							.println("[BAMobCoins] Type given for 'Category.Items." + key + ".Type' is unknown. Make sure you use 'COMMAND' or 'ITEM'. Skipping item.");
						continue;
					}

					String all = "";
					int location = 0;
					ArrayList<Integer> fillerLocations = new ArrayList<Integer>();

					if (type == ItemTypes.COMMAND || type == ItemTypes.ITEM)
					{
						location = categoryConfig.getInt("Category.Items." + key + ".Slot");
					}
					else if (type == ItemTypes.FILLER)
					{
						try
						{
							all = categoryConfig.getString("Category.Items." + key + ".Slot");
						}
						catch (Exception e)
						{
							fillerLocations = (ArrayList<Integer>) categoryConfig.getIntegerList("Category.Items." + key + ".Slot");
						}
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
						System.out
							.println("[BAMobCoins] Material given for 'Category.Items." + key + ".Display.Material' is unknown. Skipping item.");
						continue;
					}
					int damage = categoryConfig.getInt("Category.Items." + key + ".Display.Damage");
					int amount = categoryConfig.getInt("Category.Items." + key + ".Display.Amount");
					String name = Utils.convertColorCodes(categoryConfig.getString("Category.Items." + key + ".Display.Name"));
					ArrayList<String> rawLore = (ArrayList<String>) categoryConfig.getStringList("Category.Items." + key + ".Display.Lore");

					boolean glow = false;
					try
					{
						glow = categoryConfig.getBoolean("Category.Items." + key + ".Display.Glow");
					}
					catch (Exception e)
					{
					}

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

					/* Enable glow effects depending on if the server is 1.8.* or a newer version. */
					if (plugin.getServer().getVersion().contains("MC: 1.8"))
					{
						CustomLogger.sendError("Glow effect is not an option for 1.8 versions of spigot.");
					}
					else
					{
						if (glow)
						{
							Glow glowEnchant = new Glow(new NamespacedKey(plugin, "GlowingEnchant"));
							displayMeta.addEnchant(glowEnchant, 1, true);
						}

						displayItem.setItemMeta(displayMeta);
					}

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
							System.out
								.println("[BAMobCoins] Material given for 'Category.Items." + key + ".Item.Material' is unknown. Skipping item.");
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
						if (all.equalsIgnoreCase("all"))
						{
							for (int counter = 0; counter < inv.getSize(); counter++)
							{
								ItemStack stack = inv.getItem(counter);

								if (stack == null)
								{
									inv.setItem(counter, displayItem);
								}
							}
						}
						else
						{
							for (int fillerLocation : fillerLocations)
							{
								inv.setItem(fillerLocation, displayItem);
							}
						}
					}
				}

				/* Back button */
				int backSlot = categoryConfig.getInt("Category.BackSlot");
				if (backSlot > -1)
				{
					inv.setItem(backSlot, getBackButton());
				}

				shopInvs.put(id, new CustomInventory(title, inv, displayLoc, dropInfoLoc, shopItems));
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

		HashMap<String, Double> dropRates = ConfigController.getDropRates();

		ArrayList<String> lore = new ArrayList<String>();

		String mobFormat = getPercentFormat();

		/*
		 * HOSTILE MOBS
		 */
		if (Utils.getDropRate("BLAZE") > 0 || Utils.getDropRate("CAVE_SPIDER") > 0 || Utils.getDropRate("CREEPER") > 0 || Utils
			.getDropRate("DROWNED") > 0 || Utils.getDropRate("ELDER_GUARDIAN") > 0 || Utils.getDropRate("ENDER_DRAGON") > 0 || Utils
				.getDropRate("ENDERMITE") > 0 || Utils.getDropRate("EVOKER") > 0 || Utils.getDropRate("GHAST") > 0 || Utils
					.getDropRate("GIANT") > 0 || Utils.getDropRate("GUARDIAN") > 0 || Utils.getDropRate("HUSK") > 0 || Utils
						.getDropRate("ILLUSIONER") > 0 || Utils.getDropRate("MAGMA_CUBE") > 0 || Utils.getDropRate("PHANTOM") > 0 || Utils
							.getDropRate("SHULKER") > 0 || Utils.getDropRate("SILVERFISH") > 0 || Utils.getDropRate("SKELETON") > 0 || Utils
								.getDropRate("SLIME") > 0 || Utils.getDropRate("SPIDER") > 0 || Utils.getDropRate("STRAY") > 0 || Utils
									.getDropRate("VEX") > 0 || Utils.getDropRate("WITHER") > 0 || Utils.getDropRate("ZOMBIE") > 0 || Utils
										.getDropRate("ZOMBIE_VILLAGER") > 0)
		{
			lore.add(getMainColour() + "Hostile Mobs:");
		}
		if (Utils.getDropRate("BLAZE") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.BLAZE));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("BLAZE")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("CAVE_SPIDER") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.CAVE_SPIDER));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("CAVE_SPIDER")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("CREEPER") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.CREEPER));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("CREEPER")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("DROWNED") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.DROWNED));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("DROWNED")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("ELDER_GUARDIAN") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.ELDER_GUARDIAN));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("ELDER_GUARDIAN")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("ENDER_DRAGON") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.ENDER_DRAGON));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("ENDER_DRAGON")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("ENDERMITE") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.ENDERMITE));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("ENDERMITE")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("EVOKER") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.EVOKER));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("EVOKER")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("GHAST") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.GHAST));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("GHAST")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("GIANT") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.GIANT));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("GIANT")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("GUARDIAN") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.GUARDIAN));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("GUARDIAN")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("HUSK") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.HUSK));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("HUSK")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("ILLUSIONER") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.ILLUSIONER));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("ILLUSIONER")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("MAGMA_CUBE") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.MAGMA_CUBE));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("MAGMA_CUBE")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("PHANTOM") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.PHANTOM));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("PHANTOM")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("SHULKER") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.SHULKER));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("SHULKER")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("SILVERFISH") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.SILVERFISH));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("SILVERFISH")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("SKELETON") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.SKELETON));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("SKELETON")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("SLIME") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.SLIME));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("SLIME")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("SPIDER") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.SPIDER));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("SPIDER")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("STRAY") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.STRAY));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("STRAY")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("VEX") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.VEX));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("VEX")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("VINDICATOR") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.VINDICATOR));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("VINDICATOR")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("WITCH") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.WITCH));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("WITCH")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("WITHER") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.WITHER));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("WITHER")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("ZOMBIE") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.ZOMBIE));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("ZOMBIE")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("ZOMBIE_VILLAGER") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.ZOMBIE_VILLAGER));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("ZOMBIE_VILLAGER")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}

		/*
		 * PASSIVE MOBS
		 */
		if (Utils.getDropRate("BAT") > 0 || Utils.getDropRate("OCELOT") > 0 || Utils.getDropRate("CHICKEN") > 0 || Utils
			.getDropRate("COD") > 0 || Utils.getDropRate("COW") > 0 || Utils.getDropRate("DOLPHIN") > 0 || Utils.getDropRate("DONKEY") > 0 || Utils
				.getDropRate("HORSE") > 0 || Utils.getDropRate("LLAMA") > 0 || Utils.getDropRate("MULE") > 0 || Utils
					.getDropRate("MUSHROOM_COW") > 0 || Utils.getDropRate("PARROT") > 0 || Utils.getDropRate("PIG") > 0 || Utils
						.getDropRate("PUFFERFISH") > 0 || Utils.getDropRate("RABBIT") > 0 || Utils.getDropRate("SALMON") > 0 || Utils
							.getDropRate("SHEEP") > 0 || Utils.getDropRate("SKELETON_HORSE") > 0 || Utils.getDropRate("SNOWMAN") > 0 || Utils
								.getDropRate("SQUID") > 0 || Utils.getDropRate("TROPICAL_FISH") > 0 || Utils.getDropRate("TURTLE") > 0 || Utils
									.getDropRate("VILLAGER") > 0 || Utils.getDropRate("ZOMBIE_HORSE") > 0)
		{
			lore.add(getMainColour() + "Passive Mobs:");
		}
		if (Utils.getDropRate("BAT") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.BAT));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("BAT")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("OCELOT") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.OCELOT));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("OCELOT")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("CHICKEN") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.CHICKEN));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("CHICKEN")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("COD") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.COD));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("COD")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("COW") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.COW));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("COW")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("DOLPHIN") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.DOLPHIN));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("DOLPHIN")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("DONKEY") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.DONKEY));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("DONKEY")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("HORSE") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.HORSE));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("HORSE")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("LLAMA") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.LLAMA));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("LLAMA")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("MULE") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.MULE));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("MULE")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("MUSHROOM_COW") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.MUSHROOM_COW));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("MUSHROOM_COW")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("PARROT") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.PARROT));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("PARROT")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("PIG") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.PIG));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("PIG")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("PUFFERFISH") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.PUFFERFISH));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("PUFFERFISH")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("RABBIT") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.RABBIT));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("RABBIT")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("SALMON") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.SALMON));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("SALMON")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("SHEEP") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.SHEEP));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("SHEEP")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("SKELETON_HORSE") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.SKELETON_HORSE));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("SKELETON_HORSE")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("SNOWMAN") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.SNOWMAN));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("SNOWMAN")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("SQUID") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.SQUID));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("SQUID")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("TROPICAL_FISH") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.TROPICAL_FISH));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("TROPICAL_FISH")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("TURTLE") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.TURTLE));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("TURTLE")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("VILLAGER") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.VILLAGER));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("VILLAGER")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("ZOMBIE_HORSE") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.ZOMBIE_HORSE));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("ZOMBIE_HORSE")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}

		/*
		 * NEUTRAL MOBS
		 */
		if (Utils.getDropRate("ENDERMAN") > 0 || Utils.getDropRate("IRON_GOLEM") > 0 || Utils.getDropRate("PIG_ZOMBIE") > 0 || Utils
			.getDropRate("POLAR_BEAR") > 0 || Utils.getDropRate("WOLF") > 0)
		{
			lore.add(getMainColour() + "Neutral Mobs:");
		}
		if (Utils.getDropRate("ENDERMAN") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.ENDERMAN));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("ENDERMAN")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("IRON_GOLEM") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.IRON_GOLEM));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("IRON_GOLEM")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("PIG_ZOMBIE") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.PIG_ZOMBIE));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("PIG_ZOMBIE")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("POLAR_BEAR") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.POLAR_BEAR));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("POLAR_BEAR")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}
		if (Utils.getDropRate("WOLF") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.WOLF));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("WOLF")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}

		/*
		 * PLAYER
		 */
		if (Utils.getDropRate("PLAYER") > 0)
		{
			lore.add(getMainColour() + "Players:");
		}
		if (Utils.getDropRate("PLAYER") > 0)
		{
			String newMobFormat = mobFormat.replace("%MOB%", MobNameController.getMobName(EntityType.PLAYER));
			newMobFormat = newMobFormat.replace("%RATE%", String.valueOf(Utils.getDropRate("PLAYER")));

			lore.add(Utils.convertColorCodes(newMobFormat));
		}

		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
