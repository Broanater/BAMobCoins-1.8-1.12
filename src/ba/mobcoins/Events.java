package ba.mobcoins;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import ba.mobcoins.templates.*;

public class Events implements org.bukkit.event.Listener
{
	@SuppressWarnings("unused")
	private static Main plugin;

	public Events(Main pl)
	{
		plugin = pl;
	}

	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent e)
	{
		Player player = e.getPlayer();
		String playerUuid = player.getUniqueId().toString();
		CoinsAPI.createPlayer(playerUuid);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void rightClickItem(PlayerInteractEvent event)
	{
		Player player = event.getPlayer();
		ItemStack item = null;
		try
		{
			item = player.getInventory().getItemInHand();
		}
		catch (Exception e)
		{
			item = player.getInventory().getItemInMainHand();
		}
		
		
		int amount = item.getAmount();

		if (item.equals(Utils.getCoinItem(amount)))
		{
			CoinsAPI.addCoins(player.getUniqueId().toString(), amount);
			player.getInventory().removeItem(item);

			String message = Messages.getCoinDeposit();

			message = message.replace("%AMOUNT%", String.valueOf(amount));

			player.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(message));
		}

	}

	@EventHandler
	public void OnInvClick(InventoryClickEvent e)
	{
		if (e.getCurrentItem() != null)
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

								String message = Messages.getShopBoughtItem();
								message = message.replace("%ITEM%", customItem.getDisplayItem().getItemMeta().getDisplayName());
								message = message.replace("%PRICE%", String.valueOf(customItem.getPrice()));

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
										receiver.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(message));
										CoinsAPI.removeCoins(receiverUuid, customItem.getPrice());
									}
									else
									{
										String noSpaceMessage = Messages.getGiveItemNoSpace();
										noSpaceMessage = noSpaceMessage.replace("%ITEM%", customItem.getDisplayItem().getItemMeta().getDisplayName());
										receiver.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(noSpaceMessage));
									}

								}
							}
							else
							{
								receiver.closeInventory();

								String message = Messages.getShopNotEnough();
								message = message.replace("%ITEM%", customItem.getDisplayItem().getItemMeta().getDisplayName());
								message = message.replace("%PRICE%", String.valueOf(customItem.getPrice()));

								receiver.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(message));
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

	@EventHandler
	public void OnEntityDeath(EntityDeathEvent e)
	{
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Passive Mobs Start
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		if (e.getEntityType() == EntityType.PIG)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getPig())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("pig", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}

		if (e.getEntityType() == EntityType.SHEEP)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getSheep())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("sheep", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}

		if (e.getEntityType() == EntityType.CHICKEN)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getChicken())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("chicken", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.BAT)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getBat())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("bat", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.SQUID)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getSquid())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("squid", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.RABBIT)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getRabbit())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("rabbit", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.MUSHROOM_COW)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getMushroomCow())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("mushroom cow", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.SNOWMAN)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getSnowman())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("snowman", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.OCELOT)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getOcelot())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("ocelot", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.HORSE)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getHorse())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("horse", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Passive Mobs End
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Hostile Mobs Start
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		if (e.getEntityType() == EntityType.ZOMBIE)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getZombie())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("zombie", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.SKELETON)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getSkeleton())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("skeleton", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.SPIDER)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getSpider())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("spider", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.CREEPER)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getCreeper())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("creeper", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.ENDERMAN)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getEnderman())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("enderman", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.BLAZE)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getBlaze())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("blaze", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.WITCH)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getWitch())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("witch", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.CAVE_SPIDER)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getCaveSpider())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("cave spider", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.SILVERFISH)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getSilverfish())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("silverfish", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.MAGMA_CUBE)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getMagmaCube())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("magma cube", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.ENDERMITE)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getEndermite())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("endermite", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.GUARDIAN)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getGuardian())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("guardian", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.GHAST)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getGhast())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("ghast", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.SLIME)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getSlime())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("slime", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.GIANT)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getGiant())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("giant", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.WITHER)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getWither())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
						return;
					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("wither", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.ENDER_DRAGON)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getEnderDragon())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("ender dragon", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Hostile Mobs End
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Neutral Mobs Start
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		if (e.getEntityType() == EntityType.VILLAGER)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getVillager())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("villager", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.IRON_GOLEM)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getIronGolem())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("iron golem", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.WOLF)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getWolf())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("wolf", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		if (e.getEntityType() == EntityType.PIG_ZOMBIE)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getZombiePigman())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("pig zombie", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Neutral Mobs End
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Player Start
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		if (e.getEntityType() == EntityType.PLAYER)
		{
			Random object = new Random();
			for (int counter = 1; counter <= 1; counter++)
			{
				int i = 1 + object.nextInt(100);
				if (i <= Utils.getPlayer())
				{
					Player p = e.getEntity().getKiller();
					if (p == null)
					{
						return;
					}

					String player = p.getUniqueId().toString();

					p.sendMessage(Utils.getCurrencyIncreaseMessage("player", 1));
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Player End
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	}
}