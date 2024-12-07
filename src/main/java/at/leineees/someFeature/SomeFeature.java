package at.leineees.someFeature;

import at.leineees.someFeature.Commands.*;
import at.leineees.someFeature.CustomItems.CustomItems;
import at.leineees.someFeature.CustomItems.CustomRecipies;
import at.leineees.someFeature.Data.CustomMob.CustomMobManager;
import at.leineees.someFeature.Data.Coins.CoinManager;
import at.leineees.someFeature.Economy.CustomItemShop;
import at.leineees.someFeature.Enchantments.EnchantmentListener;
import at.leineees.someFeature.Enchantments.EnchantmentManager;
import at.leineees.someFeature.Feature.CustomScoreboardManager;
import at.leineees.someFeature.Listener.*;
import at.leineees.someFeature.TabCompleter.CustomEnchantmentTabCompleter;
import at.leineees.someFeature.TabCompleter.CustomItemTabCompleter;
import at.leineees.someFeature.TabCompleter.CustomMobTabCompleter;
import at.leineees.someFeature.Task.TablistTask;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public final class SomeFeature extends JavaPlugin {

    private CustomItems customItems;
    private CustomRecipies customRecipies;
    private CoinManager coinManager;
    private CustomItemShop customItemShop;
    private CustomMobManager customMobManager;
    private CustomMob customMob;
    private CustomScoreboardManager customScoreboardManager;
    private EnchantmentManager manager;
    
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
        Bukkit.broadcastMessage(ChatColor.GREEN + "SomeFeature enabled");
        SomeFeatureSettings.getInstance().load();
        
        CUSTOM_ITEM_KEY = new NamespacedKey(this, "custom_item");


        //Definitions
        customScoreboardManager = new CustomScoreboardManager(this);
        
        customItems = new CustomItems();
        customRecipies = new CustomRecipies(this);
        coinManager = new CoinManager(this, customScoreboardManager);
        customItemShop = new CustomItemShop(coinManager, customItems);
        customMobManager = new CustomMobManager(this);
        customMob = new CustomMob(this, customMobManager);
        manager = new EnchantmentManager(this);
        
        customRecipies.register();
        customMobManager.loadCustomMobs();

        //AddPlugins
        getServer().getPluginManager().registerEvents(SpawnElytraFly.create(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(customScoreboardManager, coinManager), this);
        
        //AddListener
        getServer().getPluginManager().registerEvents(new ItemListener(), this);
        getServer().getPluginManager().registerEvents(customItemShop, this);
        getServer().getPluginManager().registerEvents(customMob, this);
        getServer().getPluginManager().registerEvents(new EnchantmentListener(manager), this);
        getServer().getPluginManager().registerEvents(new AnvilListener(manager, this), this);
        
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);
        
        //AddCommands
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("vanish").setExecutor(new VanishCommand(this));
        getCommand("shop").setExecutor(new ShopCommand(customItemShop));
        getCommand("coins").setExecutor(new CoinsCommand(coinManager));
        getCommand("givecustomitem").setExecutor(new GiveCustomItemCommand(customItems));
        getCommand("spawncustommob").setExecutor(new SpawnCustomMobCommand(customMobManager));
        getCommand("removecustommob").setExecutor(new RemoveCustomMobCommand(customMobManager));
        getCommand("cenchant").setExecutor(new CEnchantCommand(manager));
        getCommand("invsee").setExecutor(new InvseeCommand());
                
                
        //TabCompleter
        this.getCommand("givecustomitem").setTabCompleter(new CustomItemTabCompleter());
        this.getCommand("spawncustommob").setTabCompleter(new CustomMobTabCompleter());
        this.getCommand("cenchant").setTabCompleter(new CustomEnchantmentTabCompleter(manager));
        
        
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
        Bukkit.broadcastMessage(ChatColor.RED + "SomeFeature disabled");
        SomeFeatureSettings.getInstance().save();
        customMobManager.saveCustomMobs();
    }
    
    public static SomeFeature getInstance(){
        return instance;
    }
}