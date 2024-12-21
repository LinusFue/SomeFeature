package at.leineees.someFeature.Listener;

import at.leineees.someFeature.SomeFeature;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ArmorItemListener implements Listener {
    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        ItemStack chestplate = player.getInventory().getChestplate();

        if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            if (chestplate != null && chestplate.getType() == Material.NETHERITE_CHESTPLATE) {
                ItemMeta meta = chestplate.getItemMeta();
                if (meta != null) {
                    PersistentDataContainer container = meta.getPersistentDataContainer();
                    if (container.has(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING) &&
                            container.get(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING).equals("somefeature:elytra_chestplate")) {

                        player.setGliding(true);
                        event.setCancelled(true);
                        ItemStack elytra = new ItemStack(Material.ELYTRA);
                        player.sendEquipmentChange(player, EquipmentSlot.CHEST, elytra);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!player.isGliding()) {
            ItemStack chestplate = player.getInventory().getChestplate();
            ItemMeta meta = chestplate.getItemMeta();
            if (meta != null) {
                PersistentDataContainer container = meta.getPersistentDataContainer();
                container.set(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING, "somefeature:elytra_chestplate");
                chestplate.setItemMeta(meta);
            }
            player.getInventory().setChestplate(chestplate);
        }
    }
}