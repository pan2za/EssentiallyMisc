package com.steamcraftmc.EssentiallyMisc.Commands;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.steamcraftmc.EssentiallyMisc.MainPlugin;

public class CmdGetblock extends BaseCommand {

	Map<String, ConfigurationSection> allKits;

	public CmdGetblock(MainPlugin plugin) {
		super(plugin, "get", 1, 2);
	}

	@Override
	@SuppressWarnings("deprecation")
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] split) {
        

	      Integer times = Integer.valueOf(1);
	      if (split.length == 2)
	      {
	        try
	        {
	          times = Integer.valueOf(Integer.parseInt(split[1]));
	        }
	        catch (Exception localException) {}
	        if (times.intValue() < 0) {
	          times = Integer.valueOf(1);
	        }
	      }
	      if ((split.length == 1) || (split.length == 2))
	      {
	        String cur = split[0];
	        boolean found = false;
	        Material[] arrayOfMaterial;
	        int j = (arrayOfMaterial = Material.values()).length;

	        for (int ai = 0; ai < j; ai++)
	        {
	          Material c = arrayOfMaterial[ai];
	          if (c.toString().toLowerCase().contains(cur.toLowerCase()))
	          {
	            found = true;
	            for (int ki = 0; ki < times.intValue(); ki++) {
	              player.getInventory().addItem(new ItemStack[] { new ItemStack(c.getId(), 1) });
	            }
	            player.sendMessage("Material added maybe: " + c.toString() + " with id " + String.valueOf(c.getId()));
	          }
	        }
	        if (!found) {
	          try
	          {
	            Integer x = Integer.valueOf(Integer.parseInt(split[0]));
	            Material[] all = Material.values();
	            int k = all.length;
	            for (j = 0; j < k; j++)
	            {
	              Material c = all[j];
	              if (c.getId() == x.intValue())
	              {
	                found = true;
	                for (int i = 0; i < times.intValue(); i++) {
	                  player.getInventory().addItem(new ItemStack[] { new ItemStack(c.getId(), 1) });
	                }
	                player.sendMessage("Material added maybe: " + c.toString() + " with id " + String.valueOf(c.getId()));
	              }
	            }
	          }
	          catch (Exception localException1) {}
	        }
	        return true;
	      }
	      return false;
	}
}
