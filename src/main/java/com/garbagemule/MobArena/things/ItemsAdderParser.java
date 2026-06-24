package com.garbagemule.MobArena.things;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.inventory.ItemStack;

class ItemsAdderParser implements ItemStackParser {

    @Override
    public ItemStack parse(String s) {
        String namespacedId;
        int amount = 1;

        String[] parts = s.split(":");
        if (parts.length >= 3) {
            try {
                amount = Integer.parseInt(parts[parts.length - 1]);
                namespacedId = parts[0] + ":" + parts[1];
            } catch (NumberFormatException e) {
                namespacedId = s;
            }
        } else {
            namespacedId = s;
        }

        CustomStack stack = CustomStack.getInstance(namespacedId);
        if (stack == null) {
            return null;
        }

        ItemStack item = stack.getItemStack().clone();
        if (amount > 1) {
            item.setAmount(amount);
        }
        return item;
    }
}
