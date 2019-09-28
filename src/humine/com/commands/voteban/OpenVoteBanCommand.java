package humine.com.commands.voteban;

import humine.com.main.StaffMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenVoteBanCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!StaffMain.getInstance().getVoteBan().isRecentVote()) {
			if(args.length >= 2) {
				StringBuilder message = new StringBuilder(" ");
				for(int i = 2; i < args.length; i++)
					message.append(args[i]).append(" ");
				Player target = Bukkit.getPlayer(args[0]);
				if(target != null && !StaffMain.getInstance().getVoteBan().isProtected(target)) {
					if(!StaffMain.getInstance().getVoteBan().isInProgress()) {
						if(Bukkit.getOnlinePlayers().size() >= 5) {
							voteban(target, message.toString());
							return true;
						}
						else
							sender.sendMessage("Pas assez de joueur connectés !");
					}
					else
						sender.sendMessage("un vote est deja en cours !");
				}
				else
					sender.sendMessage("Joueur introuvable");
			}
			else
				sender.sendMessage("/voteban <player> <reason>");


		} else {
			sender.sendMessage("Un voteban à déjà été effectué il y a moins de 5 minutes.");
		}
		return false;
	}

	private void voteban(Player target, String args) {
		Bukkit.broadcastMessage(ChatColor.GOLD + "Un vote sur le banissement sur " + target.getName() + " est lancé !");
		Bukkit.broadcastMessage("Raison: " + ChatColor.DARK_AQUA + args);
		Bukkit.broadcastMessage("Êtes-vous d'accord ? §a/voteban yes§r ou §4/voteban no§r pour voter");
		StaffMain.getInstance().getVoteBan().openVote(target, args);

		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!StaffMain.getInstance().getVoteBan().getBlinds().contains(p)) {
				StaffMain.getInstance().getVoteBan().getVoteBoard().associer(p);
			}
		}
	}
}
