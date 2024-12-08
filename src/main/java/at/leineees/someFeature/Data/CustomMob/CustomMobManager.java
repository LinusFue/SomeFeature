package at.leineees.someFeature.Data.CustomMob;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomMobManager {

    private final JavaPlugin plugin;
    private final List<CustomMobData> customMobs;
    private final File dataFile;
    private final FileConfiguration dataConfig;
    private final AtomicInteger idCounter;
    private final Map<LivingEntity, String> entityCommands;


    public CustomMobManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.customMobs = new ArrayList<>();
        this.dataFile = new File(plugin.getDataFolder(), "customMobs.yml");
        this.dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        this.idCounter = new AtomicInteger(dataConfig.getInt("lastId", 0));
        this.entityCommands = new HashMap<>(); // Initialize the map
        loadCustomMobs();
    }

    public void spawnCustomMob(CustomMobData customMobData) {
        int id = idCounter.incrementAndGet();
        LivingEntity entity = (LivingEntity) customMobData.getLocation().getWorld().spawnEntity(customMobData.getLocation(), customMobData.getEntityType());
        entity.setCustomName(customMobData.getNameTag());
        entity.setCustomNameVisible(true);
        entity.setAI(customMobData.hasAI());
        entity.setInvulnerable(customMobData.isInvulnerable());
        entity.setSilent(customMobData.isSilent());
        customMobs.add(customMobData);
        entityCommands.put(entity, customMobData.getCommand());
        saveCustomMobs();
    }

    public void removeCustomMob(int id) {
        customMobs.removeIf(mob -> {
            if (mob.getId() == id) {
                mob.getLocation().getWorld().getEntities().stream()
                        .filter(e -> e instanceof LivingEntity && mob.getNameTag().equals(e.getCustomName()))
                        .forEach(e -> {
                            entityCommands.remove(e);
                            ((LivingEntity) e).remove();
                        });
                dataConfig.set("mobs." + id, null); // Remove from dataConfig
                return true;
            }
            return false;
        });
        saveCustomMobs();
        try {
            dataConfig.save(dataFile); // Save the updated dataConfig
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeCustomMobByNameTag(String nameTag) {
        customMobs.removeIf(mob -> {
            if (mob.getNameTag().equalsIgnoreCase(nameTag)) {
                mob.getLocation().getWorld().getEntities().stream()
                        .filter(e -> e instanceof LivingEntity && mob.getNameTag().equals(e.getCustomName()))
                        .forEach(e -> {
                            entityCommands.remove(e);
                            ((LivingEntity) e).remove();
                        });
                dataConfig.set("mobs." + mob.getId(), null); // Remove from dataConfig
                return true;
            }
            return false;
        });
        saveCustomMobs();
        try {
            dataConfig.save(dataFile); // Save the updated dataConfig
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeAllCustomMobs() {
        for (CustomMobData mob : new ArrayList<>(customMobs)) {
            mob.getLocation().getWorld().getEntities().stream()
                    .filter(e -> e instanceof LivingEntity && mob.getNameTag().equals(e.getCustomName()))
                    .forEach(e -> {
                        entityCommands.remove(e);
                        ((LivingEntity) e).remove();
                    });
        }
        idCounter.set(0);
        customMobs.clear();
        dataConfig.set("mobs", null);
        saveCustomMobs();
    }

    public void saveCustomMobs() {
        dataConfig.set("lastId", idCounter.get());
        for (CustomMobData mob : customMobs) {
            String path = "mobs." + mob.getId();
            dataConfig.set(path + ".entityType", mob.getEntityType().name());
            dataConfig.set(path + ".command", mob.getCommand());
            dataConfig.set(path + ".nameTag", mob.getNameTag());
            dataConfig.set(path + ".location.world", mob.getLocation().getWorld().getName());
            dataConfig.set(path + ".location.x", mob.getLocation().getX());
            dataConfig.set(path + ".location.y", mob.getLocation().getY());
            dataConfig.set(path + ".location.z", mob.getLocation().getZ());
            dataConfig.set(path + ".ai", mob.hasAI());
            dataConfig.set(path + ".silent", mob.isSilent());
            dataConfig.set(path + ".invulnerable", mob.isInvulnerable());
        }
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCustomMobs() {
        if (dataConfig.contains("mobs")) {
            for (String key : dataConfig.getConfigurationSection("mobs").getKeys(false)) {
                int id = Integer.parseInt(key);
                EntityType entityType = EntityType.valueOf(dataConfig.getString("mobs." + key + ".entityType"));
                String command = dataConfig.getString("mobs." + key + ".command");
                String nameTag = dataConfig.getString("mobs." + key + ".nameTag");
                String worldName = dataConfig.getString("mobs." + key + ".location.world");
                double x = dataConfig.getDouble("mobs." + key + ".location.x");
                double y = dataConfig.getDouble("mobs." + key + ".location.y");
                double z = dataConfig.getDouble("mobs." + key + ".location.z");
                boolean ai = dataConfig.getBoolean("mobs." + key + ".ai");
                boolean silent = dataConfig.getBoolean("mobs." + key + ".silent");
                boolean invulnerable = dataConfig.getBoolean("mobs." + key + ".invulnerable");

                World world = Bukkit.getWorld(worldName);
                if (world != null) {
                    Location location = new Location(world, x, y, z);
                    boolean mobExists = world.getEntities().stream()
                            .filter(e -> e instanceof LivingEntity)
                            .anyMatch(e -> nameTag.equals(e.getCustomName()) && e.getLocation().equals(location));

                    if (!mobExists) {
                        CustomMobData customMobData = new CustomMobData(id, entityType, nameTag, command, location, ai, silent, invulnerable);
                        spawnCustomMob(customMobData);
                    } else {
                        plugin.getLogger().info("Custom mob with name tag " + nameTag + " already exists at location " + location);
                    }
                } else {
                    plugin.getLogger().warning("World " + worldName + " not found for custom mob with ID " + id);
                }
            }
        }
    }
    
    public List<CustomMobData> getCustomMobs() {
        return customMobs;
    }


    public int getLastId() {
        return idCounter.get();
    }
}