package ba.mobcoins.events;

import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import ba.mobcoins.*;
import ba.mobcoins.apis.*;
import ba.mobcoins.controllers.*;
import ba.mobcoins.models.*;
import ba.mobcoins.utilities.*;

public class InventoryClick implements Listener
{
	private Main plugin;
	
	public InventoryClick(Main pl)
	{
		plugin = pl;
	}
	
	@EventHandler
	public void OnInvClick(InventoryClickEvent e)
	{
		if (e.getCurrentItem() != null)
		{
			/* VERSION CHECK */
			if(plugin.getServer().getVersion().contains("MC: 1.15"))
			{
				/* Menu event */
				if (e.getView().getTitle().equals(ShopController.getMenuTitle()))
				{
					for (Category category : ShopController.categories)
					{
						if (e.getCurrentItem().equals(category.getItem()))
						{
							Player player = (Player) e.getWhoClicked();
							player.closeInventory();

							if (category.getUsePermission())
							{
								if (player.hasPermission(category.getPermission()))
								{
									player.openInventory(ShopController.getShopInventory(player.getUniqueId().toString(), category.getKey()));
								}
							}
							else
							{
								player.openInventory(ShopController.getShopInventory(player.getUniqueId().toString(), category.getKey()));
							}
						}
					}

					e.setCancelled(true);
				}

				Iterator<Entry<String, CustomInventory>> it = ShopController.shopInvs.entrySet().iterator();
				while (it.hasNext())
				{
					Entry<String, CustomInventory> pair = it.next();

					CustomInventory customInv = (CustomInventory) pair.getValue();
					if (e.getView().getTitle().equals(customInv.getTitle()))
					{
						Player receiver = (Player) e.getWhoClicked();
						for (CustomItem customItem : customInv.getShopItems())
						{
							if (e.getCurrentItem().equals(customItem.getDisplayItem()))
							{
								String receiverUuid = receiver.getUniqueId().toString();
								if (CoinsAPI.getCoins(receiverUuid).intValue() >= customItem.getPrice())
								{
									receiver.closeInventory();

									String message = MessagesController.getShopBoughtItem()
										.replace("%ITEM_NAME%", customItem.getDisplayItem().getItemMeta().getDisplayName())
										.replace("%ITEM%", customItem.getDisplayItem().getType().toString())
										.replace("%ID%", customItem.getItemKey())
										.replace("%PRICE%", String.valueOf(customItem.getPrice()));

									if (customItem.getItemType() == CustomItem.ItemTypes.COMMAND)
									{
										Utils.runShopCommands(receiver, customItem.getItemKey(), customItem.getCommands());

										CoinsAPI.removeCoins(receiverUuid, customItem.getPrice());
									}
									else if (customItem.getItemType() == CustomItem.ItemTypes.ITEM)
									{
										/* Give the player the item */
										if (Utils.givePlayerItem(receiver, customItem.getRewardItem()))
										{
											receiver.sendMessage(Utils.convertColorCodes(message));
											CoinsAPI.removeCoins(receiverUuid, customItem.getPrice());
										}
										else
										{
											String noSpaceMessage = MessagesController.getGiveItemNoSpace();
											noSpaceMessage = noSpaceMessage.replace("%ITEM%", customItem.getDisplayItem().getItemMeta().getDisplayName());
											receiver.sendMessage(Utils.convertColorCodes(noSpaceMessage));
										}

									}
								}
								else
								{
									receiver.closeInventory();

									String message = MessagesController.getShopNotEnough()
										.replace("%ITEM%", customItem.getDisplayItem().getItemMeta().getDisplayName())
										.replace("%PRICE%", String.valueOf(customItem.getPrice()));

									receiver.sendMessage(Utils.convertColorCodes(message));
								}
							}

						}

						/* Back button */
						if (e.getCurrentItem().equals(ShopController.getBackButton()))
						{
							receiver.closeInventory();

							String uuid = receiver.getUniqueId().toString();

							receiver.openInventory(ShopController.getShopInventory(uuid, "MENU"));
						}

						e.setCancelled(true);
					}
				}
			}
			else
			{
				/* Menu event */
				if (e.getInventory().getTitle().equals(ShopController.getMenuTitle()))
				{
					for (Category category : ShopController.categories)
					{
						if (e.getCurrentItem().equals(category.getItem()))
						{
							Player player = (Player) e.getWhoClicked();
							player.closeInventory();

							if (category.getUsePermission())
							{
								if (player.hasPermission(category.getPermission()))
								{
									player.openInventory(ShopController.getShopInventory(player.getUniqueId().toString(), category.getKey()));
								}
							}
							else
							{
								player.openInventory(ShopController.getShopInventory(player.getUniqueId().toString(), category.getKey()));
							}
						}
					}

					e.setCancelled(true);
				}

				Iterator<Entry<String, CustomInventory>> it = ShopController.shopInvs.entrySet().iterator();
				while (it.hasNext())
				{
					Entry<String, CustomInventory> pair = it.next();

					CustomInventory customInv = (CustomInventory) pair.getValue();
					if (e.getInventory().getTitle().equals(customInv.getTitle()))
					{
						Player receiver = (Player) e.getWhoClicked();
						for (CustomItem customItem : customInv.getShopItems())
						{
							if (e.getCurrentItem().equals(customItem.getDisplayItem()))
							{
								String receiverUuid = receiver.getUniqueId().toString();
								if (CoinsAPI.getCoins(receiverUuid).intValue() >= customItem.getPrice())
								{
									receiver.closeInventory();

									String message = MessagesController.getShopBoughtItem()
										.replace("%ITEM_NAME%", customItem.getDisplayItem().getItemMeta().getDisplayName())
										.replace("%ITEM%", customItem.getDisplayItem().getType().toString())
										.replace("%ID%", customItem.getItemKey())
										.replace("%PRICE%", String.valueOf(customItem.getPrice()));

									if (customItem.getItemType() == CustomItem.ItemTypes.COMMAND)
									{
										Utils.runShopCommands(receiver, customItem.getItemKey(), customItem.getCommands());

										CoinsAPI.removeCoins(receiverUuid, customItem.getPrice());
									}
									else if (customItem.getItemType() == CustomItem.ItemTypes.ITEM)
									{
										/* Give the player the item */
										if (Utils.givePlayerItem(receiver, customItem.getRewardItem()))
										{
											receiver.sendMessage(Utils.convertColorCodes(message));
											CoinsAPI.removeCoins(receiverUuid, customItem.getPrice());
										}
										else
										{
											String noSpaceMessage = MessagesController.getGiveItemNoSpace();
											noSpaceMessage = noSpaceMessage.replace("%ITEM%", customItem.getDisplayItem().getItemMeta().getDisplayName());
											receiver.sendMessage(Utils.convertColorCodes(noSpaceMessage));
										}

									}
								}
								else
								{
									receiver.closeInventory();

									String message = MessagesController.getShopNotEnough()
										.replace("%ITEM%", customItem.getDisplayItem().getItemMeta().getDisplayName())
										.replace("%PRICE%", String.valueOf(customItem.getPrice()));

									receiver.sendMessage(Utils.convertColorCodes(message));
								}
							}

						}

						/* Back button */
						if (e.getCurrentItem().equals(ShopController.getBackButton()))
						{
							receiver.closeInventory();

							String uuid = receiver.getUniqueId().toString();

							receiver.openInventory(ShopController.getShopInventory(uuid, "MENU"));
						}

						e.setCancelled(true);
					}
				}
			}
			
		}
	}
}
