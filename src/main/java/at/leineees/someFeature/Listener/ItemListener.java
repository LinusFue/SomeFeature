package at.leineees.someFeature.Listener;

import at.leineees.someFeature.CustomItems.CustomItems;
import at.leineees.someFeature.CustomItems.CustomRecipeBook;
import at.leineees.someFeature.Data.BackpackManager;
import at.leineees.someFeature.SomeFeature;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.*;

import static at.leineees.someFeature.CustomItems.CustomItems.MULTI_TOOL;

public class ItemListener implements Listener {

    private static final int TREE_FELLA_BLOCK_LIMIT = 300;
    private final HashMap<UUID, Long> cooldowns = new HashMap<>();
    private final Map<UUID, Double> playerBoosts = new HashMap<>();

    @EventHandler
    public void onPlayerItemHeldFlyFeather(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack newItem = player.getInventory().getItem(event.getNewSlot());
        ItemStack previousItem = player.getInventory().getItem(event.getPreviousSlot());

        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            if (newItem != null && newItem.hasItemMeta() && newItem.getItemMeta().hasDisplayName() &&
                    newItem.getItemMeta().getDisplayName().equals("§6Flight Feather")) {
                player.setAllowFlight(true);
                player.sendMessage("§aFlight enabled!");
            } else if (previousItem != null && previousItem.hasItemMeta() && previousItem.getItemMeta().hasDisplayName() &&
                    previousItem.getItemMeta().getDisplayName().equals("§6Flight Feather")) {
                player.setAllowFlight(false);
                player.sendMessage("§cFlight disabled!");
            }
        } else {
            if (newItem != null && newItem.hasItemMeta() && newItem.getItemMeta().hasDisplayName() &&
                    newItem.getItemMeta().getDisplayName().equals("§6Flight Feather")) {
                player.sendMessage("§7You already can fly!");
            }
        }
    }


    @EventHandler
    public void onPlayerUseGrapplingHook(PlayerFishEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        if (cooldowns.containsKey(playerId) && (currentTime - cooldowns.get(playerId)) < 3000) {
            player.sendMessage("§cYou must wait before using the grappling hook again!");
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null && item.getType() == Material.FISHING_ROD && item.getItemMeta() != null &&
                ("§7Grappling Hook").equals(item.getItemMeta().getDisplayName())) {
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
                ("§6Tree Fella").equals(item.getItemMeta().getDisplayName())) {
            Block block = event.getBlock();
            if (isLog(block.getType())) {
                breakTree(block, TREE_FELLA_BLOCK_LIMIT);
            }
        }
        if (item != null && item.getType() == Material.NETHERITE_PICKAXE && item.getItemMeta() != null &&
                ("§5Super Pickaxe").equals(item.getItemMeta().getDisplayName())) {
            Block block = event.getBlock();
            break3x3Area(block, player, item);
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        if (item != null && item.getItemMeta() != null) {
            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
            if (CustomItems.isCustomItem(item)
                    && container.get(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING).startsWith("somefeature:healing_spell")
                    && item.getType() == Material.EMERALD) {
                int cooldown;
                double healAmount;

                String key = container.get(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING);
                int level = Integer.parseInt(key.split("_")[key.split("_").length - 1]);
                switch (level) {
                    case 1:
                        cooldown = 20000;
                        healAmount = 3.0;
                        break;
                    case 2:
                        cooldown = 10000;
                        healAmount = 4.0;
                        break;
                    case 3:
                        cooldown = 7000;
                        healAmount = 6.0;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid level: " + level);
                }

                if (cooldowns.containsKey(playerId) && (currentTime - cooldowns.get(playerId)) < cooldown) {
                    player.sendMessage("§cYou must wait before using the healing spell again!");
                    return;
                }

                double newHealth = Math.min(player.getHealth() + healAmount, player.getMaxHealth());
                player.setHealth(newHealth);
                player.sendMessage("§aYou have been healed for " + (healAmount / 2) + " hearts!");

                cooldowns.put(playerId, currentTime);
            }
            if (CustomItems.isCustomItem(item)
                    && container.get(SomeFeature.CUSTOM_ITEM_KEY, PersistentDataType.STRING).startsWith("somefeature:recipe_book")
                    && item.getType() == Material.BOOK) {
                CustomRecipeBook.openRecipeBook(player);
            }
            if (item.getType() == Material.NAUTILUS_SHELL && item.hasItemMeta()) {
                container = item.getItemMeta().getPersistentDataContainer();
                if (container.has(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING) &&
                        container.get(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"), PersistentDataType.STRING).equals("somefeature:backpack")) {

                    // Open backpack inventory
                    event.getPlayer().openInventory(BackpackManager.getBackpack(event.getPlayer()));
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals("Backpack")) {
            BackpackManager.saveBackpack((Player) event.getPlayer(), event.getInventory());
        }
    }

    @EventHandler
    public void onPlayerLookAtBlock(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();

        // Check if held item is MultiTool
        if (!isMultiTool(mainHand)) {
            return;
        }

        Block targetBlock = player.getTargetBlock(null, 5);
        Material blockType = targetBlock.getType();
        ItemStack newTool = getAppropriateToolForBlock(blockType, mainHand);

        // Transfer the MultiTool data and name
        transferMultiToolProperties(mainHand, newTool);

        player.getInventory().setItemInMainHand(newTool);
    }


    @EventHandler
    public void onInventoryChange(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        // Schedule a task to run in the next tick to ensure inventory contents are updated
        Bukkit.getScheduler().runTask(SomeFeature.getInstance(), () -> updatePlayerHealthBoost(player));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updatePlayerHealthBoost(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        removeHealthBoost(event.getPlayer());
        playerBoosts.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Bukkit.getScheduler().runTask(SomeFeature.getInstance(), () -> updatePlayerHealthBoost(event.getPlayer()));
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            Bukkit.getScheduler().runTask(SomeFeature.getInstance(), () -> updatePlayerHealthBoost(player));
        }
    }


    /**
     * //more Methods for main Methods
     **/

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
                } else {
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
                material == Material.CRIMSON_STEM || material == Material.WARPED_STEM || material == Material.CHERRY_LOG;
    }

    private boolean isLeaf(Material material) {
        return material == Material.OAK_LEAVES || material == Material.SPRUCE_LEAVES || material == Material.BIRCH_LEAVES ||
                material == Material.JUNGLE_LEAVES || material == Material.ACACIA_LEAVES || material == Material.DARK_OAK_LEAVES ||
                material == Material.NETHER_WART_BLOCK || material == Material.WARPED_WART_BLOCK || material == Material.CHERRY_LEAVES;
    }

    private void breakTree(Block startBlock, int blockLimit) {
        Set<Block> blocksToBreak = new HashSet<>();
        findTreeBlocks(startBlock, blocksToBreak, blockLimit);

        for (Block block : blocksToBreak) {
            block.breakNaturally();
        }
    }

    private void findTreeBlocks(Block block, Set<Block> blocksToBreak, int blockLimit) {
        if (blocksToBreak.size() >= blockLimit) {
            return;
        }
        blocksToBreak.add(block);

        for (Block relative : getAdjacentBlocks(block)) {
            if ((isLog(relative.getType()) || isLeaf(relative.getType())) && !blocksToBreak.contains(relative)) {
                findTreeBlocks(relative, blocksToBreak, blockLimit);
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
    public void onPlayerFall(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                ItemStack item = player.getInventory().getItemInMainHand();
                if (item != null && item.getType() == Material.FISHING_ROD && item.getItemMeta() != null &&
                        ("§7Grappling Hook").equals(item.getItemMeta().getDisplayName())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    private boolean isMultiTool(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"),
                PersistentDataType.STRING) &&
                meta.getDisplayName().equals("§6Multi Tool");
    }

    private ItemStack getAppropriateToolForBlock(Material blockType, ItemStack currentTool) {
        Material toolMaterial;

        if (blockType == Material.AIR) {
            toolMaterial = Material.DIAMOND;
        } else if (isAxeMaterial(blockType)) {
            toolMaterial = Material.NETHERITE_AXE;
        } else if (isShovelMaterial(blockType)) {
            toolMaterial = Material.NETHERITE_SHOVEL;
        } else if (isPickaxeMaterial(blockType)) {
            toolMaterial = Material.NETHERITE_PICKAXE;
        } else {
            toolMaterial = Material.DIAMOND;
        }

        ItemStack newTool = new ItemStack(toolMaterial);
        ItemMeta newMeta = newTool.getItemMeta();
        if (newMeta != null) {
            newMeta.isUnbreakable();
        }

        return newTool;
    }

    private void transferMultiToolProperties(ItemStack source, ItemStack target) {
        ItemMeta sourceMeta = source.getItemMeta();
        ItemMeta targetMeta = target.getItemMeta();

        if (sourceMeta == null || targetMeta == null) return;

        // Transfer name
        targetMeta.setDisplayName("§6Multi Tool");

        // Transfer lore
        targetMeta.setLore(List.of("§8Allows you to bundle multiple tools into one!"));

        // Transfer enchantments
        sourceMeta.getEnchants().forEach((enchant, level) ->
                targetMeta.addEnchant(enchant, level, true));

        // Transfer persistent data
        PersistentDataContainer sourceContainer = sourceMeta.getPersistentDataContainer();
        PersistentDataContainer targetContainer = targetMeta.getPersistentDataContainer();

        targetContainer.set(
                new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"),
                PersistentDataType.STRING,
                MULTI_TOOL.toString()
        );

        target.setItemMeta(targetMeta);
    }

    private boolean isAxeMaterial(Material material) {
        return material.name().contains("LOG") ||
                material.name().contains("WOOD") ||
                material.name().contains("PLANKS") ||
                material == Material.BOOKSHELF ||
                material == Material.PUMPKIN ||
                material == Material.JACK_O_LANTERN ||
                material == Material.MELON ||
                material == Material.LADDER ||
                material == Material.BARREL ||
                material == Material.CHEST ||
                material == Material.TRAPPED_CHEST ||
                material == Material.CRAFTING_TABLE ||
                material == Material.LECTERN ||
                material == Material.BEEHIVE ||
                material == Material.BEE_NEST ||
                material == Material.BAMBOO;
    }

    private boolean isShovelMaterial(Material material) {
        return material == Material.DIRT ||
                material == Material.GRASS_BLOCK ||
                material == Material.SAND ||
                material == Material.GRAVEL ||
                material == Material.CLAY ||
                material == Material.COARSE_DIRT ||
                material == Material.PODZOL ||
                material == Material.MYCELIUM ||
                material == Material.SNOW ||
                material == Material.SNOW_BLOCK ||
                material == Material.SOUL_SAND ||
                material == Material.SOUL_SOIL ||
                material == Material.RED_SAND ||
                material == Material.ROOTED_DIRT ||
                material == Material.MUD ||
                material == Material.MOSS_BLOCK ||
                material == Material.MOSS_CARPET;
    }

    private boolean isPickaxeMaterial(Material material) {
        return material.name().contains("STONE") ||
                material.name().contains("ORE") ||
                material == Material.OBSIDIAN ||
                material == Material.ANDESITE ||
                material == Material.BASALT ||
                material == Material.BLACKSTONE ||
                material == Material.COBBLESTONE ||
                material == Material.DEEPSLATE ||
                material == Material.DIORITE ||
                material == Material.END_STONE ||
                material == Material.GRANITE ||
                material == Material.NETHERRACK ||
                material == Material.PRISMARINE ||
                material == Material.QUARTZ_BLOCK ||
                material == Material.SANDSTONE ||
                material == Material.RED_SANDSTONE ||
                material == Material.TERRACOTTA ||
                material == Material.RED_TERRACOTTA ||
                material == Material.YELLOW_TERRACOTTA ||
                material == Material.BLUE_TERRACOTTA ||
                material == Material.GREEN_TERRACOTTA ||
                material == Material.PURPLE_TERRACOTTA ||
                material == Material.BROWN_TERRACOTTA ||
                material == Material.WHITE_TERRACOTTA ||
                material == Material.LIGHT_GRAY_TERRACOTTA ||
                material == Material.GRAY_TERRACOTTA ||
                material == Material.BLACK_TERRACOTTA ||
                material == Material.PINK_TERRACOTTA ||
                material == Material.LIME_TERRACOTTA ||
                material == Material.CYAN_TERRACOTTA ||
                material == Material.MAGENTA_TERRACOTTA ||
                material == Material.ORANGE_TERRACOTTA ||
                material == Material.ENCHANTING_TABLE ||
                material == Material.ANVIL ||
                material == Material.CHIPPED_ANVIL ||
                material == Material.DAMAGED_ANVIL;
    }

    private void updatePlayerHealthBoost(Player player) {
        // Remove existing boost first
        removeHealthBoost(player);

        // Check inventory for Life Ring
        double highestBoost = 0.0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == Material.GOLD_NUGGET && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                PersistentDataContainer container = meta.getPersistentDataContainer();
                String customItemKey = container.get(
                        new NamespacedKey(SomeFeature.getInstance(), "custom_item_key"),
                        PersistentDataType.STRING
                );

                if (customItemKey != null) {
                    double boost = 0.0;
                    if (customItemKey.equals(CustomItems.LIFE_RING_1.toString())) boost = 5.0;
                    else if (customItemKey.equals(CustomItems.LIFE_RING_2.toString())) boost = 10.0;
                    else if (customItemKey.equals(CustomItems.LIFE_RING_3.toString())) boost = 20.0;

                    highestBoost = Math.max(highestBoost, boost);
                }
            }
        }
        // Apply the highest boost found
        if (highestBoost > 0) {
            player.setMaxHealth(20 + highestBoost);
            player.setHealth(Math.min(player.getHealth() + highestBoost, player.getMaxHealth()));
            playerBoosts.put(player.getUniqueId(), highestBoost);
        }
    }

    private void removeHealthBoost(Player player) {
        Double currentBoost = playerBoosts.get(player.getUniqueId());
        if (currentBoost != null) {
            player.setMaxHealth(20); // Reset to default max health
            player.setHealth(Math.min(player.getHealth(), 20)); // Ensure health doesn't exceed new max
            playerBoosts.remove(player.getUniqueId());
        }
    }
}