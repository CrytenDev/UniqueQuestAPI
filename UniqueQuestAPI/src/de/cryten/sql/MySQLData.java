package de.cryten.sql;

import de.cryten.MainClass;

public class MySQLData {
	
	public String getHost() {
		return MainClass.getInstance().getConfig().getString("MySQL.Host");
	}
	public String getDatabase() {
		return MainClass.getInstance().getConfig().getString("MySQL.Database");
	}
	public String getUsername() {
		return MainClass.getInstance().getConfig().getString("MySQL.Username");
	}
	public String getPassword() {
		return MainClass.getInstance().getConfig().getString("MySQL.Password");
	}
	public int getPort() {
		return MainClass.getInstance().getConfig().getInt("MySQL.Port");
	}
	public int getPoolsize() {
		return MainClass.getInstance().getConfig().getInt("MySQL.Poolsize");
	}
	public int getQuests() {
		return 3;
	}
}
