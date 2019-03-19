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

public class Events implements org.bukkit.event.Listener
{
	private static Main plugin;
	File CE = new File("plugins//CustomEnchants//config.yml");
	YamlConfiguration CEConfig = YamlConfiguration.loadConfiguration(CE);

	public Events(Main pl)
	{
		plugin = pl;
	}

	public static String convertPower(int i)
	{
		if (i <= 0)
		{
			return "I";
		}
		if (i == 1)
		{
			return "I";
		}
		if (i == 2)
		{
			return "II";
		}
		if (i == 3)
		{
			return "III";
		}
		if (i == 4)
		{
			return "IV";
		}
		if (i == 5)
		{
			return "V";
		}
		if (i == 6)
		{
			return "VI";
		}
		if (i == 7)
		{
			return "VII";
		}
		if (i == 8)
		{
			return "VIII";
		}
		if (i == 9)
		{
			return "IX";
		}
		if (i == 10)
		{
			return "X";
		}
		return i + "";
	}

	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		String player = p.getUniqueId().toString();
		CoinsAPI.createPlayer(player);
	}

	@EventHandler
	public void OnInvClick(InventoryClickEvent e)
	{
		if (e.getInventory().getTitle().equals(Utils.getTitle()))
		{
			Player p = (Player) e.getWhoClicked();
			for (int i = 0; i < 28; i++)
			{
				if (e.getCurrentItem().equals(Utils.getItem(i, p)))
				{
					String player = p.getUniqueId().toString();
					if (CoinsAPI.getCoins(player).intValue() >= Utils.getPrice(i, p))
					{

						p.closeInventory();
						CoinsAPI.removeCoins(player, Utils.getPrice(i, p));
						p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Brought an Item From The Shop!");
						java.util.List<String> command = plugin.getConfig().getStringList("Shop." + i + ".Commands");
						for (String cmd : command)
						{
							org.bukkit.Bukkit.getServer().dispatchCommand(
									org.bukkit.Bukkit.getServer().getConsoleSender(),
									cmd.replace("%player%", e.getWhoClicked().getName()));
						}
					} else
					{
						p.closeInventory();
						p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You don't have enough Mobcoins!");
					}
				} else
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Pig and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										
										return;
									}
								}
							}
						}
					}
					
					
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You killed a pig and gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Sheep and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Sheep and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Chicken and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Chicken and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Bat and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Bat and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Squid and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Squid and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Rabbit and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Rabbit and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Mushroom Cow and Gained " + (counter1 + 1)
												+ " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(
							Utils.getprefix() + ChatColor.GRAY + " You Killed A Mushroom Cow and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Snowman and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Snowman and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Ocelot and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Ocelot and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Horse and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Horse and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Zombie and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Zombie and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Skeleton and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Skeleon and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Spider and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Spider and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Creeper and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Creeper and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Enderman and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Enderman and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Blaze and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Blaze and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Witch and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Witch and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Cave Spider and Gained " + (counter1 + 1)
												+ " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(
							Utils.getprefix() + ChatColor.GRAY + " You Killed A Cave Spider and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Silverfish and Gained " + (counter1 + 1)
												+ " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(
							Utils.getprefix() + ChatColor.GRAY + " You Killed A Silverfish and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Magma Cube and Gained " + (counter1 + 1)
												+ " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(
							Utils.getprefix() + ChatColor.GRAY + " You Killed A Magma Cube and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Endermite and Gained " + (counter1 + 1)
												+ " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Endermite and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Guardian and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Guardian and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Ghast and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Ghast and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Slime and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Slime and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Giant and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Giant and Gained 1 Mobcoin!");
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
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Wither and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Wither and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Ender Dragon and Gained " + (counter1 + 1)
												+ " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(
							Utils.getprefix() + ChatColor.GRAY + " You Killed A Ender Dragon and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Villager and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Villager and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Iron Golem and Gained " + (counter1 + 1)
												+ " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(
							Utils.getprefix() + ChatColor.GRAY + " You Killed A Iron Golem and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Wolf and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Wolf and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Wolf and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Wolf and Gained 1 Mobcoin!");
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
						return;
					String player = p.getUniqueId().toString();
					if (p.getItemInHand() != null)
					{
						if (p.getItemInHand().hasItemMeta())
						{
							if (p.getItemInHand().getItemMeta().hasLore())
							{
								for (int counter1 = 1; counter1 <= CEConfig.getInt("MaxPower.Coins"); counter1++)
								{
									if (Utils.hasenchant("Coins " + convertPower(counter1), p.getItemInHand()))
									{
										p.sendMessage(Utils.getprefix() + ChatColor.GRAY
												+ " You Killed A Player and Gained " + (counter1 + 1) + " Mobcoins!");
										CoinsAPI.addCoins(player, counter1 + 1);
										return;
									}
								}
							}
						}
					}
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Killed A Player and Gained 1 Mobcoin!");
					CoinsAPI.addCoins(player, 1);
				}
			}
		}
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// Player End
		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	}
}