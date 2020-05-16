package com.universeguard.event.flags;

import com.universeguard.utils.InventoryUtils;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.scheduler.Task;

import java.util.function.Consumer;

/**
 * @author JimiIT92
 */
public class FlagItemUseTaskListener implements Consumer<Task> {

    private ItemStack itemStack;
    private Player player;

    public FlagItemUseTaskListener(Player player, ItemStack itemStack) {
        this.player = player;
        this.itemStack = itemStack;
    }

    @Override
    public void accept(Task task) {
        InventoryUtils.addItemStackToInventory(player, itemStack);
        task.cancel();
    }
}
