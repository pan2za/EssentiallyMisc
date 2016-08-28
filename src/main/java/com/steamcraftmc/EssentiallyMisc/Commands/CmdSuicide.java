package com.steamcraftmc.EssentiallyMisc.Commands;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import com.steamcraftmc.EssentiallyMisc.MainPlugin;

public class CmdSuicide extends BaseCommand {
    public CmdSuicide(MainPlugin plugin) {
        super(plugin, "suicide");
    }

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) throws Exception {
        player.getServer().broadcastMessage(plugin.Config.format("messages.suicide", "&3{name}&f tragically committed sucide today, full story at 11:00.", "name", player.getName()));

        @SuppressWarnings("deprecation")
		EntityDamageEvent ede = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, Short.MAX_VALUE);
        player.getServer().getPluginManager().callEvent(ede);
        player.setLastDamageCause(ede);

        player.damage(Short.MAX_VALUE);
        if (player.getHealth() > 0) {
        	player.setHealth(0);
        }
        return true;
	}
}
