package top.ctnstudio.futurefood.common.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent.Stage;
import org.joml.Matrix4f;
import top.ctnstudio.futurefood.core.init.ModItem;
import top.ctnstudio.futurefood.datagen.tag.FfBlockTags;

import java.util.List;
import java.util.stream.Stream;

public class CyberWrenchItem extends Item {
  public CyberWrenchItem(Properties properties) {
    super(properties);
  }

  @OnlyIn(Dist.CLIENT)
  @EventBusSubscriber
  public static class Render {
    @SubscribeEvent
    public static void renderLevelStageEvent(RenderLevelStageEvent event) {
      Stage stage = event.getStage();
      if (stage != Stage.AFTER_TRIPWIRE_BLOCKS && stage != Stage.AFTER_TRANSLUCENT_BLOCKS &&
          stage != Stage.AFTER_BLOCK_ENTITIES && stage != Stage.AFTER_CUTOUT_BLOCKS &&
          stage != Stage.AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS && stage != Stage.AFTER_SOLID_BLOCKS) {
        return;
      }

      Minecraft minecraft = Minecraft.getInstance();
      ClientLevel level = minecraft.level;
      if (level == null) {
        return;
      }

      LocalPlayer player = minecraft.player;
      if (player == null || !player.isAlive() ||
          player.getMainHandItem().getItem() != ModItem.CYBER_WRENCH.get()) {
        return;
      }

      BlockPos playerPos = new BlockPos((int) player.getX(), (int) player.getEyeY(), (int) player.getZ());
      AABB aabb = AABB.of(BoundingBox.fromCorners(playerPos.offset(10, 10, 10), playerPos.offset(-10, -10, -10)));
      Frustum frustum = event.getFrustum();
      Stream<BlockPos> blockPosStream = BlockPos.betweenClosedStream(aabb).filter(pos -> {
        if (!frustum.isVisible(new AABB(pos))) {
          return false;
        }
        return level.getBlockState(pos).is(FfBlockTags.UNLIMITED_LAUNCH);
      });

      List<BlockPos> list = blockPosStream.toList();
      if (list.isEmpty()) {
        return;
      }

      BufferSource bufferSource = minecraft.renderBuffers().bufferSource();
      VertexConsumer consumer = bufferSource.getBuffer(RenderType.debugQuads());
      PoseStack pose = event.getPoseStack();
      Pose last = pose.last();
//      Matrix4f modelViewMatrix = event.getModelViewMatrix();
//      LevelRenderer levelRenderer = event.getLevelRenderer();
//      Matrix4f projectionMatrix = event.getProjectionMatrix();

      list.forEach(center -> {
        pose.pushPose();
        Vec3 centerV3 = center.getCenter();
        pose.translate(centerV3.x - 0.5f, centerV3.y - 0.5f, centerV3.z - 0.5f);
        Matrix4f matrix4f = last.pose();
        consumer.addVertex(matrix4f, 0, 0, 0).setColor(0.9F, 0.9F, 0.0F, 0.1F);
        consumer.addVertex(matrix4f, 1, 0, 0).setColor(0.9F, 0.9F, 0.0F, 0.1F);
        consumer.addVertex(matrix4f, 1, 0, 1).setColor(0.9F, 0.9F, 0.0F, 0.1F);
        consumer.addVertex(matrix4f, 0, 0, 1).setColor(0.9F, 0.9F, 0.0F, 0.1F);
        pose.popPose();
      });

      bufferSource.endBatch();
    }
  }
}
