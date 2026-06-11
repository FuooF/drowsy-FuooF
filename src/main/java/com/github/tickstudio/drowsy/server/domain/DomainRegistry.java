package com.github.tickstudio.drowsy.server.domain;

import com.github.tickstudio.drowsy.Drowsy;
import com.github.tickstudio.drowsy.server.domain.block.StrawMatBlock;
import com.github.tickstudio.drowsy.server.domain.block.StrawMatBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;
import java.util.function.Supplier;

import static com.github.tickstudio.drowsy.Drowsy.MODID;

public class DomainRegistry {

    //方块注册表
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Drowsy.MODID);
    //注册 草席方块
    public static final DeferredBlock<StrawMatBlock> STRAW_MAT_BLOCK = BLOCKS.registerBlock("straw_mat",
            StrawMatBlock::new);

    // 方块实体注册表
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Drowsy.MODID);
    // 注册 草席方块实体
    public static final Supplier<BlockEntityType<StrawMatBlockEntity>> STRAW_MAT_ENTITY =
            BLOCK_ENTITIES.register("straw_mat",
                    () -> new BlockEntityType<>(
                            StrawMatBlockEntity::new,
                            Set.of(STRAW_MAT_BLOCK.get()) ,
                            null
                    )
            );

    // 物品注册表
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // 注册 草席物品
    public static final DeferredItem<BlockItem> STRAW_MAT_ITEM =
            ITEMS.registerSimpleBlockItem(STRAW_MAT_BLOCK);
    public static final DeferredItem<Item> BUNDLE_GRASS = ITEMS.registerSimpleItem(
            "bundle_grass",
            new Item.Properties().stacksTo(64)
    );



    // 创造模式标签页 注册表
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final Supplier<CreativeModeTab> BEDS = CREATIVE_MODE_TABS.register(
            "drowsy_bed",
            ()->CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.drowsy"))
                    .icon(() -> new ItemStack(STRAW_MAT_ITEM.get()))
                    .displayItems((parameters, output) -> {
                        // 这是 DisplayItemsGenerator 函数式接口的 lambda 写法
                        // parameters：ItemDisplayParameters，包含启用的功能标志、权限等
                        // output：CreativeModeTab.Output，用来"输出"物品到标签页
                        //用 accept 把 物品 添加到标签页里
                        output.accept(STRAW_MAT_ITEM.get());
                        output.accept(BUNDLE_GRASS.get());
                    })

                    .build()
    );
    public static void registerDomain(IEventBus modEventBus){
        BLOCK_ENTITIES.register(modEventBus);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
