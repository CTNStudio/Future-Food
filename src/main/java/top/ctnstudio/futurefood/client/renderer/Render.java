package top.ctnstudio.futurefood.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent.Stage;
import org.lwjgl.opengl.GL11;
import top.ctnstudio.futurefood.core.init.ModBlock;

import java.util.List;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)

@EventBusSubscriber
public class Render {
  @SubscribeEvent
  public static void renderLevelStageEvent(RenderLevelStageEvent event) {
    Stage stage = event.getStage();

    if (stage != Stage.AFTER_TRIPWIRE_BLOCKS) {
      return;
    }

//    if (stage != Stage.AFTER_TRIPWIRE_BLOCKS && stage != Stage.AFTER_TRANSLUCENT_BLOCKS &&
//        stage != Stage.AFTER_BLOCK_ENTITIES && stage != Stage.AFTER_CUTOUT_BLOCKS &&
//        stage != Stage.AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS && stage != Stage.AFTER_SOLID_BLOCKS) {
//      return;
//    }

    Minecraft minecraft = Minecraft.getInstance();
    ClientLevel level = minecraft.level;
    LocalPlayer player = minecraft.player;
    if (Objects.isNull(player) || Objects.isNull(level) || !player.isAlive()) {
      return;
    }

    final BufferSource buffer = minecraft.renderBuffers().bufferSource();
    final Frustum frustum = event.getFrustum();
    final BlockPos playerPos = player.getOnPos();
    final AABB aabb = AABB.encapsulatingFullBlocks(playerPos.offset(10, 10, 10),
      playerPos.offset(-10, -10, -10));

    // final List<BlockPos> blockPosList = BlockPos.betweenClosedStream(aabb).filter(
    //   pos -> frustum.isVisible(new AABB(pos))
    //     && level.getBlockState(pos).is(FfBlockTags.UNLIMITED_LAUNCH)).toList();
    final List<BlockPos> blockPosList = BlockPos.betweenClosedStream(aabb).filter(
      pos -> level.getBlockState(pos).is(ModBlock.QED)).toList();
    if (blockPosList.isEmpty()) {
      return;
    }

    GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    RenderSystem.enableBlend();
    RenderSystem.disableDepthTest();
    RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);

    PoseStack pose = event.getPoseStack();
    {
      final Camera camera = event.getCamera();
      final Vec3 cameraV3 = camera.getPosition().reverse();
      final double x = cameraV3.x();
      final double y = cameraV3.y();
      final double z = cameraV3.z();

      for (BlockPos blockPos : blockPosList) {
        final float red = 0F;
        final float green = 0.5F;
        final float blue = 0F;
        final float alpha = 0.5F;
//      pose.translate(centerV3.x, centerV3.y, centerV3.z);
//      PoseStack pose = new PoseStack();
//      Matrix4f matrix4f = pose.last().pose();

        final Vec3 vec = blockPos.getCenter();
        final double f = 0.5;

//      renderFilledBox(pose, buffer, aabb1, red, green, blue, alpha);
        final var type = RenderType.lines();
        VertexConsumer consumer = buffer.getBuffer(type);
        final var poseIn = pose.last().pose();

        consumer.addVertex(poseIn, (float) (vec.x - 0.5f), (float) (vec.y - 0.5f),
            (float) (vec.z - 0.5f))
          .setColor(red, green, blue, alpha);
        consumer.addVertex(poseIn, (float) (vec.x + 0.5f), (float) (vec.y - 0.5f),
            (float) (vec.z - 0.5f))
          .setColor(red, green, blue, alpha);
        consumer.addVertex(poseIn, (float) (vec.x + 0.5f), (float) (vec.y - 0.5f),
            (float) (vec.z + 0.5f))
          .setColor(red, green, blue, alpha);
        consumer.addVertex(poseIn, (float) (vec.x - 0.5f), (float) (vec.y - 0.5f),
            (float) (vec.z + 0.5f))
          .setColor(red, green, blue, alpha);
        consumer.addVertex(poseIn, (float) (vec.x - 0.5f), (float) (vec.y - 0.5f),
            (float) (vec.z - 0.5f))
          .setColor(red, green, blue, alpha);

        consumer.addVertex(poseIn, (float) (vec.x - 0.5f), (float) (vec.y + 0.5f),
            (float) (vec.z - 0.5f))
          .setColor(red, green, blue, alpha);
        consumer.addVertex(poseIn, (float) (vec.x + 0.5f), (float) (vec.y + 0.5f),
            (float) (vec.z - 0.5f))
          .setColor(red, green, blue, alpha);
        consumer.addVertex(poseIn, (float) (vec.x + 0.5f), (float) (vec.y + 0.5f),
            (float) (vec.z + 0.5f))
          .setColor(red, green, blue, alpha);
        consumer.addVertex(poseIn, (float) (vec.x - 0.5f), (float) (vec.y + 0.5f),
            (float) (vec.z + 0.5f))
          .setColor(red, green, blue, alpha);
        consumer.addVertex(poseIn, (float) (vec.x - 0.5f), (float) (vec.y + 0.5f),
            (float) (vec.z - 0.5f))
          .setColor(red, green, blue, alpha);

//        LevelRenderer.renderVoxelShape(pose, consumer, Shapes.block(),
//          vec.x - 13, vec.y - 13, vec.z - 13,
//          red, green, blue, alpha, true);
//        LevelRenderer.addChainedFilledBoxVertices(
//          pose, consumer,
//          vec.x - f + 1, vec.y - f + 1, vec.z - f + 1,
//          vec.x + f + 1, vec.y + f + 1, vec.z + f + 1,
//          red, green, blue, alpha
//        );
        buffer.endBatch(type);
      }
    }
    pose.popPose();
    RenderSystem.disableBlend();
  }

  /*VoxelShape shape = new ArrayVoxelShape(new BitSetDiscreteVoxelShape());
  shape.*/
}
