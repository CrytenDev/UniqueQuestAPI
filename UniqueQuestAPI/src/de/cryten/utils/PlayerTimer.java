package de.cryten.utils;

import java.sql.ResultSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import de.cryten.sql.MySQLTables;
import de.cryten.tool.mysql.common.StatementResult;

public class PlayerTimer extends Thread {
	
	int day = 0;
	int hours = 0;
	int minutes = 0;
	
	public void run() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					playerCheck(p.getUniqueId());
					try(StatementResult statement = MySQLTables.playtime.get(new String[]{"UUID","DAY","HOURS","MINUTES"}, new String[]{"UUID"}, new Object[]{p.getUniqueId().toString()})){
						ResultSet rs = statement.receive();
						while(rs.next()) {
							day = rs.getInt("DAY");
							hours = rs.getInt("HOURS");
							minutes = rs.getInt("MINUTES");
							minutes++;
						}
					}catch(Exception e){}
					MySQLTables.playtime.update(new String[]{"MINUTES"}, new Object[]{minutes}, new String[]{"UUID"}, new Object[]{p.getUniqueId().toString()});
					if(minutes == 60) {
						MySQLTables.playtime.update(new String[]{"MINUTES"}, new Object[]{"0"}, new String[]{"UUID"}, new Object[]{p.getUniqueId().toString()});
						hours++;
						MySQLTables.playtime.update(new String[]{"HOURS"}, new Object[]{hours}, new String[]{"UUID"}, new Object[]{p.getUniqueId().toString()});
					}
					if(hours == 24) {
						MySQLTables.playtime.update(new String[]{"HOURS"}, new Object[]{"0"}, new String[]{"UUID"}, new Object[]{p.getUniqueId().toString()});
						day++;
						MySQLTables.playtime.update(new String[]{"DAY"}, new Object[]{day}, new String[]{"UUID"}, new Object[]{p.getUniqueId().toString()});
					}
				}
			}
		}, TimeUnit.MINUTES.toMillis(1), TimeUnit.MINUTES.toMillis(1));
	}
	
	public boolean playerExists(UUID uuid) {
		try(StatementResult statementResult = MySQLTables.playtime.get(new String[]{"UUID"}, new String[]{"UUID"}, new Object[]{uuid.toString()})) {
            ResultSet result = statementResult.receive();
            if(result.next() == false) {
            	return false;
            }else {
            	do {
            		return true;
            	}while(result.next());
            }
        } catch (Exception e) {}
		return false;
	}
	
	private void playerCheck(UUID uuid) {	
		if (!playerExists(uuid)) {
			createPlayer(uuid);
		}
	}
	private void createPlayer(UUID uuid) {
		try {
			MySQLTables.connectionPool.getConnection().createStatement().executeUpdate(
					"INSERT INTO Citytime "
					+ "(UUID, DAY, HOURS, MINUTES) "
					+ "VALUES "
					+ "('"+uuid.toString()+"', '0', '0', '0');");
		}catch(Exception e) {}
	}
}
