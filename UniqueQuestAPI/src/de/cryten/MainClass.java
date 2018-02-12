package de.cryten;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import de.cryten.command.QuestInvCommand;
import de.cryten.command.QuestPlaytimeCommand;
import de.cryten.command.QuestTimerCommand;
import de.cryten.listener.QuestListeners;
import de.cryten.sql.MySQLTables;
import de.cryten.utils.ClassPath;
import de.cryten.utils.PlayerTimer;
import de.cryten.utils.QuestTimer;

public class MainClass extends JavaPlugin {
	
	ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	private static MainClass instance;
	ClassPath cp = new ClassPath();
	QuestTimer qs = new QuestTimer();
	
	PlayerTimer ptimer = new PlayerTimer();
	File file = new File(getDataFolder(), "config.yml");
	
	public static MainClass getInstance() {
		return instance;
	}
	
	public void onEnable() {
		instance = this;
		if (!file.exists()) {
		    getLogger().info("config.yml not found, creating!");
		    saveDefaultConfig();
		} else {
		    getLogger().info("config.yml found, loading!");
		}
		cp.start();
		Bukkit.getPluginManager().registerEvents(new QuestListeners(), this);
		this.getCommand("quest").setExecutor(new QuestInvCommand());
		this.getCommand("questtimer").setExecutor(new QuestTimerCommand());
		this.getCommand("playtime").setExecutor(new QuestPlaytimeCommand());
		MySQLTables.connectMySQL();
		MySQLTables.loadTable();
		qs.start();
		ptimer.start();
	}
	
	public void onDisable() {
		MySQLTables.connectionPool.close();
		cp.interrupt();
		qs.interrupt();
	}
}
