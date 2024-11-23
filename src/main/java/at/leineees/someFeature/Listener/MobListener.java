package at.leineees.someFeature.Listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;

import java.util.Random;

public class MobListener implements Listener {
    private final LootTable lootTable;

    public MobListener(LootTable lootTable) {
        this.lootTable = lootTable;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getType().equals(EntityType.SKELETON_HORSE)) {
            event.getDrops().clear();
            LootContext context = new LootContext.Builder(event.getEntity().getLocation()).build();
            lootTable.populateLoot(new Random(), context).forEach(event.getDrops()::add);
        }
    }
}