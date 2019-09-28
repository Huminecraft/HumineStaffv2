package humine.com.commands.voteban;

import humine.com.main.StaffMain;
import humine.com.main.VoteBan;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Objects;

public class VoteBoard {

    private final VoteBan vb;
    private final Scoreboard voteBoard = Objects.requireNonNull(StaffMain.getInstance().getServer().getScoreboardManager()).getNewScoreboard();
    private final Objective objective;


    public VoteBoard(VoteBan vb) {
        this.vb = vb;
        objective = voteBoard.registerNewObjective("Voteban", "dummy", "Voteban", RenderType.INTEGER);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("Voteban");
        update();
    }

    public void update() {
        Score s1 = objective.getScore("Victime: " + vb.getAccuser().getName());
        Score s2 = objective.getScore("Pour");
        s2.setScore(vb.getNumberOfPersonFor());
        Score s3 = objective.getScore("Contre");
        s3.setScore(vb.getNumberOfPersonAgainst());
    }



    public void associer(Player player) {
        player.setScoreboard(voteBoard);
    }

    public void dissocier(Player player) {
        player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
    }
}
