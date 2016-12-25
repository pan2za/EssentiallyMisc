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

public class CmdListitems extends BaseCommand {

	Map<String, ConfigurationSection> allKits;

	public CmdListitems(MainPlugin plugin) {
		super(plugin, "listit", 0, 1);
	}

	@Override
	@SuppressWarnings("deprecation")
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] split) {

	      int j;
	      if (split.length == 0)
	      {
	        String things = "";
	        Material[] arrayOfMaterial1;
	        j = (arrayOfMaterial1 = Material.values()).length;
	        for (int i = 0; i < j; i++)
	        {
	          Material c = arrayOfMaterial1[i];
	          things = things + c.toString();
	          things = things + "\r\n";
	        }
	        player.sendMessage("Material lists:\r\n" + things);
	        return true;
	      }
	      if (split.length == 1)
	      {
	        String cur = split[0];
	        boolean found = false;
	        Material[] arrayOfMaterial2;
	        int k = (arrayOfMaterial2 = Material.values()).length;
	        for (j = 0; j < k; j++)
	        {
	          Material c = arrayOfMaterial2[j];
	          if (c.toString().toLowerCase().contains(cur.toLowerCase()))
	          {
	            found = true;
	            player.sendMessage("Material maybe: " + c.toString() + "with id " + String.valueOf(c.getId()));
	          }
	        }
	        if (!found) {
	          try
	          {
	            Integer x = Integer.valueOf(Integer.parseInt(split[0]));
	            Material[] arrayOfMaterial3;
	            int m = (arrayOfMaterial3 = Material.values()).length;
	            for (k = 0; k < m; k++)
	            {
	              Material c = arrayOfMaterial3[k];
	              if (c.getId() == x.intValue())
	              {
	                found = true;
	                player.sendMessage("Material maybe: " + c.toString() + "with id " + String.valueOf(c.getId()));
	              }
	            }
	          }
	          catch (Exception localException1) {}
	        }
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
