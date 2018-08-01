package io.samdev.smallsilkspawners;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements Listener
{
    private final SmallSilkSpawners plugin;

    BlockBreakListener(SmallSilkSpawners plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event)
    {
        if (event.getClass() != BlockBreakEvent.class)
        {
            // Fake block break event, from things like McMMO
            return;
        }

        if (event.getBlock().getType() != Material.SPAWNER)
        {
            return;
        }

        Player player = event.getPlayer();
        ItemStack usedItem = player.getInventory().getItemInMainHand();

        if (plugin.isNeedsSilkTouch() && (usedItem == null || !plugin.hasSilkTouch(usedItem)))
        {
            return;
        }

        if (!plugin.isDropExp())
        {
            event.setExpToDrop(0);
        }

        BlockState block = event.getBlock().getState();
        short entityId = plugin.getSpawnerEntityId((CreatureSpawner) block);

        ItemStack spawnerStack = plugin.getSpawnerItem(entityId);
        block.getWorld().dropItem(block.getLocation(), spawnerStack);
    }
}
