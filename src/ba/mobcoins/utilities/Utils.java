package ba.mobcoins.utilities;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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

import ba.mobcoins.Main;
import ba.mobcoins.apis.CoinsAPI;
import ba.mobcoins.controllers.ConfigController;
import ba.mobcoins.controllers.MessagesController;
import ba.mobcoins.models.CustomItem;

public class Utils implements Listener
{
	private static Main plugin;

	public Utils(Main pl)
	{
		plugin = pl;
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
			message = MessagesController.getGainSingle()
			.replace("%MOB%", sMob)
			.replace("%AMOUNT%", String.valueOf(sCoinsRecieved));
		}
		else
		{
			message = MessagesController.getGainPlural()
				.replace("%MOB%", sMob)
				.replace("%AMOUNT%", String.valueOf(sCoinsRecieved));
		}
		return convertColorCodes(message);
	}

	public static void insufficientPermissions(CommandSender sSender, String sCommand)
	{
		String message = MessagesController.getGlobalInsufficientPermission()
				.replace("%COMMAND%", sCommand);
		
		sSender.sendMessage(convertColorCodes(message));
	}

	public static void sendMessage(CommandSender sSender, String sMessage)
	{
		sSender.sendMessage(convertColorCodes(sMessage));
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
			request = request.replace("%PLAYER%", player.getName());
			
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
				requestWithoutTypeId = requestWithoutTypeId.replace("%PREFIX%", ConfigController.getPrefix());
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
	
	public static void sendError(String err)
	{
		Bukkit.getConsoleSender().sendMessage(convertColorCodes("&f[&6" + plugin.getName() + "&f] &4&lERROR:&r" + err));
	}
	
	public static double getDropRate(String entityName)
	{
		HashMap<String, Double> dropRates = ConfigController.getDropRates();
		
		if (dropRates.get(entityName) == null)
		{
			return 0;
		}
		else
		{
			return dropRates.get(entityName);
		}
	}
	
}
