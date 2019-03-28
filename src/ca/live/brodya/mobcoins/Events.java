package ca.live.brodya.mobcoins;

import java.io.File;
import java.util.Random;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import ca.live.brodya.mobcoins.templates.CustomItem;

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

	@EventHandler
	public void OnInvClick(InventoryClickEvent e)
	{
		if (e.getInventory().getTitle().equals(Utils.getTitle()))
		{
			Player receiver = (Player) e.getWhoClicked();
			for (CustomItem customItem : Utils.getShopItems())
			{
				if (e.getCurrentItem().equals(Utils.getItem(customItem.itemId, receiver)))
				{
					String receiverUuid = receiver.getUniqueId().toString();
					if (CoinsAPI.getCoins(receiverUuid).intValue() >= customItem.price)
					{
						receiver.closeInventory();
						CoinsAPI.removeCoins(receiverUuid, customItem.price);
						
						String message = Messages.getShopBoughtItem();
						message = message.replace("%ITEM%", customItem.displayName);
						message = message.replace("%PRICE%", String.valueOf(customItem.price));
						
						receiver.sendMessage(Utils.getPrefix() + " " + Utils.convertColorCodes(message));
						
						Utils.runShopCommands(receiver, customItem.commands);
					}
					else
					{
						receiver.closeInventory();
						
						String message = Messages.getShopNotEnough();
						message = message.replace("%ITEM%", customItem.displayName);
						message = message.replace("%PRICE%", String.valueOf(customItem.price));
						
						receiver.sendMessage(Utils.getPrefix() + " " + Utils.convertColorCodes(message));
					}
				}
				else
				{
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