package de.cryten.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

public class ActionBar {
	static Timer timer = new Timer();
	static int count = 0;
	
    /**
     * Send Actionbar to Player.
     */
	public static void sendActionText(Player p, String message, int seconds){

		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				count++;
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(message).create());
				if(count == seconds) {
					timer.cancel();
				}
			}
			
		}, TimeUnit.SECONDS.toMillis(1), TimeUnit.SECONDS.toMillis(1));
    }
}
