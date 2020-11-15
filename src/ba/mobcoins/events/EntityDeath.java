package ba.mobcoins.events;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import ba.mobcoins.Main;
import ba.mobcoins.apis.CoinsAPI;
import ba.mobcoins.controllers.ConfigController;
import ba.mobcoins.controllers.MobNameController;
import ba.mobcoins.logger.CustomLogger;
import ba.mobcoins.utilities.Utils;

public class EntityDeath implements Listener
{
	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnEntityDeath(EntityDeathEvent e)
	{
		if (e.getEntity().getKiller() instanceof Player)
		{
			HashMap<String, Double> mobRates = ConfigController.getDropRates();
			for (String entity : mobRates.keySet())
			{
				String entityKilled = e.getEntity().getName().toUpperCase();
				
				if (entityKilled.equals(entity))
				{
					Random rand = new Random();
					int randOdd = 1 + rand.nextInt(100);

					double mobOdd = mobRates.get(entity);

					if (randOdd <= mobOdd)
					{
						Player p = e.getEntity().getKiller();
						if (p == null)
						{
							return;
						}

						String player = p.getUniqueId().toString();

						String mobName = MobNameController.getMobName(e.getEntityType());

						if (mobName == null)
						{
							p.sendMessage(Utils.getCurrencyIncreaseMessage(entity.toString(), 1));
						}
						else
						{
							p.sendMessage(Utils.getCurrencyIncreaseMessage(mobName, 1));
						}

						CoinsAPI.addCoins(player, 1);
					}
				}
			}
		}
	}
}
