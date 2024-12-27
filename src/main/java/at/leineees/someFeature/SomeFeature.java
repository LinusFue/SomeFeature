package at.leineees.someFeature;

import at.leineees.someFeature.Commands.*;
import at.leineees.someFeature.Commands.SellCommand;
import at.leineees.someFeature.CustomItems.CustomItems;
import at.leineees.someFeature.CustomItems.CustomRecipeBook;
import at.leineees.someFeature.CustomItems.CustomRecipes;
import at.leineees.someFeature.Data.BackpackManager;
import at.leineees.someFeature.Data.Coins.CoinManager;
import at.leineees.someFeature.Economy.CustomItemShop;
import at.leineees.someFeature.Economy.PriceManager;
import at.leineees.someFeature.Economy.SellItems;
import at.leineees.someFeature.Feature.CustomScoreboardManager;
import at.leineees.someFeature.Listener.*;
import at.leineees.someFeature.TabCompleter.*;
import at.leineees.someFeature.Task.TablistTask;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public final class SomeFeature extends JavaPlugin {
    private CustomItemShop customItemShop;
    private PriceManager priceManager;
    private BackpackManager backpackManager;
    
    public static NamespacedKey CUSTOM_ITEM_KEY;
    public static NamespacedKey CUSTOM_ITEM_LEVEL_KEY;
    
    @Override
    public void onEnable() {
        Bukkit.broadcastMessage("§aSomeFeature enabled");
        SomeFeatureSettings.getInstance().load();
        
        CUSTOM_ITEM_KEY = new NamespacedKey(this, "custom_item_key");
        CUSTOM_ITEM_LEVEL_KEY = new NamespacedKey("somefeature", "custom_item_level");


        //Definitions
        CustomItems customItems = new CustomItems();
        CustomScoreboardManager customScoreboardManager = new CustomScoreboardManager(this);
        CoinManager coinManager = new CoinManager(this, customScoreboardManager);
        customItemShop = new CustomItemShop(coinManager, customItems);
        backpackManager = new BackpackManager();


        CustomRecipes customRecipes = new CustomRecipes(this);
        priceManager = new PriceManager(getDataFolder());
        SellItems sellItems = new SellItems(priceManager, coinManager);
        CustomRecipeBook customRecipeBook = new CustomRecipeBook();
        
        customRecipes.register();
        customRecipeBook.loadRecipes();
        priceManager.loadPrices();
        backpackManager.init(getDataFolder());

        //AddPlugins
        getServer().getPluginManager().registerEvents(SpawnElytraFly.create(this), this);
        
        //AddListener
        getServer().getPluginManager().registerEvents(new PlayerListener(customScoreboardManager, coinManager), this);
        getServer().getPluginManager().registerEvents(new ItemListener(), this);
        getServer().getPluginManager().registerEvents(new ArmorItemListener(), this);
        getServer().getPluginManager().registerEvents(customItemShop, this);
        getServer().getPluginManager().registerEvents(sellItems, this);
        
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        
        //AddCommands
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("vanish").setExecutor(new VanishCommand(this));
        getCommand("shop").setExecutor(new ShopCommand(customItemShop));
        getCommand("coins").setExecutor(new CoinsCommand(coinManager));
        getCommand("givecustomitem").setExecutor(new GiveCustomItemCommand());
        getCommand("invsee").setExecutor(new InvseeCommand());
        getCommand("price").setExecutor(new PriceCommand(priceManager));
        getCommand("sell").setExecutor(new SellCommand(sellItems));
        getCommand("spawn").setExecutor(new SpawnCommand(this));
        getCommand("trade").setExecutor(new TradeCommand(coinManager));
                
        
        //TabCompleter
        getCommand("shop").setTabCompleter(new ShopTabCompleter(customItemShop));
        getCommand("givecustomitem").setTabCompleter(new CustomItemTabCompleter());
        getCommand("coins").setTabCompleter(new CoinTabCompleter());
        getCommand("price").setTabCompleter(new PriceTabCompleter(priceManager));
        getCommand("trade").setTabCompleter(new TradeTabCompleter());


        for(Player player : Bukkit.getOnlinePlayers()) {
            if(player.getGameMode().equals(GameMode.ADVENTURE) || player.getGameMode().equals(GameMode.SURVIVAL)) {
                player.setAllowFlight(false);
            }
        }
        
        //Schedulers
        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                coinManager.addCoins(player, 1);
            }
        }, 0L, 1200L); // 1200 ticks = 1 minute
        
        getServer().getScheduler().scheduleSyncRepeatingTask(this, TablistTask.getInstance(), 0L, 20L);
        
    }

    @Override
    public void onDisable() {
        Bukkit.broadcastMessage("§4SomeFeature disabled");
        SomeFeatureSettings.getInstance().save();
        customItemShop.saveShops();
        priceManager.savePrices();
    }
    
    public static SomeFeature getInstance(){
        return getPlugin(SomeFeature.class);
    }
}