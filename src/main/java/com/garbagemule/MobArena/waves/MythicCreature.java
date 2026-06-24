package com.garbagemule.MobArena.waves;

import com.garbagemule.MobArena.framework.Arena;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class MythicCreature extends MACreature {

    private final String mythicMobName;

    public MythicCreature(String mythicMobName) {
        super(EntityType.ZOMBIE, "mythic:" + mythicMobName);
        this.mythicMobName = mythicMobName;
    }

    @Override
    public LivingEntity spawn(Arena arena, World world, Location loc) {
        LivingEntity e = MythicMobsBridge.getInstance().spawnMythicMob(mythicMobName, loc);
        if (e == null) {
            arena.getPlugin().getLogger().warning(
                "MythicMobs mob '" + mythicMobName + "' failed to spawn, falling back to zombie."
            );
            e = super.spawn(arena, world, loc);
        }
        if (e instanceof org.bukkit.entity.Mob) {
            ((org.bukkit.entity.Mob) e).setTarget(WaveUtils.getClosestPlayer(arena, e));
        }
        return e;
    }
}
