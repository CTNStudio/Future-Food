package top.ctnstudio.futurefood.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent.Stage;
import net.neoforged.neoforge.common.Tags;
import top.ctnstudio.futurefood.datagen.tag.FfBlockTags;

import java.util.ArrayList;
import java.util.List;

import static net.minecraft.client.renderer.debug.DebugRenderer.renderFilledBox;

/**
 * 高亮无限链接渲染
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber
public class HighlightedLinksRender {
  //  private static final List<BlockPos> highlightBlockPosList = new ArrayList<>();
  private static final float red = 0F;
  private static final float green = 0.5F;
  private static final float blue = 0F;
  private static final float alpha = 0.5F;

  @SubscribeEvent
  public static void tick(ClientTickEvent.Pre event) {
//    var minecraft = Minecraft.getInstance();
//    var player = minecraft.player;
//    var level = minecraft.level;
//    if (level == null || player == null || !player.isAlive() ||
//        !player.getMainHandItem().is(Tags.Items.TOOLS_WRENCH)) {
//      return;
//    }
//    highlightBlockPosList.clear();
//    var playerPos = player.getOnPos();
//    var aabb = AABB.encapsulatingFullBlocks(playerPos.offset(10, 10, 10), playerPos.offset(-10, -10, -10));
//    highlightBlockPosList.addAll(BlockPos.betweenClosedStream(aabb)
//      .filter(pos -> level.getBlockState(pos).is(FfBlockTags.UNLIMITED_LAUNCH))
//      .toList());
  }

  @SubscribeEvent
  public static void renderLevelStageEvent(RenderLevelStageEvent event) {
//    testLayerDraw
    var stage = event.getStage();
    if (stage != Stage.AFTER_TRIPWIRE_BLOCKS) {
      return;
    }

    var minecraft = Minecraft.getInstance();
    var level = minecraft.level;
    var player = minecraft.player;
    if (level == null || player == null || !player.isAlive() ||
        !player.getMainHandItem().is(Tags.Items.TOOLS_WRENCH)) {
      return;
    }
    var playerPos = player.getOnPos();
    var aabb = AABB.encapsulatingFullBlocks(playerPos.offset(10, 10, 10), playerPos.offset(-10, -10, -10));
    var frustum = event.getFrustum();
    List<BlockPos> blockPosList = new ArrayList<>();
    BlockPos.betweenClosedStream(aabb).filter(pos -> {
      var pos1 = pos;
      return frustum.isVisible(new AABB(pos)) && level.getBlockState(pos).is(FfBlockTags.UNLIMITED_RECEIVE);
    });/*.forEach(pos -> {
    });*/

    var buffer = minecraft.renderBuffers().bufferSource();
    var pose = event.getPoseStack();

    var cameraPos = event.getCamera().getPosition();
    for (BlockPos blockPos : blockPosList) {
      pose.pushPose();

      pose.translate(-cameraPos.x - 11, -cameraPos.y - 11, -cameraPos.z - 11);

      pose.pushPose();
      var blockPosCenter = blockPos.getCenter();
//      pose.translate(vec.x - cameraPos.x, vec.y - cameraPos.y, vec.z - cameraPos.z);
//      var distance = player.distanceToSqr(blockPos.getX(), blockPos.getY(), blockPos.getZ());

      renderFilledBox(pose, buffer, new AABB(blockPos), red, green, blue, alpha);

      pose.popPose();

      pose.popPose();
      String blockText = String.format("block: x%.2f y%.2f z%.2f", blockPosCenter.x, blockPosCenter.y, blockPosCenter.z);
      String cameraText = String.format("camera: x%.2f y%.2f z%.2f", cameraPos.x, cameraPos.y, cameraPos.z);
      MutableComponent literal = Component.literal(blockText).append(" ").append(cameraText);
      minecraft.gui.setOverlayMessage(literal, true);

      buffer.endLastBatch();
    }
    buffer.endBatch();
  }
}
