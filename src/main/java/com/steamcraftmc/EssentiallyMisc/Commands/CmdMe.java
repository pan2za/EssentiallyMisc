package com.steamcraftmc.EssentiallyMisc.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.steamcraftmc.EssentiallyMisc.MainPlugin;

public class CmdMe extends BaseCommand {
    public CmdMe(MainPlugin plugin) {
		super(plugin, "me", "essentials.me", 1, 255);
    }

    @Override
    protected boolean doPlayerCommand(Player sender, Command cmd, String commandLabel, String[] args) {
        if (!sender.hasPermission(this.permission))
        	return true;
        if (args.length < 1)  {
            sender.sendMessage(ChatColor.RED + "Usage: /me <action>");
            return false;
        }

        StringBuilder message = new StringBuilder();
        message.append(ChatColor.DARK_GRAY);
        message.append("* ");
        message.append(sender.getDisplayName());

        for (String arg : args) {
            message.append(" ");
            message.append(arg);
        }

        message.append(ChatColor.DARK_GRAY);
        message.append(" *");

        sender.getServer().dispatchCommand(sender.getServer().getConsoleSender(), "broadcast " + message);

        return true;
    }
}
