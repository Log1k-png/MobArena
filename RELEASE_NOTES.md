## ItemsAdder Integration

MobArena can now use custom items from [ItemsAdder](https://itemsadder.devs.beer/) as rewards, class items, and wave drops.

### Setup
- Install [ItemsAdder](https://itemsadder.devs.beer/) on your server
- MobArena auto-detects ItemsAdder and registers its custom item parser

### Configuration
Use ItemsAdder's `namespace:item_id` format anywhere MobArena accepts items:

```yaml
# Wave rewards
waves:
  every:
    '5': iasurvival:ruby, iasurvival:magic_dust:4
  after:
    '10': iasurvival:legendary_sword

# Boss reward
  single:
    boss1:
      type: boss
      wave: 10
      monster: mythic:DragonLord
      reward: iasurvival:dragon_sword

# Class items
Knight:
  items: iasurvival:paladin_sword, iasurvival:holy_shield
  helmet: iasurvival:paladin_helmet

# Entry fee
settings:
  entry-fee: iasurvival:gold_coin:5
```

### Amount syntax
Append `:amount` to give multiple items:
- `iasurvival:ruby` — 1 ruby
- `iasurvival:ruby:5` — 5 rubies

### How it works
- ItemsAdder items are resolved at parse time via `CustomStack.getInstance()`
- The `namespace:id` format naturally fits into MobArena's existing item pipeline
- If ItemsAdder is not installed, custom item strings fall through to the vanilla parser (which will log a warning)

---

## MythicMobs 5.x Integration

MobArena can now spawn custom MythicMobs in arena waves using the `mythic:` prefix.

### Setup
- Install [MythicMobs 5.x](https://mythiccraft.io/index.php/resources/mythicmobs.1/) on your server
- MobArena auto-detects MythicMobs and registers all custom mobs

### Configuration
Use `mythic:<InternalName>` in any wave config:

```yaml
# Default wave
waves:
  recurrent:
    def1:
      type: default
      priority: 1
      frequency: 1
      monsters:
        zombies: 10
        mythic:SkeletalKnight: 5
        mythic:GoblinKing: 2

# Boss wave
  single:
    boss1:
      type: boss
      wave: 10
      monster: mythic:DragonLord
      health: high
      abilities: arrows, root-target

# Swarm wave
    swarm1:
      type: swarm
      wave: 6
      monster: mythic:SwarmRat
      amount: low
```

### How it works
- All MythicMobs custom mobs are registered with the `mythic:` prefix at startup
- Mobs retain their MythicMobs skills, equipment, AI, and particle effects
- Health multiplier and potion effects from MobArena waves still apply
- Boss waves work: custom name, abilities, health bar, and rewards
- If MythicMobs is not installed, `mythic:` entries are simply not available (no errors)

---

## Bring Items Class

New class that uses the player's inventory items but **does not restore them** on arena exit or death.

### How it works
- **My Items** (existing): Uses a copy of the player's inventory. Items are restored after the arena.
- **Bring Items** (new): Uses the player's inventory items. Items are **not restored** after the arena — they are lost on death or leaving.

### Victory behavior
- If the player **wins** the arena (completes the final wave), they **keep their items** from the arena.
- If the player **dies** or **leaves** (abandons), their inventory is **cleared** (items are lost).

### Crash safety
If the server crashes while a player is in the arena with the Bring Items class, their original inventory is still restored from the YAML backup file on reconnect.

### Config
You can optionally add a `classes > Bring Items` section in the config to set a price, unbreakable weapons/armor, or other class settings. If no section exists, the class is still available with default settings.
