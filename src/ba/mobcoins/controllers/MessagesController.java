package ba.mobcoins.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

import ba.mobcoins.Main;
import ba.mobcoins.utilities.Utils;

public class MessagesController implements Listener
{
	private static Main plugin;

	private static File messagesFile = null;
	private static FileConfiguration messagesConfig = null;
	
	public MessagesController(Main sPlugin)
	{
		plugin = sPlugin;
		
	}
	
	public static void reload()
	{
		File langFolder = new File("plugins/BAMobCoins/lang");
		
		if (!langFolder.exists())
		{
			/* Create the folder */
			langFolder.mkdir();
		}
		

		String[] langFiles = {
				"lang_en"
		};
		
		for (String langName : langFiles)
		{
			/* Lang Files */
			File langEn = new File("plugins/BAMobCoins/lang", langName + ".yml");
			
			if (!langEn.exists())
			{
				try
				{
					/* Copy the lang_en.yml default file */
					FileUtils.copyInputStreamToFile(plugin.getResource("resources/lang/" + langName + ".yml"), langEn);
				}
				catch (IOException e)
				{
					Utils.sendError("Failed to load " + langName + ".yml");
				}
			}
		}
		
		messagesFile = new File("plugins/BAMobCoins/lang", getLangFile() + ".yml");
		
		if (!messagesFile.exists())
		{
			Utils.sendError("Failed to locate lang file, '" + messagesFile.getPath() + "'. Defaulting to lang_en.yml");
			messagesFile = new File("plugins/BAMobCoins/lang", "lang_en.yml");
		}
		
		messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
		
		if (messagesConfig != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(messagesFile);
			messagesConfig.setDefaults(defConfig);
		}
	}
	
	
	
	private static String getLangFile()
	{
		return plugin.getConfig().getString("Lang");
	}
	
	/*
	 * 	GLOBAL MESSAGES
	 */
	public static String getGlobalNeverJoined()
	{
		return getMessage("Messages.Global.Never_Joined");
	}
	
	public static String getGlobalWholeNumber()
	{
		return getMessage("Messages.Global.Whole_Number");
	}
	
	public static String getGlobalNonPlayer()
	{
		return getMessage("Messages.Global.Non_Player");
	}
	
	public static String getGlobalInsufficientPermission()
	{
		return getMessage("Messages.Global.Insufficient_Permission");
	}
	
	public static String getGlobalTooManyArgs()
	{
		return getMessage("Messages.Global.Too_Many_Args");
	}
	
	public static String getGlobalNotEnoughArgs()
	{
		return getMessage("Messages.Global.Not_Enough_Args");
	}
	
	public static String getGlobalUnknownCommand()
	{
		return getMessage("Messages.Global.Unknown_Command");
	}
	
	/*
	 * 	BALANCE MESSAGES
	 */
	public static String getYourBalance()
	{
		return getMessage("Messages.Balance.Your_Balance");
	}
	
	public static String getOtherBalance()
	{
		return getMessage("Messages.Balance.Other_Balance");
	}
	
	/*
	 * 	PAY MESSAGES
	 */
	public static String getPaySender()
	{
		return getMessage("Messages.Pay.Sender");
	}
	
	public static String getPayReceiver()
	{
		return getMessage("Messages.Pay.Receiver");
	}
	
	public static String getPaySelf()
	{
		return getMessage("Messages.Pay.Self");
	}
	
	public static String getPaySendZero()
	{
		return getMessage("Messages.Pay.Zero");
	}
	
	public static String getPayNotEnough()
	{
		return getMessage("Messages.Pay.Not_Enough");
	}
	
	/*
	 * 	ADD MESSAGES
	 */
	public static String getAddAdmin()
	{
		return getMessage("Messages.Add.Admin_Message");
	}
	
	public static String getAddPlayer()
	{
		return getMessage("Messages.Add.Player_Message");
	}
	
	public static String getAddNegative()
	{
		return getMessage("Messages.Add.Negative");
	}
	
	public static String getAddZero()
	{
		return getMessage("Messages.Add.Zero");
	}
	
	/*
	 * 	SET MESSAGES
	 */
	public static String getSetAdmin()
	{
		return getMessage("Messages.Set.Admin_Message");
	}
	
	public static String getSetPlayer()
	{
		return getMessage("Messages.Set.Player_Message");
	}
	
	/*
	 * 	REMOVE MESSAGES
	 */
	public static String getRemoveAdmin()
	{
		return getMessage("Messages.Remove.Admin_Message");
	}
	
	public static String getRemovePlayer()
	{
		return getMessage("Messages.Remove.Player_Message");
	}
	
	public static String getRemoveNegative()
	{
		return getMessage("Messages.Remove.Negative");
	}
	
	public static String getRemoveZero()
	{
		return getMessage("Messages.Remove.Zero");
	}
	
	/*
	 * 	GIVE ITEM MESSAGES
	 */
	public static String getGiveItemAdmin()
	{
		return getMessage("Messages.GiveItem.Admin_Message");
	}
	
	public static String getGiveItemPlayer()
	{
		return getMessage("Messages.GiveItem.Player_Message");
	}
	
	public static String getGiveItemNoSpace()
	{
		return getMessage("Messages.GiveItem.No_Space");
	}
	
	public static String getGiveItemUnfoundItem()
	{
		return getMessage("Messages.GiveItem.Unfound_Item");
	}
	
	public static String getGiveItemUnfoundCategory()
	{
		return getMessage("Messages.GiveItem.Unfound_Category");
	}
	
	/*
	 * 	SHOP MESSAGES
	 */
	public static String getShopBoughtItem()
	{
		return getMessage("Messages.Shop.Bought_Item");
	}
	
	public static String getShopNotEnough()
	{
		return getMessage("Messages.Shop.Not_Enough");
	}

	/*
	 * 	COIN MESSAGES
	 */
	public static String getCoinWithdraw()
	{
		return getMessage("Messages.Coin.Withdraw");
	}

	public static String getCoinDeposit()
	{
		return getMessage("Messages.Coin.Deposit");
	}
	
	public static String getCoinNotEnough()
	{
		return getMessage("Messages.Coin.Not_Enough");
	}

	public static String getCoinZero()
	{
		return getMessage("Messages.Coin.Zero");
	}
	
	/*
	 * 	GAIN MESSAGES
	 */
	public static String getGainSingle()
	{
		return getMessage("Messages.Gain.Single");
	}

	public static String getGainPlural()
	{
		return getMessage("Messages.Gain.Plural");
	}
	
	/*
	 *  MESSAGES MESSAGES - Thats confusing /BAMobCoins messages <header>
	 */
	
	public static String getMessagesUnknownSection()
	{
		return getMessage("Messages.Messages.Unknown_Section");
	}
	
	/*
	 * 	RELOAD MESSAGES
	 */
	public static String getReload()
	{
		return getMessage("Messages.Reload.Admin_Message");
	}
	
	/*
	 * 	HELP MESSAGES
	 */
	public static ArrayList<String> getHelp()
	{
		return (ArrayList<String>) messagesConfig.getStringList("Messages.Help");
	}

	
	
	
	/*
	 *  SEND A PLAYER THE DESIRED MESSAGES
	 */
	public static void sendGlobalMessages(CommandSender sender)
	{
		/* Global Messages */
		String neverJoined = Utils.convertColorCodes("&fNever_Joined: " + getGlobalNeverJoined());
		String wholeNumber = Utils.convertColorCodes("&fWhole_Number: " + getGlobalWholeNumber());
		String nonPlayer = Utils.convertColorCodes("&fNon_Player: " + getGlobalWholeNumber());
		String insufficientPermission = Utils.convertColorCodes("&fInsufficient_Permission: " + getGlobalInsufficientPermission());
		String unknownCommand = Utils.convertColorCodes("&fUnknown_Command: " + getGlobalUnknownCommand());

		/* Send messages */
		sender.sendMessage(Utils.convertColorCodes("&6Global Messages"));
		sender.sendMessage("  " + neverJoined);
		sender.sendMessage("  " + wholeNumber);
		sender.sendMessage("  " + nonPlayer);
		sender.sendMessage("  " + insufficientPermission);
		sender.sendMessage("  " + unknownCommand);

	}

	public static void sendBalanceMessages(CommandSender sender)
	{
		/* Balance Messages */
		String yourBalance = Utils.convertColorCodes("&fYour_Balance: " + getYourBalance());
		String otherBalance = Utils.convertColorCodes("&fOther_Balance: " + getOtherBalance());

		/* Send messages */
		sender.sendMessage(Utils.convertColorCodes("&6Balance Messages"));
		sender.sendMessage("  " + yourBalance);
		sender.sendMessage("  " + otherBalance);
	}

	public static void sendPayMessages(CommandSender sender)
	{
		/* Pay Messages */
		String paySender = Utils.convertColorCodes("&fSender: " + getPaySender());
		String payReceiver = Utils.convertColorCodes("&fReceiver: " + getPayReceiver());
		String paySelf = Utils.convertColorCodes("&fSelf: " + getPaySelf());
		String payZero = Utils.convertColorCodes("&fZero: " + getPaySendZero());
		String payNotEnough = Utils.convertColorCodes("&fNot_Enough: " + getPayNotEnough());

		/* Send messages */
		sender.sendMessage(Utils.convertColorCodes("&6Pay Messages"));
		sender.sendMessage("  " + paySender);
		sender.sendMessage("  " + payReceiver);
		sender.sendMessage("  " + paySelf);
		sender.sendMessage("  " + payZero);
		sender.sendMessage("  " + payNotEnough);
	}

	public static void sendAddMessages(CommandSender sender)
	{
		/* Add Messages */
		String addAdmin = Utils.convertColorCodes("&fAdmin_Message: " + getAddAdmin());
		String addPlayer = Utils.convertColorCodes("&fPlayer_Message: " + getAddPlayer());
		String addZero = Utils.convertColorCodes("&fZero: " + getAddZero());

		/* Send messages */
		sender.sendMessage(Utils.convertColorCodes("&6Add Messages"));
		sender.sendMessage("  " + addAdmin);
		sender.sendMessage("  " + addPlayer);
		sender.sendMessage("  " + addZero);
	}

	public static void sendSetMessages(CommandSender sender)
	{
		/* Set Messages */
		String setAdmin = Utils.convertColorCodes("&fAdmin_Message: " + getSetAdmin());
		String setPlayer = Utils.convertColorCodes("&fPlayer_Message: " + getSetPlayer());

		/* Send messages */
		sender.sendMessage(Utils.convertColorCodes("&6Set Messages"));
		sender.sendMessage("  " + setAdmin);
		sender.sendMessage("  " + setPlayer);
	}

	public static void sendRemoveMessages(CommandSender sender)
	{
		/* Remove Messages */
		String removeAdmin = Utils.convertColorCodes("&fAdmin_Message: " + getRemoveAdmin());
		String removePlayer = Utils.convertColorCodes("&fPlayer_Message: " + getRemovePlayer());
		String removeZero = Utils.convertColorCodes("&fZero: " + getRemoveZero());

		/* Send messages */
		sender.sendMessage(Utils.convertColorCodes("&6Remove Messages"));
		sender.sendMessage("  " + removeAdmin);
		sender.sendMessage("  " + removePlayer);
		sender.sendMessage("  " + removeZero);
	}

	public static void sendGiveItemMessages(CommandSender sender)
	{
		/* Give Item Messages */
		String giveItemAdmin = Utils.convertColorCodes("&fAdmin_Message: " + getGiveItemAdmin());
		String giveItemPlayer = Utils.convertColorCodes("&fPlayer_Message: " + getGiveItemPlayer());
		String giveItemUnfoundItem = Utils.convertColorCodes("&fUnfound_Item: " + getGiveItemUnfoundItem());
		String giveItemUnfoundCategory = Utils.convertColorCodes("&fUnfound_Item: " + getGiveItemUnfoundCategory());

		/* Send messages */
		sender.sendMessage(Utils.convertColorCodes("&6Give Item Messages"));
		sender.sendMessage("  " + giveItemAdmin);
		sender.sendMessage("  " + giveItemPlayer);
		sender.sendMessage("  " + giveItemUnfoundItem);
		sender.sendMessage("  " + giveItemUnfoundCategory);
	}

	public static void sendShopMessages(CommandSender sender)
	{
		/* Shop Messages */
		String shopBoughtItem = Utils.convertColorCodes("&fBought_Item: " + getShopBoughtItem());
		String shopNotEnough = Utils.convertColorCodes("&fNot_Enough: " + getShopNotEnough());

		/* Send messages */
		sender.sendMessage(Utils.convertColorCodes("&6Shop Messages"));
		sender.sendMessage("  " + shopBoughtItem);
		sender.sendMessage("  " + shopNotEnough);
	}

	public static void sendCoinMessages(CommandSender sender)
	{
		/* Coin Messages */
		String coinWithdraw = Utils.convertColorCodes("&fWithdraw: " + getCoinWithdraw());
		String coinDeposit = Utils.convertColorCodes("&fDeposit: " + getCoinDeposit());
		String coinNotEnough = Utils.convertColorCodes("&fNot_Enough: " + getCoinNotEnough());
		String coinZero = Utils.convertColorCodes("&fZero: " + getCoinZero());

		/* Send messages */
		sender.sendMessage(Utils.convertColorCodes("&6Coin Messages"));
		sender.sendMessage("  " + coinWithdraw);
		sender.sendMessage("  " + coinDeposit);
		sender.sendMessage("  " + coinNotEnough);
		sender.sendMessage("  " + coinZero);
	}

	public static void sendGainMessages(CommandSender sender)
	{
		String gainSingle = Utils.convertColorCodes("&fSingle: " + getGainSingle());
		String gainPluarl = Utils.convertColorCodes("&fPlural: " + getGainPlural());

		/* Send messages */
		sender.sendMessage(Utils.convertColorCodes("&6Gain Messages"));
		sender.sendMessage("  " + gainSingle);
		sender.sendMessage("  " + gainPluarl);
	}

	public static void sendMessagesMessages(CommandSender sender)
	{
		String messagesUnknownHeader = Utils.convertColorCodes("&fUnknown_Header: " + getMessagesUnknownSection());

		/* Send messages */
		sender.sendMessage(Utils.convertColorCodes("&6Messages Messages - CONFUSING"));
		sender.sendMessage("  " + messagesUnknownHeader);
	}

	public static void sendReloadMessages(CommandSender sender)
	{
		/* Reload Message */
		String reloadAdmin = Utils.convertColorCodes("&fAdmin_Message: " + getReload());

		/* Send messages */
		sender.sendMessage(Utils.convertColorCodes("&6Reload Messages"));
		sender.sendMessage("  " + reloadAdmin);
	}
	

	
	
	/* Helper methods */
	private static String getMessage(String path)
	{
		String message = messagesConfig.getString(path)
				.replace("%PREFIX%", ConfigController.getPrefix());
		
		return message;
	}
}
