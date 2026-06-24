package com.garbagemule.MobArena.things;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

public class ItemsAdderBridge {

    private static ItemsAdderBridge instance;

    private final boolean enabled;

    private ItemsAdderBridge(Logger logger) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("ItemsAdder");
        if (plugin != null && plugin.isEnabled()) {
            this.enabled = true;
            logger.info("ItemsAdder detected, enabling custom item support.");
        } else {
            this.enabled = false;
        }
    }

    public static synchronized ItemsAdderBridge init(Logger logger) {
        if (instance == null) {
            instance = new ItemsAdderBridge(logger);
        }
        return instance;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void register(ThingManager thingManager) {
        if (!enabled) {
            return;
        }
        thingManager.register(new ItemsAdderParser());
    }
}
