package top.ctnstudio.futurefood.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
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

@OnlyIn(Dist.CLIENT)
public class QedBlockEntityRenderer<T extends QedBlockEntity> implements BlockEntityRenderer<T> {
  private static final RenderType FLASH_INSIDE = create("flash/inside");
  private static final RenderType FLASH_OUTSIDE = create("flash/outside");
  private static final RenderType FLASH_OUTSIDE_1 = create("flash/outside_1");
  private static final RenderType FLASH_OUTSIDE_2 = create("flash/outside_2");
  private static final RenderType FLASH_THUNDERSTORM = create("flash/thunderstorm");
  private static final RenderType FLASH1_INSIDE = create("flash1/inside");
  private static final RenderType FLASH1_OUTSIDE = create("flash1/outside");
  private static final RenderType FLASH1_OUTSIDE_1 = create("flash1/outside_1");
  private static final RenderType FLASH1_OUTSIDE_2 = create("flash1/outside_2");
  private static final RenderType FLASH1_THUNDERSTORM = create("flash1/thunderstorm");
  private static final RenderType WORK_INSIDE = create("work/inside");
  private static final RenderType WORK_OUTSIDE = create("work/outside");
  private static final RenderType WORK_OUTSIDE_1 = create("work/outside_1");
  private static final RenderType WORK_OUTSIDE_2 = create("work/outside_2");
  private static final RenderType WORK_THUNDERSTORM = create("work/thunderstorm");

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

    VertexConsumer buffer = bufferSource.getBuffer(ENERGY_BALL);
    poseStack.pushPose();
    renderQuads(poseStack, buffer, partialTick);

    poseStack.popPose();
  }

  private static void renderQuads(PoseStack poseStack, VertexConsumer buffer, float partialTick) {
  }

  private static RenderType create(String rl) {
    return ModRenderType.getEnergyBall(FutureFood.modRL("textures/block/quantum_energy_diffuser/energy_ball/" + rl));
  }
}
