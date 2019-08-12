package humine.com.main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.huminecraft.humineaypi.utils.Timer;
import com.huminecraft.humineaypi.utils.inter.TimerFinishListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

public class Check {

    private Player p;
    static Check instance;
    private static int reward = FileManager.getUpvoteConfig("reward");
    private static int delay = FileManager.getUpvoteConfig("delay");

    public static int getReward() {
        return reward;
    }


    public static int getDelay() {
        return delay;
    }



    public Check() {
    }

    public void setPlayer(Player p) {
        this.p = p;
    }

    public Player getPlayer() {
        return p;
    }

    public void start() {
        Timer Check = new Timer(StaffMain.getInstance(), delay*60,
                new TimerFinishListener() {
                    @Override
                    public void execute() {
                        if(isUpvoted(getIP(p))){
                            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "store pixel " + getPlayer().getName() + " " + reward );
                            getPlayer().sendMessage("Vous avez été crédité de " + reward + " pixels");
                        } else {
                            p.sendMessage("Vous avez déjà voté ces dernières 24h ou vous n'avez pas voté assez rapidement.");
                        };
                    }
                }, true);
        Check.start();
    }
    private boolean isUpvoted(String ip){
        try
        {
            URL url = new URL("https://www.serveurs-minecraft.org/api/is_valid_vote.php?id=48763&ip=" + ip + "&duration=" + delay);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str = in.readLine();
            in.close();
            return str.equals("1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private String getIP(Player player){
        return Objects.requireNonNull(player.getAddress()).getHostString();
    }
}