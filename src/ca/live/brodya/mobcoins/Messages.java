package ca.live.brodya.mobcoins;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;

public class Messages implements Listener
{
	private static Main plugin;

	private static FileConfiguration messagesConfig = null;
	private static File messagesConfigFile = null;
	
	public Messages(Main sPlugin)
	{
		plugin = sPlugin;
		
	}
	
	public static void reloadMessages()
	{
		if (messagesConfigFile == null)
		{
			messagesConfigFile = new File(plugin.getDataFolder(), "Messages.yml");
		}
		messagesConfig = YamlConfiguration.loadConfiguration(messagesConfigFile);

		/* Get the default from the jar. */
		if(!messagesConfigFile.exists())
		{
			/* If it doesn't exists copy it from the jar */
			plugin.saveResource("Messages.yml", false);
		}
		
		if (messagesConfigFile != null)
		{
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(messagesConfigFile);
			messagesConfig.setDefaults(defConfig);
		}
	}
	
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
	
	public static String getYourBalance()
	{
		return messagesConfig.getString("Messages.Balance.Your_Balance");
	}
	
	public static String getOtherBalance()
	{
		return messagesConfig.getString("Messages.Balance.Other_Balance");
	}
	
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
	
	public static String getSetAdmin()
	{
		return messagesConfig.getString("Messages.Set.Admin_Message");
	}
	
	public static String getSetPlayer()
	{
		return messagesConfig.getString("Messages.Set.Player_Message");
	}
	
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
	
	public static String getGiveItemAdmin()
	{
		return messagesConfig.getString("Messages.GiveItem.Admin_Message");
	}
	
	public static String getGiveItemPlayer()
	{
		return messagesConfig.getString("Messages.GiveItem.Player_Message");
	}
	
	public static String getGiveItemUnfound()
	{
		return messagesConfig.getString("Messages.GiveItem.Unfound_Item");
	}
	
	public static String getShopBoughtItem()
	{
		return messagesConfig.getString("Messages.Shop.Bought_Item");
	}
	
	public static String getShopNotEnough()
	{
		return messagesConfig.getString("Messages.Shop.Not_Enough");
	}

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
	
	public static String getReload()
	{
		return messagesConfig.getString("Messages.Reload.Admin_Message");
	}
	
	public static ArrayList<String> getHelp()
	{
		return (ArrayList<String>) messagesConfig.getStringList("Messages.Help");
	}
	
}
