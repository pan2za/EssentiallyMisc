package com.steamcraftmc.EssentiallyMisc.Commands;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import com.steamcraftmc.EssentiallyMisc.MainPlugin;

public class CmdFly extends BaseCommand {

	Map<String, ConfigurationSection> allKits;

	public CmdFly(MainPlugin plugin) {
		super(plugin, "fly", 0, 1);
	}

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) {
		Player target = player;
        if (args.length > 0) {
        	if (!player.hasPermission("essentials.heal.others")) {
        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4You do not have permission to this command."));
	            return true;
        	}
        	target = Bukkit.getPlayer(args[0]);
        	if (target == null) {
        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Unable to locate player '" + args[0] + "'."));
                return true;
        	}
        }

        if (target.getHealth() == 0) {
    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Unable to make the dead fly."));
            return true;
        }

        toggleFly(target);
        return true;
	}
	
	void toggleFly(Player player) {

        boolean enabled = !player.getAllowFlight();

        player.setFallDistance(0f);
        player.setAllowFlight(enabled);

        if (!player.getAllowFlight()) {
        	player.setFlying(false);
        }

        if (enabled) {
        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You have been given the gift of flight!"));
        }
        else {
        	player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Flight is no longer within your power."));
        }
	}

}
