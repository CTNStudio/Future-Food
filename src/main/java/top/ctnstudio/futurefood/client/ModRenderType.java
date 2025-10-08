package top.ctnstudio.futurefood.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.mojang.blaze3d.vertex.DefaultVertexFormat.POSITION_COLOR;
import static com.mojang.blaze3d.vertex.VertexFormat.Mode.DEBUG_LINE_STRIP;
import static com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;
import static net.minecraft.client.renderer.RenderStateShard.*;
import static net.minecraft.client.renderer.RenderType.CompositeState.builder;
import static net.minecraft.client.renderer.RenderType.OutlineProperty.AFFECTS_OUTLINE;
import static net.minecraft.client.renderer.RenderType.create;

@OnlyIn(Dist.CLIENT)
public final class ModRenderType {
  public static final Function<DepthTestStateShard, RenderType> HIGHLIGHTED = Util.memoize(
    depth -> create("highlighted",
      POSITION_COLOR, QUADS,
      1536, builder()
        .setShaderState(RENDERTYPE_GUI_GHOST_RECIPE_OVERLAY_SHADER)
        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
        .setCullState(NO_CULL)
        .setDepthTestState(depth)
        .createCompositeState(AFFECTS_OUTLINE)));

  public static final BiFunction<DepthTestStateShard, ResourceLocation, RenderType> ICON =
    Util.memoize((depth, texture) -> create("icon",
      DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, QUADS,
      256, false, true, builder()
        .setDepthTestState(depth)
        .setShaderState(RENDERTYPE_TEXT_SHADER)
        .setTextureState(new TextureStateShard(texture, false, false))
        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
        .setLightmapState(LIGHTMAP)
        .createCompositeState(false)));

  public static final RenderType LINK_LINE = create("link_line",
    POSITION_COLOR, DEBUG_LINE_STRIP, 1536,
    builder()
      .setShaderState(POSITION_COLOR_SHADER)
      .setTransparencyState(ADDITIVE_TRANSPARENCY)
      .setCullState(NO_CULL)
      .createCompositeState(false));

  public static final Function<ResourceLocation, RenderType> ENERGY_BALL = Util.memoize((texture) ->
    create("energy_ball", DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, QUADS,
      1536, false, false, builder()
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
