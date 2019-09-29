package humine.com.main;

import com.aypi.utils.Timer;
import com.aypi.utils.inter.TimerFinishListener;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class VoteBan {

    private ArrayList<Player> voters;
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

    public boolean isImmuned(Player player){
        return this.immuned.contains(player);
    }

    public void setImmuned(Player player){
        this.immuned.add(player);
    }

    public void setVoters(Player player){
        this.voters.add(player);
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
}
