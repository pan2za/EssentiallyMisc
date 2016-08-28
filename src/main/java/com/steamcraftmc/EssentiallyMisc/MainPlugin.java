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
	public com.steamcraftmc.EssentiallyMisc.Commands.CmdFireball fb;

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
        new com.steamcraftmc.EssentiallyMisc.Commands.CmdHeal(this);
        new com.steamcraftmc.EssentiallyMisc.Commands.CmdFixLight(this);
        new com.steamcraftmc.EssentiallyMisc.Commands.CmdFly(this);
        new com.steamcraftmc.EssentiallyMisc.Commands.CmdGameMode(this);
        new com.steamcraftmc.EssentiallyMisc.Commands.CmdSetText(this);
        afk = new com.steamcraftmc.EssentiallyMisc.Commands.CmdAfk(this);
        new com.steamcraftmc.EssentiallyMisc.Commands.CmdBurn(this);
        new com.steamcraftmc.EssentiallyMisc.Commands.CmdLightning(this);
        fb = new com.steamcraftmc.EssentiallyMisc.Commands.CmdFireball(this);
        
    	_listener = new WorldEvents(this);
        getServer().getPluginManager().registerEvents(_listener, this);
        afk.start();
        fb.start();
        log(Level.INFO, "Plugin listening for events.");
    }

    @Override
    public void onDisable() {
        afk.stop();
        fb.stop();
    	HandlerList.unregisterAll(_listener);
    }

}
