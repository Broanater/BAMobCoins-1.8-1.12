package ba.mobcoins;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import ba.mobcoins.apis.*;
import ba.mobcoins.bstats.Metrics;
import ba.mobcoins.commands.Commands;
import ba.mobcoins.controllers.*;
import ba.mobcoins.events.*;
import ba.mobcoins.updater.UpdateChecker;
import ba.mobcoins.utilities.*;

public class Main extends org.bukkit.plugin.java.JavaPlugin implements Listener
{
	public HashMap<String, Integer> coins = new HashMap<String, Integer>();
	PluginDescriptionFile pdf;


	public Main()
	{
		this.pdf = getDescription();
	}
	

	public Main(Main pl)
	{
	}

	public void onEnable()
	{
		registerEvents();
		getCommand("BAMobCoins").setExecutor(new Commands(this));
		
		initializeFiles();

		/* Check if theres any updates for the plugin on spigot. */
		new UpdateChecker(this).checkForUpdate();
		
		/* Enable bStats */
		new Metrics(this);

		System.out.print("-------------------------------");
		System.out.print("");
		System.out.print("    " + this.getName() + " Enabled!");
		System.out.print("");
		System.out.print("-------------------------------");
	}

	public void onDisable()
	{
		BalanceController.save();
		
		
		System.out.print("-------------------------------");
		System.out.print("");
		System.out.print("    " + this.getName() + " Disabled!");
		System.out.print("");
		System.out.print("-------------------------------");
	}

	private void registerEvents()
	{
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		
		PluginManager pm = Bukkit.getServer().getPluginManager();
		
		/* Utilities */
		pm.registerEvents(new Utils(this), this);
		
		/* Events */
		pm.registerEvents(new InventoryClick(), this);
		pm.registerEvents(new EntityDeath(), this);
		pm.registerEvents(new PlayerInteract(), this);
		pm.registerEvents(new PlayerJoin(), this);
		
		/* Apis */
		pm.registerEvents(new CoinsAPI(this), this);
		
		/* Controllers */
		pm.registerEvents(new MessagesController(this), this);
		pm.registerEvents(new BalanceController(this), this);
		pm.registerEvents(new ConfigController(this), this);
		pm.registerEvents(new MobNameController(this), this);
		

		pm.registerEvents(new ShopController(this), this);
	}
	
	private void initializeFiles()
	{
		ConfigController.reload();
		MessagesController.reload();
		MobNameController.reload();
		
		ShopController.reload();
		
		/* Balance Work */
		BalanceController.createFile();
		BalanceController.load();
	}
}
