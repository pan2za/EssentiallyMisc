package com.steamcraftmc.EssentiallyMisc.Commands;

import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import com.steamcraftmc.EssentiallyMisc.MainPlugin;

public class CmdGameMode extends BaseCommand {

	Map<String, ConfigurationSection> allKits;

	public CmdGameMode(MainPlugin plugin) {
		super(plugin, "essentials.gamemode", "gamemode", 0, 1);
	}

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String[] args) {
		return false;
	}

}
