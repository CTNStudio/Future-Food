package top.ctnstudio.futurefood.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.ctnstudio.futurefood.client.ModRenderType;
import top.ctnstudio.futurefood.common.block.DirectionalEntityBlock;
import top.ctnstudio.futurefood.common.block.QedEntityBlock;
import top.ctnstudio.futurefood.common.block.QedEntityBlock.Activate;
import top.ctnstudio.futurefood.common.block.QedEntityBlock.Light;
import top.ctnstudio.futurefood.common.block.tile.QedBlockEntity;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class QedBlockEntityRenderer<T extends QedBlockEntity> implements BlockEntityRenderer<T> {
  public static final ResourceLocation ENERGY_BALL_SHEET = FutureFood.modRL("energy_ball");
  public static final RenderType ENERGY_BALL_RENDER_TYPE = createRenderType(ENERGY_BALL_SHEET);
  public static final Material FLASH_INSIDE_MATERIAL = chestMaterial("flash/inside");
  public static final Material FLASH_OUTSIDE_MATERIAL = chestMaterial("flash/outside");
  public static final Material FLASH_OUTSIDE_1_MATERIAL = chestMaterial("flash/outside_1");
  public static final Material FLASH_OUTSIDE_2_MATERIAL = chestMaterial("flash/outside_2");
  public static final Material FLASH_THUNDERSTORM_MATERIAL = chestMaterial("flash/thunderstorm");
  public static final Material FLASH1_INSIDE_MATERIAL = chestMaterial("flash1/inside");
  public static final Material FLASH1_OUTSIDE_MATERIAL = chestMaterial("flash1/outside");
  public static final Material FLASH1_OUTSIDE_1_MATERIAL = chestMaterial("flash1/outside_1");
  public static final Material FLASH1_OUTSIDE_2_MATERIAL = chestMaterial("flash1/outside_2");
  public static final Material FLASH1_THUNDERSTORM_MATERIAL = chestMaterial("flash1/thunderstorm");
  public static final Material WORK_INSIDE_MATERIAL = chestMaterial("work/inside");
  public static final Material WORK_OUTSIDE_MATERIAL = chestMaterial("work/outside");
  public static final Material WORK_OUTSIDE_1_MATERIAL = chestMaterial("work/outside_1");
  public static final Material WORK_OUTSIDE_2_MATERIAL = chestMaterial("work/outside_2");
  public static final Material WORK_THUNDERSTORM_MATERIAL = chestMaterial("work/thunderstorm");

  public QedBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
  }

  @Override
  public void render(T blockEntity, float partialTick, PoseStack poseStack,
                     MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    final BlockState blockState = blockEntity.getBlockState();
    final Activate activateState = blockState.getValue(QedEntityBlock.ACTIVATE);
    if (activateState == Activate.DEFAULT) {
      return;
    }
    final Direction directionState = blockState.getValue(DirectionalEntityBlock.FACING);
    final Light lightState = blockState.getValue(QedEntityBlock.LIGHT);
    final Minecraft minecraft = Minecraft.getInstance();
    var renderBuffers = minecraft.renderBuffers();


    final VertexConsumer inside;
    final VertexConsumer outside;
    final VertexConsumer outside1;
    final VertexConsumer outside2;
    final VertexConsumer thunderstorm;
    switch (activateState) {
      case WORK -> {
        inside = WORK_INSIDE_MATERIAL.buffer(bufferSource, ModRenderType.ENERGY_BALL);
        outside = WORK_OUTSIDE_MATERIAL.buffer(bufferSource, ModRenderType.ENERGY_BALL);
        outside1 = WORK_OUTSIDE_1_MATERIAL.buffer(bufferSource, ModRenderType.ENERGY_BALL);
        outside2 = WORK_OUTSIDE_2_MATERIAL.buffer(bufferSource, ModRenderType.ENERGY_BALL);
        thunderstorm = WORK_THUNDERSTORM_MATERIAL.buffer(bufferSource, ModRenderType.ENERGY_BALL);
      }
      case FLASH -> {
        inside = FLASH_INSIDE_MATERIAL.buffer(bufferSource, ModRenderType.ENERGY_BALL);
        outside = FLASH_OUTSIDE_MATERIAL.buffer(bufferSource, ModRenderType.ENERGY_BALL);
        outside1 = FLASH_OUTSIDE_1_MATERIAL.buffer(bufferSource, ModRenderType.ENERGY_BALL);
        outside2 = FLASH_OUTSIDE_2_MATERIAL.buffer(bufferSource, ModRenderType.ENERGY_BALL);
        thunderstorm = FLASH_THUNDERSTORM_MATERIAL.buffer(bufferSource, ModRenderType.ENERGY_BALL);
      }
      case FLASH1 -> {
        inside = FLASH1_INSIDE_MATERIAL.buffer(bufferSource, ModRenderType.ENERGY_BALL);
        outside = FLASH1_OUTSIDE_MATERIAL.buffer(bufferSource, ModRenderType.ENERGY_BALL);
        outside1 = FLASH1_OUTSIDE_1_MATERIAL.buffer(bufferSource, ModRenderType.ENERGY_BALL);
        outside2 = FLASH1_OUTSIDE_2_MATERIAL.buffer(bufferSource, ModRenderType.ENERGY_BALL);
        thunderstorm = FLASH1_THUNDERSTORM_MATERIAL.buffer(bufferSource, ModRenderType.ENERGY_BALL);
      }
      default -> {
        return;
      }
    }

    poseStack.pushPose();

    // outside2
    int[] uv = {0, 0};
    int[] uv1 = {13, 13};
    renderQuads(poseStack, outside2, true, partialTick, 13, uv, uv1);

    poseStack.popPose();
  }

  private void renderQuads(PoseStack pose, VertexConsumer vertex, boolean isReverse, float partialTick, float size, int[] uv, int[] uv1) {
    PoseStack.Pose last = pose.last();
    for (Direction direction : Direction.values()) {
      renderNoodles(last, vertex, direction, partialTick, size, isReverse, uv[0], uv[1], uv1[0], uv1[1]);
    }
  }

  private void renderQuads(PoseStack pose, VertexConsumer[] vertex, boolean isReverse, float partialTick, float size, int[][] uv, int[][] uv1) {
    PoseStack.Pose last = pose.last();
    int index = 0;
    for (Direction direction : Direction.values()) {
      renderNoodles(last, vertex[index], direction, partialTick, size, isReverse, uv[index][0], uv[index][1], uv1[index][0], uv1[index][1]);
      index++;
    }
  }

  private static void renderNoodles(PoseStack.Pose last, VertexConsumer vertex, Direction direction,
                                    float partialTick, float size, boolean isReverse, int u, int v, int u1, int v1) {
    final float scopeSize = size / 2;
    float v1x = 0, v1y = 0, v1z = 0;
    float v2x = 0, v2y = 0, v2z = 0;
    float v3x = 0, v3y = 0, v3z = 0;
    float v4x = 0, v4y = 0, v4z = 0;
    float nX = 0, nY = 0, nZ = 0;
    switch (direction) {
      case DOWN -> {
        v1x = -scopeSize;
        v1y = -scopeSize;
        v1z = scopeSize;
        v2x = scopeSize;
        v2y = scopeSize;
        v2z = scopeSize;
        v3x = scopeSize;
        v3z = -scopeSize;
        v3y = -scopeSize;
        v4x = -scopeSize;
        v4y = -scopeSize;
        v4z = -scopeSize;
      }
      case UP -> {
        v1x = scopeSize;
        v1y = -scopeSize;
        v1z = -scopeSize;
        v2x = -scopeSize;
        v2y = -scopeSize;
        v2z = -scopeSize;
        v3x = scopeSize;
        v3z = scopeSize;
        v3y = scopeSize;
        v4x = scopeSize;
        v4y = scopeSize;
        v4z = scopeSize;
      }
      case NORTH, SOUTH, EAST, WEST -> {
        v1x = scopeSize;
        v1y = scopeSize;
        v1z = scopeSize;
        v2x = scopeSize;
        v2y = -scopeSize;
        v2z = -scopeSize;
        v3x = -scopeSize;
        v3z = -scopeSize;
        v3y = -scopeSize;
        v4x = -scopeSize;
        v4y = -scopeSize;
        v4z = scopeSize;
      }
    }
    switch (direction) {
      case DOWN -> nY = isReverse ? -1 : 1;
      case UP -> nY = isReverse ? 1 : -1;
      case NORTH -> nZ = isReverse ? -1 : 1;
      case SOUTH -> nZ = isReverse ? 1 : -1;
      case WEST -> nX = isReverse ? 1 : -1;
      case EAST -> nX = isReverse ? -1 : 1;
    }
    vertex.addVertex(last, v1x, v1y, v1z).setNormal(last, nX, nY, nZ).setUv(u, v).setUv2(u1, v1);
    vertex.addVertex(last, v2x, v2y, v2z).setNormal(last, nX, nY, nZ).setUv(u, v).setUv2(u1, v1);
    vertex.addVertex(last, v3x, v3y, v3z).setNormal(last, nX, nY, nZ).setUv(u, v).setUv2(u1, v1);
    vertex.addVertex(last, v4x, v4y, v4z).setNormal(last, nX, nY, nZ).setUv(u, v).setUv2(u1, v1);
  }

  private static RenderType createRenderType(ResourceLocation rl) {
    return ModRenderType.getEnergyBall(rl);
  }

  private static Function<MultiBufferSource, VertexConsumer> createVertexConsumer(RenderType renderType) {
    return mbs -> mbs.getBuffer(renderType);
  }

  protected static Material getMaterial(ResourceLocation texture) {
    return new Material(ENERGY_BALL_SHEET, texture);
  }

  protected static Material chestMaterial(String texture) {
    return new Material(ENERGY_BALL_SHEET, FutureFood.modRL("block/quantum_energy_diffuser/energy_ball/" + texture));
  }
}
