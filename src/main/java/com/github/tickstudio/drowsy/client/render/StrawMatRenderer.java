package com.github.tickstudio.drowsy.client.render;

import com.github.tickstudio.drowsy.server.domain.block.StrawMatBlock;
import com.github.tickstudio.drowsy.server.domain.block.StrawMatBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class StrawMatRenderer implements BlockEntityRenderer<StrawMatBlockEntity> {
    public StrawMatRenderer(BlockEntityRendererProvider.Context context) {
    }

    // :(
    @Override
    public void render(@NotNull StrawMatBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        // 获取模型
        BlockState blockState = blockEntity.getBlockState();
        Minecraft mc = Minecraft.getInstance();
         BakedModel model
                =  mc.getBlockRenderer().getBlockModel(blockState);
        Direction direction = blockState.getValue(StrawMatBlock.FACING);
        poseStack.pushPose();
        // 根据朝向旋转
        poseStack.translate(0.5,0,0.5);
        //toYRot() 返回的是北=0、东=90、南=180、西=270。加负号是因为 PoseStack 的旋转方向和 Minecraft 定义的朝向角反向。
        poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot() -90));
        poseStack.translate(0,0,-0.5); // dont ask why
        // 一个模型可能有多层渲染类型（如 solid+cutout），需要逐层画出来。
        // 写循环保证如果有 tint/半透明 也不会漏面。q
        for (RenderType renderType : model.getRenderTypes(
                blockState,                    // BlockState — 某些模型根据属性选不同的层，如水位
                RandomSource.create(),         // 随机种子 — 固定值保证同一格每帧选同一纹理变体
                ModelData.EMPTY                // 额外模型数据 — 普通方块不需要，传空即可
        )) {
            // 从 bufferSource 拿当前渲染层对应的 VertexConsumer。
            // getEntityRenderType 把方块模型的 RenderType 转为 BER 场景下 GPU 兼容的版本。
            VertexConsumer consumer = bufferSource.getBuffer(
                RenderTypeHelper.getEntityRenderType(renderType, false)
            );

            // 把 BakedModel 的所有面片遍历一遍，计算顶点坐标/UV/光照，写入 consumer
            mc.getBlockRenderer().getModelRenderer()
                    .renderModel(
                    poseStack.last(),           // PoseStack 当前变换矩阵（包含旋转后的姿态）
                    consumer,                   // 顶点写入器，决定了这批面走哪个 GPU Shader
                    blockState,                 // BlockState，用于判断哪些面需要做遮挡剔除
                    model,                      // 烘焙好的模型，包含所有面片数据
                    1.0f, 1.0f, 1.0f,           // RGB 染色（1.0 = 不染色，小于 1.0 变暗）
                    packedLight,                // 亮度值（天空光 + 方块光照值打包成的 int）
                    packedOverlay,              // 覆盖效果（破坏裂纹 UV / 红石充能闪烁）
                    ModelData.EMPTY,            // 额外模型数据，简单方块传空即可
                    renderType                  // 当前渲染层类型
            );
        }
        // 恢复 PoseStack，不影响后续其他格子的渲染
        poseStack.popPose();
    }
}
