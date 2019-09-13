package ba.mobcoins.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import ba.mobcoins.Main;
import ba.mobcoins.controllers.ConfigController;


public class CustomLogger implements Listener
{
	private static Main plugin;
	private static Logger logger;
	
	public CustomLogger(Main pl)
	{
		plugin = pl;
		logger = plugin.getLogger();
	}
	
	public static void sendMessage(String message)
	{
		logger.log(Level.INFO, message);
	}
	
	public static void sendError(String err)
	{
		logger.log(Level.WARNING, err);
	}
	
	public static void sendError(Exception err)
	{
		if (ConfigController.getDebug())
		{
			logger.log(Level.WARNING, err.getMessage());
		}
	}
}
