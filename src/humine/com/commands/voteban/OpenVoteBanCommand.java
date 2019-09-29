package humine.com.commands.voteban;

import humine.com.main.StaffMain;
import humine.com.main.VoteBan;
import humine.com.main.VoteBoard;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenVoteBanCommand implements CommandExecutor {

    private final String COMMAND = "/voteban <name> <reason>";
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length < 2) {
            commandSender.sendMessage("Argument insuffisant");
            commandSender.sendMessage(COMMAND);
            return false;
        }
        Player target = getPlayer(args[0]);
        if (target == null){
            commandSender.sendMessage("Joueur Introuvable");
            return false;
        }

        String message = "";
        for (int i=1; i < args.length; i++){
            message += args[i];
        }

        VoteBan voteBan = new VoteBan((Player) commandSender, target, message);
        StaffMain.getInstance().setVoteBan(voteBan);
        VoteBoard voteBoard = new VoteBoard(voteBan);
        voteBan.startVoteBan();



        return true;
    }

    private Player getPlayer(String name){
        for (Player p : Bukkit.getOnlinePlayers()){
            if (p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }

}
