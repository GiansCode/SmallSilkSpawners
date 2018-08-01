package io.samdev.smallsilkspawners;

import net.minecraft.server.v1_13_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class SmallSilkSpawners extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        saveDefaultConfig();

        needsSilkTouch = getConfig().getBoolean("needs_silk_touch");
        dropExp = getConfig().getBoolean("drop_exp");

        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(this), this);
    }

    private boolean needsSilkTouch;

    public boolean isNeedsSilkTouch()
    {
        return needsSilkTouch;
    }

    private boolean dropExp;

    public boolean isDropExp()
    {
        return dropExp;
    }

    public short getSpawnerEntityId(CreatureSpawner spawner)
    {
        return spawner.getSpawnedType().getTypeId();
    }

    public boolean hasSilkTouch(ItemStack stack)
    {
        return stack.containsEnchantment(Enchantment.SILK_TOUCH);
    }

    public ItemStack getSpawnerItem(short entityId)
    {
        ItemStack stack = new ItemStack(Material.SPAWNER, 1, entityId);
        stack = addEntityNbt(stack, entityId, EntityType.fromId((int) entityId));

        return stack;
    }

    private ItemStack addEntityNbt(ItemStack stack, short entityId, EntityType entityType)
    {
        net.minecraft.server.v1_13_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);

        NBTTagCompound nbt = nmsStack.getTag();

        if (nbt == null)
        {
            nbt = new NBTTagCompound();
            nmsStack.setTag(nbt);
        }

        // Custom Tag for plugin
        if (!nbt.hasKey("SmallSilkSpawners"))
        {
            nbt.set("SmallSilkSpawners", new NBTTagCompound());
        }
        nbt.getCompound("SmallSilkSpawners").setString("entityType", entityType.name());

        // Entity ID: BlockEntityTag
        if (!nbt.hasKey("BlockEntityTag"))
        {
            nbt.set("BlockEntityTag", new NBTTagCompound());
        }
        nbt.getCompound("BlockEntityTag").setShort("entityID", entityId);

        if (!nbt.getCompound("BlockEntityTag").hasKey("SpawnPotentials"))
        {
            nbt.getCompound("BlockEntityTag").set("SpawnPotentials", new NBTTagCompound());
        }

        // SpawnData
        if (!nbt.hasKey("SpawnData"))
        {
            nbt.set("SpawnData", new NBTTagCompound());
        }
        nbt.getCompound("SpawnData").setString("id", entityType.getName());

        // EntityTag
        if (!nbt.hasKey("EntityTag"))
        {
            nbt.set("EntityTag", new NBTTagCompound());
        }
        nbt.getCompound("EntityTag").setString("id", "minecraft:" + entityType.getName());

        nmsStack.setTag(nbt);

        return CraftItemStack.asBukkitCopy(nmsStack);
    }

    public void setSpawnerEntity(Block block, ItemStack stack)
    {
        net.minecraft.server.v1_13_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);

        NBTTagCompound nbt = nmsStack.getTag();

        if (nbt == null || !nbt.hasKey("SmallSilkSpawners"))
        {
            return;
        }

        EntityType entityType = EntityType.valueOf(nbt.getCompound("SmallSilkSpawners").getString("entityType"));

        CreatureSpawner spawner = (CreatureSpawner) block.getState();

        spawner.setSpawnedType(entityType);
        spawner.update(true);
    }
}
