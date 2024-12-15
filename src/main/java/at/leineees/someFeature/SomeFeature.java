package at.leineees.someFeature;

import at.leineees.someFeature.Commands.*;
import at.leineees.someFeature.CustomItems.CustomItems;
import at.leineees.someFeature.CustomItems.CustomRecipies;
import at.leineees.someFeature.Data.CustomMob.CustomMobManager;
import at.leineees.someFeature.Data.Coins.CoinManager;
import at.leineees.someFeature.Economy.CustomItemShop;
import at.leineees.someFeature.Feature.CustomScoreboardManager;
import at.leineees.someFeature.Listener.*;
import at.leineees.someFeature.TabCompleter.CoinTabCompleter;
import at.leineees.someFeature.TabCompleter.CustomItemTabCompleter;
import at.leineees.someFeature.TabCompleter.CustomMobTabCompleter;
import at.leineees.someFeature.TabCompleter.ShopTabCompleter;
import at.leineees.someFeature.Task.TablistTask;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public final class SomeFeature extends JavaPlugin {
    private CustomMobManager customMobManager;
    private CustomItemShop customItemShop;
    
    public static NamespacedKey CUSTOM_ITEM_KEY;

    private static SomeFeature instance;
    @Override
    public void onLoad() {
        if (instance != null) {
            throw new IllegalStateException("Plugin already initialized!");
        }
        instance = this;
    }
    @Override
    public void onEnable() {
        Bukkit.broadcastMessage("§aSomeFeature enabled");
        SomeFeatureSettings.getInstance().load();
        
        CUSTOM_ITEM_KEY = new NamespacedKey(this, "custom_item");


        //Definitions
        customMobManager = new CustomMobManager(this);
        CustomItems customItems = new CustomItems();
        CustomScoreboardManager customScoreboardManager = new CustomScoreboardManager(this);
        CoinManager coinManager = new CoinManager(this, customScoreboardManager);
        customItemShop = new CustomItemShop(coinManager, customItems);


        CustomRecipies customRecipies = new CustomRecipies(this);
        CustomMob customMob = new CustomMob(this, customMobManager);
        
        customRecipies.register();
        customMobManager.loadCustomMobs();

        //AddPlugins
        getServer().getPluginManager().registerEvents(SpawnElytraFly.create(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(customScoreboardManager, coinManager), this);
        
        //AddListener
        getServer().getPluginManager().registerEvents(new ItemListener(), this);
        getServer().getPluginManager().registerEvents(customItemShop, this);
        getServer().getPluginManager().registerEvents(customMob, this);
        
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        
        //AddCommands
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("vanish").setExecutor(new VanishCommand(this));
        getCommand("shop").setExecutor(new ShopCommand(customItemShop));
        getCommand("coins").setExecutor(new CoinsCommand(coinManager));
        getCommand("givecustomitem").setExecutor(new GiveCustomItemCommand(customItems));
        getCommand("spawncustommob").setExecutor(new SpawnCustomMobCommand(customMobManager));
        getCommand("removecustommob").setExecutor(new RemoveCustomMobCommand(customMobManager));
        getCommand("invsee").setExecutor(new InvseeCommand());
                
                
        //TabCompleter
        getCommand("shop").setTabCompleter(new ShopTabCompleter(customItemShop));
        getCommand("givecustomitem").setTabCompleter(new CustomItemTabCompleter());
        getCommand("spawncustommob").setTabCompleter(new CustomMobTabCompleter());
        getCommand("coins").setTabCompleter(new CoinTabCompleter());
        
        
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
        customMobManager.saveCustomMobs();
        customItemShop.saveShops();
    }
    
    public static SomeFeature getInstance(){
        return instance;
    }
}