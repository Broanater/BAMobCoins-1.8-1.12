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
						/* Open the shop GUI */
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
							/* Get the balance of the one sending the command. */
							Player p = (Player) sender;
							int balance = CoinsAPI.getCoins(p.getUniqueId().toString());
							
							/* Send the message to the one requesting the balance based on the value. */
							if(balance == 1)
							{
								p.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You have " + balance + " " + Utils.getCurrencyNameSingle() + ChatColor.GRAY + "!");
							}
							else
							{
								p.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You have " + balance + " " + Utils.getCurrencyNamePlural() + ChatColor.GRAY + "!");
							}
							return true;
						}

						Utils.insufficientPermissions(sender, "/BAMobCoins balance");
						return true;
					}

					if (args[0].equalsIgnoreCase("reload"))
					{
						if (sender.hasPermission("BAMobCoins.reload"))
						{
							/* Reload the plugins config file. */
							this.plugin.reloadConfig();
							sender.sendMessage(Utils.getPrefix() + " " + ChatColor.GREEN + "reloaded!");

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
							/* Get the Player of the on to check the balance of. */
							Player toCheck = Bukkit.getServer().getPlayer(args[1]);
							
							/* Get the balance of the player. */
							int balance = CoinsAPI.getCoins(toCheck.getUniqueId().toString());
							
							/* Send the message to the one requesting the balance based on the value. */
							if(balance > 1)
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + toCheck.getDisplayName() + " has " + balance + " " + Utils.getCurrencyNamePlural() + ChatColor.GRAY + "!");
							}
							else
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + toCheck.getDisplayName() + " has " + balance + " " + Utils.getCurrencyNameSingle() + ChatColor.GRAY + "!");
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
							/* Make sure that player has joined the server. */
							String player = args[1];
							Player p = Bukkit.getServer().getPlayer(player);
							String pl = p.getUniqueId().toString();
							if (!CoinsAPI.playerExists(pl))
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " That player has never joined the server!");
								return true;
							}

							
							/* Ensure they're adding a whole number. */
							int amount = 0;
							try
							{
								amount = Integer.parseInt(args[2]);
							}
							catch (Exception e)
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " Please enter a whole number!");
								return true;
							}

							
							/* Check if they're trying to add 0 */
							if(amount == 0)
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You cannot add 0 " + Utils.getCurrencyNamePlural() + ChatColor.GRAY + " to a users balance!");
								return true;
							}
							
							/* Add the coins and send correct message. */
							CoinsAPI.addCoins(pl, amount);
							if (amount > 1)
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You added " + amount + " " + Utils.getCurrencyNamePlural() + ChatColor.GRAY + " to " + p.getDisplayName());
								return true;
							}
							else
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You added " + amount + " " + Utils.getCurrencyNameSingle() + ChatColor.GRAY + " to " + p.getDisplayName());
								return true;
							}
						}

						Utils.insufficientPermissions(sender, "/BAMobCoins add <players ign> <amount>");
						return true;
					}

					if (args[0].equalsIgnoreCase("remove"))
					{
						if (sender.hasPermission("BAMobCoins.remove"))
						{
							/* Ensure the player has joined the server */
							String player = args[1];
							Player p = Bukkit.getServer().getPlayer(player);
							String UUID = p.getUniqueId().toString();

							if (!CoinsAPI.playerExists(UUID))
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " That player has never joined the server!");
								return true;
							}

							/* Ensure they're removing a whole number. */
							int amount = 0;
							try
							{
								amount = Integer.parseInt(args[2]);
							}
							catch (Exception e)
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " Please enter a whole number!");
								return true;
							}
							
							
							/* Ensure they're not trying to remove 0. */
							if(amount == 0)
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You cannot remove 0 " + Utils.getCurrencyNamePlural() + ChatColor.GRAY + " from a users balance!");
								return true;
							}
							
							
							/* Remove the coins. */
							CoinsAPI.removeCoins(UUID, amount);

							if (amount > 1)
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You removed " + amount + Utils.getCurrencyNamePlural() + " from " + p.getDisplayName() + "!");
								return true;
							}
							else
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You removed " + amount + Utils.getCurrencyNameSingle() + " from " + p.getDisplayName() + "!");
								return true;
							}
						}

						Utils.insufficientPermissions(sender, "/BAMobCoins remove <players ign> <amount>");
						return true;
					}

					if (args[0].equalsIgnoreCase("set"))
					{
						if (sender.hasPermission("BAMobCoins.set"))
						{
							/* Ensure the player has joined the server. */
							String player = args[1];
							Player p = Bukkit.getServer().getPlayer(player);
							String pl = p.getUniqueId().toString();
							if (!CoinsAPI.playerExists(pl))
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " That player has never joined the server!");
								return true;
							}

							/* Ensure they're trying to set the balance to a whole number. */
							int amount = 0;
							try
							{
								amount = Integer.parseInt(args[2]);
							}
							catch (Exception e)
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " Please enter a whole number!");
								return true;
							}

							/* Set the balance */
							CoinsAPI.setCoins(pl, amount);

							sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You set " + p.getDisplayName() + "'s " + Utils.getCurrencyNameSingle() + "balance to " + amount + "!");
							return true;
						}

						Utils.insufficientPermissions(sender, "/BaMobCoins set <players ign> <amount>");
						return true;
					}

					if (args[0].equalsIgnoreCase("pay"))
					{
						/* Double check that the one running this command is a player. */
						if (!(sender instanceof Player))
						{
							sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You must be a player to do that command!");
							return true;
						}

						if (sender.hasPermission("BAMobCoins.pay"))
						{
							/* Ensure the one receiving the  coins has joined the server. */
							String player = args[1];
							Player p = Bukkit.getServer().getPlayer(player);
							if (p == null)
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " That player has never joined the server!");
								return true;
							}
							
							String receivingUuid = p.getUniqueId().toString(); /* Receiving Coins UUID */
							String senderIgn = sender.getName(); /* Sending Coins IGN */
							Player sendingPlayer = Bukkit.getServer().getPlayer(senderIgn); /* Sending Coins Player */
							String sendingUuid = sendingPlayer.getUniqueId().toString(); /* Sending Coins UUID */
							
							/* Check if the player is attempting to pay themselves. */
							if (sender.getName().toLowerCase().equals(player.toLowerCase()))
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You can't pay yourself!");
								return true;
							}
							
							/* Ensure the player exists in the balances file. */
							if (!CoinsAPI.playerExists(receivingUuid))
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " That player has never joined the server!");
								return true;
							}
							
							/* Ensure the amount trying to be sent is a whole number. */
							int toSend = 0;
							try
							{
								toSend = Integer.parseInt(args[2]);
							}
							catch (Exception e)
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " Please enter a whole number!");
								return true;
							}

							/* Ensure they aren't trying to send 0 coins. */
							if(toSend == 0)
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You cannot send 0 " + Utils.getCurrencyNamePlural() + ChatColor.GRAY + " to "  + p.getDisplayName());
								return true;
							}
							
							/* Ensure the one trying to send has enough to send to the other user. */
							if (CoinsAPI.getCoins(sendingUuid).intValue() >= toSend)
							{
								/* Add the coins to the receiving player, remove the coins from the sending player. */
								CoinsAPI.addCoins(receivingUuid, toSend);
								CoinsAPI.removeCoins(sendingUuid, toSend);
								
								/* Send the correct message depending on the amount. */
								if (toSend > 1)
								{
									sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You payed " + player + " " + toSend + " " + Utils.getCurrencyNamePlural() + ChatColor.GRAY + "!");
									p.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You were payed " + toSend + " " + Utils.getCurrencyNamePlural() + ChatColor.GRAY + " by " + senderIgn + "!");
								}
								else
								{
									sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You payed " + player + " " + toSend + " " + Utils.getCurrencyNameSingle() + "!");
									p.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You were payed " + toSend + " " + Utils.getCurrencyNameSingle() + " by " + senderIgn + "!");
								}
							}
							else
							{
								sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You do not have enough mobcoins!");
							}
							return true;
						}

						Utils.insufficientPermissions(sender, "/BaMobCoins pay <players ign> <amount>");
						return true;
					}
					
					
					
				}
				
				sender.sendMessage(Utils.getPrefix() + ChatColor.RED + " Unknown command! Type /bamobcoins help for a list of commands.");
				return true;
				/* End of if testing if a player ran the command */
			}
			else /* Commands run from console Eg. Not a player */
			{
				if (args[0].equals("add"))
				{
					/* Make sure that player has joined the server. */
					String player = args[1];
					Player p = Bukkit.getServer().getPlayer(player);
					String pl = p.getUniqueId().toString();
					if (!CoinsAPI.playerExists(pl))
					{
						sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " That player has never joined the server!");
						return true;
					}

					
					/* Ensure they're adding a whole number. */
					int amount = 0;
					try
					{
						amount = Integer.parseInt(args[2]);
					}
					catch (Exception e)
					{
						sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " Please enter a whole number!");
						return true;
					}

					
					/* Check if they're trying to add 0 */
					if(amount == 0)
					{
						sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You cannot add 0 " + Utils.getCurrencyNamePlural() + ChatColor.GRAY + " to a users balance!");
						return true;
					}
					
					/* Add the coins and send correct message. */
					CoinsAPI.addCoins(pl, amount);
					if (amount > 1)
					{
						sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You added " + amount + " " + Utils.getCurrencyNamePlural() + ChatColor.GRAY + " to " + p.getDisplayName());
						return true;
					}
					else
					{
						sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You added " + amount + " " + Utils.getCurrencyNameSingle() + ChatColor.GRAY + " to " + p.getDisplayName());
						return true;
					}
				}

				if (args[0].equalsIgnoreCase("remove"))
				{
					/* Ensure the player has joined the server */
					String player = args[1];
					Player p = Bukkit.getServer().getPlayer(player);
					String UUID = p.getUniqueId().toString();

					if (!CoinsAPI.playerExists(UUID))
					{
						sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " That player has never joined the server!");
						return true;
					}

					/* Ensure they're removing a whole number. */
					int amount = 0;
					try
					{
						amount = Integer.parseInt(args[2]);
					}
					catch (Exception e)
					{
						sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " Please enter a whole number!");
						return true;
					}
					
					
					/* Ensure they're not trying to remove 0. */
					if(amount == 0)
					{
						sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You cannot remove 0 " + Utils.getCurrencyNamePlural() + ChatColor.GRAY + " from a users balance!");
						return true;
					}
					
					
					/* Remove the coins. */
					CoinsAPI.removeCoins(UUID, amount);

					if (amount > 1)
					{
						sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You removed " + amount + Utils.getCurrencyNamePlural() + " from " + p.getDisplayName() + "!");
						return true;
					}
					else
					{
						sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You removed " + amount + Utils.getCurrencyNameSingle() + " from " + p.getDisplayName() + "!");
						return true;
					}
				}

				if (args[0].equalsIgnoreCase("set"))
				{
					/* Ensure the player has joined the server. */
					String player = args[1];
					Player p = Bukkit.getServer().getPlayer(player);
					String pl = p.getUniqueId().toString();
					if (!CoinsAPI.playerExists(pl))
					{
						sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " That player has never joined the server!");
						return true;
					}

					/* Ensure they're trying to set the balance to a whole number. */
					int amount = 0;
					try
					{
						amount = Integer.parseInt(args[2]);
					}
					catch (Exception e)
					{
						sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " Please enter a whole number!");
						return true;
					}

					/* Set the balance */
					CoinsAPI.setCoins(pl, amount);

					sender.sendMessage(Utils.getPrefix() + ChatColor.GRAY + " You set " + p.getDisplayName() + "'s " + Utils.getCurrencyNameSingle() + "balance to " + amount + "!");
					return true;
				}
			}
		}

		return false;
	}
}
