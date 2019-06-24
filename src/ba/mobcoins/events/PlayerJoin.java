package ba.mobcoins.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ba.mobcoins.apis.CoinsAPI;

public class PlayerJoin implements Listener
{
	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent e)
	{
		Player player = e.getPlayer();
		String playerUuid = player.getUniqueId().toString();
		CoinsAPI.createPlayer(playerUuid);
	}
}
