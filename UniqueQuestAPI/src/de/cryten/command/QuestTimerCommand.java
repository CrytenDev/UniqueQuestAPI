package de.cryten.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import de.cryten.utils.QuestTimer;

public class QuestTimerCommand implements CommandExecutor {

	QuestTimer qt = new QuestTimer();
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if(p.isOp() || p.hasPermission("questapi.admin")) {
			qt.start();
			p.sendMessage("Questreset wurde gestartet!");
		}
		return false;
	}
}
