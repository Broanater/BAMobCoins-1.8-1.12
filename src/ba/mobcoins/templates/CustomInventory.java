package ba.mobcoins.templates;

import java.util.ArrayList;

import org.bukkit.inventory.Inventory;

public class CustomInventory
{
	private String title;
	private Inventory inventory;
	private int coinInfoLocation;
	private int dropInfoLocation;
	private ArrayList<CustomItem> shopItems = new ArrayList<CustomItem>();
	
	public CustomInventory(String sTitle, Inventory sInv, int sCoinInfoLocation, int sDropInfoLocation)
	{
		title = sTitle;
		inventory = sInv;
		coinInfoLocation = sCoinInfoLocation;
		dropInfoLocation = sDropInfoLocation;
	}
	
	public CustomInventory(String sTitle, Inventory sInv, int sCoinInfoLocation, int sDropInfoLocation, ArrayList<CustomItem> sShopItems)
	{
		title = sTitle;
		inventory = sInv;
		coinInfoLocation = sCoinInfoLocation;
		dropInfoLocation = sDropInfoLocation;
		shopItems = sShopItems;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public Inventory getInventory()
	{
		return inventory;
	}
	
	public int getCoinInfoLocation()
	{
		return coinInfoLocation;
	}
	
	public int getDropInfoLocation()
	{
		return dropInfoLocation;
	}
	
	public ArrayList<CustomItem> getShopItems()
	{
		return shopItems;
	}
	
}
