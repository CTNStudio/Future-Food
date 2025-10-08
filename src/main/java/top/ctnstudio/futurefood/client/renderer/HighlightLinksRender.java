package top.ctnstudio.futurefood.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.client.ModMaterialAtlases;
import top.ctnstudio.futurefood.client.ModRenderType;
import top.ctnstudio.futurefood.common.data_component.ItemBlockPosData;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModCapability;
import top.ctnstudio.futurefood.core.init.ModDataComponent;
import top.ctnstudio.futurefood.core.init.ModItem;
import top.ctnstudio.futurefood.util.BlockUtil;

import java.util.Map;

import static top.ctnstudio.futurefood.client.util.ColorUtil.colorValue;
import static top.ctnstudio.futurefood.client.util.ColorUtil.rgbColor;
import static top.ctnstudio.futurefood.client.util.GraphicsPlaneRenderUtil.renderTextures;
import static top.ctnstudio.futurefood.common.item.tool.CyberWrenchItem.SCOPE;
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

  @Override
  public void levelRender(Minecraft minecraft, ClientLevel level, Frustum frustum, PoseStack pose, Camera camera) {
    final var player = minecraft.player;
    if (level == null || player == null || !player.isAlive() || (
      !player.getMainHandItem().is(ModItem.CYBER_WRENCH) &&
        !player.getItemInHand(InteractionHand.OFF_HAND).is(ModItem.CYBER_WRENCH))) {
      return;
    }

    final var renderBuffers = minecraft.renderBuffers();
    final var cameraPos = camera.getPosition();

    final var buffer = renderBuffers.bufferSource();
    pose.pushPose();
    {
      pose.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);

      RenderSystem.disableDepthTest();
      pose.pushPose();
      renderIcon(pose, camera, player, level, buffer, frustum);
      pose.popPose();
      RenderSystem.disableDepthTest();

      pose.pushPose();
      renderLink(level, pose, player, buffer, frustum);
      pose.popPose();
    }
    pose.popPose();
    buffer.endBatch();
  }

  private static void renderLink(ClientLevel level, PoseStack pose, Player player, MultiBufferSource.BufferSource buffer, Frustum frustum) {
    ItemBlockPosData itemBlockPosData = player.getMainHandItem().get(ModDataComponent.BLOCK_POS);
    if (itemBlockPosData == null || itemBlockPosData.isEmpty()) {
      itemBlockPosData = player.getItemInHand(InteractionHand.OFF_HAND).get(ModDataComponent.BLOCK_POS);
      if (itemBlockPosData == null || itemBlockPosData.isEmpty()) {
        return;
      }
    }
    final var blockPos = itemBlockPosData.getBlockPos();
    final var capability = level.getCapability(ModCapability.ModBlockCapability.UNLIMITED_LINK_STORAGE, blockPos, null);
    if (capability == null) {
      return;
    }
    final var linkPosList = capability.getLinkPosList();
    if (linkPosList.isEmpty()) {
      return;
    }

    final var blockCenterV3 = blockPos.getCenter();
    final var consumer = buffer.getBuffer(RenderType.debugLineStrip(5));
    final var linkColor = rgbColor("#00cd12");
    final int red = ARGB32.red(linkColor);
    final int green = ARGB32.green(linkColor);
    final int blue = ARGB32.blue(linkColor);
    final int alpha = 127;
    final var x = (float) blockCenterV3.x;
    final var y = (float) blockCenterV3.y;
    final var z = (float) blockCenterV3.z;
    final var last = pose.last().pose();

    for (final var pos : linkPosList) {
      final var linkPos = pos.getCenter();
      pose.pushPose();
      {
        consumer.addVertex(last, x, y, z).setColor(red, green, blue, alpha);
        consumer.addVertex(last, (float) linkPos.x, (float) linkPos.y, (float) linkPos.z).setColor(red, green, blue, alpha);
        consumer.addVertex(last, x, y, z).setColor(red, green, blue, alpha);
      }
      pose.popPose();
    }
  }

  private static void renderIcon(PoseStack pose, Camera camera, Player player, ClientLevel level, MultiBufferSource.BufferSource buffer, Frustum frustum) {
    final var blockPosList = getRenderIconBlock(level, frustum, player.getOnPos());
    if (blockPosList.isEmpty()) {
      return;
    }
    final var recentlyBlockPos = getRecentlyBlockPos(player, level);
    final var isContainsRecentlyPos = blockPosList.containsKey(recentlyBlockPos);

    for (final var entry : blockPosList.entrySet()) {
      final var blockPos = entry.getKey();
      final var blockState = entry.getValue();
      final var blockCenterV3 = blockPos.getCenter();
      float size;
      float alpha;
      var receiveColor = rgbColor("#af5300");
      var launchColor = rgbColor("#0083af");

      // 如果是眼前最近的方块，则高亮
      if (blockPos.equals(recentlyBlockPos)) {
        alpha = 1f;
        size = 0.9f;
      } else {
        alpha = isContainsRecentlyPos ? 0.4f : 1f;
        size = 0.5f;
      }

      pose.pushPose();
      {
        pose.translate(blockCenterV3.x, blockCenterV3.y, blockCenterV3.z);
        pose.mulPose(camera.rotation());
        pose.pushPose();
        {
          if (blockState.is(UNLIMITED_RECEIVE)) {
            renderIcon(pose, buffer, RECEIVE_ICON, 0, 0, 0, size, receiveColor, alpha);
          } else if (blockState.is(UNLIMITED_LAUNCH)) {
            renderIcon(pose, buffer, LAUNCH_ICON, 0, 0, 0, size, launchColor, alpha);
          }
        }
        pose.popPose();
      }
      pose.popPose();
    }
  }

  private static @NotNull BlockPos getRecentlyBlockPos(Player player, ClientLevel level) {
    final var from = player.getEyePosition();
    final var playerLookAngle = player.getLookAngle();
    final var scope = SCOPE;
    final var to = from.add(playerLookAngle.x * scope, playerLookAngle.y * scope, playerLookAngle.z * scope);
    final var context = new ClipBlockStateContext(from, to, HighlightLinksRender::filterBlock);
    return BlockGetter.traverseBlocks(context.getFrom(), context.getTo(), context,
      (context1, pos) -> context1.isTargetBlock().test(level.getBlockState(pos)) ? pos : null,
      a -> null);
  }

  private static void renderIcon(PoseStack pose, MultiBufferSource.BufferSource bufferSource, Material material,
                                 float x, float y, float z, float size, int rgb, float a) {
    renderTextures(pose, material.buffer(bufferSource, ModRenderType::getIcon), size, x, y, z, 0, 0, 1, 1,
      ARGB32.red(rgb), ARGB32.green(rgb), ARGB32.blue(rgb), colorValue(a));
  }

  private static @NotNull Map<BlockPos, BlockState> getRenderIconBlock(ClientLevel level, Frustum frustum, BlockPos playerPos) {
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
}
