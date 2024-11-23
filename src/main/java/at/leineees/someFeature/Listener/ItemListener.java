package at.leineees.someFeature.Listener;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;

public class ItemListener implements Listener {

    private final HashMap<UUID, Long> cooldowns = new HashMap<>();



    @EventHandler
    public void onPlayerItemHeldFlyFeather(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());

        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            if (newItem != null && newItem.hasItemMeta() && newItem.getItemMeta().hasDisplayName() &&
                    newItem.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Flight Feather")) {
                player.setAllowFlight(true);
                player.sendMessage(ChatColor.GREEN + "Flight enabled!");
            } else if (previousItem != null && previousItem.hasItemMeta() && previousItem.getItemMeta().hasDisplayName() &&
                    previousItem.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Flight Feather")) {
                player.setAllowFlight(false);
                player.sendMessage(ChatColor.RED + "Flight disabled!");
            }
        } else {
            if (newItem != null && newItem.hasItemMeta() && newItem.getItemMeta().hasDisplayName() &&
                    newItem.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "Flight Feather")) {
                player.sendMessage(ChatColor.GRAY + "You already can fly!");
            }
        }
    }


    @EventHandler
    public void onPlayerUseGrapplingHook(PlayerFishEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        if (cooldowns.containsKey(playerId) && (currentTime - cooldowns.get(playerId)) < 3000) {
            player.sendMessage(ChatColor.RED + "You must wait before using the grappling hook again!");
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null && item.getType() == Material.FISHING_ROD && item.getItemMeta() != null &&
                (ChatColor.GRAY + "Grappling Hook").equals(item.getItemMeta().getDisplayName())) {
            if (event.getState() == PlayerFishEvent.State.REEL_IN || event.getState() == PlayerFishEvent.State.IN_GROUND) {
                Location hookLocation = event.getHook().getLocation();
                Vector direction = hookLocation.toVector().subtract(player.getLocation().toVector()).normalize();
                player.setVelocity(direction.multiply(2)); // Adjust the multiplier for speed
                player.setFallDistance(0); // Reset fall distance to prevent fall damage
                cooldowns.put(playerId, currentTime); // Update the cooldown
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item != null && item.getType() == Material.NETHERITE_AXE && item.getItemMeta() != null &&
                (ChatColor.GOLD + "Tree Fella").equals(item.getItemMeta().getDisplayName())) {
            Block block = event.getBlock();
            if (isLog(block.getType())) {
                breakTree(block);
            }
        }
        if (item != null && item.getType() == Material.NETHERITE_PICKAXE && item.getItemMeta() != null &&
                (ChatColor.DARK_PURPLE + "Super Pickaxe").equals(item.getItemMeta().getDisplayName())) {
            Block block = event.getBlock();
            break3x3Area(block, player, item);
        }
    }
    
    

    //more Methods for main Methods

    private void break3x3Area(Block startBlock, Player player, ItemStack item) {
        Set<Block> blocksToBreak = new HashSet<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Block relative = startBlock.getRelative(x, y, z);
                    if (relative.getType() != Material.AIR && !isUnbreakable(relative.getType())) {
                        blocksToBreak.add(relative);
                    }
                }
            }
        }

        for (Block block : blocksToBreak) {
            if (player.getGameMode().equals(GameMode.CREATIVE)) {
                block.setType(Material.AIR); // Remove the block without dropping items
            } else {
                if (item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) { //silktouch enchantment
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(block.getType()));
                    block.setType(Material.AIR); // Remove the block
                } else if (item.getEnchantments().containsKey(Enchantment.FORTUNE)) { // Fortune enchantment
                    int fortuneLevel = item.getEnchantmentLevel(Enchantment.FORTUNE);
                    Collection<ItemStack> drops = block.getDrops(item);
                    for (ItemStack drop : drops) {
                        drop.setAmount(drop.getAmount() * (fortuneLevel + 1));
                        block.getWorld().dropItemNaturally(block.getLocation(), drop);
                    }
                    block.setType(Material.AIR); // Remove the block
                }else {
                    block.breakNaturally(item);
                }
            }
        }
    }

    private boolean isUnbreakable(Material material) {
        return material == Material.BEDROCK || material == Material.BARRIER || material == Material.END_PORTAL_FRAME ||
                material == Material.END_PORTAL || material == Material.NETHER_PORTAL || material == Material.COMMAND_BLOCK ||
                material == Material.CHAIN_COMMAND_BLOCK || material == Material.REPEATING_COMMAND_BLOCK;
    }
    
    private boolean isLog(Material material) {
        return material == Material.OAK_LOG || material == Material.SPRUCE_LOG || material == Material.BIRCH_LOG ||
                material == Material.JUNGLE_LOG || material == Material.ACACIA_LOG || material == Material.DARK_OAK_LOG ||
                material == Material.CRIMSON_STEM || material == Material.WARPED_STEM;
    }

    private boolean isLeaf(Material material) {
        return material == Material.OAK_LEAVES || material == Material.SPRUCE_LEAVES || material == Material.BIRCH_LEAVES ||
                material == Material.JUNGLE_LEAVES || material == Material.ACACIA_LEAVES || material == Material.DARK_OAK_LEAVES ||
                material == Material.NETHER_WART_BLOCK || material == Material.WARPED_WART_BLOCK;
    }

    private void breakTree(Block startBlock) {
        Set<Block> blocksToBreak = new HashSet<>();
        findTreeBlocks(startBlock, blocksToBreak);

        for (Block block : blocksToBreak) {
            block.breakNaturally();
        }
    }

    private void findTreeBlocks(Block block, Set<Block> blocksToBreak) {
        if (blocksToBreak.contains(block)) {
            return;
        }

        blocksToBreak.add(block);

        for (Block relative : getAdjacentBlocks(block)) {
            if (isLog(relative.getType()) || isLeaf(relative.getType())) {
                findTreeBlocks(relative, blocksToBreak);
            }
        }
    }

    private Set<Block> getAdjacentBlocks(Block block) {
        Set<Block> adjacentBlocks = new HashSet<>();
        adjacentBlocks.add(block.getRelative(1, 0, 0));
        adjacentBlocks.add(block.getRelative(-1, 0, 0));
        adjacentBlocks.add(block.getRelative(0, 1, 0));
        adjacentBlocks.add(block.getRelative(0, -1, 0));
        adjacentBlocks.add(block.getRelative(0, 0, 1));
        adjacentBlocks.add(block.getRelative(0, 0, -1));
        return adjacentBlocks;
    }
    
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.isGliding() && player.isOnGround()) {
            player.setGliding(false);
            player.sendMessage(ChatColor.RED + "You have landed and stopped gliding.");
        }
    }

    @EventHandler
    public void onPlayerFall(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item != null && item.getType() == Material.FISHING_ROD && item.getItemMeta() != null &&
                        (ChatColor.GRAY + "Grappling Hook").equals(item.getItemMeta().getDisplayName()) ||
                        item != null && item.getType() == Material.DIAMOND_SHOVEL && item.getItemMeta() != null &&
                                (ChatColor.DARK_PURPLE + "Aspect of the Void").equals(item.getItemMeta().getDisplayName())) {
                    event.setCancelled(true); // Cancel fall damage
                }
            }
        }
    }
}