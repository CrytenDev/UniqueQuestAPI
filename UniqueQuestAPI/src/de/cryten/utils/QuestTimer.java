package de.cryten.utils;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import de.cryten.sql.MySQLTables;
import de.cryten.sql.MySQL;
import de.cryten.sql.MySQLData;
import de.cryten.tool.mysql.common.StatementResult;

public class QuestTimer extends Thread{
	
	ArrayList<Integer> ids = new ArrayList<>();
	Timer timer = new Timer();
	Timer idclear = new Timer();
	Timer timerreset = new Timer();
	MySQLData data = new MySQLData();
	MySQL mysql = new MySQL();
	RandomManager rnd = new RandomManager();
	ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	private int number;
	
	public QuestTimer() {
		this.number = data.getQuests();
	}
	
	public void run() {
		console.sendMessage("§aQuestTimer Thread started!");
		timerreset.schedule(new TimerTask() {
			@Override
			public void run() {
				
				Date date = new Date();
				SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
				String formattedDate = df.format(date);

				if(formattedDate.equals("06:00:00")) {
					resetDailyQuest();
				}
			}
		}, TimeUnit.SECONDS.toMillis(1), TimeUnit.SECONDS.toMillis(1));

	}
	
	public void listClear() {
		idclear.schedule(new TimerTask() {
			
			@Override
			public void run() {
				ids.clear();
			}
		}, TimeUnit.SECONDS.toMillis(1));
	}
	
	public void resetDailyQuest() {
		console.sendMessage("§c====================================");
		console.sendMessage("§c= §2DailyQuest Reset wird gestartet! §c=");
		console.sendMessage("§c====================================");
		console.sendMessage("");
		console.sendMessage("§aSetze alle aktiven Quests auf State: §e2");

		try {
			MySQLTables.quests.update(new String[]{"STATE"}, new Object[]{"2"}, new String[]{"STATE"}, new Object[]{"1"});
		}catch(Exception e){
			MySQLTables.quests.update(new String[]{"STATE"}, new Object[]{"2"}, new String[]{"STATE"}, new Object[]{"1"});
		}

		timer.schedule(new TimerTask() {
			@Override
			public void run() {	
				try(StatementResult statementResult = MySQLTables.quests.get(new String[]{"QUESTID"}, new String[]{"STATE"}, new Object[]{"0"})) {
		            ResultSet result = statementResult.receive();
		            while(result.next()) {
		            	ids.add(result.getInt("QUESTID"));
		            }
		        } catch (Exception e) {
		        	System.out.println("MySQL Fehler: Hole Quests: " + e.getMessage());
		        }
				
				console.sendMessage("§aQuests in der Liste: §e" + ids.size());
				for(int i = 0; i < number; i++) {
					int index = rnd.generatedRandomInt(i, ids.size());
					int quest = ids.get(index);
					
					console.sendMessage("§aAktiviere QuestID: §e" + quest);
					MySQLTables.quests.update(new String[]{"STATE"}, new Object[]{"1"}, new String[]{"QUESTID"}, new Object[]{quest});
					ids.remove(index);
				}
				
				try {
					MySQLTables.questtracker.delete();
					MySQLTables.quests.update(new String[]{"STATE"}, new Object[]{"0"}, new String[]{"STATE"}, new Object[]{"2"});
				}catch(Exception e){
					System.out.println("MySQL Fehler: Questtracker, Quests von 2 auf 0: " + e.getMessage());
				}
				listClear();
				if(Bukkit.getOnlinePlayers().size() > 0) {
					for(Player p : Bukkit.getOnlinePlayers()){
						mysql.getQuests(p, p.getUniqueId());
					}
				}
				console.sendMessage("");
				console.sendMessage("§c===================================");
				console.sendMessage("§c= §2DailyQuest Reset abgeschlossen! §c=");
				console.sendMessage("§c===================================");
			}
			
		}, TimeUnit.SECONDS.toMillis(3));	
	}
}
