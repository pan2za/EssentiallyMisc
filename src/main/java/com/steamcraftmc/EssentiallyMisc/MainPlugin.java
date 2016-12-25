package com.steamcraftmc.EssentiallyMisc;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
 
public class MainPlugin extends JavaPlugin {
	public final   Logger  _logger;
	private WorldEvents _listener;
	public Boolean _exLogging;
	public final MainConfig Config;
	public com.steamcraftmc.EssentiallyMisc.Commands.CmdAfk afk;
	public com.steamcraftmc.EssentiallyMisc.Commands.CmdRecipe recipe;

	public MainPlugin() {
		_exLogging = true;
		_logger = getLogger();
		_logger.setLevel(Level.ALL);
		_logger.log(Level.CONFIG, "Plugin initializing...");
		
		Config = new MainConfig(this);
		Config.load();
	}

	public void log(Level level, String text) {
		_logger.log(Level.INFO, text);
	}

    @Override
    public void onEnable() {
        new com.steamcraftmc.EssentiallyMisc.Commands.CmdSetText(this);
        afk = new com.steamcraftmc.EssentiallyMisc.Commands.CmdAfk(this);
        new com.steamcraftmc.EssentiallyMisc.Commands.CmdMe(this);
        new com.steamcraftmc.EssentiallyMisc.Commands.CmdHat(this);
        new com.steamcraftmc.EssentiallyMisc.Commands.CmdKing(this);
        new com.steamcraftmc.EssentiallyMisc.Commands.CmdPos(this);
        new com.steamcraftmc.EssentiallyMisc.Commands.CmdListitems(this);
        new com.steamcraftmc.EssentiallyMisc.Commands.CmdGetblock(this);
        new com.steamcraftmc.EssentiallyMisc.Commands.CmdNear(this);
        new com.steamcraftmc.EssentiallyMisc.Commands.CmdSuicide(this);
        recipe = new com.steamcraftmc.EssentiallyMisc.Commands.CmdRecipe(this);
        new com.steamcraftmc.EssentiallyMisc.Commands.CmdEnderChest(this);
        new com.steamcraftmc.EssentiallyMisc.Commands.CmdWorkbench(this);
                
    	_listener = new WorldEvents(this);
        getServer().getPluginManager().registerEvents(_listener, this);
        afk.start();
        recipe.start();
        log(Level.INFO, "Plugin listening for events.");
    }

    @Override
    public void onDisable() {
        recipe.stop();
        afk.stop();
    	HandlerList.unregisterAll(_listener);
    }

}
