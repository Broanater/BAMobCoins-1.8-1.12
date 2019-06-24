package ba.mobcoins.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import ba.mobcoins.apis.CoinsAPI;
import ba.mobcoins.controllers.MessagesController;
import ba.mobcoins.utilities.Utils;

public class PlayerInteract implements Listener
{
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

			String message = MessagesController.getCoinDeposit();

			message = message.replace("%AMOUNT%", String.valueOf(amount));

			player.sendMessage(Utils.getPrefix() + Utils.convertColorCodes(message));
		}

	}
}
