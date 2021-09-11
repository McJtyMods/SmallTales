package com.mcjty.smalltales.client;

import com.mcjty.smalltales.modules.story.items.ConfiguratorItem;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mcjty.lib.client.CustomRenderTypes;
import mcjty.lib.client.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RenderWorldLastEventHandler {

    private final static Map<BlockPos, Long> positions = new HashMap<>();

    public static void registerAnchor(BlockPos pos) {
        positions.put(pos, System.currentTimeMillis());
    }

    public static void tick(RenderWorldLastEvent evt) {
        renderHilightedBlock(evt);
    }

    private static void renderHilightedBlock(RenderWorldLastEvent evt) {
        if (Minecraft.getInstance().player.getMainHandItem().getItem() instanceof ConfiguratorItem) {
            MatrixStack matrixStack = evt.getMatrixStack();
            IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            IVertexBuilder builder = buffer.getBuffer(CustomRenderTypes.OVERLAY_LINES);

            matrixStack.pushPose();

            Vector3d projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);

            Matrix4f positionMatrix = matrixStack.last().pose();

            positions.forEach((c, time) -> RenderHelper.renderHighLightedBlocksOutline(builder, positionMatrix, c.getX(), c.getY(), c.getZ(), 0.0f, 0.0f, 1.0f, 1.0f));

            matrixStack.popPose();
            RenderSystem.disableDepthTest();
            buffer.endBatch(CustomRenderTypes.OVERLAY_LINES);
        }

        if (!positions.isEmpty()) {
            long time = System.currentTimeMillis();
            Set<BlockPos> toDelete = new HashSet<>();
            positions.forEach((c, t) -> {
                if (time > t + 500) {
                    toDelete.add(c);
                }
            });
            toDelete.forEach(positions::remove);
        }
    }
}
