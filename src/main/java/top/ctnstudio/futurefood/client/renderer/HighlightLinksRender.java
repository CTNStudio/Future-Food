package top.ctnstudio.futurefood.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.client.core.ModMaterialAtlases;
import top.ctnstudio.futurefood.client.core.ModRenderType;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModItem;
import top.ctnstudio.futurefood.util.BlockUtil;

import java.util.Map;

import static top.ctnstudio.futurefood.client.util.ColorUtil.colorValue;
import static top.ctnstudio.futurefood.client.util.ColorUtil.rgbColor;
import static top.ctnstudio.futurefood.client.util.GraphicsPlaneRenderUtil.renderTextures;
import static top.ctnstudio.futurefood.common.item.CyberWrenchItem.SCOPE;
import static top.ctnstudio.futurefood.datagen.tag.FfBlockTags.UNLIMITED_LAUNCH;
import static top.ctnstudio.futurefood.datagen.tag.FfBlockTags.UNLIMITED_RECEIVE;

/**
 * 无线链接高亮渲染
 */
@OnlyIn(Dist.CLIENT)
public class HighlightLinksRender implements ModRender {
  private static final HighlightLinksRender INSTANCE = new HighlightLinksRender();
  private static final Material RECEIVE_ICON = chestMaterial("receive");
  private static final Material LAUNCH_ICON = chestMaterial("launch");

  public static ModRender get() {
    return INSTANCE;
  }

  private static @NotNull Map<BlockPos, BlockState> getBlock(ClientLevel level, Frustum frustum, BlockPos playerPos) {
    return BlockUtil.rangePos(level, playerPos, SCOPE, entry -> {
      BlockPos pos = entry.getKey();
      if (!frustum.isVisible(new AABB(pos))) {
        return false;
      }
      BlockState blockState = level.getBlockState(pos);
      return filterBlock(blockState);
    });
  }

  private static boolean filterBlock(BlockState blockState) {
    return !blockState.isEmpty() && (blockState.is(UNLIMITED_RECEIVE) || blockState.is(UNLIMITED_LAUNCH));
  }

  private static @NotNull Material chestMaterial(String texture) {
    return chestMaterial(FutureFood.modRL(texture));
  }

  private static @NotNull Material chestMaterial(ResourceLocation rl) {
    return new Material(ModMaterialAtlases.ICON, rl);
  }

  private static void renderIcon(PoseStack pose, MultiBufferSource.BufferSource bufferSource, Material material,
                                 float x, float y, float z, float size, int rgb, float a) {
    renderTextures(pose, material.buffer(bufferSource, ModRenderType::getIcon), size, x, y, z, 0, 0, 1, 1,
      ARGB32.red(rgb), ARGB32.green(rgb), ARGB32.blue(rgb), colorValue(a));
  }

  // TODO 更酷的渲染
  @Override
  public void levelRender(Minecraft minecraft, ClientLevel level, Frustum frustum, PoseStack pose, Camera camera) {
    final LocalPlayer player = minecraft.player;
    if (level == null || player == null || !player.isAlive() || (
      !player.getMainHandItem().is(ModItem.CYBER_WRENCH) &&
        !player.getItemInHand(InteractionHand.OFF_HAND).is(ModItem.CYBER_WRENCH))) {
      return;
    }
    final BlockPos playerPos = player.getOnPos();
    final Map<BlockPos, BlockState> blockPosList = getBlock(level, frustum, playerPos);
    if (blockPosList.isEmpty()) {
      return;
    }

    final RenderBuffers renderBuffers = minecraft.renderBuffers();
    final Vec3 cameraPos = camera.getPosition();
    final Vec3 from = player.getEyePosition();
    final Vec3 playerLookAngle = player.getLookAngle();
    final double scope = SCOPE;
    final Vec3 to = from.add(playerLookAngle.x * scope, playerLookAngle.y * scope, playerLookAngle.z * scope);
    final ClipBlockStateContext context = new ClipBlockStateContext(from, to, HighlightLinksRender::filterBlock);
    final BlockPos recentlyBlockPos = BlockGetter.traverseBlocks(context.getFrom(), context.getTo(), context,
      (context1, pos) -> context1.isTargetBlock().test(level.getBlockState(pos)) ? pos : null,
      a -> null);

    final boolean isContainsRecentlyPos = blockPosList.containsKey(recentlyBlockPos);

    RenderSystem.disableDepthTest();
    final MultiBufferSource.BufferSource buffer = renderBuffers.bufferSource();
    pose.pushPose();
    pose.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
    for (Map.Entry<BlockPos, BlockState> entry : blockPosList.entrySet()) {
      final BlockPos blockPos = entry.getKey();
      final BlockState blockState = entry.getValue();
      final Vec3 blockCenterV3 = blockPos.getCenter();
      var size = 0.5f;
      var alpha = isContainsRecentlyPos ? 0.4f : 1f;
      var receiveColor = rgbColor("#af5300");
      var launchColor = rgbColor("#0083af");
      // 如果是眼前最近的方块，则高亮
      if (blockPos.equals(recentlyBlockPos)) {
        alpha = 1f;
        size = 0.9f;
      }

      pose.pushPose();

      pose.translate(blockCenterV3.x, blockCenterV3.y, blockCenterV3.z);
      pose.mulPose(camera.rotation());
      pose.pushPose();
      if (blockState.is(UNLIMITED_RECEIVE)) {
        renderIcon(pose, buffer, chestMaterial("receive"), 0, 0, 0, size, receiveColor, alpha);
      } else if (blockState.is(UNLIMITED_LAUNCH)) {
        renderIcon(pose, buffer, chestMaterial("launch"), 0, 0, 0, size, launchColor, alpha);
      }

      pose.popPose();

      pose.popPose();
    }
    pose.popPose();
    buffer.endBatch();
    RenderSystem.disableDepthTest();
  }
}
