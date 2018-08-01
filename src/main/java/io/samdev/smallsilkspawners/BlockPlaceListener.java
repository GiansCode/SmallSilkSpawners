package io.samdev.smallsilkspawners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener
{
    private final SmallSilkSpawners plugin;

    BlockPlaceListener(SmallSilkSpawners plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event)
    {
        if (event.getClass() != BlockPlaceEvent.class)
        {
            // Fake block place event, from things like McMMO
            return;
        }

        if (event.getBlock().getType() != Material.SPAWNER)
        {
            return;
        }

        plugin.setSpawnerEntity(event.getBlock(), event.getItemInHand());
    }
}
