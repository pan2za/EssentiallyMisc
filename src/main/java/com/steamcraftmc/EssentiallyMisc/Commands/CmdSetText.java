package com.steamcraftmc.EssentiallyMisc.Commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.steamcraftmc.EssentiallyMisc.MainPlugin;

public class CmdSetText extends BaseCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdSetText(MainPlugin plugin) {
		super(plugin, "essentials.signs.edit", "settext", 1, 25);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) throws Exception {
		if (args.length < 1 || !args[0].matches("^[1234]$")) {
			return false;
		}
		int line = Integer.parseInt(args[0]);

		Set<Material> m = null;
		Block block = player.getTargetBlock(m, 5);
		
		if (block == null || (block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST)) {
			player.sendMessage(
					plugin.Config.get("messages.not-facing-sign", "&cYou must be facing a sign.")
				);
			return true;
		}
		
		Sign sign = (Sign)block.getState(); 
		StringBuilder sb = new StringBuilder();
		for (int ix=0; ix < args.length; ix++) {
			if (ix > 0) sb.append(' ');
			sb.append(args[ix]);
		}

        BlockPlaceEvent placeEvent = new BlockPlaceEvent(block, sign, block.getRelative(0, -1, 0), 
        		player.getInventory().getItemInMainHand(), player, true, EquipmentSlot.HAND);

        // don't call: plugin.getServer().getPluginManager().callEvent(placeEvent);
        if (!placeEvent.isCancelled() && placeEvent.canBuild()) {
	        String[] lines = sign.getLines();
	        lines[line - 1] = sb.toString();

	        SignChangeEvent chgEvent = new SignChangeEvent(block, player, lines);
            plugin.getServer().getPluginManager().callEvent(chgEvent);
        	if (!chgEvent.isCancelled()) {
        		for (int ixl=0; ixl < 4; ixl++)
        			sign.setLine(ixl, chgEvent.getLine(ixl));
    			sign.update();
    			player.sendMessage(
    					plugin.Config.get("messages.sign-edited", "&6Changed the sign text on line " + line + ".")
    				);
    			return true;
        	}
        }
		
		player.sendMessage(
				plugin.Config.get("messages.edit-cancel", "&cYou are unable to modify that sign.")
			);
		return true;
	}
	
}
