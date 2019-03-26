package ca.live.brodya.mobcoins.templates;

import java.util.ArrayList;
import java.util.List;

public class CustomItem
{
	public String itemId;
	public String material;
	public int meta;
	public int amount;
	public int slot;
	public String displayName;
	public int price;
	public List<String> lore = new ArrayList<String>();
    public List<String> commands = new ArrayList<String>();
    
    public CustomItem(String sItemId, String sMaterial, int sMeta, int sAmount, int sSlot, String sDisplayName, int sPrice, List<String> sLore, List<String> sCommands)
    {
    	itemId = sItemId;
    	material = sMaterial;
    	meta = sMeta;
    	amount = sAmount;
    	slot = sSlot;
    	displayName = sDisplayName;
    	price = sPrice;
    	lore = sLore;
    	commands = sCommands;
    }
}
