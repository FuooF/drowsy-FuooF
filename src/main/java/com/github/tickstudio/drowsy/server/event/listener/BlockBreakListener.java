package com.github.tickstudio.drowsy.server.event.listener;

import com.github.tickstudio.drowsy.Config;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

@EventBusSubscriber
public class BlockBreakListener {
    @SubscribeEvent
    public static void onGrassDestroy(BlockDropsEvent event){
        BlockState blockState = event.getState();
        Entity breaker = event.getBreaker();

        if (!(breaker instanceof Player) || !((Player) breaker).getMainHandItem().is(ItemTags.HOES))
            return;

        if (blockState.is(Blocks.SHORT_GRASS)||blockState.is(Blocks.TALL_GRASS)){
            if (Math.random() > Config.GRASS_DROP_CHANCE.get())
                return;

            event.getDrops().add(new ItemEntity(
                    event.getLevel(),
                    event.getPos().getX() + 0.5,
                    event.getPos().getY() + 0.5,
                    event.getPos().getZ() + 0.5,
                    new ItemStack(Items.SHORT_GRASS,1)
            ));

        }
    }
}
