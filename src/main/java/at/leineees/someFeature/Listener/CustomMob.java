package at.leineees.someFeature.Listener;

import at.leineees.someFeature.Data.CustomMob.CustomMobData;
import at.leineees.someFeature.Data.CustomMob.CustomMobManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomMob implements Listener {

    private final JavaPlugin plugin;
    private final CustomMobManager customMobManager;
    private final Set<LivingEntity> customEntities = new HashSet<>();
    private final Map<LivingEntity, String> entityCommands = new HashMap<>();

    public CustomMob(JavaPlugin plugin, CustomMobManager customMobManager) {
        this.plugin = plugin;
        this.customMobManager = customMobManager;
    }

    public void spawnCustomMob(CustomMobData customMobData) {
        customMobManager.spawnCustomMob(customMobData);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity().getCustomName() != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getCustomName() != null) {
            event.getDrops().clear();
            event.setDroppedExp(0);
            customEntities.remove(event.getEntity());
            entityCommands.remove(event.getEntity());
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            String customName = livingEntity.getCustomName();
            if (customName != null) {
                for (CustomMobData mobData : customMobManager.getCustomMobs()) {
                    if (customName.equals(ChatColor.translateAlternateColorCodes('&', mobData.getNameTag()))) {
                        player.performCommand(mobData.getCommand());
                        break;
                    }
                }
            }
        }
    }
}