package com.steamcraftmc.EssentiallyMisc.Commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import com.steamcraftmc.EssentiallyMisc.MainPlugin;

public class CmdEnderChest extends BaseCommand {

	public CmdEnderChest(MainPlugin plugin) {
		super(plugin, "enderchest", 0);
	}

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) {
        player.openInventory(player.getEnderChest());
        return true;
	}
}

