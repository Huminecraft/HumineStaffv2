package humine.com.main;

import com.aypi.utils.Timer;
import com.aypi.utils.inter.TimerFinishListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class VoteBan {

    private HashMap<Player, String> voters;
    private Player target;
    private int votebanDuration;
    private ArrayList<Player> immuned;
    private int votesFor;
    private int votesAgainst;
    private String reason;
    private Player sender;
    private VoteBoard voteBoard;

    public VoteBan(Player sender, Player target, String reason) {
        this.sender = sender;
        this.target = target;
        this.reason = reason;
        this.voteBoard = new VoteBoard(this);

    }

    public VoteBoard getVoteBoard() {
        return voteBoard;
    }

    public void addForVote(){
        this.votesFor +=1;
    }

    public boolean isImmunised(Player player){
        return this.immuned.contains(player);
    }

    public void setImmuned(Player player){
        this.immuned.add(player);
    }

    public void setVoters(Player player, String str){
        this.voters.put(player, str);
    }

    public void setTarget(Player player){
        this.target = player;
    }

    public void startVoteBan(){
        Timer voteBan = new Timer(StaffMain.getInstance(), 30, new TimerFinishListener() {
            @Override
            public void execute() {
                if (pollResult()){
                    target.kickPlayer("Vous avez été ban par le voteban.");
                    Bukkit.broadcastMessage(target + " a été banni par le vote du public");
                    StaffMain.getInstance().setVoteBan(null);
                } else {
                    Bukkit.broadcastMessage(target + " n'a pas été banni par le voteban.");
                    StaffMain.getInstance().getVoteBan().setImmuned(target);
                    Timer immunised = new Timer(StaffMain.getInstance(), 600, new TimerFinishListener() {
                        @Override
                        public void execute() {
                            StaffMain.getInstance().getVoteBan().setVulnerable(target);
                        }
                    });

                }
            }
        });
    }

    public void executeBan(Player player){
        player.kickPlayer("Vous avez été banni par le vote.");
    }

    public boolean pollResult(){
        return ((double) this.votesFor/(this.votesFor+this.votesAgainst)) >= 0.75;
    }

    public Player getTarget() {
        return target;
    }

    public int getVotesFor() {
        return votesFor;
    }

    public int getVotesAgainst() {
        return votesAgainst;
    }

    public int getVotebanDuration() {
        return votebanDuration;
    }

    public void setVulnerable(Player player){
            immuned.remove(player);
    }
}
