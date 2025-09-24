package top.ctnstudio.futurefood.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent.Stage;
import net.neoforged.neoforge.common.Tags;
import top.ctnstudio.futurefood.util.ModUtil;

import java.util.Set;

import static top.ctnstudio.futurefood.datagen.tag.FfBlockTags.UNLIMITED_RECEIVE;

/**
 * 高亮无限链接渲染
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber
public class HighlightedLinksRender {
  private static final float red = 0F;
  private static final float green = 0.5F;
  private static final float blue = 0F;
  private static final float alpha = 0.5F;

  @SubscribeEvent
  public static void renderLevelStageEvent(RenderLevelStageEvent event) {
    var stage = event.getStage();
    if (stage != Stage.AFTER_TRIPWIRE_BLOCKS) {
      return;
    }

    var minecraft = Minecraft.getInstance();
    var level = minecraft.level;
    var player = minecraft.player;
    if (level == null || player == null || !player.isAlive() || (
      !player.getMainHandItem().is(Tags.Items.TOOLS_WRENCH) &&
      !player.getItemInHand(InteractionHand.OFF_HAND).is(Tags.Items.TOOLS_WRENCH))) {
      return;
    }
    var playerPos = player.getOnPos();
    var frustum = event.getFrustum();

    final Set<BlockPos> blockPosList;
    blockPosList = ModUtil.rangePos(playerPos, 10, pos -> {
      if (!frustum.isVisible(new AABB(pos))) {
        return false;
      }
      BlockState blockState = level.getBlockState(pos);
      return !blockState.isEmpty() && blockState.is(UNLIMITED_RECEIVE);
    });
    if (blockPosList.isEmpty()) {
      return;
    }

    RenderSystem.disableDepthTest();
    RenderSystem.enableBlend();
    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    var buffer = minecraft.renderBuffers().bufferSource();
    var pose = event.getPoseStack();
    pose.pushPose();

    Camera camera = event.getCamera();
    var cameraPos = camera.getPosition();

    for (BlockPos blockPos : blockPosList) {
      pose.pushPose();

      var blockCenterPos = blockPos.getCenter();
      double x = -cameraPos.x + blockCenterPos.x;
      double y = -cameraPos.y + blockCenterPos.y;
      double z = -cameraPos.z + blockCenterPos.z;
      pose.translate(x, y, z);
      pose.mulPose(camera.rotation());
      pose.pushPose();

      pose.translate(-0.5, -0.5, 0);

      var consumer = VertexMultiConsumer.create(buffer.getBuffer(RenderType.lineStrip()));
      var matrix4f = pose.last().pose();
      float p = 0;
      float p1 = 1f;
      // TODO 未完成
      consumer.addVertex(matrix4f, p, p, 0).setColor(red, green, blue, alpha).setNormal(0, 0, 0);
      consumer.addVertex(matrix4f, p1, p, 0).setColor(red, green, blue, alpha).setNormal(0, 0, 0);
      consumer.addVertex(matrix4f, p1, p1, 0).setColor(red, green, blue, alpha).setNormal(0, 0, 0);
      consumer.addVertex(matrix4f, p, p1, 0).setColor(red, green, blue, alpha).setNormal(0, 0, 0);
      consumer.addVertex(matrix4f, p, p, 0).setColor(red, green, blue, alpha).setNormal(0, 0, 0);
      pose.popPose();

      pose.popPose();
    }
    pose.popPose();
    buffer.endBatch();
  }
}
