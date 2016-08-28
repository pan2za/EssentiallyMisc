package com.steamcraftmc.EssentiallyMisc.Commands;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.steamcraftmc.EssentiallyMisc.MainPlugin;

public class CmdTime extends BaseCommand {
	
    public static final int ticksAtMidnight = 18000;
    public static final int ticksPerDay = 24000;
    public static final int ticksPerHour = 1000;
    public static final double ticksPerMinute = 1000d / 60d;
    public static final double ticksPerSecond = 1000d / 60d / 60d;
	final SimpleDateFormat SDFTwentyFour;
	
	public CmdTime(MainPlugin plugin) {
        super(plugin, "time", 0);
        
        SDFTwentyFour = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    	SDFTwentyFour.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

	@Override
	protected boolean doPlayerCommand(Player player, Command cmd, String commandLabel, String[] args) throws Exception {
		String time = formatDateFormat(player.getWorld().getTime(), SDFTwentyFour);
		player.sendMessage(plugin.Config.format("message.time", "&6The time is: &f{time}", "time", time));
		return true;
	}

    public static String formatDateFormat(final long ticks, final SimpleDateFormat format) {
        final Date date = ticksToDate(ticks);
        return format.format(date);
    }

    public static Date ticksToDate(long ticks) {
        // Assume the server time starts at 0. It would start on a day.
        // But we will simulate that the server started with 0 at midnight.
        ticks = ticks - ticksAtMidnight + ticksPerDay;

        // How many ingame days have passed since the server start?
        final long days = ticks / ticksPerDay;
        ticks -= days * ticksPerDay;

        // How many hours on the last day?
        final long hours = ticks / ticksPerHour;
        ticks -= hours * ticksPerHour;

        // How many minutes on the last day?
        final long minutes = (long) Math.floor(ticks / ticksPerMinute);
        final double dticks = ticks - minutes * ticksPerMinute;

        // How many seconds on the last day?
        final long seconds = (long) Math.floor(dticks / ticksPerSecond);

        // Now we create an english GMT calendar (We wan't no daylight savings)
        final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.ENGLISH);
        cal.setLenient(true);

        // And we set the time to 0! And append the time that passed!
        cal.set(0, Calendar.JANUARY, 1, 0, 0, 0);
        cal.add(Calendar.DAY_OF_YEAR, (int) days);
        cal.add(Calendar.HOUR_OF_DAY, (int) hours);
        cal.add(Calendar.MINUTE, (int) minutes);
        cal.add(Calendar.SECOND, (int) seconds + 1); // To solve rounding errors.

        return cal.getTime();
    }
}
