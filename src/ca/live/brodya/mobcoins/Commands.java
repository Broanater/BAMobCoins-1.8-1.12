package ca.live.brodya.mobcoins;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor
{
	Main plugin;

	public Commands(Main plugin)
	{
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("mobcoins"))
		{
			if ((sender instanceof Player))
			{
				if (args.length == 0)
				{
					Player p = (Player) sender;
					p.openInventory(Utils.showInventory(p));
					return true;
				}

				if (args.length == 1)
				{

					if ((args[0].equalsIgnoreCase("balance")) || (args[0].equalsIgnoreCase("bal")))
					{
						Player p = (Player) sender;
						p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You have "
								+ CoinsAPI.getCoins(p.getUniqueId().toString()) + " MobCoins!");
						return false;
					}

					if (args[0].equalsIgnoreCase("reload"))
					{
						if (sender.hasPermission("mobcoins.reload") || sender.isOp())
						{
							this.plugin.reloadConfig();
							sender.sendMessage(Utils.getprefix() + " " + ChatColor.GREEN + "reloaded!");

							return false;
						}

						sender.sendMessage(Utils.getprefix() + ChatColor.RED + " Insufficient Permissions!");
					}
				}

				if ((args.length == 2) && ((args[0].equalsIgnoreCase("balance")) || (args[0].equalsIgnoreCase("bal"))))
				{
					Player pl = Bukkit.getServer().getPlayer(args[1]);
					Player p = (Player) sender;
					p.sendMessage(Utils.getprefix() + ChatColor.GRAY + pl.getName() + " has "
							+ CoinsAPI.getCoins(pl.getUniqueId().toString()) + " MobCoins!");

					return false;
				}

				if (args.length == 3)
				{
					if ((args[0].equalsIgnoreCase("give")) || (args[0].equals("add")))
					{
						if (sender.hasPermission("mobcoins.give"))
						{
							String player = args[1];
							Player p = Bukkit.getServer().getPlayer(player);
							String pl = p.getUniqueId().toString();
							if (!CoinsAPI.playerExists(pl))
							{
								sender.sendMessage(Utils.getprefix() + ChatColor.GRAY
										+ " That Player Has Never Joined The Server!");
								return true;
							}

							int amount = 0;
							try
							{
								amount = Integer.parseInt(args[2]);
							} catch (Exception e)
							{
								sender.sendMessage(
										Utils.getprefix() + ChatColor.GRAY + " Please enter a whole Number!");
								return true;
							}

							CoinsAPI.addCoins(pl, amount);
							sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Gave " + p.getDisplayName()
									+ " " + amount + " MobCoins!");
							return true;
						}

						sender.sendMessage(Utils.getprefix() + ChatColor.RED + " Insufficient Permissions!");
						return true;
					}

					if (args[0].equalsIgnoreCase("remove"))
					{
						if (sender.hasPermission("mobcoins.remove"))
						{
							String player = args[1];
							Player p = Bukkit.getServer().getPlayer(player);
							String UUID = p.getUniqueId().toString();

							if (!CoinsAPI.playerExists(UUID))
							{
								sender.sendMessage(Utils.getprefix() + ChatColor.GRAY
										+ " That player has never joined the server!");
								return true;
							}

							int amount = 0;
							try
							{
								amount = Integer.parseInt(args[2]);
							} catch (Exception e)
							{
								sender.sendMessage(
										Utils.getprefix() + ChatColor.GRAY + " Please enter a whole number!");
								return true;
							}

							CoinsAPI.removeCoins(UUID, amount);
							sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You took " + amount
									+ " MobCoins from " + player + "!");
							return true;
						}

						sender.sendMessage(Utils.getprefix() + ChatColor.RED + " Insufficient permissions!");
						return true;
					}

					if (args[0].equalsIgnoreCase("set"))
					{
						if (sender.hasPermission("mobcoins.set"))
						{
							String player = args[1];
							Player p = Bukkit.getServer().getPlayer(player);
							String pl = p.getUniqueId().toString();
							if (!CoinsAPI.playerExists(pl))
							{
								sender.sendMessage(Utils.getprefix() + ChatColor.GRAY
										+ " That Player Has Never Joined The Server!");
								return true;
							}
							int amount = 0;
							try
							{
								amount = Integer.parseInt(args[2]);
							} catch (Exception e)
							{
								sender.sendMessage(
										Utils.getprefix() + ChatColor.GRAY + " Please Enter A Whole Number!");
								return true;
							}
							CoinsAPI.setCoins(pl, amount);
							sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You set " + player
									+ "'s MobCoins to " + amount + "!");
							return true;
						}
						sender.sendMessage(Utils.getprefix() + ChatColor.RED + " Insufficient Permissions!");
						return true;
					}

					if (args[0].equalsIgnoreCase("pay"))
					{
						if (!(sender instanceof Player))
						{
							sender.sendMessage(
									Utils.getprefix() + ChatColor.GRAY + " You Must be a player to do that command!");
						}
						String player = args[1];
						Player p = Bukkit.getServer().getPlayer(player);
						if (p == null)
						{
							sender.sendMessage(
									Utils.getprefix() + ChatColor.GRAY + " That Player Has Never Joined The Server!");
							return true;
						}
						String pl = p.getUniqueId().toString();
						String cs = sender.getName();
						Player pcs = Bukkit.getServer().getPlayer(cs);
						String plcs = pcs.getUniqueId().toString();
						if (sender.getName().toLowerCase().equals(player.toLowerCase()))
						{
							sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Can't Pay Yourself!");
							return true;
						}
						if (!CoinsAPI.playerExists(pl))
						{
							sender.sendMessage(
									Utils.getprefix() + ChatColor.GRAY + " That Player Has Never Joined The Server!");
							return true;
						}
						int amount = 0;
						try
						{
							amount = Integer.parseInt(args[2]);
						} catch (Exception e)
						{
							sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " Please Enter A Whole Number!");
							return true;
						}
						if (CoinsAPI.getCoins(plcs).intValue() >= amount)
						{
							CoinsAPI.addCoins(pl, amount);
							CoinsAPI.removeCoins(plcs, amount);
							sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You payed " + player + " "
									+ amount + " MobCoin(s)!");
							p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " you were payed " + amount
									+ " MobCoin(s) by " + cs + "!");
						} else
						{
							sender.sendMessage(
									Utils.getprefix() + ChatColor.GRAY + " You do not have enough mobcoins!");
						}
						return true;
					}
				} else
				{
					sender.sendMessage(ChatColor.GRAY + "-------------------[ " + ChatColor.GOLD + "MobCoins Help "
							+ ChatColor.GRAY + "]-------------------");
					sender.sendMessage(ChatColor.GOLD + "/MobCoins " + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY
							+ "Opens The GUI");

					sender.sendMessage(ChatColor.GOLD + "/MobCoins balance " + ChatColor.DARK_GRAY + ": "
							+ ChatColor.GRAY + "Tells You You Balance");
					sender.sendMessage(ChatColor.GOLD + "/MobCoins balance <Name> " + ChatColor.DARK_GRAY + ": "
							+ ChatColor.GRAY + "Tells You You Balance of another player");
					sender.sendMessage(ChatColor.GOLD + "/MobCoins reload " + ChatColor.DARK_GRAY + ": "
							+ ChatColor.GRAY + "Reloads The Plugin");
					sender.sendMessage(ChatColor.GOLD + "/MobCoins pay <name> <amount>" + ChatColor.DARK_GRAY + ": "
							+ ChatColor.GRAY + "Pay a player");
					sender.sendMessage(ChatColor.GOLD + "/MobCoins add <name> <amount>" + ChatColor.DARK_GRAY + ": "
							+ ChatColor.GRAY + "Give a player MobCoins");
					sender.sendMessage(ChatColor.GOLD + "/MobCoins set <name> <amount>" + ChatColor.DARK_GRAY + ": "
							+ ChatColor.GRAY + "Set a players mobcoins");
					sender.sendMessage(ChatColor.GOLD + "/MobCoins remove <name> <amount>" + ChatColor.DARK_GRAY + ": "
							+ ChatColor.GRAY + "Remove a players MobCoins");
					sender.sendMessage(ChatColor.GOLD + "/MobCoins help " + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY
							+ "View a helpful list of commands!");
					sender.sendMessage(ChatColor.GRAY + "-----------------------------------------------------");
				}
			} else
			{
				if ((args[0].equalsIgnoreCase("give")) || (args[0].equals("add")))
				{
					if (sender.hasPermission("mobcoins.give"))
					{
						String player = args[1];
						Player p = Bukkit.getServer().getPlayer(player);
						String pl = p.getUniqueId().toString();
						if (!CoinsAPI.playerExists(pl))
						{
							sender.sendMessage(
									Utils.getprefix() + ChatColor.GRAY + " That Player Has Never Joined The Server!");
							return true;
						}
						int amount = 0;
						try
						{
							amount = Integer.parseInt(args[2]);
						} catch (Exception e)
						{
							sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " Please Enter A Whole Number!");
							return true;
						}
						CoinsAPI.addCoins(pl, amount);
						sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Gave " + player + " " + amount
								+ " MobCoins!");
						return true;
					}
					sender.sendMessage(Utils.getprefix() + ChatColor.RED + " Insufficient Permissions!");
					return true;
				}

				if (args[0].equalsIgnoreCase("remove"))
				{
					if (sender.hasPermission("mobcoins.remove"))
					{
						String player = args[1];
						Player p = Bukkit.getServer().getPlayer(player);
						String pl = p.getUniqueId().toString();
						if (!CoinsAPI.playerExists(pl))
						{
							sender.sendMessage(
									Utils.getprefix() + ChatColor.GRAY + " That Player Has Never Joined The Server!");
							return true;
						}
						int amount = 0;
						try
						{
							amount = Integer.parseInt(args[2]);
						} catch (Exception e)
						{
							sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " Please Enter A Whole Number!");
							return true;
						}
						CoinsAPI.removeCoins(pl, amount);
						sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You Took " + amount
								+ " MobCoins from " + player + "!");
						return true;
					}
					sender.sendMessage(Utils.getprefix() + ChatColor.RED + " Insufficient Permissions!");
					return true;
				}

				if (args[0].equalsIgnoreCase("set"))
				{
					if (sender.hasPermission("mobcoins.set"))
					{
						String player = args[1];
						Player p = Bukkit.getServer().getPlayer(player);
						String pl = p.getUniqueId().toString();
						if (!CoinsAPI.playerExists(pl))
						{
							sender.sendMessage(
									Utils.getprefix() + ChatColor.GRAY + " That Player Has Never Joined The Server!");
							return true;
						}
						int amount = 0;
						try
						{
							amount = Integer.parseInt(args[2]);
						} catch (Exception e)
						{
							sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " Please Enter A Whole Number!");
							return true;
						}
						CoinsAPI.setCoins(pl, amount);
						sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You set " + player + "'s MobCoins to "
								+ amount + "!");
						return true;
					}
					sender.sendMessage(Utils.getprefix() + ChatColor.RED + " Insufficient Permissions!");
					return true;
				}

				sender.sendMessage(Utils.getprefix() + ChatColor.RED + " You Must Be A Player!");
			}
		}

		return false;
	}
}
