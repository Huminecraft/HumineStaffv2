package humine.com.main;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Objects;

public class VoteBoard {

    private final VoteBan voteBan;
    private final Scoreboard voteBoard = Objects.requireNonNull(StaffMain.getInstance().getServer().getScoreboardManager()).getNewScoreboard();
    private final Objective objective;

    public VoteBoard(VoteBan voteBan) {
        this.voteBan = voteBan;
        objective = voteBoard.registerNewObjective("Voteban", "dummy", "Voteban", RenderType.INTEGER);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("Voteban");
        update();
    }

    public void update() {
        Score s1 = objective.getScore("Victime: " + voteBan.getTarget().getName());
        Score s2 = objective.getScore("Pour");
        s2.setScore(voteBan.getVotesFor());
        Score s3 = objective.getScore("Contre");
        s3.setScore(voteBan.getVotesAgainst());
    }


    public void associate(Player player) {
        player.setScoreboard(voteBoard);
    }

    public void dissociate(Player player) {
        player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
    }

    public Scoreboard getVoteBoard() {
        return voteBoard;
    }
}
