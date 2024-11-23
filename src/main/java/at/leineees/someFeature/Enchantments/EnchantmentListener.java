package at.leineees.someFeature.Enchantments;

import at.leineees.someFeature.Enchantments.Enchants.LogFortuneEnchantment;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class EnchantmentListener implements Listener {
    private final EnchantmentManager manager;

    public EnchantmentListener(EnchantmentManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        Block block = event.getBlock();
        
        if (block.getType().toString().endsWith("_LOG")) {
            CustomEnchantment ench = manager.getEnchantment("log_fortune");
            if (ench != null && ench instanceof LogFortuneEnchantment) {
                LogFortuneEnchantment logFortune = (LogFortuneEnchantment) ench;
                if (manager.hasEnchantment(tool, logFortune)) {
                    int level = manager.getLevel(tool, logFortune);
                    double chance;
                    switch (level) {
                        case 1 -> chance = 0.2;
                        case 2 -> chance = 0.30;
                        case 3 -> chance = 0.50;
                        default -> chance = 0.0;
                    }

                    if (Math.random() < chance) {
                        ItemStack drops = new ItemStack(block.getType(), 1);
                        block.getWorld().dropItemNaturally(block.getLocation(), drops);
                    }
                }
            }
        }
    }
}