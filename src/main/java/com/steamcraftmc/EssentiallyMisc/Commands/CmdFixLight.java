package com.steamcraftmc.EssentiallyMisc.Commands;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.steamcraftmc.EssentiallyMisc.MainPlugin;
import com.steamcraftmc.EssentiallyMisc.utils.ChunkWrap;

public class CmdFixLight extends BaseCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFixLight(MainPlugin plugin) {
		super(plugin, "essentials.fixlight", "fixlight", 1, 1);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) throws Exception {
		
		if (args.length == 0) {
			return false;
		}
		
		// Args
		int radius = Integer.parseInt(args[0]);
		if (radius < 0)
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Radius may not be a negative value."));
			return true;
		}
		if (radius > plugin.Config.getInt("lighting.maxradius", 16))
		{
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&4The maxium radius allowed is &6%d&4.", plugin.Config.getInt("lighting.maxradius", 16))));
			return true;
		}
		
		Chunk origin = player.getLocation().getChunk();
		int originX = origin.getX();
		int originZ = origin.getZ();
		World world = player.getWorld();
		
		int side = (1 + radius * 2);
		int target = side * side;
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Chunks around you will now be relighted."));
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&6Radius &7%d&6, Side &7%d&6, Chunks &7%d&6.", radius, side, target)));
		
		// Apply
		for (int deltaX = -radius; deltaX <= radius; deltaX++)
		{
			for (int deltaZ = -radius; deltaZ <= radius; deltaZ++)
			{
				int x = originX + deltaX;
				int z = originZ + deltaZ;
				new ChunkWrap(world, x, z).recalcLightLevel();
			}	
		}
		
		// Post Inform
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Chunk relight complete."));
		return true;
	}
	
}
