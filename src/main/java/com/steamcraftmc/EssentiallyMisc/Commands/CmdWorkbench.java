package com.steamcraftmc.EssentiallyMisc.Commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.steamcraftmc.EssentiallyMisc.MainPlugin;

public class CmdWorkbench extends BaseCommand {

	public CmdWorkbench(MainPlugin plugin) {
		super(plugin, "workbench", 0);
	}

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) {
        player.openWorkbench(null, true);
        return true;
	}
}
