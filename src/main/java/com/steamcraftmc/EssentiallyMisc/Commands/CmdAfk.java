package com.steamcraftmc.EssentiallyMisc.Commands;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;

import com.steamcraftmc.EssentiallyMisc.MainPlugin;

public class CmdAfk extends BaseCommand implements Listener, Runnable {

	class AfkInfo {
		public final Player player;
		public long lastAction;
		public Location startPosition;
		public Location lastPosition;
		public boolean isAfk;
		
		public AfkInfo(Player player) {
			this.player = player;
		}

		public void goAfk() {
			lastAction = System.currentTimeMillis();
			startPosition = lastPosition = player.getLocation().clone();
			isAfk = true;
		}
	}
	
	final HashMap<UUID, AfkInfo> _afkPlayers = new HashMap<UUID, AfkInfo>();
	private int _taskId;
	
    public CmdAfk(MainPlugin plugin) {
        super(plugin, "afk", 0, 50);
    }

    public void start() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 60, 60);
    }
    
    public void stop() {
    	HandlerList.unregisterAll(this);
    	plugin.getServer().getScheduler().cancelTask(_taskId);
    }
    
    private AfkInfo getPlayer(Player player) {
    	AfkInfo info = _afkPlayers.get(player.getUniqueId());
    	if (info == null) {
    		_afkPlayers.put(player.getUniqueId(), info = new AfkInfo(player));
    	}
    	return info;
    }

	@Override
	public void run() {
		for (Object i : _afkPlayers.values().toArray()) {
			AfkInfo info = (AfkInfo)i;
			if (!info.player.isOnline()) {
				_afkPlayers.remove(info.player.getUniqueId());
				continue;
			}
			else if (info.isAfk) {
		    	info.lastAction = System.currentTimeMillis();
				info.lastPosition = info.player.getLocation();

				if (info.lastPosition.distanceSquared(info.startPosition) > 20) {
					removeAfkFlag(info.player);
				}
			}
		}
	}

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) throws Exception {
		
		AfkInfo info = getPlayer(player);
		if (info.isAfk == false) {
			String message = args.length == 0 
					? plugin.Config.format("messages.afk", "&8* &3{name}&8 is now afk *", "name", player.getName()) 
					: plugin.Config.format("messages.afk-message", "&8* &3{name}&8 is now afk: {message} *", "name", player.getName(), "message", String.join(" ", args));
			player.getServer().broadcastMessage(message);
			info.goAfk();
		}
		else {
			removeAfkFlag(player);
		}
		return true;
	}

	private void removeAfkFlag(Player player) {
		AfkInfo info = getPlayer(player);
		if (info.isAfk && player.isOnline()) {
			info.isAfk = false;
			String message = plugin.Config.format("messages.afk-return", "&8* &3{name}&8 is no longer afk *", "name", player.getName()); 
			player.getServer().broadcastMessage(message);
		}
	}

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
    	_afkPlayers.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void PlayerAction(PlayerToggleSneakEvent event) {
    	onPlayerAction(event.getPlayer());
    }
    
    @EventHandler
    public void PlayerAction(PlayerToggleSprintEvent event) {
    	onPlayerAction(event.getPlayer());
    }
    
    @EventHandler
    public void PlayerAction(PlayerTeleportEvent event) {
    	onPlayerAction(event.getPlayer());
    }

    @EventHandler
    public void PlayerAction(PlayerItemHeldEvent event) {
    	onPlayerAction(event.getPlayer());
    }
    
    @EventHandler
    public void PlayerAction(PlayerItemConsumeEvent event) {
    	onPlayerAction(event.getPlayer());
    }

    @EventHandler
    public void PlayerAction(InventoryOpenEvent event) {
    	HumanEntity e = event.getPlayer();
    	if (e instanceof Player)
    		onPlayerAction((Player)e);
    }

    @EventHandler
    public void PlayerAction(PlayerInteractEvent event) {
    	onPlayerAction(event.getPlayer());
    }
    
    @EventHandler
    public void PlayerAction(PlayerCommandPreprocessEvent event) {
    	onPlayerAction(event.getPlayer());
    }
    
    public void onPlayerAction(final Player player) {
    	try {
			AfkInfo info = getPlayer(player);
			info.lastAction = System.currentTimeMillis();
			info.lastPosition = player.getLocation();

			if (info.isAfk) {
				plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
					@Override
					public void run() {
						removeAfkFlag(player);
					}}, 10);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
    }
}

