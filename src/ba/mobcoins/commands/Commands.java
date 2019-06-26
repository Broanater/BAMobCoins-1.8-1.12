package ba.mobcoins.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ba.mobcoins.Main;
import ba.mobcoins.apis.CoinsAPI;
import ba.mobcoins.controllers.ConfigController;
import ba.mobcoins.controllers.MessagesController;
import ba.mobcoins.controllers.MobNameController;
import ba.mobcoins.controllers.ShopController;
import ba.mobcoins.models.CustomItem;
import ba.mobcoins.utilities.Utils;

public class Commands implements CommandExecutor
{
	private Main plugin;

	public Commands(Main main)
	{
		plugin = main;
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
						p.openInventory(ShopController.getShopInventory(p.getUniqueId().toString(), "MENU"));
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

							String message = MessagesController.getYourBalance();

							message = message.replace("%BALANCE%", String.valueOf(balance));

							p.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(message));

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
							ConfigController.reload();
							MessagesController.reload();
							ShopController.reload();
							MobNameController.reload();
							
							sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(MessagesController.getReload()));

							return true;
						}

						Utils.insufficientPermissions(sender, "/BAMobCoins reload");
						return true;
					}

					if (args[0].equalsIgnoreCase("help"))
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

					if (args[0].equalsIgnoreCase("messages") || args[0].equalsIgnoreCase("msgs"))
					{
						if (sender.hasPermission("BAMobCoins.messages"))
						{
							/* Global Messages */
							String neverJoined = Utils.convertColorCodes("&fNever_Joined: " + MessagesController.getGlobalNeverJoined());
							String wholeNumber = Utils.convertColorCodes("&fWhole_Number: " + MessagesController.getGlobalWholeNumber());
							String nonPlayer = Utils.convertColorCodes("&fNon_Player: " + MessagesController.getGlobalWholeNumber());
							String insufficientPermission = Utils.convertColorCodes("&fInsufficient_Permission: " + MessagesController.getGlobalInsufficientPermission());
							String unknownCommand = Utils.convertColorCodes("&fUnknown_Command: " + MessagesController.getGlobalUnknownCommand());

							/* Balance Messages */
							String yourBalance = Utils.convertColorCodes("&fYour_Balance: " + MessagesController.getYourBalance());
							String otherBalance = Utils.convertColorCodes("&fOther_Balance: " + MessagesController.getOtherBalance());

							/* Pay Messages */
							String paySender = Utils.convertColorCodes("&fSender: " + MessagesController.getPaySender());
							String payReceiver = Utils.convertColorCodes("&fReceiver: " + MessagesController.getPayReceiver());
							String paySelf = Utils.convertColorCodes("&fSelf: " + MessagesController.getPaySelf());
							String payZero = Utils.convertColorCodes("&fZero: " + MessagesController.getPaySendZero());
							String payNotEnough = Utils.convertColorCodes("&fNot_Enough: " + MessagesController.getPayNotEnough());

							/* Add Messages */
							String addAdmin = Utils.convertColorCodes("&fAdmin_Message: " + MessagesController.getAddAdmin());
							String addPlayer = Utils.convertColorCodes("&fPlayer_Message: " + MessagesController.getAddPlayer());
							String addZero = Utils.convertColorCodes("&fZero: " + MessagesController.getAddZero());

							/* Set Messages */
							String setAdmin = Utils.convertColorCodes("&fAdmin_Message: " + MessagesController.getSetAdmin());
							String setPlayer = Utils.convertColorCodes("&fPlayer_Message: " + MessagesController.getSetPlayer());

							/* Remove Messages */
							String removeAdmin = Utils.convertColorCodes("&fAdmin_Message: " + MessagesController.getRemoveAdmin());
							String removePlayer = Utils.convertColorCodes("&fPlayer_Message: " + MessagesController.getRemovePlayer());
							String removeZero = Utils.convertColorCodes("&fZero: " + MessagesController.getRemoveZero());

							/* Give Item Messages */
							String giveItemAdmin = Utils.convertColorCodes("&fAdmin_Message: " + MessagesController.getGiveItemAdmin());
							String giveItemPlayer = Utils.convertColorCodes("&fPlayer_Message: " + MessagesController.getGiveItemPlayer());
							String giveItemUnfoundItem = Utils.convertColorCodes("&fUnfound_Item: " + MessagesController.getGiveItemUnfoundItem());
							String giveItemUnfoundCategory = Utils.convertColorCodes("&fUnfound_Item: " + MessagesController.getGiveItemUnfoundCategory());

							/* Shop Messages */
							String shopBoughtItem = Utils.convertColorCodes("&fBought_Item: " + MessagesController.getShopBoughtItem());
							String shopNotEnough = Utils.convertColorCodes("&fNot_Enough: " + MessagesController.getShopNotEnough());

							/* Coin Messages */
							String coinWithdraw = Utils.convertColorCodes("&fWithdraw: " + MessagesController.getCoinWithdraw());
							String coinDeposit = Utils.convertColorCodes("&fDeposit: " + MessagesController.getCoinDeposit());
							String coinZero = Utils.convertColorCodes("&fZero: " + MessagesController.getCoinZero());

							/* Reload Message */
							String reloadAdmin = Utils.convertColorCodes("&fAdmin_Message: " + MessagesController.getReload());

							sender.sendMessage(ChatColor.GRAY + "-------------[ " + ChatColor.GOLD + "BAMobCoins V" + plugin.getDescription().getVersion() + " Messages " + ChatColor.GRAY + "]-------------");

							sender.sendMessage(Utils.convertColorCodes("&6Global Messages"));
							sender.sendMessage("  " + neverJoined);
							sender.sendMessage("  " + wholeNumber);
							sender.sendMessage("  " + nonPlayer);
							sender.sendMessage("  " + insufficientPermission);
							sender.sendMessage("  " + unknownCommand);

							sender.sendMessage(Utils.convertColorCodes("&6Balance Messages"));
							sender.sendMessage("  " + yourBalance);
							sender.sendMessage("  " + otherBalance);

							sender.sendMessage(Utils.convertColorCodes("&6Pay Messages"));
							sender.sendMessage("  " + paySender);
							sender.sendMessage("  " + payReceiver);
							sender.sendMessage("  " + paySelf);
							sender.sendMessage("  " + payZero);
							sender.sendMessage("  " + payNotEnough);

							sender.sendMessage(Utils.convertColorCodes("&6Add Messages"));
							sender.sendMessage("  " + addAdmin);
							sender.sendMessage("  " + addPlayer);
							sender.sendMessage("  " + addZero);

							sender.sendMessage(Utils.convertColorCodes("&6Set Messages"));
							sender.sendMessage("  " + setAdmin);
							sender.sendMessage("  " + setPlayer);

							sender.sendMessage(Utils.convertColorCodes("&6Remove Messages"));
							sender.sendMessage("  " + removeAdmin);
							sender.sendMessage("  " + removePlayer);
							sender.sendMessage("  " + removeZero);

							sender.sendMessage(Utils.convertColorCodes("&6Give Item Messages"));
							sender.sendMessage("  " + giveItemAdmin);
							sender.sendMessage("  " + giveItemPlayer);
							sender.sendMessage("  " + giveItemUnfoundItem);
							sender.sendMessage("  " + giveItemUnfoundCategory);

							sender.sendMessage(Utils.convertColorCodes("&6Shop Messages"));
							sender.sendMessage("  " + shopBoughtItem);
							sender.sendMessage("  " + shopNotEnough);

							sender.sendMessage(Utils.convertColorCodes("&6Coin Messages"));
							sender.sendMessage("  " + coinWithdraw);
							sender.sendMessage("  " + coinDeposit);
							sender.sendMessage("  " + coinZero);

							sender.sendMessage(Utils.convertColorCodes("&6Reload Messages"));
							sender.sendMessage("  " + reloadAdmin);

							sender.sendMessage(ChatColor.GRAY + "-----------------------------------------------------");

							return true;
						}

						Utils.insufficientPermissions(sender, "/BAMobCoins messages");
						return true;
					}

				}

				if (args.length == 2)
				{
					if (args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("bal"))
					{
						if (sender.hasPermission("BAMobCoins.balance.others"))
						{
							/* Get the Player of the on to check the balance of. */
							Player toCheck = Bukkit.getServer().getPlayer(args[1]);

							/* Get the balance of the player. */
							int balance = CoinsAPI.getCoins(toCheck.getUniqueId().toString());

							String message = MessagesController.getOtherBalance();

							message = message.replace("%BALANCE%", String.valueOf(balance));
							message = message.replace("%PLAYER%", sender.getName());

							sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(message));
							return true;
						}

						Utils.insufficientPermissions(sender, "/BAMobCoins balance <players ign>");
						return true;
					}

					if (args[0].equalsIgnoreCase("withdraw"))
					{
						if (sender.hasPermission("BAMobCoins.withdraw"))
						{
							Player player = (Player) sender;
							int amount = Integer.valueOf(args[1]);

							if (amount == 0)
							{
								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(MessagesController.getCoinZero()));
								return true;
							}

							if (CoinsAPI.getCoins(player.getUniqueId().toString()) >= amount)
							{
								String message = MessagesController.getCoinWithdraw();

								message = message.replace("%AMOUNT%", String.valueOf(amount));

								Utils.withdrawCoins(player, amount);
								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(message));
							}

							return true;
						}

						Utils.insufficientPermissions(sender, "/BAHappyHour reload");
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
							String receiverIgn = args[1];
							Player receiver = Bukkit.getServer().getPlayer(receiverIgn);
							String receiverUuid = receiver.getUniqueId().toString();
							if (!CoinsAPI.playerExists(receiverUuid))
							{
								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(MessagesController.getGlobalNeverJoined()));
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
								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(MessagesController.getGlobalWholeNumber()));
								return true;
							}

							/* Check if they're trying to add 0 */
							if (amount == 0)
							{
								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(MessagesController.getAddZero()));
								return true;
							}

							/* Add the coins and send correct message. */
							CoinsAPI.addCoins(receiverUuid, amount);
							if (amount > 1)
							{
								String adminMessage = MessagesController.getAddAdmin();
								adminMessage = adminMessage.replace("%AMOUNT%", String.valueOf(amount));
								adminMessage = adminMessage.replace("%PLAYER%", receiverIgn);

								String playerMessage = MessagesController.getAddPlayer();
								playerMessage = playerMessage.replace("%AMOUNT%", String.valueOf(amount));
								playerMessage = playerMessage.replace("%PLAYER%", receiverIgn);

								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(adminMessage));
								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(playerMessage));
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
							String receiverIgn = args[1];
							Player receiver = Bukkit.getServer().getPlayer(receiverIgn);
							String UUID = receiver.getUniqueId().toString();

							if (!CoinsAPI.playerExists(UUID))
							{
								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(MessagesController.getGlobalNeverJoined()));
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
								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(MessagesController.getGlobalWholeNumber()));
								return true;
							}

							/* Ensure they're not trying to remove 0. */
							if (amount == 0)
							{
								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(MessagesController.getRemoveZero()));
								return true;
							}

							/* Remove the coins. */
							CoinsAPI.removeCoins(UUID, amount);

							String adminMessage = MessagesController.getRemoveAdmin();
							adminMessage = adminMessage.replace("%PLAYER%", receiverIgn);
							adminMessage = adminMessage.replace("%AMOUNT%", String.valueOf(amount));

							String playerMessage = MessagesController.getRemovePlayer();
							playerMessage = playerMessage.replace("%PLAYER%", receiverIgn);
							playerMessage = playerMessage.replace("%AMOUNT%", String.valueOf(amount));

							sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(adminMessage));
							receiver.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(playerMessage));
							return true;
						}

						Utils.insufficientPermissions(sender, "/BAMobCoins remove <players ign> <amount>");
						return true;
					}

					if (args[0].equalsIgnoreCase("set"))
					{
						if (sender.hasPermission("BAMobCoins.set"))
						{
							/* Ensure the player has joined the server. */
							String receiverIgn = args[1];
							Player receiver = Bukkit.getServer().getPlayer(receiverIgn);
							String pl = receiver.getUniqueId().toString();
							if (!CoinsAPI.playerExists(pl))
							{
								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(MessagesController.getGlobalNeverJoined()));
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
								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(MessagesController.getGlobalWholeNumber()));
								return true;
							}

							/* Set the balance */
							CoinsAPI.setCoins(pl, amount);

							String adminMessage = MessagesController.getSetAdmin();
							adminMessage = adminMessage.replace("%PLAYER%", receiverIgn);
							adminMessage = adminMessage.replace("%AMOUNT%", String.valueOf(amount));

							String playerMessage = MessagesController.getSetPlayer();
							playerMessage = playerMessage.replace("%PLAYER%", receiverIgn);
							playerMessage = playerMessage.replace("%AMOUNT%", String.valueOf(amount));

							sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(adminMessage));
							receiver.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(playerMessage));
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

							sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(MessagesController.getGlobalNonPlayer()));
							return true;
						}

						if (sender.hasPermission("BAMobCoins.pay"))
						{
							/* Ensure the one receiving the coins has joined the server. */
							String receiverIgn = args[1];
							Player receiver = Bukkit.getServer().getPlayer(receiverIgn);
							if (receiver == null)
							{
								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(MessagesController.getGlobalNeverJoined()));
								return true;
							}

							String receivingUuid = receiver.getUniqueId().toString(); /* Receiving Coins UUID */
							String senderIgn = sender.getName(); /* Sending Coins IGN */
							Player sendingPlayer = Bukkit.getServer().getPlayer(senderIgn); /* Sending Coins Player */
							String sendingUuid = sendingPlayer.getUniqueId().toString(); /* Sending Coins UUID */

							/* Check if the player is attempting to pay themselves. */
							if (sender.getName().equalsIgnoreCase(receiverIgn))
							{
								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(MessagesController.getPaySelf()));
								return true;
							}

							/* Ensure the player exists in the balances file. */
							if (!CoinsAPI.playerExists(receivingUuid))
							{
								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(MessagesController.getGlobalNeverJoined()));
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
								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(MessagesController.getGlobalWholeNumber()));
								return true;
							}

							/* Ensure they aren't trying to send 0 coins. */
							if (toSend == 0)
							{
								String message = MessagesController.getPaySendZero();
								message = message.replace("%AMOUNT%", "0");
								message = message.replace("%SENDER%", senderIgn);
								message = message.replace("%RECEIVER%", receiverIgn);

								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(message));
								return true;
							}

							/* Ensure the one trying to send has enough to send to the other user. */
							if (CoinsAPI.getCoins(sendingUuid).intValue() >= toSend)
							{
								/*
								 * Add the coins to the receiving player, remove the coins from the sending
								 * player.
								 */
								CoinsAPI.addCoins(receivingUuid, toSend);
								CoinsAPI.removeCoins(sendingUuid, toSend);

								String messageSender = MessagesController.getPaySender();
								messageSender = messageSender.replace("%AMOUNT%", String.valueOf(toSend));
								messageSender = messageSender.replace("%SENDER%", senderIgn);
								messageSender = messageSender.replace("%RECEIVER%", receiverIgn);

								String messageReceiver = MessagesController.getPayReceiver();
								messageReceiver = messageReceiver.replace("%AMOUNT%", String.valueOf(toSend));
								messageReceiver = messageReceiver.replace("%SENDER%", senderIgn);
								messageReceiver = messageReceiver.replace("%RECEIVER%", receiverIgn);

								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(messageSender));
								receiver.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(messageReceiver));
							}
							else
							{
								String message = MessagesController.getPayNotEnough();
								message = message.replace("%AMOUNT%", String.valueOf(toSend));
								message = message.replace("%SENDER%", senderIgn);
								message = message.replace("%RECEIVER%", receiverIgn);

								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(message));
							}
							return true;
						}

						Utils.insufficientPermissions(sender, "/BaMobCoins pay <players ign> <amount>");
						return true;
					}
				}
				
				if (args.length == 4)
				{
					/* /bamobcoins giveItem <player> <shopId> <itemId> */
					if (args[0].equalsIgnoreCase("giveItem"))
					{
						if (sender.hasPermission("BAMobCoins.giveItem"))
						{
							String receiverIgn = args[1];
							Player receiver = Bukkit.getPlayer(receiverIgn);

							String shopKey = args[2];
							String itemKey = args[3];

							ArrayList<CustomItem> shopItems = ShopController.getShopItems(shopKey);
							if (shopItems == null)
							{
								String message = MessagesController.getGiveItemUnfoundCategory();
								message = message.replace("CATEGORY", shopKey);
								message = message.replace("ITEM", itemKey);
								message = message.replace("PLAYER", receiverIgn);

								sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(message));
								return true;
							}
							for (CustomItem customItem : shopItems)
							{
								if (customItem.getItemKey().equalsIgnoreCase(itemKey))
								{

									Utils.runShopCommands(receiver, customItem.getItemKey(), customItem.getCommands());

									String adminMessage = MessagesController.getGiveItemAdmin();
									adminMessage = adminMessage.replace("%PLAYER%", receiverIgn);
									adminMessage = adminMessage.replace("%ITEM%", itemKey);

									String playerMessage = MessagesController.getGiveItemPlayer();
									playerMessage = playerMessage.replace("%PLAYER%", receiverIgn);
									playerMessage = playerMessage.replace("%ITEM%", itemKey);

									sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(adminMessage));
									receiver.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(playerMessage));

									return true;
								}
							}

							/* Item they tried to give doesn't exist */
							String message = MessagesController.getGiveItemUnfoundItem();
							message = message.replace("%PLAYER%", receiverIgn);
							message = message.replace("%ITEM%", itemKey);

							sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(message));
						}

						Utils.insufficientPermissions(sender, "/BaMobCoins giveItem <players ign> <itemId>");
						return true;
					}
				}
				
				
				

				sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(MessagesController.getGlobalUnknownCommand()));
				return true;
				/* End of if testing if a player ran the command */
			}
			else /* Commands run from console Eg. Not a player */
			{
				if (args[0].equals("add"))
				{
					/* Make sure that player has joined the server. */
					String receiverIgn = args[1];
					Player receiver = Bukkit.getServer().getPlayer(receiverIgn);
					String receiverUuid = receiver.getUniqueId().toString();
					if (!CoinsAPI.playerExists(receiverUuid))
					{
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
						return true;
					}

					/* Check if they're trying to add 0 */
					if (amount == 0)
					{
						String message = MessagesController.getAddZero();
						message = message.replace("%AMOUNT%", String.valueOf(amount));
						message = message.replace("%PLAYER", receiverIgn);

						sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(message));
						return true;
					}

					/* Add the coins and send correct message. */
					CoinsAPI.addCoins(receiverUuid, amount);

					String playerMessage = MessagesController.getAddPlayer();
					playerMessage = playerMessage.replace("%AMOUNT%", String.valueOf(amount));
					playerMessage = playerMessage.replace("%PLAYER%", receiverIgn);

					receiver.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(playerMessage));
					return true;

				}

				if (args[0].equalsIgnoreCase("remove"))
				{
					/* Ensure the player has joined the server */
					String receiverIgn = args[1];
					Player receiver = Bukkit.getServer().getPlayer(receiverIgn);
					String receiverUuid = receiver.getUniqueId().toString();

					if (!CoinsAPI.playerExists(receiverUuid))
					{
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
						return true;
					}

					/* Ensure they're not trying to remove 0. */
					if (amount == 0)
					{
						return true;
					}

					/* Remove the coins. */
					CoinsAPI.removeCoins(receiverUuid, amount);

					String playerMessage = MessagesController.getRemovePlayer();
					playerMessage = playerMessage.replace("%AMOUNT%", String.valueOf(amount));
					playerMessage = playerMessage.replace("%PLAYER%", receiverIgn);

					receiver.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(playerMessage));
					return true;
				}

				if (args[0].equalsIgnoreCase("set"))
				{
					/* Ensure the player has joined the server. */
					String receiverIgn = args[1];
					Player receiver = Bukkit.getServer().getPlayer(receiverIgn);
					String receiverUuid = receiver.getUniqueId().toString();
					if (!CoinsAPI.playerExists(receiverUuid))
					{
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
						return true;
					}

					/* Set the balance */
					CoinsAPI.setCoins(receiverUuid, amount);

					String playerMessage = MessagesController.getSetPlayer();
					playerMessage = playerMessage.replace("%AMOUNT%", String.valueOf(amount));
					playerMessage = playerMessage.replace("%PLAYER%", receiverIgn);

					receiver.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(playerMessage));
					return true;
				}

				/* /bamobcoins giveItem <player> <shopId> <itemId> */
				if (args[0].equalsIgnoreCase("giveItem"))
				{
					String receiverIgn = args[1];
					Player receiver = Bukkit.getPlayer(receiverIgn);

					String shopKey = args[2];
					String itemKey = args[3];

					ArrayList<CustomItem> shopItems = ShopController.getShopItems(shopKey);
					if (shopItems == null)
					{
						String message = MessagesController.getGiveItemUnfoundCategory();
						message = message.replace("CATEGORY", shopKey);
						message = message.replace("ITEM", itemKey);
						message = message.replace("PLAYER", receiverIgn);

						sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(message));
						return true;
					}
					for (CustomItem customItem : shopItems)
					{
						if (customItem.getItemKey().equalsIgnoreCase(itemKey))
						{

							Utils.runShopCommands(receiver, customItem.getItemKey(), customItem.getCommands());

							String adminMessage = MessagesController.getGiveItemAdmin();
							adminMessage = adminMessage.replace("%PLAYER%", receiverIgn);
							adminMessage = adminMessage.replace("%ITEM%", itemKey);

							String playerMessage = MessagesController.getGiveItemPlayer();
							playerMessage = playerMessage.replace("%PLAYER%", receiverIgn);
							playerMessage = playerMessage.replace("%ITEM%", itemKey);

							sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(adminMessage));
							receiver.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(playerMessage));

							return true;
						}
					}

					/* Item they tried to give doesn't exist */
					String message = MessagesController.getGiveItemUnfoundItem();
					message = message.replace("%PLAYER%", receiverIgn);
					message = message.replace("%ITEM%", itemKey);

					sender.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(message));
					return true;
				}
			}
		}

		return false;
	}
}