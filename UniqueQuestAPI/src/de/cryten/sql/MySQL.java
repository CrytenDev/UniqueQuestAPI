package de.cryten.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import de.cryten.MainClass;
import de.cryten.tool.mysql.ConnectionPool;
import de.cryten.tool.mysql.common.StatementResult;

public class MySQL {
	
	public ArrayList<Integer> questid = new ArrayList<>();
	public static ConnectionPool connectionPool;
	private static String host = MainClass.getInstance().getConfig().getString("MySQL.Host");
	private static String database = MainClass.getInstance().getConfig().getString("MySQL.Database");
	private static String username = MainClass.getInstance().getConfig().getString("MySQL.Username");
	private static String password = MainClass.getInstance().getConfig().getString("MySQL.Password");
	private static int port = MainClass.getInstance().getConfig().getInt("MySQL.Port");
	private static int poolsize = MainClass.getInstance().getConfig().getInt("MySQL.Poolsize");
	
    /**
     * Connect HikariCP MySQL.
     */
	public static void connectMySQL() {
		connectionPool = new ConnectionPool(host, port, database, username, password);
        connectionPool.setMaxPoolSize(poolsize);
        connectionPool.open();
        LoadMySQLTables.loadTable();
	}
	
	public static boolean playerExists(UUID uuid, int questid) {
		try(StatementResult statementResult = LoadMySQLTables.questtracker.get(new String[]{"UUID"}, new String[]{"UUID", "QUESTID"}, new Object[]{uuid.toString(), questid})) {
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
	
	private static void createPlayer(int questid, String name, UUID uuid) {
		try {
			MySQL.connectionPool.getConnection().createStatement().executeUpdate(
					"INSERT INTO Questtracker VALUES (NULL, '"+questid+"', '"+name+"', '"+uuid.toString()+"', '0', '0', '0', '0')");
		}catch(SQLException e) {
			Bukkit.broadcastMessage("Fehler: " + e.getMessage());
		}
	}
	
	private static void playerCheck(int questid, Player p, UUID uuid) {	
		if (!playerExists(uuid, questid)) {
			createPlayer(questid, p.getName() , uuid);
		}
	}
	
	public static void getQuests(Player p, UUID uuid) {
		int id = 0;
		try(StatementResult statementResult = LoadMySQLTables.quests.get(new String[]{"QUESTID"}, new String[]{"STATE"}, new Object[]{"1"})) {
            ResultSet result = statementResult.receive();
            
            if(result.next() == true) {
            	do {
            		id = result.getInt("QUESTID");
            		playerCheck(id, p, uuid);
            	}while(result.next());
            }
        } catch (Exception e) {}
	}
	
	public void addTokens(UUID uuid, int amount) {
		int token = getTokens(uuid);
		token += amount;	
		LoadMySQLTables.tokens.update(new String[]{"TOKENS"}, new Object[]{token}, new String[]{"UUID"}, new Object[]{uuid.toString()});
	}
	
	public int getTokens(UUID uuid) {
		
		int tokens = 0;

		try(StatementResult statementResult = LoadMySQLTables.tokens.get(new String[]{"TOKENS"}, new String[]{"UUID"}, new Object[]{uuid.toString()})) {
            ResultSet result = statementResult.receive();

            while(result.next()) {
            	tokens = result.getInt("TOKENS");
            }
        } catch (Exception e) {}
		return tokens;
	}
	
	public int getReward(int questid) {

		int tokens = 0;
		
		try(StatementResult statementResult = LoadMySQLTables.quests.get(new String[]{"REWARD"}, new String[]{"QUESTID"}, new Object[]{questid})) {
            ResultSet result = statementResult.receive();

            while(result.next()) {
            	tokens = result.getInt("REWARD");
            }
        } catch (Exception e) {}
		
		return tokens;
	}
	
	public int getItemID(int questid) {
		
		int itemid = 0;

		try(StatementResult statementResult = LoadMySQLTables.quests.get(new String[]{"ITEMID"}, new String[]{"QUESTID"}, new Object[]{questid})) {
            ResultSet result = statementResult.receive();

            while(result.next()) {
            	itemid = result.getInt("ITEMID");
            }
        } catch (Exception e) {}
		
		return itemid;
	}
	
	public void getQuestID(UUID uuid) {
		
		try(StatementResult statementResult = LoadMySQLTables.questtracker.get(new String[]{"QUESTID"}, new String[]{"UUID"}, new Object[]{uuid.toString()})) {
            ResultSet result = statementResult.receive();

            while(result.next()) {
            	questid.add(result.getInt("QUESTID"));
            }
        } catch (Exception e) {}
	}
	
	public int getProgress(UUID uuid, int questid) {
		int progress = 0;
		
		try(StatementResult statementResult = LoadMySQLTables.questtracker.get(new String[]{"QUESTTRACKER"}, new String[]{"UUID", "QUESTID"}, new Object[]{uuid.toString(), questid})) {
            ResultSet result = statementResult.receive();

            while(result.next()) {
            	progress = result.getInt("QUESTTRACKER");
            }
        } catch (Exception e) {}

		return progress;
	}
	
	public int getValue(int questid) {
		int value = 0;
		
		try(StatementResult statementResult = LoadMySQLTables.quests.get(new String[]{"VALUE"}, new String[]{"QUESTID"}, new Object[]{questid})) {
            ResultSet result = statementResult.receive();

            while(result.next()) {
            	value = result.getInt("VALUE");
            }
        } catch (Exception e) {}
		
		return value;
	}
	
	public void setProgress(UUID uuid, int questid) {
		int progress = getProgress(uuid, questid);
		if (progress <= getValue(questid)) {
			progress += 1;		
			LoadMySQLTables.questtracker.update(
				 new String[]{"QUESTTRACKER"}, 
				 new Object[]{progress}, 
				 new String[]{"UUID", "QUESTID"}, 
				 new Object[]{uuid.toString(), questid});
		}
				
	}
	
	
	public int getGather(int questid) {
		int value = 0;
		
		try(StatementResult statementResult = LoadMySQLTables.quests.get(new String[]{"GATHER"}, new String[]{"QUESTID"}, new Object[]{questid})) {
            ResultSet result = statementResult.receive();

            while(result.next()) {
            	value = result.getInt("GATHER");
            }
        } catch (Exception e) {}
		
		return value;
	}
	
	public int getGatherProgress(UUID uuid, int questid) {
		int progress = 0;

		try(StatementResult statementResult = LoadMySQLTables.questtracker.get(new String[]{"GATHERTRACKER"}, new String[]{"UUID", "QUESTID"}, new Object[]{uuid.toString(), questid})) {
            ResultSet result = statementResult.receive();

            while(result.next()) {
            	progress = result.getInt("GATHERTRACKER");
            }
        } catch (Exception e) {}

		return progress;
	}
	
	public void setGather(UUID uuid, int questid, int amount) {

		LoadMySQLTables.questtracker.update(
				 new String[]{"GATHERTRACKER"}, 
				 new Object[]{amount}, 
				 new String[]{"UUID", "QUESTID"}, 
				 new Object[]{uuid.toString(), questid});
	}
	
	public int getKillCounter(int questid) {
		int value = 0;
		
		try(StatementResult statementResult = LoadMySQLTables.quests.get(new String[]{"KILLCOUNTER"}, new String[]{"QUESTID"}, new Object[]{questid})) {
            ResultSet result = statementResult.receive();

            while(result.next()) {
            	value = result.getInt("KILLCOUNTER");
            }
        } catch (Exception e) {}
		
		return value;
	}
	
	public EntityType getKillMobName(int questid) {
		EntityType mob = null;
		
		try(StatementResult statementResult = LoadMySQLTables.quests.get(new String[]{"MOBNAME"}, new String[]{"QUESTID"}, new Object[]{questid})) {
            ResultSet result = statementResult.receive();

            while(result.next()) {
            	mob = EntityType.valueOf(result.getString("MOBNAME"));
            }
        } catch (Exception e) {}
		
		return mob;
	}
	
	public int getKillProgress(UUID uuid, int questid) {
		int progress = 0;

		try(StatementResult statementResult = LoadMySQLTables.questtracker.get(new String[]{"KILLTRACKER"}, new String[]{"UUID", "QUESTID"}, new Object[]{uuid.toString(), questid})) {
            ResultSet result = statementResult.receive();

            while(result.next()) {
            	progress = result.getInt("KILLTRACKER");
            }
        } catch (Exception e) {}

		return progress;
	}
	
	public void setKillProgress(UUID uuid, int questid) {
		int progress = getKillProgress(uuid, questid);
		if (progress <= getKillCounter(questid) - 1) {
			progress += 1;		
			LoadMySQLTables.questtracker.update(
				 new String[]{"KILLTRACKER"}, 
				 new Object[]{progress}, 
				 new String[]{"UUID", "QUESTID"}, 
				 new Object[]{uuid.toString(), questid});
		}
				
	}

	public int getKillPlayerCounter(int questid) {
		int value = 0;
		
		try(StatementResult statementResult = LoadMySQLTables.quests.get(new String[]{"PLAYERTOKILL"}, new String[]{"QUESTID"}, new Object[]{questid})) {
            ResultSet result = statementResult.receive();

            while(result.next()) {
            	value = result.getInt("PLAYERTOKILL");
            }
        } catch (Exception e) {}
		
		return value;
	}
	
	public int getKillPlayerProgress(UUID uuid, int questid) {
		int progress = 0;

		try(StatementResult statementResult = LoadMySQLTables.questtracker.get(new String[]{"PLAYERTRACKER"}, new String[]{"UUID", "QUESTID"}, new Object[]{uuid.toString(), questid})) {
            ResultSet result = statementResult.receive();

            while(result.next()) {
            	progress = result.getInt("PLAYERTRACKER");
            }
        } catch (Exception e) {}

		return progress;
	}
	
	public void setKillPlayerProgress(UUID uuid, int questid) {
		int progress = getKillPlayerProgress(uuid, questid);
		if (progress <= getKillPlayerCounter(questid) - 1) {
			progress += 1;		
			LoadMySQLTables.questtracker.update(
				 new String[]{"PLAYERTRACKER"}, 
				 new Object[]{progress}, 
				 new String[]{"UUID", "QUESTID"}, 
				 new Object[]{uuid.toString(), questid});
		}		
	}
	
	public String getQuestName(int questid) {
		
		String text = null;

		try(StatementResult statementResult = LoadMySQLTables.quests.get(new String[]{"QUESTNAME"}, new String[]{"QUESTID"}, new Object[]{questid})) {
            ResultSet result = statementResult.receive();

            while(result.next()) {
            	text = result.getString("QUESTNAME");
            }
        } catch (Exception e) {}

		return text;
	}
}
