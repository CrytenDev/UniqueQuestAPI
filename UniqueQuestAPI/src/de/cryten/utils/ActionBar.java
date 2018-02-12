package de.cryten.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

public class ActionBar {

    /**
     * Send Actionbar to Player.
     */
	public void sendActionText(Player p, String message, int seconds){
		Timer timer = new Timer();
		
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(message).create());
			}
			
		}, TimeUnit.SECONDS.toMillis(1), TimeUnit.SECONDS.toMillis(1));
    }
}
