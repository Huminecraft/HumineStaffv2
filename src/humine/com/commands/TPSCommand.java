package humine.com.commands;

import humine.com.main.FileManager;
import humine.com.main.StaffMain;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;


@SuppressWarnings("NullableProblems")
public class TPSCommand implements CommandExecutor {

    private static boolean enabled = FileManager.getTPSConfig();
    private static double tps;

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length != 0) {
            if (args[0].equalsIgnoreCase("enable")) {
                enabled = true;
                sender.sendMessage("Le système de mesure des TPS est activé");
            } else if (args[0].equalsIgnoreCase("disable")) {
                enabled = false;
                sender.sendMessage("Le système de mesure des TPS est désactivé");
            } else if (args[0].equalsIgnoreCase("status")) {
                sender.sendMessage("Le serveur est à " + tps + " TPS");
            }
            return true;
        } else {
            sender.sendMessage("Erreur, veuillez préciser si vous souhaiter activer ou désactiver le monitoring");
            return false;
        }
    }

    public static void startTPSManager() {
        Bukkit.getServer().getScheduler().runTaskTimer(StaffMain.getInstance(), new Runnable() {
                long secstart;
                long secend;

                int ticks;

                @Override
                public void run() {
                    secstart = (System.currentTimeMillis() / 1000);

                    if (secstart == secend) {
                        ticks++;
                    } else {
                        secend = secstart;
                        tps = (tps == 0) ? ticks : ((tps + ticks) / 2);
                        ticks = 1;
                    }
                }

            }, 0, 1);

            Bukkit.getServer().getScheduler().runTaskTimer(StaffMain.getInstance(), new Runnable() {
                @Override
                public void run() {
                    if (tps < 18.5 && TPSCommand.isEnabled()) {
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop");
                    } else if (TPSCommand.isEnabled()){
                        Bukkit.getServer().getLogger().log(Level.INFO, "Current TPS : " + Math.round(tps));
                    }
                }

            }, 1200, 1200);
        }

    public static boolean isEnabled() {
        return enabled;
    }
}
