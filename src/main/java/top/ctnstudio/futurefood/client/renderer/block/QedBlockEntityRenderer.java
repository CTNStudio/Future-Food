package top.ctnstudio.futurefood.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.ctnstudio.futurefood.client.core.ModMaterialAtlases;
import top.ctnstudio.futurefood.client.core.ModModelLayer;
import top.ctnstudio.futurefood.client.core.ModRenderType;
import top.ctnstudio.futurefood.common.block.DirectionalEntityBlock;
import top.ctnstudio.futurefood.common.block.QedEntityBlock;
import top.ctnstudio.futurefood.common.block.QedEntityBlock.Activate;
import top.ctnstudio.futurefood.common.block.QedEntityBlock.Light;
import top.ctnstudio.futurefood.common.block.tile.QedBlockEntity;
import top.ctnstudio.futurefood.core.FutureFood;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class QedBlockEntityRenderer<T extends QedBlockEntity> implements BlockEntityRenderer<T> {
  private static final String RL = "textures/block/quantum_energy_diffuser/energy_ball/";
  public final List<Material> flashMaterials;
  public final List<Material> flash1Materials;
  public final List<Material> workMaterials;

  private final ModelPart sphere;

  public QedBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    ModelPart modelpart = context.bakeLayer(ModModelLayer.ENERGY_BALL);
    this.sphere = modelpart.getChild("sphere");

    flashMaterials = new ArrayList<>();
    flash1Materials = new ArrayList<>();
    workMaterials = new ArrayList<>();
    ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
    int i = 0;
    while (resourceManager.getResource(FutureFood.modRL(RL + "work/sphere" + i + ".png")).isPresent()) {
      workMaterials.add(chestMaterial("work/sphere" + i));
      i++;
    }

    i = 0;
    while (resourceManager.getResource(FutureFood.modRL(RL + "flash/sphere" + i + ".png")).isPresent()) {
      flashMaterials.add(chestMaterial("flash/sphere" + i));
      i++;
    }

    i = 0;
    while (resourceManager.getResource(FutureFood.modRL(RL + "flash1/sphere" + i + ".png")).isPresent()) {
      flash1Materials.add(chestMaterial("flash1/sphere" + i));
      i++;
    }
  }

  public static LayerDefinition createBodyLayer() {
    MeshDefinition md = new MeshDefinition();
    PartDefinition pd = md.getRoot();
    PartDefinition pd1 = pd.addOrReplaceChild(
      "sphere", CubeListBuilder.create()
        .texOffs(0, 0)
        .addBox(0, 0, 0, 8, 8, 8),
      PartPose.offset(-4, 0, -4));

    pd1.addOrReplaceChild("outer_ring",
      CubeListBuilder.create()
        .texOffs(72, 20)
        .addBox(-1, -11, -1, -10, -10, -10,
          new CubeDeformation(0.01f, 0.01f, 0.01f)),
      PartPose.offset(10, 20, 10));

    pd1.addOrReplaceChild("outer_ring1",
      CubeListBuilder.create()
        .texOffs(120, 24)
        .addBox(-2, -10, -2, -12, -12, -12,
          new CubeDeformation(0.01f, 0.01f, 0.01f)),
      PartPose.offset(12, 20, 12));

    pd1.addOrReplaceChild("outer_ring2",
      CubeListBuilder.create()
        .texOffs(172, 26)
        .addBox(-2.5f, -9.5f, -2.5f, -13, -13, -13),
      PartPose.offset(13, 20, 13));

    return LayerDefinition.create(md, 172, 26);
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

    final long timeVariable = System.currentTimeMillis() % 10000;

    final VertexConsumer vertexConsumer = getVertexConsumer(activateState, bufferSource, timeVariable, partialTick);
    if (vertexConsumer == null) {
      return;
    }
    poseStack.pushPose();
    poseStack.translate(0.5, 1, 0.5);

    poseStack.pushPose();
    switch (lightState) {
      case DEFAULT -> {
        poseStack.translate(0, 0.1f, 0);
        float time = (timeVariable / 2000.0f * (float) Math.PI);
        poseStack.mulPose(Axis.YP.rotation(time));
        float bobbing = (float) Math.sin(time * 2) * 0.1f;
        poseStack.translate(0, bobbing, 0);
      }
      case WORK -> {
        poseStack.translate(0, 0.2f, 0);
        float time = (timeVariable / 1000.0f * (float) Math.PI);
        poseStack.mulPose(Axis.YP.rotation(time));
        float bobbing = (float) Math.sin(time * 2) * 0.1f;
        poseStack.translate(0, bobbing, 0);
      }
      case ABNORMAL -> {
        float time = (timeVariable / 4000.0f * (float) Math.PI);
        float bobbing = (float) Math.sin(time * 2) * 0.05f;
        poseStack.translate(0, bobbing, 0);
      }
    }
    poseStack.pushPose();

    sphere.render(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, packedOverlay);

    poseStack.popPose();
    poseStack.popPose();
    poseStack.popPose();
  }

  @Nullable
  private VertexConsumer getVertexConsumer(Activate activate, MultiBufferSource bufferSource, long timeVariable, float partialTick) {
    return switch (activate) {
      case WORK -> getVertexConsumer(bufferSource, workMaterials, timeVariable, partialTick);
      case FLASH -> getVertexConsumer(bufferSource, flashMaterials, timeVariable, partialTick);
      case FLASH1 -> getVertexConsumer(bufferSource, flash1Materials, timeVariable, partialTick);
      default -> null;
    };
  }

  @Nullable
  private VertexConsumer getVertexConsumer(MultiBufferSource bufferSource, List<Material> workMaterials, long timeVariable, float partialTick) {
    if (workMaterials.isEmpty()) {
      return null;
    }
    int size = workMaterials.size() - 1;
    int index = (int) (timeVariable / 50.0 % size);
    if (index > size) {
      index = size;
    } else if (index < 0) {
      index = 0;
    }
    return workMaterials.get(index).buffer(bufferSource, ModRenderType::getEnergyBall);
  }

  private static Function<MultiBufferSource, VertexConsumer> createVertexConsumer(RenderType renderType) {
    return mbs -> mbs.getBuffer(renderType);
  }

  protected static Material chestMaterial(String texture) {
    return new Material(ModMaterialAtlases.ENERGY_BALL, FutureFood.modRL(texture));
  }
}
