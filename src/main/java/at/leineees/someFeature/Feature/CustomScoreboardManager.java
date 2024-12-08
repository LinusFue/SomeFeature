package at.leineees.someFeature.Feature;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public class CustomScoreboardManager {

    private final JavaPlugin plugin;

    public CustomScoreboardManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void createScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        Objective objective = board.registerNewObjective("SMP", "dummy", "§l" + "§aSMP");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore("   ").setScore(6);
        Team coinsTeam = board.registerNewTeam("coins");
        coinsTeam.addEntry("§6Coins: ");
        objective.getScore("§6Coins: ").setScore(5);
        
        String separator = new String(new char[("§6Coins: ").length()]).replace('\0', '_');
        objective.getScore(separator).setScore(4);
        objective.getScore("  ").setScore(3);

        Team biomeTeam = board.registerNewTeam("biome");
        biomeTeam.addEntry("§aBiome: ");
        objective.getScore("§aBiome: ").setScore(2);
        
        player.setScoreboard(board);
    }

    public void updateCoins(Player player, int newCoins, int amount) {
        Scoreboard board = player.getScoreboard();
        Team coinsTeam = board.getTeam("coins");
        if (coinsTeam != null) {
            String changeText = amount > 0 ? "§e(+" + amount + ")" : "§c(" + amount + ")";
            coinsTeam.setSuffix("§f" + newCoins + " " + changeText);

            // Schedule a task to remove the changeText after 3 seconds
            new BukkitRunnable() {
                @Override
                public void run() {
                    coinsTeam.setSuffix("§f" + String.valueOf(newCoins));
                }
            }.runTaskLater(plugin, 60L); // 60 ticks = 3 seconds
        }
    }

    public void updateBiome(Player player) {
        Scoreboard board = player.getScoreboard();
        Team biomeTeam = board.getTeam("biome");
        if (biomeTeam != null) {
            String biome = player.getLocation().getBlock().getBiome().name();
            biomeTeam.setSuffix("§f" + biome);
        }
    }
}