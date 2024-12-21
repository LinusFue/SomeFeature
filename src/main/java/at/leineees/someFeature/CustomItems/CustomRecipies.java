package at.leineees.someFeature.CustomItems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CustomRecipies {
    private JavaPlugin plugin;
    public CustomRecipies(JavaPlugin plugin){
        this.plugin = plugin;
    }
    private List<Recipe> recipes = new ArrayList<>();
    public void register(){

        ItemStack healingSpell1 = CustomItems.createHealingSpell(1);
        ItemStack healingSpell2 = CustomItems.createHealingSpell(2);
        ItemStack healingSpell3 = CustomItems.createHealingSpell(3);
        
        Bukkit.addRecipe(treeFellaRecipe());
        Bukkit.addRecipe(spellRecipe1(healingSpell1));
        Bukkit.addRecipe(spellRecipe2(healingSpell1, healingSpell2));
        Bukkit.addRecipe(spellRecipe3(healingSpell2, healingSpell3));

        recipes.add(treeFellaRecipe());
        recipes.add(spellRecipe1(healingSpell1));
        recipes.add(spellRecipe2(healingSpell1, healingSpell2));
        recipes.add(spellRecipe3(healingSpell2, healingSpell3));
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
    
    private ShapedRecipe spellRecipe1(ItemStack output){
        ShapedRecipe spellRecipe = new ShapedRecipe(new NamespacedKey("somefeature", "healing_spell1_recipe"), output);
        spellRecipe.shape("DED", "EGE", "DED");
        spellRecipe.setIngredient('G', Material.ENCHANTED_GOLDEN_APPLE);
        spellRecipe.setIngredient('E', Material.EMERALD);
        spellRecipe.setIngredient('D', Material.DIAMOND_BLOCK);
        return spellRecipe;
    }
    private ShapedRecipe spellRecipe2(ItemStack input, ItemStack output){
        ShapedRecipe spellRecipe = new ShapedRecipe(new NamespacedKey("somefeature", "healing_spell2_recipe"), output);
        spellRecipe.shape("ENE", "NHN", "ENE");
        spellRecipe.setIngredient('H', input);
        spellRecipe.setIngredient('N', Material.NETHERITE_INGOT);
        spellRecipe.setIngredient('E', Material.EMERALD_BLOCK);
        return spellRecipe;
    }
    private ShapedRecipe spellRecipe3(ItemStack input, ItemStack output){
        ShapedRecipe spellRecipe = new ShapedRecipe(new NamespacedKey("somefeature", "healing_spell3_recipe"), output);
        spellRecipe.shape("NSN", "SHS", "NSN");
        spellRecipe.setIngredient('H', input);
        spellRecipe.setIngredient('S', Material.NETHER_STAR);
        spellRecipe.setIngredient('N', Material.NETHERITE_INGOT);
        return spellRecipe;
    }
}
