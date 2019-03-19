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
		if (cmd.getName().equalsIgnoreCase("BAMobCoins"))
		{
			if ((sender instanceof Player))
			{
				if (args.length == 0)
				{
					if (sender.hasPermission("BAMobCoins.shop"))
					{
						Player p = (Player) sender;
						p.openInventory(Utils.showInventory(p));
						return true;
					}

					Utils.insufficientPermissions(sender, "/BAMobCoins");
				}

				if (args.length == 1)
				{

					if ((args[0].equalsIgnoreCase("balance")) || (args[0].equalsIgnoreCase("bal")))
					{
						if (sender.hasPermission("BAMobCoins.balance"))
						{
							Player p = (Player) sender;
							p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You have " + CoinsAPI.getCoins(p.getUniqueId().toString()) + " MobCoins!");

							return true;
						}

						Utils.insufficientPermissions(sender, "/BAMobCoins balance");
						return true;
					}

					if (args[0].equalsIgnoreCase("reload"))
					{
						if (sender.hasPermission("BAMobCoins.reload"))
						{
							this.plugin.reloadConfig();
							sender.sendMessage(Utils.getprefix() + " " + ChatColor.GREEN + "reloaded!");

							return true;
						}

						Utils.insufficientPermissions(sender, "/BAMobCoins reload");
						return true;
					}

					if (args[0].equalsIgnoreCase("help"))
					{
						if (sender.hasPermission("BAMobCoins.help"))
						{
							sender.sendMessage(ChatColor.GRAY + "-------------------[ " + ChatColor.GOLD + "BAMobCoins V1.0 Help " + ChatColor.GRAY + "]-------------------");
							sender.sendMessage(ChatColor.GOLD + "/BAMobCoins " + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + "Opens the shop GUI.");
							sender.sendMessage(ChatColor.GOLD + "/BAMobCoins balance " + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + "See your balance.");
							sender.sendMessage(ChatColor.GOLD + "/BAMobCoins balance <players ign> " + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + "See other players balance.");
							sender.sendMessage(ChatColor.GOLD + "/BAMobCoins pay <players ign> <amount>" + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + "Pay another player from your balance.");
							sender.sendMessage(ChatColor.GOLD + "/BAMobCoins add <players ign> <amount>" + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + "Adds MobCoins to a players balance.");
							sender.sendMessage(ChatColor.GOLD + "/BAMobCoins set <players ign> <amount>" + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + "Set a players balance.");
							sender.sendMessage(ChatColor.GOLD + "/BAMobCoins remove <players ign> <amount>" + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + "Removes MobCoins from a players balance.");
							sender.sendMessage(ChatColor.GOLD + "/BAMobCoins reload " + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + "Reloads the plugins config files.");
							sender.sendMessage(ChatColor.GOLD + "/BAMobCoins help " + ChatColor.DARK_GRAY + ": " + ChatColor.GRAY + "Sends the help message listing commands.");
							sender.sendMessage(ChatColor.GRAY + "-----------------------------------------------------");

							return true;
						}

						Utils.insufficientPermissions(sender, "/BAMobCoins help");
						return true;
					}

				}

				if (args.length == 2)
				{
					if(args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("bal"))
					{
						if (sender.hasPermission("BaMobCoins.balance.others"))
						{
							Player pl = Bukkit.getServer().getPlayer(args[1]);
							Player p = (Player) sender;
							
							int balance = CoinsAPI.getCoins(pl.getUniqueId().toString());
							if(balance > 1)
							{
								p.sendMessage(Utils.getprefix() + ChatColor.GRAY + pl.getDisplayName() + " has " + balance + " " + Utils.getCurrencyNamePlural() + "!");
							}
							else
							{
								p.sendMessage(Utils.getprefix() + ChatColor.GRAY + pl.getDisplayName() + " has " + balance + " " + Utils.getCurrencyNameSingle() + "!");
							}
	
							return true;
						}
	
						Utils.insufficientPermissions(sender, "/BAMobCoins balance <players ign>");
						return true;
					}
				}

				if (args.length == 3)
				{
					if (args[0].equals("add"))
					{
						if (sender.hasPermission("BAMobCoins.add"))
						{
							String player = args[1];
							Player p = Bukkit.getServer().getPlayer(player);
							String pl = p.getUniqueId().toString();
							if (!CoinsAPI.playerExists(pl))
							{
								sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " That player has never joined the server!");
								return true;
							}

							int amount = 0;
							try
							{
								amount = Integer.parseInt(args[2]);
							}
							catch (Exception e)
							{
								sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " Please enter a whole number!");
								return true;
							}

							CoinsAPI.addCoins(pl, amount);
							if (amount > 1)
							{
								sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You added " + amount + " " + Utils.getCurrencyNamePlural() + " to " + p.getDisplayName());
								return true;
							}
							else
							{
								sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You added " + amount + " " + Utils.getCurrencyNameSingle() + " to " + p.getDisplayName());
								return true;
							}
						}

						Utils.insufficientPermissions(sender, "/BaMobCoins add <players ign> <amount>");
						return true;
					}

					if (args[0].equalsIgnoreCase("remove"))
					{
						if (sender.hasPermission("BAMobCoins.remove"))
						{
							String player = args[1];
							Player p = Bukkit.getServer().getPlayer(player);
							String UUID = p.getUniqueId().toString();

							if (!CoinsAPI.playerExists(UUID))
							{
								sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " That player has never joined the server!");
								return true;
							}

							int amount = 0;
							try
							{
								amount = Integer.parseInt(args[2]);
							}
							catch (Exception e)
							{
								sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " Please enter a whole number!");
								return true;
							}

							CoinsAPI.removeCoins(UUID, amount);

							if (amount > 1)
							{
								sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You removed " + amount + Utils.getCurrencyNamePlural() + " from " + p.getDisplayName() + "!");
								return true;
							}
							else
							{
								sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You removed " + amount + Utils.getCurrencyNameSingle() + " from " + p.getDisplayName() + "!");
								return true;
							}
						}

						Utils.insufficientPermissions(sender, "/BaMobCoins remove <players ign> <amount>");
						return true;
					}

					if (args[0].equalsIgnoreCase("set"))
					{
						if (sender.hasPermission("BAMobCoins.set"))
						{
							String player = args[1];
							Player p = Bukkit.getServer().getPlayer(player);
							String pl = p.getUniqueId().toString();

							if (!CoinsAPI.playerExists(pl))
							{
								sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " That player has never joined the server!");
								return true;
							}

							int amount = 0;
							try
							{
								amount = Integer.parseInt(args[2]);
							}
							catch (Exception e)
							{
								sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " Please enter a whole number!");
								return true;
							}

							CoinsAPI.setCoins(pl, amount);

							sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You set " + p.getDisplayName() + "'s " + Utils.getCurrencyNameSingle() + "balance to " + amount + "!");
							return true;
						}

						Utils.insufficientPermissions(sender, "/BaMobCoins set <players ign> <amount>");
						return true;
					}

					if (args[0].equalsIgnoreCase("pay"))
					{
						if (!(sender instanceof Player))
						{
							sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You must be a player to do that command!");
							return true;
						}

						if (sender.hasPermission("BAMobCoins.pay"))
						{
							String player = args[1];
							Player p = Bukkit.getServer().getPlayer(player);
							if (p == null)
							{
								sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " That player has never joined the server!");
								return true;
							}
							String pl = p.getUniqueId().toString();
							String cs = sender.getName();
							Player pcs = Bukkit.getServer().getPlayer(cs);
							String plcs = pcs.getUniqueId().toString();
							if (sender.getName().toLowerCase().equals(player.toLowerCase()))
							{
								sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You can't pay yourself!");
								return true;
							}
							if (!CoinsAPI.playerExists(pl))
							{
								sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " That player has never joined the server!");
								return true;
							}
							int amount = 0;
							try
							{
								amount = Integer.parseInt(args[2]);
							}
							catch (Exception e)
							{
								sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " Please enter a whole number!");
								return true;
							}

							if (CoinsAPI.getCoins(plcs).intValue() >= amount)
							{
								CoinsAPI.addCoins(pl, amount);
								CoinsAPI.removeCoins(plcs, amount);
								if (amount > 1)
								{
									sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You payed " + player + " " + amount + " " + Utils.getCurrencyNamePlural() + "!");
									p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " you were payed " + amount + " " + Utils.getCurrencyNamePlural() + " by " + cs + "!");
								}
								else
								{
									sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You payed " + player + " " + amount + " " + Utils.getCurrencyNameSingle() + "!");
									p.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You were payed " + amount + " " + Utils.getCurrencyNameSingle() + " by " + cs + "!");
								}
							}
							else
							{
								sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You do not have enough mobcoins!");
							}
							return true;
						}

						Utils.insufficientPermissions(sender, "/BaMobCoins pay <players ign> <amount>");
						return true;
					}
					
					
					
				}
				
				sender.sendMessage(Utils.getprefix() + ChatColor.RED + " Unknown command! Type /bamobcoins help for a list of commands.");
				return true;
				/* End of if testing if a player ran the command */
			}
			else /* Commands run from console Eg. Not a player */
			{
				if (args[0].equals("add"))
				{
					String player = args[1];
					Player p = Bukkit.getServer().getPlayer(player);
					String pl = p.getUniqueId().toString();
					if (!CoinsAPI.playerExists(pl))
					{
						sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " That player has never joined the server!");
						return true;
					}

					int amount = 0;
					try
					{
						amount = Integer.parseInt(args[2]);
					}
					catch (Exception e)
					{
						sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " Please enter a whole number!");
						return true;
					}

					CoinsAPI.addCoins(pl, amount);
					if (amount > 1)
					{
						sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You added " + amount + " " + Utils.getCurrencyNamePlural() + " to " + p.getDisplayName());
						return true;
					}
					else
					{
						sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You added " + amount + " " + Utils.getCurrencyNameSingle() + " to " + p.getDisplayName());
						return true;
					}
				}

				if (args[0].equalsIgnoreCase("remove"))
				{
					String player = args[1];
					Player p = Bukkit.getServer().getPlayer(player);
					String UUID = p.getUniqueId().toString();

					if (!CoinsAPI.playerExists(UUID))
					{
						sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " That player has never joined the server!");
						return true;
					}

					int amount = 0;
					try
					{
						amount = Integer.parseInt(args[2]);
					}
					catch (Exception e)
					{
						sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " Please enter a whole number!");
						return true;
					}

					CoinsAPI.removeCoins(UUID, amount);

					if (amount > 1)
					{
						sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You removed " + amount + Utils.getCurrencyNamePlural() + " from " + p.getDisplayName() + "!");
						return true;
					}
					else
					{
						sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You removed " + amount + Utils.getCurrencyNameSingle() + " from " + p.getDisplayName() + "!");
						return true;
					}
				}

				if (args[0].equalsIgnoreCase("set"))
				{
					String player = args[1];
					Player p = Bukkit.getServer().getPlayer(player);
					String pl = p.getUniqueId().toString();

					if (!CoinsAPI.playerExists(pl))
					{
						sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " That player has never joined the server!");
						return true;
					}

					int amount = 0;
					try
					{
						amount = Integer.parseInt(args[2]);
					}
					catch (Exception e)
					{
						sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " Please enter a whole number!");
						return true;
					}

					CoinsAPI.setCoins(pl, amount);

					sender.sendMessage(Utils.getprefix() + ChatColor.GRAY + " You set " + p.getDisplayName() + "'s " + Utils.getCurrencyNameSingle() + "balance to " + amount + "!");
					return true;
				}
			}
		}

		return false;
	}
}
