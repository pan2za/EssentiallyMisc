package com.steamcraftmc.EssentiallyMisc.Commands;

import java.util.*;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import com.steamcraftmc.EssentiallyMisc.MainPlugin;

public class CmdNear extends BaseCommand {
    public CmdNear(MainPlugin plugin) {
        super(plugin, "near", 0, 1);
    }


	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) throws Exception {
        long maxRadius = plugin.Config.getInt("near.maxradius", 100);
        long radius = plugin.Config.getInt("near.default", 25);

        if (args.length > 0 && args[0].matches("^\\d+$")) {
            radius = Math.min(maxRadius, Integer.parseInt(args[0]));
        }

        radius = Math.abs(radius);
        long radiusSquared = radius * (long)radius;
        Location playerLoc = player.getLocation();
        List<Entity> all = player.getNearbyEntities(radius, radius, radius);
        HashMap<EntityType, Integer> counts = new HashMap<EntityType, Integer>(); 

        StringBuilder sb = new StringBuilder();
        
        for (Entity e : all) {
        	EntityType etype = e.getType();
        	if (etype == EntityType.PLAYER) {
        		if (player.hasPermission("essentials.near.players") 
        			&& e instanceof Player && player.canSee((Player)e)) {
        			
                    final long delta = (long) playerLoc.distanceSquared(e.getLocation());
                    if (delta < radiusSquared) {
                    	if (sb.length() == 0) {
                            sb.append(plugin.Config.get("messages.near-player-prefix", "&6Nearby players:"));
                    	}
                		sb.append("\n");
                    	sb.append(
                    			plugin.Config.format("messages.near-player", "&3{name}&6 - (&f{distance}m&6)",
                    					"name", e.getName(), "distance", (long)Math.sqrt(delta))
                			);
                    }
        		}
        	}
        	else if (etype.isAlive()) {
        		boolean passive = isPassive(etype);
    			if ((!passive && player.hasPermission("essentials.near.hostile-mobs"))
					|| (passive && player.hasPermission("essentials.near.passive-mobs"))
					) {
	        		if (counts.containsKey(etype)) {
	        			counts.put(etype, 1 + counts.get(etype));        			
	        		} else {
	        			counts.put(etype, 1);
	        		}
	        	}
        	}
        }
        
        if (counts.size() > 0) {
            sb.append(plugin.Config.get("messages.near-mob-prefix", "&6Nearby Mobs:"));
        	for (EntityType etype : counts.keySet()) {
        		sb.append("\n");
            	sb.append(
            			plugin.Config.format("messages.near-mob", "&2{name}&6 - (&f{count}&6)",
            					"name", etype.toString().replaceAll("_", " "), "count", counts.get(etype))
        			);
        	}
        }
        
        player.sendMessage(sb.toString());
        return true;
    }

	private boolean isPassive(EntityType etype) {
		switch(etype) {
		case CHICKEN:
		case COW:
		case HORSE:
		case MUSHROOM_COW:
		case OCELOT:
		case PIG:
		case RABBIT:
		case SHEEP:
		case SQUID:
		case VILLAGER:
		case WOLF:
			return true;
		default:
			return false;
		}
	}
}
