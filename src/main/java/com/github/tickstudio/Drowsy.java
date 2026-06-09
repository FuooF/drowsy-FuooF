package com.github.tickstudio;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Drowsy.MODID)
public class Drowsy {
    // 模组id,代码全局引用
    public static final String MODID = "drowsy";
    // 日志
    public static final Logger LOGGER = LogUtils.getLogger();
    //方块注册表
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // 物品注册表
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // 创造模式标签页 注册表
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);





    public Drowsy(IEventBus modEventBus, ModContainer modContainer) {
        // 注册启动方法
        modEventBus.addListener(this::commonSetup);

        //注册到事件总线
        NeoForge.EVENT_BUS.register(this);


        // 注册模组配置
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    // 双端共享启动方法
    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code

    }



    // 监听服务器启动事件
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }
}
