package top.ctnstudio.futurefood.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FastColor.ARGB32;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent.Stage;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import top.ctnstudio.futurefood.util.ModUtil;

import java.util.Set;
import java.util.regex.Pattern;

import static net.minecraft.client.renderer.RenderStateShard.*;
import static net.minecraft.client.renderer.RenderType.OutlineProperty.AFFECTS_OUTLINE;
import static top.ctnstudio.futurefood.datagen.tag.FfBlockTags.UNLIMITED_RECEIVE;

/**
 * 高亮无限链接渲染
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber
public class HighlightedLinksRender {
  public static final String RGB_WHITE = "#ffffff";

  public static final RenderType HIGHLIGHTED = RenderType.create("highlighted",
    DefaultVertexFormat.POSITION_COLOR, Mode.QUADS, 1536,
    RenderType.CompositeState.builder()
      .setShaderState(RENDERTYPE_GUI_GHOST_RECIPE_OVERLAY_SHADER)
      .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
      .setOutputState(ITEM_ENTITY_TARGET)
      .setWriteMaskState(COLOR_DEPTH_WRITE)
      .setCullState(NO_CULL)

      .setDepthTestState(NO_DEPTH_TEST) // 使用这个会无视层次渲染
      .createCompositeState(AFFECTS_OUTLINE));

  private static final Pattern COLOR_6_PATTERN = Pattern.compile("^#[a-fA-F0-9]{6}$");

  private static final int SCOPE = 10;

  @SubscribeEvent
  public static void renderLevelStageEvent(final RenderLevelStageEvent event) {
    final var stage = event.getStage();
    if (stage != Stage.AFTER_TRIPWIRE_BLOCKS) {
      return;
    }

    final Minecraft minecraft = Minecraft.getInstance();
    final ClientLevel level = minecraft.level;
    final LocalPlayer player = minecraft.player;
    if (level == null || player == null || !player.isAlive() || (
      !player.getMainHandItem().is(Tags.Items.TOOLS_WRENCH) &&
      !player.getItemInHand(InteractionHand.OFF_HAND).is(Tags.Items.TOOLS_WRENCH))) {
      return;
    }
    final BlockPos playerPos = player.getOnPos();
    final Frustum frustum = event.getFrustum();

    final Set<BlockPos> blockPosList;
    blockPosList = ModUtil.rangePos(playerPos, SCOPE, pos -> {
      if (!frustum.isVisible(new AABB(pos))) {
        return false;
      }
      BlockState blockState = level.getBlockState(pos);
      return !blockState.isEmpty() && blockState.is(UNLIMITED_RECEIVE);
    });
    if (blockPosList.isEmpty()) {
      return;
    }

    PoseStack pose = event.getPoseStack();
    Camera camera = event.getCamera();

    render(pose, camera, minecraft, blockPosList);
  }

  private static void render(PoseStack pose, Camera camera, Minecraft minecraft, Set<BlockPos> blockPosList) {
    RenderBuffers renderBuffers = minecraft.renderBuffers();
    OutlineBufferSource buffer = renderBuffers.outlineBufferSource();
//    RenderSystem.disableDepthTest();
//    RenderSystem.enableBlend();
//    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    pose.pushPose();

    Vec3 cameraPos = camera.getPosition();
    for (BlockPos blockPos : blockPosList) {
      var blockCenterV3 = blockPos.getCenter();
      double x = -cameraPos.x + blockCenterV3.x;
      double y = -cameraPos.y + blockCenterV3.y;
      double z = -cameraPos.z + blockCenterV3.z;
      pose.translate(x, y, z);

      pose.mulPose(camera.rotation());
      pose.mulPose(Axis.XP.rotationDegrees(270));

      float size = 0.05f;

      float alpha = 0.35f;

      int color = rgbColor("#4CAF50");
      var consumer = buffer.getBuffer(getRenderType());
      Pose last = pose.last();
      var matrix4f = last.pose();
      int red = ARGB32.red(color);
      int green = ARGB32.green(color);
      int blue = ARGB32.blue(color);
      pose.pushPose();

      renderSquare(pose, consumer, matrix4f, size, red, green, blue, alpha);

      pose.popPose();
    }
    pose.popPose();
    buffer.endOutlineBatch();
  }

  public static int rgbColor(String color) {
    return TextColor.parseColor(handleColor(color)).getOrThrow().getValue();
  }

  public static RenderType getRenderType() {
    return HIGHLIGHTED;
  }

  private static void renderSquare(PoseStack pose, VertexConsumer consumer, Matrix4f matrix4f, float size,
    int red, int green, int blue, float alpha) {
    renderSquare(pose, consumer, matrix4f, -0.5f, -0.5f, 0.5f, 0.5f, size, red, green, blue, alpha);
  }

  private static void renderSquare(PoseStack pose, VertexConsumer consumer, Matrix4f matrix4f,
    float minX, float minZ, float maxX, float maxZ, float size,
    int red, int green, int blue, float alpha) {
    float minInternalX = minX - size;
    float maxInternalX = maxX + size;
    float minInternalZ = minZ - size;
    float maxInternalZ = maxZ + size;

    renderQuads(pose, consumer, matrix4f, minX, minInternalZ, minX, minZ, minZ, minInternalZ, red, green, blue, alpha);
    renderQuads(pose, consumer, matrix4f, maxInternalX, minInternalZ, maxX, minZ, maxZ, maxInternalZ, red, green, blue, alpha);
    renderQuads(pose, consumer, matrix4f, maxInternalX, maxInternalZ, maxX, maxZ, maxZ, maxInternalZ, red, green, blue, alpha);
    renderQuads(pose, consumer, matrix4f, minInternalX, maxInternalZ, minX, maxZ, minZ, minInternalZ, red, green, blue, alpha);
  }

  /**
   * 等腰梯形
   */
  private static void renderQuads(PoseStack pose, VertexConsumer consumer, Matrix4f matrix4f,
    float pX, float pZ, float p1X, float p1Z, float p2Z, float p3Z,
    int red, int green, int blue, float alpha) {
    pose.pushPose();
    consumer.addVertex(matrix4f, pX, 0, pZ).setColor(red, green, blue, alpha);
    consumer.addVertex(matrix4f, p1X, 0, p1Z).setColor(red, green, blue, alpha);
    consumer.addVertex(matrix4f, p1X, 0, p2Z).setColor(red, green, blue, alpha);
    consumer.addVertex(matrix4f, pX, 0, p3Z).setColor(red, green, blue, alpha);
    pose.popPose();
  }

  private static @NotNull String handleColor(String color) {
    if (color == null || !COLOR_6_PATTERN.matcher(color).matches()) {
      color = RGB_WHITE;
    }
    return color;
  }
}
