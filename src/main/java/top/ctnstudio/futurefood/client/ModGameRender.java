package top.ctnstudio.futurefood.client;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import top.ctnstudio.futurefood.client.renderer.HighlightLinksRender;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModEffect;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = FutureFood.ID, value = Dist.CLIENT)
public final class ModGameRender {
  @SubscribeEvent
  public static void levelRender(final RenderLevelStageEvent event) {
    final RenderLevelStageEvent.Stage stage = event.getStage();
    final Minecraft minecraft = Minecraft.getInstance();
    final ClientLevel level = minecraft.level;
    final Frustum frustum = event.getFrustum();
    final PoseStack pose = event.getPoseStack();
    final Camera camera = event.getCamera();

    if (stage == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
      HighlightLinksRender.get().levelRender(minecraft, level, frustum, pose, camera);
    }
  }

  @SubscribeEvent
  public static void fogColorRender(ViewportEvent.ComputeFogColor event) {
    final var entity = event.getCamera().getEntity();
    if (!(entity instanceof LivingEntity living)) {
      return;
    }

    if (living.hasEffect(ModEffect.RADIATION)) {
      event.setRed(Color.GREEN.getRed() / 255f);
      event.setGreen(Color.GREEN.getGreen() / 255f);
      event.setBlue(Color.GREEN.getBlue() / 255f);
    }
  }

  @SubscribeEvent
  public static void fogRender(ViewportEvent.RenderFog event) {
    final var entity = event.getCamera().getEntity();
    if (!(entity instanceof LivingEntity living)) {
      return;
    }

    if (!living.hasEffect(ModEffect.RADIATION)) {
      return;
    }

    final float start;
    final float end;

    float f = (float) Mth.lerp(1.0f, event.getPartialTick(), 5.0F);
    if (event.getMode() == FogRenderer.FogMode.FOG_SKY) {
      start = 0.0F;
      end = f * 0.8F;
    } else {
      start = f * 0.25F;
      end = f;
    }

    event.setCanceled(true);
    event.setFogShape(FogShape.SPHERE);
    event.setNearPlaneDistance(start);
    event.setFarPlaneDistance(end);
  }
}
