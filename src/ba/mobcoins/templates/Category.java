package ba.mobcoins.templates;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

public class Category
{
	private String key;
	private ItemStack item;
	
	private int location;
	private String file;
	
	private boolean usePermission;
	private String permission;
	
	public Category(String sKey, ItemStack sItem, int sLocation, String sFile, boolean sUsePermission)
	{
		key = sKey;
		item = sItem;
		location = sLocation;
		file = sFile;
		usePermission = sUsePermission;
	}
	
	public Category(String sKey, ItemStack sItem, int sLocation, String sFile, boolean sUsePermission, String sPermission)
	{
		key = sKey;
		item = sItem;
		location = sLocation;
		file = sFile;
		usePermission = sUsePermission;
		permission = sPermission;
	}
	
	
	/* Getter methods */
	public String getKey()
	{
		return key;
	}
	
	public ItemStack getItem()
	{
		return item;
	}
	
	public int getLocation()
	{
		return location;
	}
	
	public String getFile()
	{
		return file;
	}
	
	public boolean getUsePermission()
	{
		return usePermission;
	}
	
	public String getPermission()
	{
		return permission;
	}
	
}
