package humine.com.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import humine.com.main.StaffMain;

public class AnnonceCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length >= 1) {
			StringBuilder message = new StringBuilder();
			for(String str : args) {
				message.append(str).append(" ");
			}
			
			makeAnnonce(message.toString());
		}
		else
			StaffMain.sendMessage(sender, "Entrez le message !");
		
		return false;
	}

	private void makeAnnonce(String message) {
		Bukkit.broadcastMessage(ChatColor.GOLD + "=======[§4§lANNONCE§r§6]=======");
		Bukkit.broadcastMessage(ChatColor.AQUA + message);
		Bukkit.broadcastMessage(ChatColor.GOLD + "=======================");
	}
}
