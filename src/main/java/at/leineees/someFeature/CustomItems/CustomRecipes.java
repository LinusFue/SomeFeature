package at.leineees.someFeature.CustomItems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class CustomRecipes {
    private final JavaPlugin plugin;
    public CustomRecipes(JavaPlugin plugin){
        this.plugin = plugin;
    }
    private static final List<Recipe> recipes = new ArrayList<>();
    public static List<Recipe> getRecipes(){
        return recipes;
    }
    private static final List<ItemStack> results = new ArrayList<>();
    public static List<ItemStack> getResults(){
        return results;
    }
    public static Recipe getRecipe(ItemStack result){
        return recipes.get(results.indexOf(result));
    }
    public void register(){
        //outputs
        ItemStack healingSpell1 = CustomItems.createHealingSpell(1);
        ItemStack healingSpell2 = CustomItems.createHealingSpell(2);
        ItemStack healingSpell3 = CustomItems.createHealingSpell(3);
        ItemStack elytraChestplate = CustomItems.createElytraChestplate();
        ItemStack multiToolBase = CustomItems.createMultiToolBase();
        ItemStack multiTool = CustomItems.createMultiTool();
        ItemStack backpack = CustomItems.createBackpack();
        
        recipes.add(spellRecipe1(healingSpell1));
        recipes.add(spellRecipe2(healingSpell1, healingSpell2));
        recipes.add(spellRecipe3(healingSpell2, healingSpell3));
        recipes.add(elytraChestplateRecipe(elytraChestplate));
        recipes.add(multiToolBaseRecipe(multiToolBase));
        recipes.add(multiToolRecipe(multiToolBase, multiTool));
        recipes.add(backpackRecipe(backpack));
        
        for (Recipe recipe : recipes) {
            Bukkit.addRecipe(recipe);
            results.add(recipe.getResult());
        }        
    }
    
    private ShapelessRecipe treeFellaRecipe(ItemStack output){
        ShapelessRecipe recipeTreeFella = new ShapelessRecipe(new NamespacedKey("somefeature", "tree_fella_recipe"), output);
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
    private AnvilRecipe elytraChestplateRecipe(ItemStack output){
        RecipeChoice base = new RecipeChoice.MaterialChoice(Material.NETHERITE_CHESTPLATE);
        RecipeChoice addition = new RecipeChoice.MaterialChoice(Material.ELYTRA);
        return new AnvilRecipe(new NamespacedKey("somefeature", "elytra_chestplate_recipe"), output, base, addition);
    }
    private ShapedRecipe multiToolBaseRecipe(ItemStack output){
        ShapedRecipe multiToolBaseR = new ShapedRecipe(new NamespacedKey("somefeature", "multi-tool-base-recipe"), output);
        multiToolBaseR.shape(" L ", "LCL", " D ");
        multiToolBaseR.setIngredient('D', Material.DIAMOND);
        multiToolBaseR.setIngredient('C', Material.END_CRYSTAL);
        multiToolBaseR.setIngredient('L', Material.LEAD);
        return multiToolBaseR;
    }
    private ShapelessRecipe multiToolRecipe(ItemStack input, ItemStack output) {
        ShapelessRecipe multiToolR = new ShapelessRecipe(new NamespacedKey("somefeature", "multi_tool_recipe"), output);
        multiToolR.addIngredient(input);
        multiToolR.addIngredient(Material.NETHERITE_PICKAXE);
        multiToolR.addIngredient(Material.NETHERITE_SHOVEL);
        multiToolR.addIngredient(Material.NETHERITE_AXE);
        return multiToolR;
    }

    private ShapedRecipe backpackRecipe(ItemStack output){
        ShapedRecipe backpackR = new ShapedRecipe(new NamespacedKey("somefeature", "backpack_recipe"), output);
        backpackR.shape("LCL", "LCL", "LLL");
        backpackR.setIngredient('C', Material.CHEST);
        backpackR.setIngredient('L', Material.LEATHER);
        return backpackR;
    }
}
