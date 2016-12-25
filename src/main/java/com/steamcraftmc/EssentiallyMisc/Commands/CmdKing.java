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

public class CmdKing extends BaseCommand {

	Map<String, ConfigurationSection> allKits;

	public CmdKing(MainPlugin plugin) {
		super(plugin, "king", 0);
	}

	@Override
	@SuppressWarnings("deprecation")
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) {
        

	      Integer times = Integer.valueOf(1);
	      
	      Inventory pi = player.getInventory();
	      pi.clear();
	      pi.addItem(new ItemStack[] { new ItemStack(Material.ARROW, 64) });
	      pi.addItem(new ItemStack[] { new ItemStack(Material.GLASS, 64) });
	      pi.addItem(new ItemStack[] { new ItemStack(Material.BOW) });
	      pi.addItem(new ItemStack[] { new ItemStack(Material.DIAMOND_PICKAXE) });
	      pi.addItem(new ItemStack[] { new ItemStack(Material.DIAMOND_SWORD) });
	      pi.addItem(new ItemStack[] { new ItemStack(Material.DIAMOND_HELMET) });
	      pi.addItem(new ItemStack[] { new ItemStack(Material.DIAMOND_BOOTS) });
	      pi.addItem(new ItemStack[] { new ItemStack(Material.DIAMOND_CHESTPLATE) });
	      pi.addItem(new ItemStack[] { new ItemStack(Material.DIAMOND_LEGGINGS) });
	      pi.addItem(new ItemStack[] { new ItemStack(Material.GOLDEN_APPLE, 64) });
	      player.sendMessage("Material added to you my king");
	      return true;
	}
}
