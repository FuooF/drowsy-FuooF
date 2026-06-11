package com.github.tickstudio.drowsy;

import com.github.tickstudio.drowsy.server.domain.DomainRegistry;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Drowsy.MODID)
public class Drowsy {
    // 模组id,代码全局引用
    public static final String MODID = "drowsy";
    // 日志
    public static final Logger LOGGER = LogUtils.getLogger();







    public Drowsy(IEventBus modEventBus, ModContainer modContainer) {
        // 注册启动方法
        modEventBus.addListener(this::commonSetup);

        //注册到事件总线
        NeoForge.EVENT_BUS.register(this);
        // 注册模组的内容
        DomainRegistry.registerDomain(modEventBus);

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
