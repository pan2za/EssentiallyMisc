package com.steamcraftmc.EssentiallyMisc.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import com.steamcraftmc.EssentiallyMisc.MainPlugin;

public class CmdSetText extends BaseCommand implements TabCompleter
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdSetText(MainPlugin plugin) {
		super(plugin, "essentials.signs.edit", "edit", 1, 25);
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
		for (int ix = 1; ix < args.length; ix++) {
			if (ix > 1) sb.append(' ');
			sb.append(args[ix]);
		}

		//Check build permissions directly below sign so as not to break signshop, unless they
		//are changing the first line, which will break it anyway...
		Block breaker = block;
		if (line > 1) {
			while (breaker.getType() == Material.WALL_SIGN || breaker.getType() == Material.SIGN_POST) {
				breaker = breaker.getRelative(0, 1, 0);
			}
		}
		
		BlockBreakEvent placeEvent = new BlockBreakEvent(breaker, player);
        plugin.getServer().getPluginManager().callEvent(placeEvent);
        if (!placeEvent.isCancelled()) {
	        String[] lines = sign.getLines();
	        lines[line - 1] = sb.toString();

	        SignChangeEvent chgEvent = new SignChangeEvent(block, player, lines);
            plugin.getServer().getPluginManager().callEvent(chgEvent);
        	if (!chgEvent.isCancelled()) {
        		for (int ixl=0; ixl < 4; ixl++)
        			sign.setLine(ixl, chgEvent.getLine(ixl));
    			sign.update();
    			player.sendMessage(
    					plugin.Config.format("messages.sign-changed", "&6Changed the sign text on line {line} to {text}.",
    							"line", args[0], "text", sb.toString())
    				);
    			return true;
        	}
        }
		
		player.sendMessage(
				plugin.Config.get("messages.edit-cancel", "&cYou are unable to modify that sign.")
			);
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		ArrayList<String> response = new ArrayList<String>();
		if (!(sender instanceof Player) || args.length == 0)
			return response;
		
		if (args.length == 2 && args[1].length() == 0) {
			args = new String[] { args[0] };
		}
		if (args.length != 1 || !args[0].matches("^[1234]\\s*$")) {
			return response;
		} 
		int line = Integer.parseInt(args[0].trim());

		Player player = (Player)sender;
		Set<Material> m = null;
		Block block = player.getTargetBlock(m, 5);
		
		if (block == null || (block.getType() != Material.WALL_SIGN && block.getType() != Material.SIGN_POST)) {
			return response;
		}
		
		try {
			Sign sign = (Sign)block.getState(); 
			response.add(args[0].trim() + " " + sign.getLine(line - 1).replace(ChatColor.COLOR_CHAR, '&'));
			return response;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return response;
		}
	}
	
}
