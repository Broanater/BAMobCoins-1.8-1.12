package ba.mobcoins;

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

import ba.mobcoins.templates.CustomItem;

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


	public static String getTitle()
	{
		String title = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Options.Title"));
		return title;
	}

	public static String getPrefix()
	{
		String prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Options.Prefix"));
		
		if (prefix.charAt(prefix.length() - 1) != ' ')
		{
			prefix += " ";
		}
		
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

	public static boolean fullInv(Player p, int amount)
	{
		float rawAmount = amount/64;
		int neededSlots = (int) rawAmount + 1;
		Inventory inv = p.getInventory();
		int emptySlots = 0;
		for (ItemStack item : inv)
		{
			if (item == null || item.getType() == Material.AIR)
			{
				emptySlots++;
			}
		}

		return neededSlots < emptySlots;
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
			message = Messages.getGainSingle();
			message = message.replace("%MOB%", sMob);
			message = message.replace("%AMOUNT%", String.valueOf(sCoinsRecieved));
			
			message = Utils.getPrefix() + convertColorCodes(message);
		}
		else
		{
			message = Messages.getGainPlural();
			message = message.replace("%MOB%", sMob);
			message = message.replace("%AMOUNT%", String.valueOf(sCoinsRecieved));
			
			message = Utils.getPrefix() + convertColorCodes(message);
		}
		return message;
	}

	public static void insufficientPermissions(CommandSender sSender, String sCommand)
	{
		String message = Messages.getGlobalInsufficientPermission();
		
		message = message.replace("%COMMAND%", sCommand);
		
		sSender.sendMessage(getPrefix() + convertColorCodes(message));
	}

	public static void sendMessage(CommandSender sSender, String sMessage)
	{
		sSender.sendMessage(getPrefix() + convertColorCodes(sMessage));
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
		int damage = plugin.getConfig().getInt("Coin.Damage");

		ItemStack item = new ItemStack(Material.getMaterial(plugin.getConfig().getString("Coin.Item").toUpperCase()), amount, (short) damage);
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
	
	public static void runShopCommands(Player player, String itemId, List<String> sCommands)
	{
		for (String request : sCommands)
		{
			request = request.replace("%player%", player.getName());
			
			char typeIdentifier = request.charAt(0);
			String requestWithoutTypeId = request.substring(1, request.length());
			
			/* Run command as player */
			if (typeIdentifier == '>')
			{
				Bukkit.getServer().dispatchCommand(player, requestWithoutTypeId);
			}
			/* Run command as console. */
			else if (typeIdentifier == '!')
			{
				Bukkit.getServer().dispatchCommand(org.bukkit.Bukkit.getServer().getConsoleSender(), requestWithoutTypeId);
			}
			/* Send message to player purchasing */
			else if (typeIdentifier == ':')
			{
				sendMessage(player, requestWithoutTypeId);
			}
			/* Broadcast message to server */
			else if (typeIdentifier == '^')
			{
				sendBroadcast(requestWithoutTypeId);
			}
			else
			{
				System.out.println("[BAMobCoins] Command for item '" + itemId + "' does not have a type identifier. Ignoring command.");
			}
		}
	}
	
	public static boolean givePlayerItem(Player player, ItemStack rewardItem)
	{
		int amount = rewardItem.getAmount();
		/* Inventory full */
		if (!fullInv(player, amount))
		{
			return false;
		}
		/* Space */
		else
		{
			while (amount > 0)
			{
				if (amount >= 64)
				{
					amount -= 64;
					
					ItemStack item = new ItemStack(rewardItem.getType(), 64, rewardItem.getDurability());
					item.setItemMeta(rewardItem.getItemMeta());
					player.getInventory().addItem(item);
				}
				else
				{
					ItemStack item = new ItemStack(rewardItem.getType(), amount, rewardItem.getDurability());
					item.setItemMeta(rewardItem.getItemMeta());
					player.getInventory().addItem(item);
					
					amount -= amount;
				}
			}
			
			return true;
		}
		
		
	}
	
}
