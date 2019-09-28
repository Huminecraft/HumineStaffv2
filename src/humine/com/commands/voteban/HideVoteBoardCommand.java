package humine.com.commands.voteban;

import humine.com.main.StaffMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HideVoteBoardCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (args[0].equals("disable")) {
                StaffMain.getInstance().getVoteBan().addBlinds(player);
                sender.sendMessage("[HumineStaff] Les votes bans ne seront affichés que si vous en êtes victime.");
            } else if (args[0].equals("enable")) {
                StaffMain.getInstance().getVoteBan().removeBlinds(player);
                sender.sendMessage("[HumineStaff] Les votes bans sont affichés.");

            } else {
                sender.sendMessage("[HumineStaff] : Utilisation : /voteban [disable/enable].");

            }

        }
        return false;
    }
}
