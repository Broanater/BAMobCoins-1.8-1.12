package ba.mobcoins;

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
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.FileUtil;

import com.google.common.io.Files;

import net.md_5.bungee.api.ChatColor;

import ba.mobcoins.*;
import ba.mobcoins.templates.*;
import ba.mobcoins.templates.CustomItem.ItemTypes;

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

				/* Back button */
				int backSlot = categoryConfig.getInt("Category.BackSlot");
				if (backSlot > -1)
				{
					inv.setItem(backSlot, getBackButton());
				}

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

	private static String getSecondaryColour()
	{
		return Utils.convertColorCodes(shopConfig.getString("Shop.Drop_Info.Secondary_Colour"));
	}

	private static String getThirdColour()
	{
		return Utils.convertColorCodes(shopConfig.getString("Shop.Drop_Info.Third_Colour"));
	}

	private static int getDropInfoLocation()
	{
		return shopConfig.getInt("Shop.Menu.Drop_Info_Location");
	}

	private static ItemStack getDropInfo()
	{
		ItemStack item = getDropInfoItem();
		ItemMeta meta = item.getItemMeta();

		/* Passive mob percents */
		float batPercent = Utils.getBat();
		float squidPercent = Utils.getSquid();
		float rabbitPercent = Utils.getRabbit();
		float chickenPercent = Utils.getChicken();
		float pigPercent = Utils.getPig();
		float sheepPercent = Utils.getSheep();
		float cowPercent = Utils.getCow();
		float mushroomCowPercent = Utils.getMushroomCow();
		float snowmanPercent = Utils.getSnowman();
		float ocelotPercent = Utils.getOcelot();
		float horsePercent = Utils.getHorse();

		/* Hostile mob percents */
		float zombiePercent = Utils.getZombie();
		float skeletonPercent = Utils.getSkeleton();
		float spiderPercent = Utils.getSpider();
		float caveSpiderPercent = Utils.getCaveSpider();
		float creeperPercent = Utils.getCreeper();
		float endermanPercent = Utils.getEnderman();
		float blazePercent = Utils.getBlaze();
		float witchPercent = Utils.getWitch();
		float silverfishPercent = Utils.getSilverfish();
		float magmaCubePercent = Utils.getMagmaCube();
		float endermitePercent = Utils.getEndermite();
		float guardianPercent = Utils.getGuardian();
		float ghastPercent = Utils.getGhast();
		float slimePercent = Utils.getSlime();
		float giantPercent = Utils.getGiant();
		float witherPercent = Utils.getWither();
		float enderDragonPercent = Utils.getEnderDragon();

		/* Neutral mob percents */
		float villagerPercent = Utils.getVillager();
		float ironGolemPercent = Utils.getIronGolem();
		float pigmanPercent = Utils.getZombiePigman();
		float wolfPercent = Utils.getWolf();

		/* Player percent */
		float playerPercent = Utils.getPlayer();

		ArrayList<String> lore = new ArrayList<String>();
		/* Passive mobcoin display */
		if (batPercent > 0 || squidPercent > 0 || rabbitPercent > 0 || chickenPercent > 0 || pigPercent > 0 || sheepPercent > 0 || cowPercent > 0 || mushroomCowPercent > 0 || snowmanPercent > 0 || ocelotPercent > 0 || horsePercent > 0)
		{
			lore.add(getMainColour() + "Passive Mobs:");
		}
		if (Utils.getBat() > 0)
		{
			lore.add(getSecondaryColour() + "Bat - " + getThirdColour() + Utils.getBat() + "%");
		}
		if (Utils.getSquid() > 0)
		{
			lore.add(getSecondaryColour() + "Squid - " + getThirdColour() + Utils.getSquid() + "%");
		}
		if (Utils.getRabbit() > 0)
		{
			lore.add(getSecondaryColour() + "Rabbit - " + getThirdColour() + Utils.getRabbit() + "%");
		}
		if (Utils.getChicken() > 0)
		{
			lore.add(getSecondaryColour() + "Chicken - " + getThirdColour() + Utils.getChicken() + "%");
		}
		if (Utils.getPig() > 0)
		{
			lore.add(getSecondaryColour() + "Pig - " + getThirdColour() + Utils.getPig() + "%");
		}
		if (Utils.getSheep() > 0)
		{
			lore.add(getSecondaryColour() + "Sheep - " + getThirdColour() + Utils.getSheep() + "%");
		}
		if (Utils.getCow() > 0)
		{
			lore.add(getSecondaryColour() + "Cow - " + getThirdColour() + Utils.getCow() + "%");
		}
		if (Utils.getMushroomCow() > 0)
		{
			lore.add(getSecondaryColour() + "Mushroom Cow - " + getThirdColour() + Utils.getMushroomCow() + "%");
		}
		if (Utils.getSnowman() > 0)
		{
			lore.add(getSecondaryColour() + "Snowman - " + getThirdColour() + Utils.getSnowman() + "%");
		}
		if (Utils.getOcelot() > 0)
		{
			lore.add(getSecondaryColour() + "Ocelot - " + getThirdColour() + Utils.getOcelot() + "%");
		}
		if (Utils.getHorse() > 0)
		{
			lore.add(getSecondaryColour() + "Horse - " + getThirdColour() + Utils.getHorse() + "%");
		}

		/* Hostile mobcoin chance display */
		if (zombiePercent > 0 || skeletonPercent > 0 || spiderPercent > 0 || caveSpiderPercent > 0 || creeperPercent > 0 || endermanPercent > 0 || blazePercent > 0 || silverfishPercent > 0 || witchPercent > 0 || magmaCubePercent > 0 || endermitePercent > 0 || guardianPercent > 0 || ghastPercent > 0 || slimePercent > 0 || giantPercent > 0 || witherPercent > 0 || enderDragonPercent > 0)
		{
			lore.add(getMainColour() + "Hostile Mobs:");
		}
		if (Utils.getZombie() > 0)
		{
			lore.add(getSecondaryColour() + "Zombie - " + getThirdColour() + Utils.getZombie() + "%");
		}
		if (Utils.getSkeleton() > 0)
		{
			lore.add(getSecondaryColour() + "Skeleton - " + getThirdColour() + Utils.getSkeleton() + "%");
		}
		if (Utils.getSpider() > 0)
		{
			lore.add(getSecondaryColour() + "Spider - " + getThirdColour() + Utils.getSpider() + "%");
		}
		if (Utils.getCaveSpider() > 0)
		{
			lore.add(getSecondaryColour() + "Cave Spider - " + getThirdColour() + Utils.getCaveSpider() + "%");
		}
		if (Utils.getCreeper() > 0)
		{
			lore.add(getSecondaryColour() + "Creeper - " + getThirdColour() + Utils.getCreeper() + "%");
		}
		if (Utils.getEnderman() > 0)
		{
			lore.add(getSecondaryColour() + "Enderman - " + getThirdColour() + Utils.getEnderman() + "%");
		}
		if (Utils.getBlaze() > 0)
		{
			lore.add(getSecondaryColour() + "Blaze - " + getThirdColour() + Utils.getBlaze() + "%");
		}
		if (Utils.getWitch() > 0)
		{
			lore.add(getSecondaryColour() + "Witch - " + getThirdColour() + Utils.getWitch() + "%");
		}
		if (Utils.getSilverfish() > 0)
		{
			lore.add(getSecondaryColour() + "Silverfish - " + getThirdColour() + Utils.getSilverfish() + "%");
		}
		if (Utils.getMagmaCube() > 0)
		{
			lore.add(getSecondaryColour() + "Magma Cube - " + getThirdColour() + Utils.getMagmaCube() + "%");
		}
		if (Utils.getEndermite() > 0)
		{
			lore.add(getSecondaryColour() + "Endermite - " + getThirdColour() + Utils.getEndermite() + "%");
		}
		if (Utils.getGuardian() > 0)
		{
			lore.add(getSecondaryColour() + "Guardian - " + getThirdColour() + Utils.getGuardian() + "%");
		}
		if (Utils.getGhast() > 0)
		{
			lore.add(getSecondaryColour() + "Ghast - " + getThirdColour() + Utils.getGhast() + "%");
		}
		if (Utils.getSlime() > 0)
		{
			lore.add(getSecondaryColour() + "Slime - " + getThirdColour() + Utils.getSlime() + "%");
		}
		if (Utils.getGiant() > 0)
		{
			lore.add(getSecondaryColour() + "Giant - " + getThirdColour() + Utils.getGiant() + "%");
		}
		if (Utils.getWither() > 0)
		{
			lore.add(getSecondaryColour() + "Wither - " + getThirdColour() + Utils.getWither() + "%");
		}
		if (Utils.getEnderDragon() > 0)
		{
			lore.add(getSecondaryColour() + "Ender Dragon - " + getThirdColour() + Utils.getEnderDragon() + "%");
		}

		/* Neutral mobcoin chance display */
		if (villagerPercent > 0 || ironGolemPercent > 0 || pigmanPercent > 0 || wolfPercent > 0)
		{
			lore.add(getMainColour() + "Neutral Mobs:");
		}
		if (Utils.getVillager() > 0)
		{
			lore.add(getSecondaryColour() + "Villager - " + getThirdColour() + Utils.getVillager() + "%");
		}
		if (Utils.getIronGolem() > 0)
		{
			lore.add(getSecondaryColour() + "Iron Golem - " + getThirdColour() + Utils.getIronGolem() + "%");
		}
		if (Utils.getZombiePigman() > 0)
		{
			lore.add(getSecondaryColour() + "Zombie Pigman - " + getThirdColour() + Utils.getZombiePigman() + "%");
		}
		if (Utils.getWolf() > 0)
		{
			lore.add(getSecondaryColour() + "Wolf - " + getThirdColour() + Utils.getWolf() + "%");
		}

		/* Player mobcoin chance display */
		if (playerPercent > 0)
		{
			lore.add(getMainColour() + "Players:");
		}
		if (Utils.getPlayer() > 0)
		{
			lore.add(getSecondaryColour() + "Players - " + getThirdColour() + Utils.getPlayer() + "%");
		}

		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

}
