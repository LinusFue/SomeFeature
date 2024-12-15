package at.leineees.someFeature.CustomItems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomRecipies {
    private JavaPlugin plugin;
    public CustomRecipies(JavaPlugin plugin){
        this.plugin = plugin;
    }
    public void register(){
        Bukkit.addRecipe(treeFellaRecipe());
    }
    
    private ShapelessRecipe treeFellaRecipe(){
        ShapelessRecipe recipeTreeFella = new ShapelessRecipe(new NamespacedKey("somefeature", "tree_fella_recipe"), CustomItems.createTreeFella());
        recipeTreeFella.addIngredient(Material.NETHERITE_AXE);
        recipeTreeFella.addIngredient(Material.OAK_LOG)
                .addIngredient(Material.SPRUCE_LOG)
                .addIngredient(Material.BIRCH_LOG)
                .addIngredient(Material.JUNGLE_LOG)
                .addIngredient(Material.ACACIA_LOG)
                .addIngredient(Material.DARK_OAK_LOG)
                .addIngredient(Material.CRIMSON_STEM)
                .addIngredient(Material.WARPED_STEM);
        return recipeTreeFella;
    }
}
