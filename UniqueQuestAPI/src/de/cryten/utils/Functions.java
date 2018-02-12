package de.cryten.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class Functions {
	
	
	
    /**
     * Get amount of items in player inventory.
     */
	@SuppressWarnings("deprecation")
	public static int getAmount(Player p, int itemid) {
		PlayerInventory inv = p.getInventory();
		ItemStack[] items = inv.getContents();
		int counter = 0;
		for(ItemStack item : items) {
			if((item != null) && (item.getTypeId() == itemid) && (item.getAmount() > 0)) {
				counter += item.getAmount();
			}
		}
		return counter;
	}
	
	//Neue Methode ohne TypeID dafür mit Material Grund: Deprecated function -> getTypeId
	public static int getMaterialAmount(Player p, Material material) {
		PlayerInventory inv = p.getInventory();
		ItemStack[] items = inv.getContents();
		int counter = 0;
		
		for(ItemStack item : items) {
			if(item != null && (item.getType() == material) && (item.getAmount() > 0)) {
				counter += item.getAmount();
			}
		}		
		return counter;
		
	}
}
