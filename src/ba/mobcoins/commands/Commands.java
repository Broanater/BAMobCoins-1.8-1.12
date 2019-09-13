package ba.mobcoins.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ba.mobcoins.Main;
import ba.mobcoins.apis.*;
import ba.mobcoins.controllers.*;
import ba.mobcoins.models.CustomItem;
import ba.mobcoins.utilities.*;

public class Commands implements CommandExecutor
{
	private Main plugin;

	public Commands(Main pl)
	{
		plugin = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (cmd.getName().equalsIgnoreCase("BAMobCoins"))
		{
			/*
			 * ---------------------------------------------- ---------------------------------------------- PLAYER COMMANDS
			 * ---------------------------------------------- ----------------------------------------------
			 */
			if ((sender instanceof Player))
			{
				if (args.length == 0)
				{
					/* Open shop command */
					if (sender.hasPermission("BAMobCoins.shop"))
					{
						/* Open the shop GUI */
						Player p = (Player) sender;
						p.openInventory(ShopController.getShopInventory(p.getUniqueId().toString(), "MENU"));
						return true;
					}

					Utils.insufficientPermissions(sender, "/BAMobCoins");
					return true;
				}

				/* View mob coin balance. */
				if ((args[0].equalsIgnoreCase("balance")) || (args[0].equalsIgnoreCase("bal")))
				{
					/* Get the balance of the player sending the command */
					if (args.length == 1)
					{
						if (sender.hasPermission("BAMobCoins.balance"))
						{
							Player p = (Player) sender;
							int balance = CoinsAPI.getCoins(p.getUniqueId().toString());

							String message = MessagesController.getYourBalance()
								.replace("%BALANCE%", String.valueOf(balance));

							p.sendMessage(Utils.convertColorCodes(message));

							return true;
						}

						Utils.insufficientPermissions(sender, "/BAMobCoins balance");
						return true;
					}
					/* Get the balance of the players name given. */
					else if (args.length == 2)
					{
						if (sender.hasPermission("BAMobCoins.balance.others"))
						{
							/* Get the Player of the on to check the balance of. */
							OfflinePlayer toCheck = Bukkit.getServer().getPlayer(args[1]);

							/* Get the balance of the player. */
							int balance = CoinsAPI.getCoins(toCheck.getUniqueId().toString());

							String message = MessagesController.getOtherBalance()
								.replace("%BALANCE%", String.valueOf(balance))
								.replace("%PLAYER%", sender.getName());

							sender.sendMessage(Utils.convertColorCodes(message));
							return true;
						}

						Utils.insufficientPermissions(sender, "/BAMobCoins balance <players ign>");
						return true;
					}
					else if (args.length > 2)
					{
						String message = MessagesController.getGlobalTooManyArgs()
							.replace("%COMMAND%", "/BAMobCoins balance or /BAMobCoins balance <players ign>.");

						sender.sendMessage(Utils.convertColorCodes(message));
					}
				}

				/* Display help information */
				if (args[0].equalsIgnoreCase("help"))
				{
					if (args.length == 1)
					{
						if (sender.hasPermission("BAMobCoins.help"))
						{
							ArrayList<String> helpMessages = MessagesController.getHelp();

							for (String message : helpMessages)
							{
								message = message.replace("%VERSION%", plugin.getDescription().getVersion());

								sender.sendMessage(Utils.convertColorCodes(message));
							}

							return true;
						}

						Utils.insufficientPermissions(sender, "/BAMobCoins help");
						return true;
					}
					else if (args.length > 1)
					{
						String message = MessagesController.getGlobalTooManyArgs()
							.replace("%COMMAND%", "/BAMobCoins help.");

						sender.sendMessage(Utils.convertColorCodes(message));
					}

				}

				/* Withdraw mob coins into item form */
				if (args[0].equalsIgnoreCase("withdraw"))
				{
					if (sender.hasPermission("BAMobCoins.withdraw"))
					{
						Player player = (Player) sender;
						int amount = Integer.valueOf(args[1]);

						if (amount == 0)
						{
							sender.sendMessage(Utils.convertColorCodes(MessagesController.getCoinZero()));
							return true;
						}

						int playersCoins = CoinsAPI.getCoins(player.getUniqueId().toString());

						if (playersCoins >= amount)
						{
							String message = MessagesController.getCoinWithdraw()
								.replace("%AMOUNT%", String.valueOf(amount));

							Utils.withdrawCoins(player, amount);
							sender.sendMessage(Utils.convertColorCodes(message));
						}
						else
						{
							String message = MessagesController.getCoinNotEnough()
								.replace("%AMOUNT%", String.valueOf(amount));

							sender.sendMessage(Utils.convertColorCodes(message));
						}

						return true;
					}

					Utils.insufficientPermissions(sender, "/BAHappyHour withdraw <amount>");
					return true;
				}

				/* Pay command */
				if (args[0].equalsIgnoreCase("pay"))
				{
					/*
					 * Double check that the one running this command is a player. As thats important
					 */
					if (!(sender instanceof Player))
					{

						sender.sendMessage(Utils.convertColorCodes(MessagesController.getGlobalNonPlayer()));
						return true;
					}

					if (sender.hasPermission("BAMobCoins.pay"))
					{
						if (args.length == 3)
						{
							/* Ensure the one receiving the coins has joined the server. */
							String receiverIgn = args[1];
							Player receiver = Bukkit.getServer().getPlayer(receiverIgn);
							if (receiver == null)
							{
								sender.sendMessage(Utils.convertColorCodes(MessagesController.getGlobalNeverJoined()));
								return true;
							}

							String receivingUuid = receiver.getUniqueId().toString(); /* Receiving Coins UUID */
							String senderIgn = sender.getName(); /* Sending Coins IGN */
							Player sendingPlayer = Bukkit.getServer().getPlayer(senderIgn); /* Sending Coins Player */
							String sendingUuid = sendingPlayer.getUniqueId().toString(); /* Sending Coins UUID */

							/* Check if the player is attempting to pay themselves. */
							if (sender.getName().equalsIgnoreCase(receiverIgn))
							{
								sender.sendMessage(Utils.convertColorCodes(MessagesController.getPaySelf()));
								return true;
							}

							/* Ensure the player exists in the balances file. */
							if (!CoinsAPI.playerExists(receivingUuid))
							{
								sender.sendMessage(Utils.convertColorCodes(MessagesController.getGlobalNeverJoined()));
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
								sender.sendMessage(Utils.convertColorCodes(MessagesController.getGlobalWholeNumber()));
								return true;
							}

							/* Ensure they aren't trying to send 0 coins. */
							if (toSend == 0)
							{
								String message = MessagesController.getPaySendZero()
									.replace("%AMOUNT%", "0")
									.replace("%SENDER%", senderIgn)
									.replace("%RECEIVER%", receiverIgn);

								sender.sendMessage(Utils.convertColorCodes(message));
								return true;
							}

							/* Ensure the one trying to send has enough to send to the other user. */
							if (CoinsAPI.getCoins(sendingUuid).intValue() >= toSend)
							{
								/*
								 * Add the coins to the receiving player, remove the coins from the sending player.
								 */
								CoinsAPI.addCoins(receivingUuid, toSend);
								CoinsAPI.removeCoins(sendingUuid, toSend);

								String messageSender = MessagesController.getPaySender()
									.replace("%AMOUNT%", String.valueOf(toSend))
									.replace("%SENDER%", senderIgn)
									.replace("%RECEIVER%", receiverIgn);

								String messageReceiver = MessagesController.getPayReceiver()
									.replace("%AMOUNT%", String.valueOf(toSend))
									.replace("%SENDER%", senderIgn)
									.replace("%RECEIVER%", receiverIgn);

								sender.sendMessage(Utils.convertColorCodes(messageSender));
								receiver.sendMessage(Utils.convertColorCodes(messageReceiver));
							}
							else
							{
								String message = MessagesController.getPayNotEnough()
									.replace("%AMOUNT%", String.valueOf(toSend))
									.replace("%SENDER%", senderIgn)
									.replace("%RECEIVER%", receiverIgn);

								sender.sendMessage(Utils.convertColorCodes(message));
							}
							return true;
						}
						else if (args.length > 3)
						{
							String message = MessagesController.getGlobalTooManyArgs()
								.replace("%COMMAND%", "/BAMobCoins pay <players ign> <amount>");

							sender.sendMessage(Utils.convertColorCodes(message));
						}
						else if (args.length < 3)
						{
							String message = MessagesController.getGlobalNotEnoughArgs()
								.replace("%COMMAND%", "/BAMobCoins pay <players ign> <amount>");

							sender.sendMessage(Utils.convertColorCodes(message));
						}
					}

					Utils.insufficientPermissions(sender, "/BaMobCoins pay <players ign> <amount>");
					return true;
				}

				/*
				 * ---------------------------------------------- ---------------------------------------------- ADMIN COMMANDS
				 * ---------------------------------------------- ----------------------------------------------
				 */
				/* Reload command */
				if (args[0].equalsIgnoreCase("reload"))
				{
					if (args.length == 1)
					{
						if (sender.hasPermission("BAMobCoins.reload"))
						{
							/* Reload the plugins config file. */
							ConfigController.reload();
							MessagesController.reload();
							ShopController.reload();
							MobNameController.reload();

							sender.sendMessage(Utils.convertColorCodes(MessagesController.getReload()));

							return true;
						}

						Utils.insufficientPermissions(sender, "/BAMobCoins reload");
						return true;
					}
					else
					{
						String message = MessagesController.getGlobalTooManyArgs();

						message = message.replace("%COMMAND%", "/BAMobCoins reload.");

						sender.sendMessage(Utils.convertColorCodes(message));
					}
				}

				/* Show messages command */
				if (args[0].equalsIgnoreCase("messages") || args[0].equalsIgnoreCase("msgs"))
				{
					if (sender.hasPermission("BAMobCoins.messages"))
					{
						if (args.length == 2)
						{
							String version = plugin.getDescription()
								.getVersion();
							/* Send messages header */
							sender
								.sendMessage(ChatColor.GRAY + "-------------[ " + ChatColor.GOLD + "BAMobCoins V" + version + " Messages " + ChatColor.GRAY + "]-------------");

							String section = args[1];

							if (section.equalsIgnoreCase("Global"))
							{
								MessagesController.sendGlobalMessages(sender);
							}
							else if (section.equalsIgnoreCase("Balance"))
							{
								MessagesController.sendBalanceMessages(sender);
							}
							else if (section.equalsIgnoreCase("Pay"))
							{
								MessagesController.sendPayMessages(sender);
							}
							else if (section.equalsIgnoreCase("Add"))
							{
								MessagesController.sendAddMessages(sender);
							}
							else if (section.equalsIgnoreCase("Set"))
							{
								MessagesController.sendSetMessages(sender);
							}
							else if (section.equalsIgnoreCase("Remove"))
							{
								MessagesController.sendRemoveMessages(sender);
							}
							else if (section.equalsIgnoreCase("GiveItem"))
							{
								MessagesController.sendGiveItemMessages(sender);
							}
							else if (section.equalsIgnoreCase("Shop"))
							{
								MessagesController.sendShopMessages(sender);
							}
							else if (section.equalsIgnoreCase("Coin"))
							{
								MessagesController.sendCoinMessages(sender);
							}
							else if (section.equalsIgnoreCase("Gain"))
							{
								MessagesController.sendGainMessages(sender);
							}
							else if (section.equalsIgnoreCase("Messages"))
							{
								MessagesController.sendMessagesMessages(sender);
							}
							else if (section.equalsIgnoreCase("Reload"))
							{
								MessagesController.sendReloadMessages(sender);
							}
							else
							{
								String message = MessagesController.getMessagesUnknownSection()
									.replace("%SECTION%", section);

								sender.sendMessage(Utils.convertColorCodes(message));
							}

							return true;
						}
						else if (args.length == 1)
						{
							String message = MessagesController.getGlobalNotEnoughArgs()
								.replace("%COMMAND%", "/BAMobCoins messages <section>.");

							sender.sendMessage(Utils.convertColorCodes(message));
						}
						else if (args.length > 2)
						{
							String message = MessagesController.getGlobalTooManyArgs()
								.replace("%COMMAND%", "/BAMobCoins messages <section>.");

							sender.sendMessage(Utils.convertColorCodes(message));
						}

						return true;
					}

					Utils.insufficientPermissions(sender, "/BAMobCoins messages <section>");
					return true;
				}

				/* Add/Give mob coins to player */
				if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("give"))
				{
					if (sender.hasPermission("BAMobCoins.add"))
					{
						if (args.length == 3)
						{
							/* Make sure that player has joined the server. */
							String receiverIgn = args[1];
							Player receiver = Bukkit.getServer().getPlayer(receiverIgn);
							String receiverUuid = receiver.getUniqueId().toString();
							if (!CoinsAPI.playerExists(receiverUuid))
							{
								sender.sendMessage(Utils.convertColorCodes(MessagesController.getGlobalNeverJoined()));
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
								sender.sendMessage(Utils.convertColorCodes(MessagesController.getGlobalWholeNumber()));
								return true;
							}

							/* Check if they're trying to add 0 */
							if (amount == 0)
							{
								sender.sendMessage(Utils.convertColorCodes(MessagesController.getAddZero()));
								return true;
							}

							/* Check for negative number */
							if (amount < 0)
							{
								sender.sendMessage(Utils.convertColorCodes(MessagesController.getAddNegative()));
								return true;
							}

							/* Add the coins and send correct message. */
							CoinsAPI.addCoins(receiverUuid, amount);
							if (amount > 1)
							{
								/* Get the messages */
								String adminMessage = MessagesController.getAddAdmin()
									.replace("%AMOUNT%", String.valueOf(amount))
									.replace("%PLAYER%", receiverIgn);

								String playerMessage = MessagesController.getAddPlayer()
									.replace("%AMOUNT%", String.valueOf(amount))
									.replace("%PLAYER%", receiverIgn);

								/* Send the messages */
								sender.sendMessage(Utils.convertColorCodes(adminMessage));
								receiver.sendMessage(Utils.convertColorCodes(playerMessage));
								return true;
							}
						}
						else if (args.length > 3)
						{
							String message = MessagesController.getGlobalTooManyArgs()
								.replace("%COMMAND%", "/BAMobCoins add <players ign> <amount>");

							sender.sendMessage(Utils.convertColorCodes(message));
							return true;
						}
						else if (args.length < 3)
						{
							String message = MessagesController.getGlobalNotEnoughArgs()
								.replace("%COMMAND%", "/BAMobCoins add <players ign> <amount>");

							sender.sendMessage(Utils.convertColorCodes(message));
							return true;
						}

					}

					Utils.insufficientPermissions(sender, "/BAMobCoins add <players ign> <amount>");
					return true;
				}

				/* Remove mob coins from a player */
				if (args[0].equalsIgnoreCase("remove"))
				{
					if (sender.hasPermission("BAMobCoins.remove"))
					{
						if (args.length == 3)
						{
							/* Ensure the player has joined the server */
							String receiverIgn = args[1];
							Player receiver = Bukkit.getServer().getPlayer(receiverIgn);
							String UUID = receiver.getUniqueId().toString();

							if (!CoinsAPI.playerExists(UUID))
							{
								sender.sendMessage(Utils.convertColorCodes(MessagesController.getGlobalNeverJoined()));
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
								sender.sendMessage(Utils.convertColorCodes(MessagesController.getGlobalWholeNumber()));
								return true;
							}

							/* Ensure they're not trying to remove 0. */
							if (amount == 0)
							{
								sender.sendMessage(Utils.convertColorCodes(MessagesController.getRemoveZero()));
								return true;
							}

							/* Check for negative number */
							if (amount < 0)
							{
								sender.sendMessage(Utils.convertColorCodes(MessagesController.getRemoveNegative()));
								return true;
							}

							/* Remove the coins. */
							CoinsAPI.removeCoins(UUID, amount);

							/* Get the messages */
							String adminMessage = MessagesController.getRemoveAdmin()
								.replace("%PLAYER%", receiverIgn)
								.replace("%AMOUNT%", String.valueOf(amount));

							String playerMessage = MessagesController.getRemovePlayer()
								.replace("%PLAYER%", receiverIgn)
								.replace("%AMOUNT%", String.valueOf(amount));

							/* Send the messages */
							sender.sendMessage(Utils.convertColorCodes(adminMessage));
							receiver.sendMessage(Utils.convertColorCodes(playerMessage));
							return true;
						}

						Utils.insufficientPermissions(sender, "/BAMobCoins remove <players ign> <amount>");
						return true;
					}
					else if (args.length > 3)
					{
						String message = MessagesController.getGlobalTooManyArgs()
							.replace("%COMMAND%", "/BAMobCoins remove <players ign> <amount>");

						sender.sendMessage(Utils.convertColorCodes(message));
					}
					else if (args.length < 3)
					{
						String message = MessagesController.getGlobalNotEnoughArgs()
							.replace("%COMMAND%", "/BAMobCoins remove <players ign> <amount>");

						sender.sendMessage(Utils.convertColorCodes(message));
					}
				}

				/* Set a players mob coin balance */
				if (args[0].equalsIgnoreCase("set"))
				{
					if (sender.hasPermission("BAMobCoins.set"))
					{
						if (args.length == 3)
						{
							/* Ensure the player has joined the server. */
							String receiverIgn = args[1];
							Player receiver = Bukkit.getServer().getPlayer(receiverIgn);
							String pl = receiver.getUniqueId().toString();

							if (!CoinsAPI.playerExists(pl))
							{
								sender.sendMessage(Utils.convertColorCodes(MessagesController.getGlobalNeverJoined()));
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
								sender.sendMessage(Utils.convertColorCodes(MessagesController.getGlobalWholeNumber()));
								return true;
							}

							/* Set the balance */
							CoinsAPI.setCoins(pl, amount);

							String adminMessage = MessagesController.getSetAdmin()
								.replace("%PLAYER%", receiverIgn)
								.replace("%AMOUNT%", String.valueOf(amount));

							String playerMessage = MessagesController.getSetPlayer()
								.replace("%PLAYER%", receiverIgn)
								.replace("%AMOUNT%", String.valueOf(amount));

							sender.sendMessage(Utils.convertColorCodes(adminMessage));
							receiver.sendMessage(Utils.convertColorCodes(playerMessage));
							return true;
						}
						else if (args.length > 3)
						{
							String message = MessagesController.getGlobalTooManyArgs()
								.replace("%COMMAND%", "/BAMobCoins remove <players ign> <amount>");

							sender.sendMessage(Utils.convertColorCodes(message));
						}
						else if (args.length < 3)
						{
							String message = MessagesController.getGlobalNotEnoughArgs()
								.replace("%COMMAND%", "/BAMobCoins remove <players ign> <amount>");

							sender.sendMessage(Utils.convertColorCodes(message));
						}

					}

					Utils.insufficientPermissions(sender, "/BAMobCoins set <players ign> <amount>");
					return true;
				}

				/* Give a player an item from the shop */
				/* /bamobcoins giveItem <player> <shopId> <itemId> */
				if (args[0].equalsIgnoreCase("giveItem"))
				{
					if (sender.hasPermission("BAMobCoins.giveItem"))
					{
						if (args.length == 4)
						{
							String receiverIgn = args[1];
							Player receiver = Bukkit.getPlayer(receiverIgn);

							String shopKey = args[2];
							String itemKey = args[3];

							ArrayList<CustomItem> shopItems = ShopController.getShopItems(shopKey);
							if (shopItems == null)
							{
								String message = MessagesController.getGiveItemUnfoundCategory()
									.replace("CATEGORY", shopKey)
									.replace("ITEM", itemKey)
									.replace("PLAYER", receiverIgn);

								sender.sendMessage(Utils.convertColorCodes(message));
								return true;
							}
							for (CustomItem customItem : shopItems)
							{
								if (customItem.getItemKey().equalsIgnoreCase(itemKey))
								{
									if (customItem.getItemType() == CustomItem.ItemTypes.COMMAND)
									{
										Utils.runShopCommands(receiver, customItem.getItemKey(), customItem.getCommands());
									}
									else if (customItem.getItemType() == CustomItem.ItemTypes.ITEM)
									{
										/* Give the player the item */
										if (Utils.givePlayerItem(receiver, customItem.getRewardItem()))
										{
											String message = MessagesController.getShopBoughtItem()
												.replace("%ITEM%", customItem.getDisplayItem().getItemMeta().getDisplayName())
												.replace("%PRICE%", String.valueOf(customItem.getPrice()));

											receiver.sendMessage(Utils.convertColorCodes(message));
										}
										else
										{
											String noSpaceMessage = MessagesController.getGiveItemNoSpace()
												.replace("%ITEM%", customItem.getDisplayItem().getItemMeta().getDisplayName());

											receiver.sendMessage(Utils.convertColorCodes(noSpaceMessage));
										}

									}

									return true;
								}
							}

							/* Item they tried to give doesn't exist */
							String message = MessagesController.getGiveItemUnfoundItem()
								.replace("%PLAYER%", receiverIgn)
								.replace("%ITEM%", itemKey);

							sender.sendMessage(Utils.convertColorCodes(message));
						}
						else if (args.length > 4)
						{
							/* Too many arguments */
							String message = MessagesController.getGlobalTooManyArgs()
								.replace("%COMMAND%", "/BAMobCoins remove <players ign> <amount>");

							sender.sendMessage(Utils.convertColorCodes(message));
						}
						else if (args.length < 4)
						{
							/* Not enough arguments */
							String message = MessagesController.getGlobalNotEnoughArgs()
								.replace("%COMMAND%", "/BAMobCoins remove <players ign> <amount>");

							sender.sendMessage(Utils.convertColorCodes(message));
						}
					}

					Utils.insufficientPermissions(sender, "/BaMobCoins giveItem <players ign> <itemId>");
					return true;
				}

				sender.sendMessage(Utils.convertColorCodes(MessagesController.getGlobalUnknownCommand()));
				return true;
			}
			/*
			 * ---------------------------------------------- ---------------------------------------------- CONSOLE COMMANDS
			 * ---------------------------------------------- ----------------------------------------------
			 */
			else
			{
				/* Reload command */
				if (args[0].equalsIgnoreCase("reload"))
				{
					if (args.length == 1)
					{
						/* Reload the plugins config file. */
						ConfigController.reload();
						MessagesController.reload();
						ShopController.reload();
						MobNameController.reload();

						sender.sendMessage(Utils.convertColorCodes(MessagesController.getReload()));

						return true;
					}
					else
					{
						String message = MessagesController.getGlobalTooManyArgs();

						message = message.replace("%COMMAND%", "/BAMobCoins reload.");

						sender.sendMessage(Utils.convertColorCodes(message));
					}
				}

				/* Show messages command */
				if (args[0].equalsIgnoreCase("messages") || args[0].equalsIgnoreCase("msgs"))
				{
					if (args.length == 2)
					{
						String version = plugin.getDescription()
							.getVersion();
						/* Send messages header */
						sender
							.sendMessage(ChatColor.GRAY + "-------------[ " + ChatColor.GOLD + "BAMobCoins V" + version + " Messages " + ChatColor.GRAY + "]-------------");

						String section = args[1];

						if (section.equalsIgnoreCase("Global"))
						{
							MessagesController.sendGlobalMessages(sender);
						}
						else if (section.equalsIgnoreCase("Balance"))
						{
							MessagesController.sendBalanceMessages(sender);
						}
						else if (section.equalsIgnoreCase("Pay"))
						{
							MessagesController.sendPayMessages(sender);
						}
						else if (section.equalsIgnoreCase("Add"))
						{
							MessagesController.sendAddMessages(sender);
						}
						else if (section.equalsIgnoreCase("Set"))
						{
							MessagesController.sendSetMessages(sender);
						}
						else if (section.equalsIgnoreCase("Remove"))
						{
							MessagesController.sendRemoveMessages(sender);
						}
						else if (section.equalsIgnoreCase("GiveItem"))
						{
							MessagesController.sendGiveItemMessages(sender);
						}
						else if (section.equalsIgnoreCase("Shop"))
						{
							MessagesController.sendShopMessages(sender);
						}
						else if (section.equalsIgnoreCase("Coin"))
						{
							MessagesController.sendCoinMessages(sender);
						}
						else if (section.equalsIgnoreCase("Gain"))
						{
							MessagesController.sendGainMessages(sender);
						}
						else if (section.equalsIgnoreCase("Messages"))
						{
							MessagesController.sendMessagesMessages(sender);
						}
						else if (section.equalsIgnoreCase("Reload"))
						{
							MessagesController.sendReloadMessages(sender);
						}
						else
						{
							String message = MessagesController.getMessagesUnknownSection()
								.replace("%SECTION%", section);

							sender.sendMessage(Utils.convertColorCodes(message));
						}

						return true;
					}
					else if (args.length == 1)
					{
						String message = MessagesController.getGlobalNotEnoughArgs()
							.replace("%COMMAND%", "/BAMobCoins messages <section>.");

						sender.sendMessage(Utils.convertColorCodes(message));
					}
					else if (args.length > 2)
					{
						String message = MessagesController.getGlobalTooManyArgs()
							.replace("%COMMAND%", "/BAMobCoins messages <section>.");

						sender.sendMessage(Utils.convertColorCodes(message));
					}

					return true;
				}

				/* Add/Give mob coins to player */
				if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("give"))
				{
					if (args.length == 3)
					{
						/* Make sure that player has joined the server. */
						String receiverIgn = args[1];
						Player receiver = Bukkit.getServer().getPlayer(receiverIgn);
						String receiverUuid = receiver.getUniqueId().toString();
						if (!CoinsAPI.playerExists(receiverUuid))
						{
							sender.sendMessage(Utils.convertColorCodes(MessagesController.getGlobalNeverJoined()));
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
							sender.sendMessage(Utils.convertColorCodes(MessagesController.getGlobalWholeNumber()));
							return true;
						}

						/* Check if they're trying to add 0 */
						if (amount == 0)
						{
							sender.sendMessage(Utils.convertColorCodes(MessagesController.getAddZero()));
							return true;
						}

						/* Check for negative number */
						if (amount < 0)
						{
							sender.sendMessage(Utils.convertColorCodes(MessagesController.getAddNegative()));
							return true;
						}

						/* Add the coins and send correct message. */
						CoinsAPI.addCoins(receiverUuid, amount);
						if (amount > 1)
						{
							/* Get the messages */
							String adminMessage = MessagesController.getAddAdmin()
								.replace("%AMOUNT%", String.valueOf(amount))
								.replace("%PLAYER%", receiverIgn);

							String playerMessage = MessagesController.getAddPlayer()
								.replace("%AMOUNT%", String.valueOf(amount))
								.replace("%PLAYER%", receiverIgn);

							/* Send the messages */
							sender.sendMessage(Utils.convertColorCodes(adminMessage));
							receiver.sendMessage(Utils.convertColorCodes(playerMessage));
							return true;
						}
					}
					else if (args.length > 3)
					{
						String message = MessagesController.getGlobalTooManyArgs()
							.replace("%COMMAND%", "/BAMobCoins add <players ign> <amount>");

						sender.sendMessage(Utils.convertColorCodes(message));
						return true;
					}
					else if (args.length < 3)
					{
						String message = MessagesController.getGlobalNotEnoughArgs()
							.replace("%COMMAND%", "/BAMobCoins add <players ign> <amount>");

						sender.sendMessage(Utils.convertColorCodes(message));
						return true;
					}
				}

				/* Remove mob coins from a player */
				if (args[0].equalsIgnoreCase("remove"))
				{
					if (args.length == 3)
					{
						/* Ensure the player has joined the server */
						String receiverIgn = args[1];
						Player receiver = Bukkit.getServer().getPlayer(receiverIgn);
						String UUID = receiver.getUniqueId().toString();

						if (!CoinsAPI.playerExists(UUID))
						{
							sender.sendMessage(Utils.convertColorCodes(MessagesController.getGlobalNeverJoined()));
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
							sender.sendMessage(Utils.convertColorCodes(MessagesController.getGlobalWholeNumber()));
							return true;
						}

						/* Ensure they're not trying to remove 0. */
						if (amount == 0)
						{
							sender.sendMessage(Utils.convertColorCodes(MessagesController.getRemoveZero()));
							return true;
						}

						/* Check for negative number */
						if (amount < 0)
						{
							sender.sendMessage(Utils.convertColorCodes(MessagesController.getRemoveNegative()));
							return true;
						}

						/* Remove the coins. */
						CoinsAPI.removeCoins(UUID, amount);

						/* Get the messages */
						String adminMessage = MessagesController.getRemoveAdmin()
							.replace("%PLAYER%", receiverIgn)
							.replace("%AMOUNT%", String.valueOf(amount));

						String playerMessage = MessagesController.getRemovePlayer()
							.replace("%PLAYER%", receiverIgn)
							.replace("%AMOUNT%", String.valueOf(amount));

						/* Send the messages */
						sender.sendMessage(Utils.convertColorCodes(adminMessage));
						receiver.sendMessage(Utils.convertColorCodes(playerMessage));
						return true;
					}
					else if (args.length > 3)
					{
						String message = MessagesController.getGlobalTooManyArgs()
							.replace("%COMMAND%", "/BAMobCoins remove <players ign> <amount>");

						sender.sendMessage(Utils.convertColorCodes(message));
					}
					else if (args.length < 3)
					{
						String message = MessagesController.getGlobalNotEnoughArgs()
							.replace("%COMMAND%", "/BAMobCoins remove <players ign> <amount>");

						sender.sendMessage(Utils.convertColorCodes(message));
					}
				}

				/* Set a players mob coin balance */
				if (args[0].equalsIgnoreCase("set"))
				{
					if (args.length == 3)
					{
						/* Ensure the player has joined the server. */
						String receiverIgn = args[1];
						Player receiver = Bukkit.getServer().getPlayer(receiverIgn);
						String pl = receiver.getUniqueId().toString();

						if (!CoinsAPI.playerExists(pl))
						{
							sender.sendMessage(Utils.convertColorCodes(MessagesController.getGlobalNeverJoined()));
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
							sender.sendMessage(Utils.convertColorCodes(MessagesController.getGlobalWholeNumber()));
							return true;
						}

						/* Set the balance */
						CoinsAPI.setCoins(pl, amount);

						String adminMessage = MessagesController.getSetAdmin()
							.replace("%PLAYER%", receiverIgn)
							.replace("%AMOUNT%", String.valueOf(amount));

						String playerMessage = MessagesController.getSetPlayer()
							.replace("%PLAYER%", receiverIgn)
							.replace("%AMOUNT%", String.valueOf(amount));

						sender.sendMessage(Utils.convertColorCodes(adminMessage));
						receiver.sendMessage(Utils.convertColorCodes(playerMessage));
						return true;
					}
					else if (args.length > 3)
					{
						String message = MessagesController.getGlobalTooManyArgs()
							.replace("%COMMAND%", "/BAMobCoins remove <players ign> <amount>");

						sender.sendMessage(Utils.convertColorCodes(message));
					}
					else if (args.length < 3)
					{
						String message = MessagesController.getGlobalNotEnoughArgs()
							.replace("%COMMAND%", "/BAMobCoins remove <players ign> <amount>");

						sender.sendMessage(Utils.convertColorCodes(message));
					}
				}

				/* Give a player an item from the shop */
				/* /bamobcoins giveItem <player> <shopId> <itemId> */
				if (args[0].equalsIgnoreCase("giveItem"))
				{
					if (args.length == 4)
					{
						String receiverIgn = args[1];
						Player receiver = Bukkit.getPlayer(receiverIgn);

						String shopKey = args[2];
						String itemKey = args[3];

						ArrayList<CustomItem> shopItems = ShopController.getShopItems(shopKey);
						if (shopItems == null)
						{
							String message = MessagesController.getGiveItemUnfoundCategory()
								.replace("CATEGORY", shopKey)
								.replace("ITEM", itemKey)
								.replace("PLAYER", receiverIgn);

							sender.sendMessage(Utils.convertColorCodes(message));
							return true;
						}
						for (CustomItem customItem : shopItems)
						{
							if (customItem.getItemKey().equalsIgnoreCase(itemKey))
							{
								if (customItem.getItemType() == CustomItem.ItemTypes.COMMAND)
								{
									Utils.runShopCommands(receiver, customItem.getItemKey(), customItem.getCommands());
								}
								else if (customItem.getItemType() == CustomItem.ItemTypes.ITEM)
								{
									/* Give the player the item */
									if (Utils.givePlayerItem(receiver, customItem.getRewardItem()))
									{
										String message = MessagesController.getShopBoughtItem()
											.replace("%ITEM%", customItem.getDisplayItem().getItemMeta().getDisplayName())
											.replace("%PRICE%", String.valueOf(customItem.getPrice()));

										receiver.sendMessage(Utils.convertColorCodes(message));
									}
									else
									{
										String noSpaceMessage = MessagesController.getGiveItemNoSpace()
											.replace("%ITEM%", customItem.getDisplayItem().getItemMeta().getDisplayName());

										receiver.sendMessage(Utils.convertColorCodes(noSpaceMessage));
									}

								}

								return true;
							}
						}

						/* Item they tried to give doesn't exist */
						String message = MessagesController.getGiveItemUnfoundItem()
							.replace("%PLAYER%", receiverIgn)
							.replace("%ITEM%", itemKey);

						sender.sendMessage(Utils.convertColorCodes(message));
					}
					else if (args.length > 4)
					{
						/* Too many arguments */
						String message = MessagesController.getGlobalTooManyArgs()
							.replace("%COMMAND%", "/BAMobCoins remove <players ign> <amount>");

						sender.sendMessage(Utils.convertColorCodes(message));
					}
					else if (args.length < 4)
					{
						/* Not enough arguments */
						String message = MessagesController.getGlobalNotEnoughArgs()
							.replace("%COMMAND%", "/BAMobCoins remove <players ign> <amount>");

						sender.sendMessage(Utils.convertColorCodes(message));
					}

				}
			}
		}

		return false;
	}

}
