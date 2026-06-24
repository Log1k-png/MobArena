package com.garbagemule.MobArena.waves;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MythicMobsBridge {

    private static final String PREFIX = "mythic:";
    private static MythicMobsBridge instance;

    private final boolean enabled;
    private final MythicBukkit mythicPlugin;
    private final Logger logger;

    private MythicMobsBridge(Logger logger) {
        this.logger = logger;
        Plugin plugin = Bukkit.getPluginManager().getPlugin("MythicMobs");
        if (plugin instanceof MythicBukkit) {
            this.mythicPlugin = (MythicBukkit) plugin;
            this.enabled = true;
            logger.info("MythicMobs detected, enabling mythic: mob support.");
        } else {
            this.mythicPlugin = null;
            this.enabled = false;
        }
    }

    public static synchronized MythicMobsBridge init(Logger logger) {
        if (instance == null) {
            instance = new MythicMobsBridge(logger);
        }
        return instance;
    }

    public static MythicMobsBridge getInstance() {
        if (instance == null) {
            throw new IllegalStateException("MythicMobsBridge not initialized");
        }
        return instance;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public LivingEntity spawnMythicMob(String internalName, Location loc) {
        if (!enabled) {
            logger.severe("Cannot spawn MythicMobs mob '" + internalName + "': MythicMobs is not installed!");
            return null;
        }
        try {
            ActiveMob activeMob = mythicPlugin.getMobManager().spawnMob(internalName, loc);
            if (activeMob == null) {
                logger.warning("MythicMobs returned null for mob '" + internalName + "'. Does it exist?");
                return null;
            }
            return (LivingEntity) activeMob.getEntity().getBukkitEntity();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to spawn MythicMobs mob '" + internalName + "'", e);
            return null;
        }
    }

    private List<String> getMobNames() {
        if (!enabled) {
            return new ArrayList<>();
        }
        Collection<String> names = mythicPlugin.getMobManager().getMobNames();
        return new ArrayList<>(names);
    }

    public void registerMythicCreatures() {
        if (!enabled) {
            return;
        }
        List<String> mobNames = getMobNames();
        for (String mobName : mobNames) {
            String key = PREFIX + mobName;
            String squashed = squash(key);
            MythicCreature creature = new MythicCreature(mobName);
            MACreature.register(key, creature);
            MACreature.register(squashed, creature);
        }
        if (!mobNames.isEmpty()) {
            logger.info("Registered " + mobNames.size() + " MythicMobs creatures with 'mythic:' prefix.");
        }
    }

    private static String squash(String string) {
        return string
            .replaceAll("[-_.]", "")
            .toLowerCase();
    }
}
