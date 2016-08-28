package com.steamcraftmc.EssentiallyMisc.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import com.steamcraftmc.EssentiallyMisc.MainPlugin;

public class CmdFeed extends BaseCommand {

	public CmdFeed(MainPlugin plugin) {
		super(plugin, "feed", 0, 1);
	}

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) {

		Player target = player;
        if (args.length > 0) {
        	if (!player.hasPermission("essentials.heal.others")) {
        		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4You do not have permission to this command."));
	            return true;
        	}
        	target = player.getServer().getPlayer(args[0]);
        	if (target == null) {
            	player.sendMessage(plugin.Config.format("message.player-not-found", "&cPlayer not found."));
                return true;
        	}
        }

        if (target.getHealth() == 0) {
    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Unable to feed the dead."));
            return true;
        }

        feedPlayer(target);
        return true;
	}
	
	public void feedPlayer(Player player) {
        player.setFoodLevel(20);
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You have been satiated."));
    	return;
	}

}
