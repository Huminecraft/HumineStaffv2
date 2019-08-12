package humine.com.commands;

import humine.com.main.Check;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class UpvoteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            sender.sendMessage("Vous pouvez upvote le serveur sur " +
                    "ce lien : (utilisable une fois toutes les 24 heures) : " +
                    "https://www.serveurs-minecraft.org/vote.php?id=48763");
            Check check = new Check();
            Player player = (Player) sender;
            check.setPlayer(player);
            check.start();
            return true;
        }
        return false;
    }

}