package com.steamcraftmc.EssentiallyMisc.Commands;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.steamcraftmc.EssentiallyMisc.MainPlugin;

public class CmdHat extends BaseCommand {

	Map<String, ConfigurationSection> allKits;

	public CmdHat(MainPlugin plugin) {
		super(plugin, "hat", 0);
	}

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) {
        
		if (player.getItemInHand().getType() != Material.AIR) {
            final ItemStack hand = player.getItemInHand();
            if (hand.getType().getMaxDurability() == 0) {
                final PlayerInventory inv = player.getInventory();
                final ItemStack head = inv.getHelmet();
                inv.setHelmet(hand);
                inv.setItemInHand(head);
                player.sendMessage(plugin.Config.get("messages.hat", "&6Enjoy you new digs!"));
                return true;
            }
        }
		
        player.sendMessage(plugin.Config.get("messages.hat-invalid", "&6Unable to use that for a hat."));
        return true;
	}
}
