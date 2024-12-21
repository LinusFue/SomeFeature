package at.leineees.someFeature.CustomItems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.Collection;

public class CustomRecipeBook {
    private static final Inventory recipeInventory = Bukkit.createInventory(null, 54, "§aRecipies");

    public CustomRecipeBook() {}

    public void loadRecipes() {
        int slot = 0;
        for (Recipe recipe : CustomRecipies.getRecipes()) {
            if (recipe instanceof ShapedRecipe ||
                    recipe instanceof ShapelessRecipe ||
                    recipe instanceof FurnaceRecipe ||
                    recipe instanceof SmithingRecipe) {
                
                recipeInventory.setItem(slot, recipe.getResult());
                slot++;

                if (slot >= 54) break;
            }
        }
    }

    public static void openRecipeBook(Player player) {
        player.openInventory(recipeInventory);
    }
}
