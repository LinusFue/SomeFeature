package at.leineees.someFeature.Feature;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

        Objective objective = board.registerNewObjective("SMP", "dummy", ChatColor.BOLD + "" + ChatColor.GREEN + "SMP");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore("   ").setScore(6);
        Team coinsTeam = board.registerNewTeam("coins");
        coinsTeam.addEntry(ChatColor.GOLD + "Coins: ");
        objective.getScore(ChatColor.GOLD + "Coins: ").setScore(5);
        
        String separator = ChatColor.GRAY + new String(new char[(ChatColor.GOLD + "Coins: ").length()]).replace('\0', '_');
        objective.getScore(separator).setScore(4);
        objective.getScore("  ").setScore(3);

        Team biomeTeam = board.registerNewTeam("biome");
        biomeTeam.addEntry(ChatColor.GREEN + "Biome: ");
        objective.getScore(ChatColor.GREEN + "Biome: ").setScore(2);
        
        player.setScoreboard(board);
    }

    public void updateCoins(Player player, int newCoins, int amount) {
        Scoreboard board = player.getScoreboard();
        Team coinsTeam = board.getTeam("coins");
        if (coinsTeam != null) {
            String changeText = amount > 0 ? ChatColor.YELLOW + "(+" + amount + ")" : ChatColor.RED + "(" + amount + ")";
            coinsTeam.setSuffix(ChatColor.WHITE + "" + newCoins + " " + changeText);

            // Schedule a task to remove the changeText after 3 seconds
            new BukkitRunnable() {
                @Override
                public void run() {
                    coinsTeam.setSuffix(ChatColor.WHITE + String.valueOf(newCoins));
                }
            }.runTaskLater(plugin, 60L); // 60 ticks = 3 seconds
        }
    }

    public void updateBiome(Player player) {
        Scoreboard board = player.getScoreboard();
        Team biomeTeam = board.getTeam("biome");
        if (biomeTeam != null) {
            String biome = player.getLocation().getBlock().getBiome().name();
            biomeTeam.setSuffix(ChatColor.WHITE + biome);
        }
    }
}