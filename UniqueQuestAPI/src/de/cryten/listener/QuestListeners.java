package de.cryten.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.cryten.MainClass;
import de.cryten.command.QuestCommand;
import de.cryten.sql.MySQL;
import de.cryten.utils.ActionBar;
import de.cryten.utils.Functions;

@SuppressWarnings("deprecation")
public class QuestListeners implements Listener {
	
	MySQL sql = new MySQL();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		MySQL.getQuests(p, p.getUniqueId());
		e.setJoinMessage(null);
		if(sql.questid.size() <= 0){
			sql.getQuestID(p.getUniqueId());
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		e.setDeathMessage(null);
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {

		ItemStack item = e.getCurrentItem();

		if (item == null || item.getType() == null || item.getType() == Material.AIR)
			return;

		if (QuestCommand.inv != null && e.getInventory().getName().equals(QuestCommand.inv.getName())) {
			e.setCancelled(true);
		}
	}

	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		
		if(sql.questid.size() <= 0){
			sql.getQuestID(p.getUniqueId());
		}
		
		for(int i : sql.questid) {
			
			if (sql.getValue(i) != 0) {
				
				if (sql.getProgress(p.getUniqueId(), i) < sql.getValue(i) && e.getBlock().getTypeId() == sql.getItemID(i)) {
					sql.setProgress(p.getUniqueId(), i);
				
					p.sendMessage("§l§6Quest: §2" + sql.getQuestName(i) + " §6- §2" + sql.getProgress(p.getUniqueId(), i) + "/" + sql.getValue(i));
					ActionBar.sendActionText(p, "§l§6Quest: §2" + sql.getQuestName(i) + " §6- §2" + sql.getProgress(p.getUniqueId(), i) + "/" + sql.getValue(i), 3);
					
					if(sql.getProgress(p.getUniqueId(), i) == sql.getValue(i)) {
						p.sendMessage("§k123§r §6Quest: §2" + sql.getQuestName(i) + " §6- Abgeschlossen §f§k123");
						p.sendMessage("§k123§r §6Du hast §e" + sql.getReward(i) + " §6Unique Coins bekommen §f§k123");
						//UniqueQuestActionBar.sendActionText(p, "§l§6Quest: §2" + sql.getQuestName(i) + " §6- Abgeschlossen | Belohnung: §2" + sql.getReward(i) + "§6 Unique Coins");
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
						
						sql.addTokens(p.getUniqueId(), sql.getReward(i));
					}
				}


			}
		}
	}
	
	@EventHandler
	public void onGather(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		
		if(sql.questid.size() <= 0){
			sql.getQuestID(p.getUniqueId());
		}
		
		Bukkit.getServer().getScheduler().runTaskLater(MainClass.getInstance(), new BukkitRunnable() {
			public void run() {
				
				for(int i : sql.questid) {
					
					int itemid = sql.getItemID(i);
					
					if (e.getItem().getItemStack().getType() == Material.getMaterial(itemid)) {

						if(Functions.getAmount(p, itemid) < sql.getGather(i) && sql.getGather(i) != 0) {
							
							p.sendMessage("§l§6Quest: §2" + sql.getQuestName(i) + " §6- §2" + Functions.getAmount(p, itemid) + "/" + sql.getGather(i));
							ActionBar.sendActionText(p, "§l§6Quest: §2" + sql.getQuestName(i) + " §6- §2" + Functions.getAmount(p, itemid) + "/" + sql.getGather(i), 3);
						}
						if(Functions.getAmount(p, itemid) == sql.getGather(i) && sql.getGather(i) != 0) {
							sql.setGather(p.getUniqueId(), i, Functions.getAmount(p, itemid));
							
							p.sendMessage("§k123§r §6Quest: §2" + sql.getQuestName(i) + " §6- Abgeschlossen §f§k123");
							p.sendMessage("§k123§r §6Du hast §e" + sql.getReward(i) + " §6Unique Coins bekommen §f§k123");
							//UniqueQuestActionBar.sendActionText(p, "§l§6Quest: §2" + sql.getQuestName(i) + " §6- Abgeschlossen | Belohnung: §2" + sql.getReward(i) + " §6Unique Coins");
							p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
							
							sql.addTokens(p.getUniqueId(), sql.getReward(i));
						}
					}
				}
			}
		}, 20);
	}
	
	@EventHandler
	public void onKill(EntityDeathEvent e) {

		Player p = e.getEntity().getKiller();
		Entity enti = e.getEntity();		
		
		if(enti.getLastDamageCause() instanceof EntityDamageByEntityEvent) {

			if(sql.questid.size() == 0){
				sql.getQuestID(p.getUniqueId());
			}
			
			if(p instanceof Player && enti instanceof Player) {
				
				for(int i : sql.questid) {
					
					if (sql.getKillPlayerProgress(p.getUniqueId(), i) < sql.getKillPlayerCounter(i) && sql.getKillPlayerCounter(i) != 0) {
						
						sql.setKillPlayerProgress(p.getUniqueId(), i);
						p.sendMessage("§l§6Quest: §2" + sql.getQuestName(i) + " §6- §2" + sql.getKillPlayerProgress(p.getUniqueId(), i) + "/" + sql.getKillPlayerCounter(i));
					}
					if(sql.getKillPlayerProgress(p.getUniqueId(), i) == sql.getKillPlayerCounter(i) && sql.getKillPlayerCounter(i) != 0) {
						p.sendMessage("§k123§r §6Quest: §2" + sql.getQuestName(i) + " §6- Abgeschlossen §f§k123");
						p.sendMessage("§k123§r §6Du hast §e" + sql.getReward(i) + " §6Unique Coins bekommen §f§k123");
						
						//UniqueQuestActionBar.sendActionText(p, "§l§6Quest: §2" + sql.getQuestName(i) + " §6- Abgeschlossen | Belohnung: §2" + sql.getReward(i) + " §6Unique Coins");
						p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
						
						sql.addTokens(p.getUniqueId(), sql.getReward(i));
					}
				}				
			}
			
			if(p instanceof Player && p != null && enti instanceof Entity) {
				
				for(int i : sql.questid) {
					
					if (sql.getKillProgress(p.getUniqueId(), i) < sql.getKillCounter(i) && sql.getKillCounter(i) != 0) {
						
						if(enti.getType().equals(sql.getKillMobName(i))) {
							
							sql.setKillProgress(p.getUniqueId(), i);
							p.sendMessage("§l§6Quest: §2" + sql.getQuestName(i) + " §6- §6" + sql.getKillProgress(p.getUniqueId(), i) + "/" + sql.getKillCounter(i));
							ActionBar.sendActionText(p, "§l§6Quest: §2" + sql.getQuestName(i) + " §6- §e" + sql.getKillProgress(p.getUniqueId(), i) + "/" + sql.getKillCounter(i), 3);
							
						}
						if(sql.getKillProgress(p.getUniqueId(), i) == sql.getKillCounter(i) && sql.getKillCounter(i) != 0) {

							p.sendMessage("§k123§r §6Quest: §2" + sql.getQuestName(i) + " §6- Abgeschlossen §f§k123");
							p.sendMessage("§k123§r §6Du hast §e" + sql.getReward(i) + " §6Unique Coins bekommen §f§k123");
							//UniqueQuestActionBar.sendActionText(p, "§l§6Quest: §2" + sql.getQuestName(i) + " §6- Abgeschlossen | Belohnung: §2" + sql.getReward(i) + " §6Unique Coins");
							p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
							
							sql.addTokens(p.getUniqueId(), sql.getReward(i));
						}
					}
				}
			}
		}
	}
}
