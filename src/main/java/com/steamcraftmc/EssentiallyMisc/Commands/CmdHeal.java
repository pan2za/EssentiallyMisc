package com.steamcraftmc.EssentiallyMisc.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.potion.PotionEffect;

import com.steamcraftmc.EssentiallyMisc.MainPlugin;

public class CmdHeal extends BaseCommand {

	public CmdHeal(MainPlugin plugin) {
		super(plugin, "heal", 0, 1);
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
            	player.sendMessage(plugin.Config.format("message.player-not-found", "&cPlayer not found."));
                return true;
        	}
        }

        if (target.getHealth() == 0) {
    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Unable to return the dead to life."));
            return true;
        }

        healPlayer(target);
        return true;
	}
	
	public void healPlayer(Player player) {
        
        final double amount = player.getMaxHealth() - player.getHealth();
        final EntityRegainHealthEvent erhe = new EntityRegainHealthEvent(player, amount, RegainReason.CUSTOM);
        Bukkit.getPluginManager().callEvent(erhe);
        if (erhe.isCancelled()) {
    		return;
        }

        double newAmount = player.getHealth() + erhe.getAmount();
        if (newAmount > player.getMaxHealth()) {
            newAmount = player.getMaxHealth();
        }

        player.setHealth(newAmount);
        player.setFoodLevel(20);
        player.setFireTicks(0);
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You have been healed."));
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    	return;
	}

}
