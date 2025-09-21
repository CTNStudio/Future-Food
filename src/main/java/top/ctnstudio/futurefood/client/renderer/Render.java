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
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
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
import org.lwjgl.opengl.GL11;
import top.ctnstudio.futurefood.datagen.tag.FfBlockTags;

import java.util.List;
import java.util.stream.Stream;

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
    if (level == null) {
      return;
    }

    LocalPlayer player = minecraft.player;
    if (player == null || !player.isAlive() /*||
          player.getMainHandItem().getItem() != ModItem.CYBER_WRENCH.get()*/) {
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

    List<BlockPos> blockPosList = blockPosStream.toList();
    if (blockPosList.isEmpty()) {
      return;
    }

    Matrix4f modelViewMatrix = event.getModelViewMatrix();
    Matrix4f projectionMatrix = event.getProjectionMatrix();

    PoseStack pose = event.getPoseStack();
    pose.pushPose();
    BufferSource buffer = minecraft.renderBuffers().crumblingBufferSource();
    GL11.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
    RenderSystem.disableBlend();
    RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
    RenderSystem.disableDepthTest();
    for (BlockPos blockPos : blockPosList) {
      Camera camera = event.getCamera();
      Vec3 cameraV3 = camera.getPosition().reverse();

      BufferSource buffer1 = minecraft.renderBuffers().crumblingBufferSource();
      pose.pushPose();
      GL11.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
      RenderSystem.disableBlend();
      RenderSystem.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
      RenderSystem.disableDepthTest();

      final float red = 0F;
      final float green = 0.5F;
      final float blue = 0F;
      final float alpha = 0.5F;
//        pose.translate(centerV3.x, centerV3.y, centerV3.z);
//        PoseStack pose = new PoseStack();
//        Matrix4f matrix4f = pose.last().pose();

      AABB blokcAabb = new AABB(blockPos)
        .move(cameraV3.add(-11, -11, -11));

//        renderFilledBox(pose, buffer, aabb1, red, green, blue, alpha);
      VertexConsumer consumer = buffer1.getBuffer(RenderType.debugFilledBox());
      LevelRenderer.addChainedFilledBoxVertices(
        pose, consumer, blokcAabb.minX, blokcAabb.minY, blokcAabb.minZ, blokcAabb.maxX, blokcAabb.maxY, blokcAabb.maxZ, red, green, blue, alpha
      );

      RenderSystem.enableBlend();
      pose.popPose();
      buffer1.endLastBatch();
      buffer1.endBatch(RenderType.debugFilledBox());
    }
    RenderSystem.enableBlend();
    buffer.endLastBatch();
    buffer.endBatch(RenderType.debugFilledBox());
    pose.popPose();
  }

  /*VoxelShape shape = new ArrayVoxelShape(new BitSetDiscreteVoxelShape());
  shape.*/
}
