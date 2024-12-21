package at.leineees.someFeature.Listener;

import at.leineees.someFeature.CustomItems.CustomItems;
import at.leineees.someFeature.CustomItems.CustomRecipeBook;
import at.leineees.someFeature.CustomItems.CustomRecipies;
import at.leineees.someFeature.SomeFeature;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().endsWith("'s Inventory")) {
            Player viewer = (Player) event.getWhoClicked();
            String targetName = event.getView().getTitle().replace("'s Inventory", "");
            Player target = Bukkit.getPlayer(targetName);

            if (target != null) {
                Inventory targetInventory = target.getInventory();
                Inventory clickedInventory = event.getClickedInventory();

                if (clickedInventory != null && clickedInventory.equals(event.getView().getTopInventory())) {
                    int slot = event.getSlot();
                    ItemStack currentItem = event.getCurrentItem();
                    targetInventory.setItem(slot, currentItem);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().endsWith("'s Inventory")) {
            Player viewer = (Player) event.getPlayer();
            String targetName = event.getView().getTitle().replace("'s Inventory", "");
            Player target = Bukkit.getPlayer(targetName);

            if (target != null) {
                Inventory targetInventory = target.getInventory();
                Inventory viewerInventory = event.getInventory();

                for (int i = 0; i < targetInventory.getSize(); i++) {
                    targetInventory.setItem(i, viewerInventory.getItem(i));
                }

                ItemStack[] armorContents = new ItemStack[4];
                for (int i = 0; i < 4; i++) {
                    armorContents[i] = viewerInventory.getItem(36 + i);
                }
                target.getInventory().setArmorContents(armorContents);

                target.getInventory().setItemInOffHand(viewerInventory.getItem(40));
            }
        }
    }

    @EventHandler
    public void onRecipeBookClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("§aRecipies")) {
            event.setCancelled(true);

            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                showRecipeDetails((Player) event.getWhoClicked(), clickedItem);
            }
        }
        if(event.getView().getTitle().startsWith("§6Recipe: ")) {
            event.setCancelled(true);
            ItemStack clickedItem = event.getCurrentItem();
            if(clickedItem.getItemMeta().getDisplayName().equals("§7Back")){
                CustomRecipeBook.openRecipeBook((Player) event.getWhoClicked());
            }
        }
    }

    private void showRecipeDetails(Player player, ItemStack result) {
        Inventory recipeView = Bukkit.createInventory(null, 27, "§6Recipe: " + result.getType().name());

        // Crafting Grid (Slots 10-12, 19-21)
        
        Recipe recipe = CustomRecipies.getRecipe(result);
        
        if (recipe instanceof ShapedRecipe) {
            ShapedRecipe shaped = (ShapedRecipe) recipe;
            String[] shape = shaped.getShape();
            Map<Character, ItemStack> ingrediens = shaped.getIngredientMap();

            recipeView.setItem(0, new ItemStack(Material.CRAFTING_TABLE));
            
            int[] slots = {2, 3, 4, 11, 12, 13, 20, 21, 22};
            int index = 0;
            

            for (String row : shape) {
                for (char c : row.toCharArray()) {
                    if (ingrediens.containsKey(c)) {
                        recipeView.setItem(slots[index], ingrediens.get(c));
                    }
                    index++;
                }
            }
        }
        
        
        if(recipe instanceof ShapelessRecipe){
            ShapelessRecipe shapeless = (ShapelessRecipe) recipe;
            List<ItemStack> ingredients = shapeless.getIngredientList();
            
            recipeView.setItem(0, new ItemStack(Material.CRAFTING_TABLE));
            
            int[] slots = {2, 3, 4, 11, 12, 13, 20, 21, 22};

            
            int index = 0;
            for(ItemStack ingredient : ingredients){
                recipeView.setItem(slots[index], ingredient);
                index++;
            }
        }
        
        if(recipe instanceof FurnaceRecipe){
            FurnaceRecipe furnace = (FurnaceRecipe) recipe;
            ItemStack ingredient = furnace.getInput();
            ItemStack smeltingResult = furnace.getResult();

            recipeView.setItem(0, new ItemStack(Material.FURNACE));
            
            recipeView.setItem(11, ingredient);
            recipeView.setItem(20, smeltingResult);
        }
        
        if(recipe instanceof BlastingRecipe){
            BlastingRecipe blasting = (BlastingRecipe) recipe;
            ItemStack ingredient = blasting.getInput();
            ItemStack smeltingResult = blasting.getResult();

            recipeView.setItem(0, new ItemStack(Material.BLAST_FURNACE));
            
            recipeView.setItem(11, ingredient);
            recipeView.setItem(20, smeltingResult);
        }
        
        if(recipe instanceof CampfireRecipe){
            CampfireRecipe campfire = (CampfireRecipe) recipe;
            ItemStack ingredient = campfire.getInput();
            ItemStack smeltingResult = campfire.getResult();

            recipeView.setItem(0, new ItemStack(Material.CAMPFIRE));
            
            recipeView.setItem(11, ingredient);
            recipeView.setItem(20, smeltingResult);
        }
        
        if(recipe instanceof SmokingRecipe){
            SmokingRecipe smoking = (SmokingRecipe) recipe;
            ItemStack ingredient = smoking.getInput();
            ItemStack smeltingResult = smoking.getResult();

            recipeView.setItem(0, new ItemStack(Material.SMOKER));
            
            recipeView.setItem(11, ingredient);
            recipeView.setItem(20, smeltingResult);
        }
        
        if(recipe instanceof StonecuttingRecipe){
            StonecuttingRecipe stonecutting = (StonecuttingRecipe) recipe;
            ItemStack ingredient = stonecutting.getInput();
            ItemStack smeltingResult = stonecutting.getResult();

            recipeView.setItem(0, new ItemStack(Material.STONECUTTER));
            
            recipeView.setItem(11, ingredient);
            recipeView.setItem(20, smeltingResult);
        }
        
        if(recipe instanceof SmithingRecipe){
            SmithingRecipe smithing = (SmithingRecipe) recipe;
            ItemStack base = smithing.getBase().getItemStack();
            ItemStack addition = smithing.getAddition().getItemStack();
            ItemStack resultSmithing = smithing.getResult();

            recipeView.setItem(0, new ItemStack(Material.SMITHING_TABLE));
            
            recipeView.setItem(10, base);
            recipeView.setItem(12, addition);
            recipeView.setItem(21, resultSmithing);
        }
        
        ItemStack backArrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = backArrow.getItemMeta();
        arrowMeta.setDisplayName("§7Back");
        backArrow.setItemMeta(arrowMeta);
        recipeView.setItem(18, backArrow);
        
        // Result (Slot 15)
        recipeView.setItem(15, result);

        player.openInventory(recipeView);
    }

}