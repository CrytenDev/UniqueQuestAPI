package de.cryten;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.cryten.command.QuestCommand;
import de.cryten.listener.QuestListeners;
import de.cryten.sql.MySQL;
import de.cryten.utils.ClassPath;

public class MainClass extends JavaPlugin {
	
	ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	private static MainClass instance;

	public static MainClass getInstance() {
		return instance;
	}
	
	public void onEnable() {
		instance = this;
		ClassPath.checkLibs();
		MySQL.connectMySQL();
		Bukkit.getPluginManager().registerEvents(new QuestListeners(), this);
		this.saveDefaultConfig();
		this.getCommand("quest").setExecutor(new QuestCommand());

	}
	
	public void onDisable() {
		MySQL.connectionPool.close();
	}
}
