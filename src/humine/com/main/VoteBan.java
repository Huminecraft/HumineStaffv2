package humine.com.main;

import com.huminecraft.humineaypi.utils.Timer;
import com.huminecraft.humineaypi.utils.inter.TimerFinishListener;
import humine.com.commands.voteban.VoteBoard;
import org.bukkit.BanList.Type;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class VoteBan {

	private HashMap<Player, Boolean> participants;
	private HashMap<Player, String> banished;
	private Set<Player> immunized;
	private Player accuser;
	private String reason;
	private boolean inProgress;
	private boolean recentVote;
	private VoteBoard voteBoard;
	private Set<Player> blinds;


	public VoteBan() {
		this.participants = new HashMap<Player, Boolean>();
		this.banished = new HashMap<Player, String>();
		this.accuser = null;
		this.reason = "";
		this.inProgress = false;
		this.immunized = new HashSet<Player>();
		this.voteBoard = new VoteBoard(StaffMain.getInstance().getVoteBan());
		this.blinds = new HashSet<Player>();
	}
	
	public void openVote(Player accuser, String reason) {
		this.accuser = accuser;
		this.reason = reason;
		this.inProgress = true;
		this.voteBoard.update();
		Timer timer = new Timer(StaffMain.getInstance(), 30, new TimerFinishListener() {
			@Override
			public void execute() {
				inProgress = false;
				float percentage = (float) (((float) getNumberOfPersonFor() * 100.0) / (float) Bukkit.getOnlinePlayers().size());
				judgement(percentage);
			}
		});
		
		timer.start();
	}


	
	private void judgement(float percentage) {
		if(percentage >= 75.0) {
			Bukkit.getBanList(Type.NAME).addBan(this.accuser.getName(), this.reason, null, "Vote Ban");
			this.accuser.kickPlayer(this.reason);
			Bukkit.broadcastMessage("Le joueur a été banni du serveur par le peuple");
		}
		else
		{
			Bukkit.broadcastMessage("Le joueur n'a pas etait banni du serveur par le peuple");
			immunized.add(accuser);
			Timer remover = new Timer(StaffMain.getInstance(), 600, new TimerFinishListener() {
				@Override
				public void execute() {
					immunized.remove(accuser);
				}
			});
		}
		for(Player p : Bukkit.getOnlinePlayers()){
			StaffMain.getInstance().getVoteBan().getVoteBoard().dissocier(p);
		}
		recentVote = true;
		Timer recentVoteTimer = new Timer(StaffMain.getInstance(), 300, new TimerFinishListener() {
			@Override
			public void execute() {
				recentVote = false;
			}
		});
	}


	public int getNumberOfPersonFor() {
		int number = 0;
		for(boolean choose : this.participants.values()) {
			if(choose)
				number += 1;
		}
		
		return number;
	}
	
	public int getNumberOfPersonAgainst() {
		int number = 0;
		for(boolean choose : this.participants.values()) {
			if(!choose)
				number += 1;
		}
		
		return number;
	}
	
	public void addParticipant(Player player, boolean choose) {
		this.participants.put(player, choose);
	}

	public HashMap<Player, Boolean> getParticipants() {
		return participants;
	}

	public void setParticipants(HashMap<Player, Boolean> participants) {
		this.participants = participants;
	}

	public HashMap<Player, String> getBanished() {
		return banished;
	}

	public void setBanished(HashMap<Player, String> banished) {
		this.banished = banished;
	}

	public Player getAccuser() {
		return accuser;
	}

	public void setAccuser(Player accuser) {
		this.accuser = accuser;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public boolean isInProgress() {
		return inProgress;
	}

	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
	}

	public boolean isProtected(Player player){
		return (player.hasPermission("huminestaff.admin.unbannable") || this.immunized.contains(player));
	}

	public boolean isRecentVote() {
		return recentVote;
	}

	public void setRecentVote(boolean recentVote) {
		this.recentVote = recentVote;
	}

	public VoteBoard getVoteBoard(){
		return voteBoard;
	}
	public Set getBlinds(){
		return blinds;
	}

	public void addBlinds(Player player){
		blinds.add(player);
	}

	public void removeBlinds(Player player){
		blinds.remove(player);
	}
}
