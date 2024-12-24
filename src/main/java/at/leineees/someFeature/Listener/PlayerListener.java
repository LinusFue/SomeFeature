package at.leineees.someFeature.Listener;

import at.leineees.someFeature.Data.Coins.CoinManager;
import at.leineees.someFeature.Feature.CustomScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {

    private final CustomScoreboardManager scoreboardManager;
    private final CoinManager coinManager;

    public PlayerListener(CustomScoreboardManager scoreboardManager, CoinManager coinManager) {
        this.scoreboardManager = scoreboardManager;
        this.coinManager = coinManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        scoreboardManager.createScoreboard(player);
        coinManager.addCoins(player, 0);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        scoreboardManager.updateBiome(player);
    }
    
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        coinManager.removeCoins(event.getEntity(), coinManager.getCoins(event.getEntity()) / 2);
    }
}