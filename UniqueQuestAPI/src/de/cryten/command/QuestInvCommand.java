package de.cryten.command;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import de.cryten.sql.MySQL;
import de.cryten.sql.MySQLTables;
import de.cryten.tool.mysql.common.StatementResult;
import de.cryten.utils.Functions;

public class QuestInvCommand implements CommandExecutor {
	ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
	MySQL sql = new MySQL();
	public static Inventory inv;
	int size = 0;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
        if (sender instanceof Player) {
            Player p = (Player) sender;
          
            inv = Bukkit.createInventory(null, 27, "§2Questlog");
    		
    		for(int i = 0; i < inv.getSize(); i++) {
    			
    			if(inv.getItem(i) == null || (inv.getItem(i) != null && inv.getItem(i).getType().equals(Material.AIR))) {
    			   inv.setItem(i, stack("§c", Material.STAINED_GLASS_PANE, "", "", 1, (short) 7));
    			}
    		}
    		
			try(ResultSet rs = MySQLTables.connectionPool.getConnection().createStatement().executeQuery(
    				"SELECT * FROM Quests "
    				+ "LEFT JOIN Questtracker "
    				+ "ON Quests.QUESTID = Questtracker.QUESTID "
    				+ "WHERE STATE = 1 "
    				+ "&& UUID = '"+ p.getUniqueId() +"';")) {
				
	            while(rs.next()) {
					ItemStack s = new ItemStack(sql.getMaterialName(rs.getInt("QUESTID")));
					
					try(StatementResult statement = MySQLTables.quests.get(new String[]{"ITEMSHORT"}, new String[]{"QUESTID"}, new Object[]{""+rs.getInt("QUESTID")+""})){
						ResultSet result = statement.receive();
						if(result.next()) {
							if(result.getInt("ITEMSHORT") != 0){
								s.setDurability((short) result.getInt("ITEMSHORT"));
							}
						}
					}catch(Exception e){
						console.sendMessage("§cMySQL Fehler -> ITEMSHORT: §4" + e.getMessage());
					}
					
                	if(rs.getInt("VALUE") == 0 && rs.getInt("GATHER") == 0 && rs.getInt("PLAYERTOKILL") == 0 && rs.getInt("KILLTRACKER") < rs.getInt("KILLCOUNTER")) {
						String[] questext = rs.getString("QUESTTEXT").split("\\|", -1);
						ArrayList<String> lorelist = new ArrayList<>();
						
						lorelist.add("§7Aufgabe:");
						for (String name : questext) {
							lorelist.add(name);
						}
						lorelist.add("§eFortschritt: " + rs.getInt("KILLTRACKER") + "/"  + rs.getInt("KILLCOUNTER"));
						lorelist.add("");
						lorelist.add("§aBelohnung: §e" + sql.getReward(rs.getInt("QUESTID")) + " §aUnique Coins");
						
						ItemMeta itemmeta = s.getItemMeta();
						itemmeta.setDisplayName("§6" + sql.getQuestName(rs.getInt("QUESTID")));
						itemmeta.setLore(lorelist);
						s.setItemMeta(itemmeta);
						
                	}else if(rs.getInt("VALUE") == 0 && rs.getInt("GATHER") == 0 && rs.getInt("PLAYERTOKILL") == 0 && rs.getInt("KILLTRACKER") == rs.getInt("KILLCOUNTER")) {
						String[] questext = rs.getString("QUESTTEXT").split("\\|", -1);
						ArrayList<String> lorelist = new ArrayList<>();
						
						lorelist.add("§7Aufgabe:");
						for (String name : questext) {
							lorelist.add(name);
						}
						lorelist.add("§eFortschritt: §2" + rs.getInt("KILLTRACKER") + "/"  + rs.getInt("KILLCOUNTER") + " (Fertig)");
						lorelist.add("");
						lorelist.add("§aBelohnung: §e" + sql.getReward(rs.getInt("QUESTID")) + " §aUnique Coins");
						
						ItemMeta itemmeta = s.getItemMeta();
						itemmeta.setDisplayName("§6" + sql.getQuestName(rs.getInt("QUESTID")));
						itemmeta.setLore(lorelist);
						s.setItemMeta(itemmeta);
                	}

                	if(rs.getInt("VALUE") == 0 && rs.getInt("GATHER") == 0 && rs.getInt("KILLCOUNTER") == 0 && rs.getInt("PLAYERTRACKER") < rs.getInt("PLAYERTOKILL")) {
						String[] questext = rs.getString("QUESTTEXT").split("\\|", -1);
						ArrayList<String> lorelist = new ArrayList<>();
						
						lorelist.add("§7Aufgabe:");
						for (String name : questext) {
							lorelist.add(name);
						}
						lorelist.add("§eFortschritt: " + rs.getInt("PLAYERTRACKER") + "/"  + rs.getInt("PLAYERTOKILL"));
						lorelist.add("");
						lorelist.add("§aBelohnung: §e" + sql.getReward(rs.getInt("QUESTID")) + " §aUnique Coins");
						
						ItemMeta itemmeta = s.getItemMeta();
						itemmeta.setDisplayName("§6" + sql.getQuestName(rs.getInt("QUESTID")));
						itemmeta.setLore(lorelist);
						s.setItemMeta(itemmeta);
                	}else if(rs.getInt("VALUE") == 0 && rs.getInt("GATHER") == 0 && rs.getInt("KILLCOUNTER") == 0 && rs.getInt("PLAYERTOKILL") == rs.getInt("PLAYERTRACKER")) {
						String[] questext = rs.getString("QUESTTEXT").split("\\|", -1);
						ArrayList<String> lorelist = new ArrayList<>();
						
						lorelist.add("§7Aufgabe:");
						for (String name : questext) {
							lorelist.add(name);
						}
						lorelist.add("§eFortschritt: §2" + rs.getInt("PLAYERTRACKER") + "/"  + rs.getInt("PLAYERTOKILL") + " (Fertig)");
						lorelist.add("");
						lorelist.add("§aBelohnung: §e" + sql.getReward(rs.getInt("QUESTID")) + " §aUnique Coins");
						
						ItemMeta itemmeta = s.getItemMeta();
						itemmeta.setDisplayName("§6" + sql.getQuestName(rs.getInt("QUESTID")));
						itemmeta.setLore(lorelist);
						s.setItemMeta(itemmeta);
                	}
                	
                	if(rs.getInt("VALUE") == 0 && rs.getInt("PLAYERTOKILL") == 0 && rs.getInt("KILLCOUNTER") == 0 && Functions.getMaterialAmount(p, Material.getMaterial(rs.getString("ITEM"))) < rs.getInt("GATHER")) {
						String[] questext = rs.getString("QUESTTEXT").split("\\|", -1);
						ArrayList<String> lorelist = new ArrayList<>();
						
						lorelist.add("§7Aufgabe:");
						for (String name : questext) {
							lorelist.add(name);
						}
						lorelist.add("§eFortschritt: " + Functions.getMaterialAmount(p, Material.getMaterial(rs.getString("ITEM"))) + "/"  + rs.getInt("GATHER"));
						lorelist.add("");
						lorelist.add("§aBelohnung: §e" + sql.getReward(rs.getInt("QUESTID")) + " §aUnique Coins");
						
						ItemMeta itemmeta = s.getItemMeta();
						itemmeta.setDisplayName("§6" + sql.getQuestName(rs.getInt("QUESTID")));
						itemmeta.setLore(lorelist);
						s.setItemMeta(itemmeta);
						
                	}else if(rs.getInt("VALUE") == 0 && rs.getInt("PLAYERTOKILL") == 0 && rs.getInt("KILLCOUNTER") == 0 && Functions.getMaterialAmount(p, Material.getMaterial(rs.getString("ITEM"))) >= rs.getInt("GATHER")) {
                		
						String[] questext = rs.getString("QUESTTEXT").split("\\|", -1);
						ArrayList<String> lorelist = new ArrayList<>();
						
						lorelist.add("§7Aufgabe:");
						for (String name : questext) {
							lorelist.add(name);
						}
						lorelist.add("§eFortschritt: §2" + rs.getInt("GATHERTRACKER") + "/"  + rs.getInt("GATHER") + " (Fertig)");
						lorelist.add("");
						lorelist.add("§aBelohnung: §e" + sql.getReward(rs.getInt("QUESTID")) + " §aUnique Coins");
						
						ItemMeta itemmeta = s.getItemMeta();
						itemmeta.setDisplayName("§6" + sql.getQuestName(rs.getInt("QUESTID")));
						itemmeta.setLore(lorelist);
						s.setItemMeta(itemmeta);
                	}
                	
                	if(rs.getInt("GATHER") == 0 && rs.getInt("PLAYERTOKILL") == 0 && rs.getInt("KILLCOUNTER") == 0 && rs.getInt("QUESTTRACKER") < rs.getInt("VALUE")) {
						String[] questext = rs.getString("QUESTTEXT").split("\\|", -1);
						ArrayList<String> lorelist = new ArrayList<>();
						
						lorelist.add("§7Aufgabe:");
						for (String name : questext) {
							lorelist.add(name);
						}
						lorelist.add("§eFortschritt: " + rs.getInt("QUESTTRACKER") + "/"  + rs.getInt("VALUE"));
						lorelist.add("");
						lorelist.add("§aBelohnung: §e" + sql.getReward(rs.getInt("QUESTID")) + " §aUnique Coins");
						
						ItemMeta itemmeta = s.getItemMeta();
						itemmeta.setDisplayName("§6" + sql.getQuestName(rs.getInt("QUESTID")));
						itemmeta.setLore(lorelist);
						s.setItemMeta(itemmeta);
                	}else if(rs.getInt("GATHER") == 0 && rs.getInt("PLAYERTOKILL") == 0 && rs.getInt("KILLCOUNTER") == 0 && rs.getInt("QUESTTRACKER") == rs.getInt("VALUE")) {
						String[] questext = rs.getString("QUESTTEXT").split("\\|", -1);
						ArrayList<String> lorelist = new ArrayList<>();
						
						lorelist.add("§7Aufgabe:");
						for (String name : questext) {
							lorelist.add(name);
						}
						lorelist.add("§eFortschritt: §2" + rs.getInt("QUESTTRACKER") + "/"  + rs.getInt("VALUE") + " (Fertig)");
						lorelist.add("");
						lorelist.add("§aBelohnung: §e" + sql.getReward(rs.getInt("QUESTID")) + " §aUnique Coins");
						
						ItemMeta itemmeta = s.getItemMeta();
						itemmeta.setDisplayName("§6" + sql.getQuestName(rs.getInt("QUESTID")));
						itemmeta.setLore(lorelist);
						s.setItemMeta(itemmeta);
                	}
					inv.setItem(size, s);
					size++;
	            }
	            rs.close();
	        } catch (Exception e) {
	        	console.sendMessage("§cMySQL Fehler -> Quests: §4" + e.getMessage());
	        }
    		size = 0;
    		p.openInventory(inv);
        }
		return false;
	}
	
	public ItemStack stack(String Display, Material m, String lores, String string, int Anzahl, short Shorts) {
		ItemStack istack = new ItemStack(m, Anzahl, Shorts);
		ItemMeta istackMeta = istack.getItemMeta();
		istackMeta.setDisplayName(Display);
		if (!lores.equalsIgnoreCase("")) {
			List<String> lore = new ArrayList<String>();
			lore.add(lores);
			lore.add(string);
			istackMeta.setLore(lore);
		}
		istack.setItemMeta(istackMeta);
		return istack;
	}
}