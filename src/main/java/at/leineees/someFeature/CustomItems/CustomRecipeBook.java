package at.leineees.someFeature.CustomItems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.Recipe;

public class CustomRecipeBook {
    private static final Inventory recipeInventory = Bukkit.createInventory(null, 54, "§aRecipies");

    public CustomRecipeBook() {
    }

    public static void openRecipeBook(Player player) {
        player.openInventory(recipeInventory);
    }

    public void loadRecipes() {
        int slot = 0;
        for (Recipe recipe : CustomRecipes.getRecipes()) {
            if (recipe instanceof Recipe) {

                recipeInventory.setItem(slot, recipe.getResult());
                slot++;

                if (slot >= 54) break;
            }
        }
    }
}
