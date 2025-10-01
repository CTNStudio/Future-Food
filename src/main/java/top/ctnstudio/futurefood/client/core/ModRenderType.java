package top.ctnstudio.futurefood.client.core;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.BiFunction;
import java.util.function.Function;

import static net.minecraft.client.renderer.RenderStateShard.*;
import static net.minecraft.client.renderer.RenderType.OutlineProperty.AFFECTS_OUTLINE;

@OnlyIn(Dist.CLIENT)
public final class ModRenderType {
  public static final Function<DepthTestStateShard, RenderType> HIGHLIGHTED = Util.memoize(
    depth -> RenderType.create("highlighted",
      DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS,
      1536, RenderType.CompositeState.builder()
        .setShaderState(RENDERTYPE_GUI_GHOST_RECIPE_OVERLAY_SHADER)
        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
        .setCullState(NO_CULL)
        .setDepthTestState(depth)
        .createCompositeState(AFFECTS_OUTLINE)));

  public static final BiFunction<DepthTestStateShard, ResourceLocation, RenderType> ICON =
    Util.memoize((depth, texture) -> RenderType.create("icon",
      DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS,
      256, false, true, RenderType.CompositeState.builder()
        .setDepthTestState(depth)
        .setShaderState(RenderType.RENDERTYPE_TEXT_SHADER)
        .setTextureState(new TextureStateShard(texture, false, false))
        .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
        .setLightmapState(RenderType.LIGHTMAP)
        .createCompositeState(false)));

  public static final Function<ResourceLocation, RenderType> ENERGY_BALL = Util.memoize((texture) ->
    RenderType.create("energy_ball", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS,
      1536, false, false, RenderType.CompositeState.builder()
        .setShaderState(RENDERTYPE_TEXT_SHADER)
        .setTextureState(new TextureStateShard(texture, false, false))
      .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
      .setLightmapState(LIGHTMAP)
      .setOverlayState(OVERLAY)
        .createCompositeState(false)));

  public static RenderType getHighlighted(DepthTestStateShard depth) {
    return HIGHLIGHTED.apply(depth);
  }

  /**
   * 使用这个会无视层次渲染
   */
  public static RenderType getHighlighted() {
    return HIGHLIGHTED.apply(NO_DEPTH_TEST);
  }

  public static RenderType getIcon(ResourceLocation rl) {
    return ICON.apply(NO_DEPTH_TEST, rl);
  }

  public static RenderType getEnergyBall(ResourceLocation rl) {
    return ENERGY_BALL.apply(rl);
  }
}
