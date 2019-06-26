package ba.mobcoins.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
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
		return messagesConfig.getString("Messages.Global.Never_Joined");
	}
	
	public static String getGlobalWholeNumber()
	{
		return messagesConfig.getString("Messages.Global.Whole_Number");
	}
	
	public static String getGlobalNonPlayer()
	{
		return messagesConfig.getString("Messages.Global.Non_Player");
	}
	
	public static String getGlobalInsufficientPermission()
	{
		return messagesConfig.getString("Messages.Global.Insufficient_Permission");
	}
	
	public static String getGlobalUnknownCommand()
	{
		return messagesConfig.getString("Messages.Global.Unknown_Command");
	}
	
	/*
	 * 	BALANCE MESSAGES
	 */
	public static String getYourBalance()
	{
		return messagesConfig.getString("Messages.Balance.Your_Balance");
	}
	
	public static String getOtherBalance()
	{
		return messagesConfig.getString("Messages.Balance.Other_Balance");
	}
	
	/*
	 * 	PAY MESSAGES
	 */
	public static String getPaySender()
	{
		return messagesConfig.getString("Messages.Pay.Sender");
	}
	
	public static String getPayReceiver()
	{
		return messagesConfig.getString("Messages.Pay.Receiver");
	}
	
	public static String getPaySelf()
	{
		return messagesConfig.getString("Messages.Pay.Self");
	}
	
	public static String getPaySendZero()
	{
		return messagesConfig.getString("Messages.Pay.Zero");
	}
	
	public static String getPayNotEnough()
	{
		return messagesConfig.getString("Messages.Pay.Not_Enough");
	}
	
	/*
	 * 	ADD MESSAGES
	 */
	public static String getAddAdmin()
	{
		return messagesConfig.getString("Messages.Add.Admin_Message");
	}
	
	public static String getAddPlayer()
	{
		return messagesConfig.getString("Messages.Add.Player_Message");
	}
	
	public static String getAddZero()
	{
		return messagesConfig.getString("Messages.Add.Zero");
	}
	
	/*
	 * 	SET MESSAGES
	 */
	public static String getSetAdmin()
	{
		return messagesConfig.getString("Messages.Set.Admin_Message");
	}
	
	public static String getSetPlayer()
	{
		return messagesConfig.getString("Messages.Set.Player_Message");
	}
	
	/*
	 * 	REMOVE MESSAGES
	 */
	public static String getRemoveAdmin()
	{
		return messagesConfig.getString("Messages.Remove.Admin_Message");
	}
	
	public static String getRemovePlayer()
	{
		return messagesConfig.getString("Messages.Remove.Player_Message");
	}
	
	public static String getRemoveZero()
	{
		return messagesConfig.getString("Messages.Remove.Zero");
	}
	
	/*
	 * 	GIVE ITEM MESSAGES
	 */
	public static String getGiveItemAdmin()
	{
		return messagesConfig.getString("Messages.GiveItem.Admin_Message");
	}
	
	public static String getGiveItemPlayer()
	{
		return messagesConfig.getString("Messages.GiveItem.Player_Message");
	}
	
	public static String getGiveItemNoSpace()
	{
		return messagesConfig.getString("Messages.GiveItem.No_Space");
	}
	
	public static String getGiveItemUnfoundItem()
	{
		return messagesConfig.getString("Messages.GiveItem.Unfound_Item");
	}
	
	public static String getGiveItemUnfoundCategory()
	{
		return messagesConfig.getString("Messages.GiveItem.Unfound_Category");
	}
	
	/*
	 * 	SHOP MESSAGES
	 */
	public static String getShopBoughtItem()
	{
		return messagesConfig.getString("Messages.Shop.Bought_Item");
	}
	
	public static String getShopNotEnough()
	{
		return messagesConfig.getString("Messages.Shop.Not_Enough");
	}

	/*
	 * 	COIN MESSAGES
	 */
	public static String getCoinWithdraw()
	{
		return messagesConfig.getString("Messages.Coin.Withdraw");
	}

	public static String getCoinDeposit()
	{
		return messagesConfig.getString("Messages.Coin.Deposit");
	}

	public static String getCoinZero()
	{
		return messagesConfig.getString("Messages.Coin.Zero");
	}
	
	/*
	 * 	GAIN MESSAGES
	 */
	public static String getGainSingle()
	{
		return messagesConfig.getString("Messages.Gain.Single");
	}

	public static String getGainPlural()
	{
		return messagesConfig.getString("Messages.Gain.Plural");
	}
	
	/*
	 * 	RELOAD MESSAGES
	 */
	public static String getReload()
	{
		return messagesConfig.getString("Messages.Reload.Admin_Message");
	}
	
	/*
	 * 	HELP MESSAGES
	 */
	public static ArrayList<String> getHelp()
	{
		return (ArrayList<String>) messagesConfig.getStringList("Messages.Help");
	}
	
}
