package ca.live.brodya.mobcoins;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ca.live.brodya.mobcoins.templates.CustomItem;

public class Utils implements Listener
{
	private static Main plugin;

	public Utils(Main pl)
	{
		plugin = pl;
	}

	/*
	 * Passive Mobs Start
	 */
	public static int getPig()
	{
		int slot = plugin.getConfig().getInt("DropChances.PIG");
		return slot;
	}

	public static int getBat()
	{
		int slot = plugin.getConfig().getInt("DropChances.BAT");
		return slot;
	}

	public static int getSquid()
	{
		int slot = plugin.getConfig().getInt("DropChances.SQUID");
		return slot;
	}

	public static int getRabbit()
	{
		int slot = plugin.getConfig().getInt("DropChances.RABBIT");
		return slot;
	}

	public static int getCow()
	{
		int slot = plugin.getConfig().getInt("DropChances.COW");
		return slot;
	}

	public static int getMushroomCow()
	{
		int slot = plugin.getConfig().getInt("DropChances.MUSHROOMCOW");
		return slot;
	}

	public static int getSnowman()
	{
		int slot = plugin.getConfig().getInt("DropChances.SNOWMAN");
		return slot;
	}

	public static int getOcelot()
	{
		int slot = plugin.getConfig().getInt("DropChances.OCELOT");
		return slot;
	}

	public static int getHorse()
	{
		int slot = plugin.getConfig().getInt("DropChances.HORSE");
		return slot;
	}

	public static int getSheep()
	{
		int slot = plugin.getConfig().getInt("DropChances.SHEEP");
		return slot;
	}

	public static int getChicken()
	{
		int slot = plugin.getConfig().getInt("DropChances.CHICKEN");
		return slot;
	}
	/*
	 * Passive Mobs End
	 */

	/*
	 * Hostile Mobs Start
	 */
	public static int getZombie()
	{
		int slot = plugin.getConfig().getInt("DropChances.ZOMBIE");
		return slot;
	}

	public static int getSkeleton()
	{
		int slot = plugin.getConfig().getInt("DropChances.SKELETON");
		return slot;
	}

	public static int getSpider()
	{
		int slot = plugin.getConfig().getInt("DropChances.SPIDER");
		return slot;
	}

	public static int getCaveSpider()
	{
		int slot = plugin.getConfig().getInt("DropChances.CAVESPIDER");
		return slot;
	}

	public static int getSilverfish()
	{
		int slot = plugin.getConfig().getInt("DropChances.SILVERFISH");
		return slot;
	}

	public static int getMagmaCube()
	{
		int slot = plugin.getConfig().getInt("DropChances.MAGMACUBE");
		return slot;
	}

	public static int getEndermite()
	{
		int slot = plugin.getConfig().getInt("DropChances.ENDERMITE");
		return slot;
	}

	public static int getGuardian()
	{
		int slot = plugin.getConfig().getInt("DropChances.GUARDIAN");
		return slot;
	}

	public static int getGhast()
	{
		int slot = plugin.getConfig().getInt("DropChances.GHAST");
		return slot;
	}

	public static int getSlime()
	{
		int slot = plugin.getConfig().getInt("DropChances.SLIME");
		return slot;
	}

	public static int getGiant()
	{
		int slot = plugin.getConfig().getInt("DropChances.GIANT");
		return slot;
	}

	public static int getWither()
	{
		int slot = plugin.getConfig().getInt("DropChances.WITHER");
		return slot;
	}

	public static int getEnderDragon()
	{
		int slot = plugin.getConfig().getInt("DropChances.ENDERDRAGON");
		return slot;
	}

	public static int getCreeper()
	{
		int slot = plugin.getConfig().getInt("DropChances.CREEPER");
		return slot;
	}

	public static int getEnderman()
	{
		int slot = plugin.getConfig().getInt("DropChances.ENDERMAN");
		return slot;
	}

	public static int getBlaze()
	{
		int slot = plugin.getConfig().getInt("DropChances.BLAZE");
		return slot;
	}

	public static int getWitch()
	{
		int slot = plugin.getConfig().getInt("DropChances.WITCH");
		return slot;
	}
	/*
	 * Hostile Mobs End
	 */

	/*
	 * Neutral Mobs Start
	 */
	public static int getIronGolem()
	{
		int slot = plugin.getConfig().getInt("DropChances.IRONGOLEM");
		return slot;
	}

	public static int getVillager()
	{
		int slot = plugin.getConfig().getInt("DropChances.VILLAGER");
		return slot;
	}

	public static int getWolf()
	{
		int slot = plugin.getConfig().getInt("DropChances.WOLF");
		return slot;
	}

	public static int getZombiePigman()
	{
		int slot = plugin.getConfig().getInt("DropChances.PIGMAN");
		return slot;
	}
	/*
	 * Neutral Mobs End
	 */

	/*
	 * Player Start
	 */
	public static int getPlayer()
	{
		int slot = plugin.getConfig().getInt("DropChances.PLAYER");
		return slot;
	}
	/*
	 * Player End
	 */

	public static ItemStack createItem(Material mat, int amt, int durability, String name)
	{
		ItemStack item = new ItemStack(mat, amt);
		ItemMeta meta = item.getItemMeta();
		item.setDurability((short) durability);
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}

	public static int getPrice(int i, Player p)
	{
		int slot = plugin.getConfig().getInt("Shop." + i + ".Price");
		return slot;
	}

	public static ItemStack getItem(String itemId, Player p)
	{
		if (!plugin.getConfig().contains("Shop." + itemId))
		{
			return null;
		}

		int amnt = plugin.getConfig().getInt("Shop." + itemId + ".Amount");
		int id = plugin.getConfig().getInt("Shop." + itemId + ".Meta");

		ItemStack item = new ItemStack(Material.getMaterial(plugin.getConfig().getString("Shop." + itemId + ".Item").toUpperCase()), amnt, (short) id);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.convertColorCodes(plugin.getConfig().getString("Shop." + itemId + ".DisplayName")));

		List<String> loreRaw = plugin.getConfig().getStringList("Shop." + itemId + ".Lore");

		loreRaw.add("&7Price: " + plugin.getConfig().getInt("Shop." + itemId + ".Price"));

		List<String> lore = new ArrayList<String>();
		for (String loreLine : loreRaw)
		{
			lore.add(Utils.convertColorCodes(loreLine));
		}

		meta.setLore(lore);

		item.setItemMeta(meta);
		return item;
	}

	private static ItemStack getinfo()
	{
		ItemStack item = new ItemStack(Material.BOOK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + getCurrencyNamePlural() + ChatColor.GRAY + " Can be obtained by killing the following mobs.");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + ChatColor.UNDERLINE + "Available Mobs:");

		/* Passive mob percents */
		float batPercent = getBat();
		float squidPercent = getSquid();
		float rabbitPercent = getRabbit();
		float chickenPercent = getChicken();
		float pigPercent = getPig();
		float sheepPercent = getSheep();
		float cowPercent = getCow();
		float mushroomCowPercent = getMushroomCow();
		float snowmanPercent = getSnowman();
		float ocelotPercent = getOcelot();
		float horsePercent = getHorse();

		/* Hostile mob percents */
		float zombiePercent = getZombie();
		float skeletonPercent = getSkeleton();
		float spiderPercent = getSpider();
		float caveSpiderPercent = getCaveSpider();
		float creeperPercent = getCreeper();
		float endermanPercent = getEnderman();
		float blazePercent = getBlaze();
		float witchPercent = getWitch();
		float silverfishPercent = getSilverfish();
		float magmaCubePercent = getMagmaCube();
		float endermitePercent = getEndermite();
		float guardianPercent = getGuardian();
		float ghastPercent = getGhast();
		float slimePercent = getSlime();
		float giantPercent = getGiant();
		float witherPercent = getWither();
		float enderDragonPercent = getEnderDragon();

		/* Neutral mob percents */
		float villagerPercent = getVillager();
		float ironGolemPercent = getIronGolem();
		float pigmanPercent = getZombiePigman();
		float wolfPercent = getWolf();

		/* Player percent */
		float playerPercent = getPlayer();

		/* Passive mobcoin display */
		if (batPercent > 0 || squidPercent > 0 || rabbitPercent > 0 || chickenPercent > 0 || pigPercent > 0 || sheepPercent > 0 || cowPercent > 0 || mushroomCowPercent > 0 || snowmanPercent > 0 || ocelotPercent > 0 || horsePercent > 0)
		{
			lore.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Passive Mobs:");
		}
		if (getBat() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Bat (" + getBat() + "%)");
		}
		if (getSquid() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Squid (" + getSquid() + "%)");
		}
		if (getRabbit() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Rabbit (" + getRabbit() + "%)");
		}
		if (getChicken() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Chicken (" + getChicken() + "%)");
		}
		if (getPig() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Pig (" + getPig() + "%)");
		}
		if (getSheep() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Sheep (" + getSheep() + "%)");
		}
		if (getCow() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Cow (" + getCow() + "%)");
		}
		if (getMushroomCow() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Mushroom Cow (" + getMushroomCow() + "%)");
		}
		if (getSnowman() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Snowman (" + getSnowman() + "%)");
		}
		if (getOcelot() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Ocelot (" + getOcelot() + "%)");
		}
		if (getHorse() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Horse (" + getHorse() + "%)");
		}

		/* Hostile mobcoin chance display */
		if (zombiePercent > 0 || skeletonPercent > 0 || spiderPercent > 0 || caveSpiderPercent > 0 || creeperPercent > 0 || endermanPercent > 0 || blazePercent > 0 || silverfishPercent > 0 || witchPercent > 0 || magmaCubePercent > 0 || endermitePercent > 0 || guardianPercent > 0 || ghastPercent > 0 || slimePercent > 0 || giantPercent > 0 || witherPercent > 0 || enderDragonPercent > 0)
		{
			lore.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Hostile Mobs:");
		}
		if (getZombie() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Zombie (" + getZombie() + "%)");
		}
		if (getSkeleton() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Skeleton (" + getSkeleton() + "%)");
		}
		if (getSpider() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Spider (" + getSpider() + "%)");
		}
		if (getCaveSpider() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Cave Spider (" + getCaveSpider() + "%)");
		}
		if (getCreeper() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Creeper (" + getCreeper() + "%)");
		}
		if (getEnderman() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Enderman (" + getEnderman() + "%)");
		}
		if (getBlaze() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Blaze (" + getBlaze() + "%)");
		}
		if (getWitch() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Witch (" + getWitch() + "%)");
		}
		if (getSilverfish() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Silverfish (" + getSilverfish() + "%)");
		}
		if (getMagmaCube() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Magma Cube (" + getMagmaCube() + "%)");
		}
		if (getEndermite() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Endermite (" + getEndermite() + "%)");
		}
		if (getGuardian() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Guardian (" + getGuardian() + "%)");
		}
		if (getGhast() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Ghast (" + getGhast() + "%)");
		}
		if (getSlime() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Slime (" + getSlime() + "%)");
		}
		if (getGiant() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Giant (" + getGiant() + "%)");
		}
		if (getWither() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Wither (" + getWither() + "%)");
		}
		if (getEnderDragon() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Ender Dragon (" + getEnderDragon() + "%)");
		}

		/* Neutral mobcoin chance display */
		if (villagerPercent > 0 || ironGolemPercent > 0 || pigmanPercent > 0 || wolfPercent > 0)
		{
			lore.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Neutral Mobs:");
		}
		if (getVillager() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Villager (" + getVillager() + "%)");
		}
		if (getIronGolem() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Iron Golem (" + getIronGolem() + "%)");
		}
		if (getZombiePigman() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Zombie Pigman (" + getZombiePigman() + "%)");
		}
		if (getWolf() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Wolf (" + getWolf() + "%)");
		}

		/* Player mobcoin chance display */
		if (playerPercent > 0)
		{
			lore.add(ChatColor.AQUA + "" + ChatColor.BOLD + "Players:");
		}
		if (getPlayer() > 0)
		{
			lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Players (" + getPlayer() + "%)");
		}

		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	private static ItemStack getCoins(Player p)
	{
		String player = p.getUniqueId().toString();
		ItemStack item = new ItemStack(Material.DOUBLE_PLANT);
		ItemMeta im = item.getItemMeta();
		if (CoinsAPI.getCoins(player) > 1)
		{
			im.setDisplayName(ChatColor.BOLD + "" + ChatColor.GOLD + "You have " + CoinsAPI.getCoins(player) + " " + getCurrencyNamePlural());
		}
		else
		{

			im.setDisplayName(ChatColor.BOLD + "" + ChatColor.GOLD + "You have " + CoinsAPI.getCoins(player) + " " + getCurrencyNameSingle());
		}
		item.setItemMeta(im);
		return item;
	}

	private static ItemStack getBorder()
	{
		ItemStack item = createItem(Material.STAINED_GLASS_PANE, 1, 11, " ");
		return item;
	}

	public static String getTitle()
	{
		String title = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Options.Title"));
		return title;
	}

	public static String getPrefix()
	{
		String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Options.Prefix"));
		return prefix;
	}

	public static String getCurrencyNameSingle()
	{
		String currency = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Options.CurrencyNameSingle"));
		return currency;
	}

	public static String getCurrencyNamePlural()
	{
		String currency = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Options.CurrencyNamePlural"));
		return currency;
	}

	public static boolean hasAccount(Player p)
	{
		return plugin.getConfig().contains("Players." + p.getUniqueId());
	}

	public static List<CustomItem> getShopItems()
	{
		List<CustomItem> customItems = new ArrayList<CustomItem>();
		for (String key : plugin.getConfig().getConfigurationSection("Shop").getKeys(false))
		{
			String material = plugin.getConfig().getString("Shop." + key + ".Item");
			int meta = plugin.getConfig().getInt("Shop." + key + ".Meta");
			int amount = plugin.getConfig().getInt("Shop." + key + ".Amount");
			int slot = plugin.getConfig().getInt("Shop." + key + ".Slot");
			String displayName = plugin.getConfig().getString("Shop." + key + ".DisplayName");
			int price = plugin.getConfig().getInt("Shop." + key + ".Price");
			List<String> lore = plugin.getConfig().getStringList("Shop." + key + ".Lore");
			List<String> commands = plugin.getConfig().getStringList("Shop." + key + ".Commands");

			CustomItem customItem = new CustomItem(key, material, meta, amount, slot, displayName, price, lore, commands);
			customItems.add(customItem);
		}

		return customItems;
	}

	public static Inventory showInventory(Player p)
	{
		Inventory inv = Bukkit.createInventory(null, 54, getTitle());
		inv.setItem(0, getBorder());
		inv.setItem(1, getBorder());
		inv.setItem(2, getBorder());
		inv.setItem(3, getBorder());
		inv.setItem(4, getCoins(p));
		inv.setItem(5, getBorder());
		inv.setItem(6, getBorder());
		inv.setItem(7, getBorder());
		inv.setItem(8, getBorder());
		inv.setItem(9, getBorder());
		inv.setItem(17, getBorder());
		inv.setItem(18, getBorder());

		inv.setItem(26, getBorder());
		inv.setItem(27, getBorder());
		inv.setItem(35, getBorder());
		inv.setItem(36, getBorder());
		inv.setItem(44, getBorder());
		inv.setItem(45, getBorder());
		inv.setItem(46, getBorder());
		inv.setItem(47, getBorder());
		inv.setItem(48, getBorder());
		inv.setItem(49, getinfo());
		inv.setItem(50, getBorder());
		inv.setItem(51, getBorder());
		inv.setItem(52, getBorder());
		inv.setItem(53, getBorder());
		for (CustomItem customItem : getShopItems())
		{
			inv.setItem(customItem.slot, getItem(customItem.itemId, p));
		}
		inv.setItem(0, getBorder());
		return inv;
	}

	public static boolean fullInv(Player p)
	{
		int check = p.getInventory().firstEmpty();
		return check == -1;
	}

	public static boolean canAfford(Player p, int slot)
	{
		return plugin.getConfig().getInt("Players." + p.getUniqueId() + ".Tokens") >= plugin.getConfig().getInt("Shop." + slot + ".Price");
	}

	public static String getBroadcastPrefix()
	{
		String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Options.BroadcastPrefix"));
		return prefix;
	}

	public static String getCurrencyIncreaseMessage(String sMob, int sCoinsRecieved)
	{
		String message = "";
		if (sCoinsRecieved > 1)
		{
			message = Utils.getPrefix() + ChatColor.GRAY + " You killed a " + sMob + " and gained " + sCoinsRecieved + " " + getCurrencyNamePlural() + "!";
		}
		else
		{
			message = Utils.getPrefix() + ChatColor.GRAY + " You killed a " + sMob + " and gained " + sCoinsRecieved + " " + getCurrencyNameSingle() + "!";
		}
		return message;
	}

	public static void insufficientPermissions(CommandSender sSender, String sCommand)
	{
		String message = Messages.getGlobalInsufficientPermission();
		
		message = message.replace("%COMMAND%", sCommand);
		
		sSender.sendMessage(getPrefix() + " " + convertColorCodes(message));
	}

	public static void sendMessage(CommandSender sSender, String sMessage)
	{
		sSender.sendMessage(getPrefix() + ChatColor.AQUA + sMessage);
	}

	public static void sendBroadcast(String sMessage)
	{
		Bukkit.broadcastMessage(getBroadcastPrefix() + " " + convertColorCodes(sMessage));
	}

	public static String convertColorCodes(String sString)
	{
		String newString = ChatColor.translateAlternateColorCodes('&', sString);
		return newString;
	}
	
	public static ItemStack getCoinItem(int amount)
	{
		if (!plugin.getConfig().contains("Coin"))
		{
			return null;
		}

		String name = plugin.getConfig().getString("Coin.Name");
		int id = plugin.getConfig().getInt("Coin.Meta");

		ItemStack item = new ItemStack(Material.getMaterial(plugin.getConfig().getString("Coin.Item").toUpperCase()), amount, (short) id);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(Utils.convertColorCodes(name));

		List<String> loreRaw = plugin.getConfig().getStringList("Coin.Lore");

		List<String> lore = new ArrayList<String>();
		for (String loreLine : loreRaw)
		{
			lore.add(Utils.convertColorCodes(loreLine));
		}

		meta.setLore(lore);

		item.setItemMeta(meta);
		return item;
	}

	public static void withdrawCoins(Player player, int amount)
	{
		player.getInventory().addItem(getCoinItem(amount));
		CoinsAPI.removeCoins(player.getUniqueId().toString(), amount);
	}
	
	public static void runShopCommands(Player player, List<String> sCommands)
	{
		for (String command : sCommands)
		{
			String request = command.replace("%PLAYER%", player.getName());
			if (request.startsWith("[MESSAGE]"))
			{
				request = request.replace("[MESSAGE]", "");
				Utils.sendMessage(player, request);
			}
			else if (request.startsWith("[BROADCAST]"))
			{
				request = request.replace("[BROADCAST]", "");
				sendBroadcast(request);
			}
			else
			{
				Bukkit.getServer().dispatchCommand(org.bukkit.Bukkit.getServer().getConsoleSender(), request);
			}
		}
	}
}
