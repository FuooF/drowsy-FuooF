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

    @Override
    public void render(@NotNull StrawMatBlockEntity blockEntity, float partialTick,
                       @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource,
                       int packedLight, int packedOverlay) {
        BlockState blockState = blockEntity.getBlockState();
        Minecraft mc = Minecraft.getInstance();
        BakedModel model = mc.getBlockRenderer().getBlockModel(blockState);
        Direction direction = blockState.getValue(StrawMatBlock.FACING);

        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot() - 90));
        poseStack.translate(0, 0, -0.5);

        // 方块模型可能有多层 RenderType（solid/cutout/translucent），逐层渲染
        for (RenderType renderType : model.getRenderTypes(
                blockState, RandomSource.create(), ModelData.EMPTY)) {
            // 转为 BER 可用的 entity 版本 RenderType
            RenderType entityType = RenderTypeHelper.getEntityRenderType(renderType, false);
            VertexConsumer consumer = bufferSource.getBuffer(entityType);

            mc.getBlockRenderer().getModelRenderer()
                    .renderModel(
                            poseStack.last(), consumer, blockState, model,
                            1.0f, 1.0f, 1.0f,
                            packedLight, packedOverlay,
                            ModelData.EMPTY, entityType);
        }

        poseStack.popPose();
    }
}
