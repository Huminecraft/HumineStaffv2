package humine.com.commands.voteban;

import humine.com.main.StaffMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class VoteBanCommand implements CommandExecutor {

    private final String COMMAND = "/vote <yes|no>";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length < 1){
           commandSender.sendMessage("Argument insufissant");
           commandSender.sendMessage("Usage : " + COMMAND);
           return false;
        }

        if(StaffMain.getInstance().getVoteBan() == null) {
            commandSender.sendMessage("Aucun VoteBan en cours");
            return false;
        }

        if (args[0].equalsIgnoreCase("yes")){
            StaffMain.getInstance().getVoteBan().addForVote();
        } else if (args[0].equalsIgnoreCase("no")){
            StaffMain.getInstance().getVoteBan().addForVote();
        } else {
            commandSender.sendMessage("Arguement Invalide");
            return false;
        }
        StaffMain.getInstance().getVoteBan().getVoteBoard().update();
        return true;
    }
}
