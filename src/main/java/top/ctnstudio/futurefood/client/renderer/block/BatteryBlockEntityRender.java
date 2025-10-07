package top.ctnstudio.futurefood.client.renderer.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.util.FastColor;
import net.neoforged.neoforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.client.ModMaterialAtlases;
import top.ctnstudio.futurefood.client.ModModelLayer;
import top.ctnstudio.futurefood.client.ModRenderType;
import top.ctnstudio.futurefood.common.block.tile.BatteryBlockEntity;
import top.ctnstudio.futurefood.core.FutureFood;

public class BatteryBlockEntityRender implements BlockEntityRenderer<BatteryBlockEntity> {
  public final static Material FULL_MATERIALS = chestMaterial("full");
  public final static Material UNDERAGE_MATERIALS = chestMaterial("underage");
  public final static Material EMPTY_MATERIALS = chestMaterial("empty");
  public final static Material INVENT_MATERIALS = chestMaterial("invent");
  private final ModelPart full;
  private final ModelPart underage;
  private final ModelPart empty;

  public BatteryBlockEntityRender(BlockEntityRendererProvider.Context context) {
    full = context.bakeLayer(ModModelLayer.BATTERY_FULL).getChild("root");
    underage = context.bakeLayer(ModModelLayer.BATTERY_UNDERAGE).getChild("root");
    empty = context.bakeLayer(ModModelLayer.BATTERY_EMPTY).getChild("root");
  }

  public static LayerDefinition createFullBodyLayer() {
    var md = new MeshDefinition();
    var pd = md.getRoot();
    var pd1 = pd.addOrReplaceChild("root", CubeListBuilder.create()
        .texOffs(32, 1)
        .addBox(0, 0, 0,
          8, 8, 8),
      PartPose.offset(-4, -4, -4));

    pd1.addOrReplaceChild("a", CubeListBuilder.create()
        .texOffs(36, 18)
        .addBox(0, 0, 0,
          -9, -9, -9),
      PartPose.offset(8.5f, 8.5f, 8.5f));
    return LayerDefinition.create(md, 64, 64);
  }

  public static LayerDefinition createUnderageBodyLayer() {
    var md = new MeshDefinition();
    var pd = md.getRoot();
    var pd1 = pd.addOrReplaceChild("root", CubeListBuilder.create()
        .texOffs(16, 17)
        .addBox(0, 0, 0,
          4, 4, 4),
      PartPose.offset(-2, -2, -2));

    pd1.addOrReplaceChild("a", CubeListBuilder.create()
        .texOffs(20, 26)
        .addBox(0, 0, 0,
          -5, -5, -5),
      PartPose.offset(4.5f, 4.5f, 4.5f));

    pd1.addOrReplaceChild("b", CubeListBuilder.create()
        .texOffs(32, 16)
        .addBox(0, 0, 0,
          -8, -8, -8),
      PartPose.offset(6, 6, 6));

    return LayerDefinition.create(md, 32, 32);
  }

  public static LayerDefinition createEmptyBodyLayer() {
    var md = new MeshDefinition();
    var pd = md.getRoot();
    var pd1 = pd.addOrReplaceChild(
      "root", CubeListBuilder.create()
        .texOffs(0, 8)
        .addBox(0, 0, 0,
          2, 2, 2),
      PartPose.offset(-1, -1, -1));

    pd1.addOrReplaceChild("a",
      CubeListBuilder.create()
        .texOffs(16, 8)
        .addBox(0, 0, 0,
          -4, -4, -4),
      PartPose.offset(3, 3, 3));

    return LayerDefinition.create(md, 16, 16);
  }

  @Override
  public void render(BatteryBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack,
                     @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
    final IEnergyStorage iEnergyStorage;
    final float energyStoredPercentage;
    final VertexConsumer vertexConsumer;
    final ModelPart modelPart;
    if (!blockEntity.isInfinite()) {
      iEnergyStorage = blockEntity.externalGetEnergyStorage(null);
      energyStoredPercentage = (float) iEnergyStorage.getEnergyStored() / iEnergyStorage.getMaxEnergyStored();
      if (energyStoredPercentage > 0.9f) {
        vertexConsumer = getVertexConsumer(bufferSource, FULL_MATERIALS);
        modelPart = full;
      } else if (energyStoredPercentage > 0.1f) {
        vertexConsumer = getVertexConsumer(bufferSource, UNDERAGE_MATERIALS);
        modelPart = underage;
      } else {
        vertexConsumer = getVertexConsumer(bufferSource, EMPTY_MATERIALS);
        modelPart = empty;
      }
    } else {
      vertexConsumer = getVertexConsumer(bufferSource, INVENT_MATERIALS);
      modelPart = underage;
      energyStoredPercentage = Integer.MAX_VALUE;
    }
    final long timeVariable = System.currentTimeMillis() % 10000;
    final double timeVariable1 = System.currentTimeMillis() / 500.0;

    poseStack.pushPose();
    {
      poseStack.translate(0.5f, 0.46875f, 0.5f);
      poseStack.pushPose();
      {
        float time = (timeVariable / 1000.0f * (float) Math.PI);
        poseStack.mulPose(Axis.YP.rotation(time));
        poseStack.mulPose(Axis.XP.rotation(time));
        poseStack.translate(0, (float) Math.sin(time) * 0.02f, 0);
        float scale = (float) (0.7 + Math.sin(timeVariable1 * 2) * 0.1);
        poseStack.scale(scale, scale, scale);
        poseStack.pushPose();
        {
          double a = blockEntity.isInfinite() ?
            1 :
            Math.clamp(0.3 + energyStoredPercentage + Math.sin(timeVariable1 * 2) * (energyStoredPercentage * 0.5), 0.3, 1);
          int color = FastColor.ARGB32.color((int) (a * 255), 255, 255, 255);
          modelPart.render(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, packedOverlay, color);
        }
        poseStack.popPose();
      }
      poseStack.popPose();
    }
    poseStack.popPose();
  }

  private static Material chestMaterial(String texture) {
    return new Material(ModMaterialAtlases.BATTERY, FutureFood.modRL(texture));
  }

  private @NotNull VertexConsumer getVertexConsumer(MultiBufferSource bufferSource, Material material) {
    return material.buffer(bufferSource, ModRenderType::getEnergyBall);
  }
}
