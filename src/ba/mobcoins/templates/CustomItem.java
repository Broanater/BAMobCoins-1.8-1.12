package ba.mobcoins.templates;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class CustomItem
{
	public static enum ItemTypes {
			COMMAND,
			ITEM,
			FILLER
	};
	
	private ItemTypes itemType;
	private String itemKey;
	private ItemStack displayItem;
	private int location;
	private int price;
	
	/* One or the other is used for each item. Never both */
	private ArrayList<String> commands = new ArrayList<String>();
	private ItemStack rewardItem;
    
    public CustomItem(ItemTypes sItemType, String sItemId, ItemStack sDisplayItem, int sLocation, int sPrice, ArrayList<String> sCommands)
    {
    	itemType = sItemType;
    	itemKey = sItemId;
    	displayItem = sDisplayItem;
    	location = sLocation;
    	price = sPrice;
    	commands = sCommands;
    }
    
    public CustomItem(ItemTypes sItemType, String sItemId, ItemStack sDisplayItem, int sLocation, int sPrice, ItemStack sRewardItem)
    {
    	itemType = sItemType;
    	itemKey = sItemId;
    	displayItem = sDisplayItem;
    	location = sLocation;
    	price = sPrice;
    	rewardItem = sRewardItem;
    }
    
    public ItemTypes getItemType()
    {
    	return itemType;
    }
    
    public String getItemKey()
    {
    	return itemKey;
    }
    
    public ItemStack getDisplayItem()
    {
    	return displayItem;
    }
    
    public int getLocation()
    {
    	return location;
    }
    
    public int getPrice()
    {
    	return price;
    }
    
    public ArrayList<String> getCommands()
    {
    	return commands;
    }
    
    public ItemStack getRewardItem()
    {
    	return rewardItem;
    }
    
    
}
