package ca.live.brodya.mobcoins;

public class CoinsAPI implements org.bukkit.event.Listener
{
	private static Main plugin;

	public CoinsAPI(Main pl)
	{
		plugin = pl;
	}

	public static Integer getCoins(String p)
	{
		if (!playerExists(p))
		{
			return Integer.valueOf(0);
		}

		int bal = ((Integer) plugin.coins.get(p)).intValue();
		return Integer.valueOf(bal);
	}

	public static void addCoins(String p, int coins)
	{
		int c = getCoins(p).intValue();
		c += coins;
		plugin.coins.put(p, Integer.valueOf(c));
	}

	public static void removeCoins(String p, int coins)
	{
		int c = getCoins(p).intValue();
		c -= coins;
		plugin.coins.put(p, Integer.valueOf(c));
	}

	public static void setCoins(String p, int coins)
	{
		int c = getCoins(p).intValue();
		int a = 0;
		c = a + coins;
		plugin.coins.put(p, Integer.valueOf(c));
	}

	public static void createPlayer(String p)
	{
		if (!playerExists(p))
		{
			plugin.coins.put(p, Integer.valueOf(0));
		}
	}

	public static boolean playerExists(String player)
	{
		if (plugin.coins.containsKey(player))
		{
			return true;
		}
		return false;
	}
}
