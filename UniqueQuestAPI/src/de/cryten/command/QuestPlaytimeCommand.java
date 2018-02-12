package de.cryten.command;

import java.sql.ResultSet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.cryten.sql.MySQLTables;
import de.cryten.tool.mysql.common.StatementResult;

public class QuestPlaytimeCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		try(StatementResult statement = MySQLTables.playtime.get(new String[]{"DAY","HOURS","MINUTES"}, new String[]{"UUID"}, new Object[]{p.getUniqueId().toString()})){
			ResultSet rs = statement.receive();
			if(rs.next()) {
				p.sendMessage("§aDeine CityBuild Spielzeit: §e" 
						+ rs.getInt("DAY") + " §a"+getDayString(rs.getInt("DAY"))+", §e" 
						+ rs.getInt("HOURS") + " §a"+getHoursString(rs.getInt("HOURS"))+", §e" 
						+ rs.getInt("MINUTES") + " §a"+getMinutesString(rs.getInt("MINUTES"))+".");
			}else {
				p.sendMessage("§2Nach keine Daten verfügbar. Warte noch einige Minuten!");
			}
		}catch(Exception e) {}
		return false;
	}
	
	private String getDayString(int day) {
		String s = "";
		if(day == 1) { s = "Tag"; }else {	s = "Tage"; }
		return s;
	}
	private String getHoursString(int hours) {
		String s = "";
		if(hours == 1) { s = "Stunde"; }else { s = "Stunden"; }
		return s;
	}
	private String getMinutesString(int minutes) {
		String s = "";
		if(minutes == 1) { s = "Minute"; }else { s = "Minuten"; }
		return s;
	}
}
