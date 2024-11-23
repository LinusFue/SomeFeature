package at.leineees.someFeature.Data.CustomMob;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class CustomMobData {
    private int id;
    private final EntityType entityType;
    private final String nameTag;
    private final String command;
    private final Location location;
    private final boolean ai;
    private final boolean silent;
    private final boolean invulnerable;

    public CustomMobData(int id, EntityType entityType, String nameTag, String command, Location location, boolean ai, boolean silent, boolean invulnerable) {
        this.id = id;
        this.entityType = entityType;
        this.nameTag = nameTag;
        this.command = command;
        this.location = location;
        this.ai = ai;
        this.silent = silent;
        this.invulnerable = invulnerable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public String getNameTag() {
        return nameTag;
    }

    public String getCommand() {
        return command;
    }

    public Location getLocation() {
        return location;
    }

    public boolean hasAI() {
        return ai;
    }

    public boolean isSilent() {
        return silent;
    }

    public boolean isInvulnerable() {
        return invulnerable;
    }
}