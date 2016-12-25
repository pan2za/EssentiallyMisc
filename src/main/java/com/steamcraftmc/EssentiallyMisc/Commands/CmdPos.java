package com.steamcraftmc.EssentiallyMisc.Commands;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.steamcraftmc.EssentiallyMisc.MainPlugin;

public class CmdPos extends BaseCommand {

	Map<String, ConfigurationSection> allKits;

	public CmdPos(MainPlugin plugin) {
		super(plugin, "pos", 0, 3);
	}

	@Override
	@SuppressWarnings("deprecation")
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] split) {

	      if (split.length == 0)
	      {
	        Location location = player.getLocation();
	        player.sendMessage("You are currently at " + location.getX() + "," + location.getY() + "," + location.getZ() + 
	          " with " + location.getYaw() + " yaw and " + location.getPitch() + " pitch");
	        return true;
	      }
	      if (split.length == 3)
	      {
	        try
	        {
	          double x = Double.parseDouble(split[0]);
	          double y = Double.parseDouble(split[1]);
	          double z = Double.parseDouble(split[2]);
	          
	          player.teleport(new Location(player.getWorld(), x, y, z));
	        }
	        catch (NumberFormatException ex)
	        {
	          player.sendMessage("Given location is invalid");
	        }
	        return true;
	      }
	      return false;
	}
}
