package top.ctnstudio.futurefood.client.core;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.function.Function;

import static com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;
import static net.minecraft.client.renderer.RenderStateShard.*;
import static net.minecraft.client.renderer.RenderType.OutlineProperty.AFFECTS_OUTLINE;

@OnlyIn(Dist.CLIENT)
public final class ModRenderType {
  public static final Function<DepthTestStateShard, RenderType> HIGHLIGHTED = depth -> RenderType.create(
    "highlighted",
    DefaultVertexFormat.POSITION_COLOR,
    QUADS,
    1536,
    RenderType.CompositeState.builder()
      .setShaderState(RENDERTYPE_GUI_GHOST_RECIPE_OVERLAY_SHADER)
      .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
      .setOutputState(ITEM_ENTITY_TARGET)
      .setWriteMaskState(COLOR_DEPTH_WRITE)
      .setCullState(NO_CULL)

      .setDepthTestState(depth)
      .createCompositeState(AFFECTS_OUTLINE));

  public static final Function<ResourceLocation, RenderType> ENERGY_BALL = (texture) -> {
    RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
      .setShaderState(RENDERTYPE_ENTITY_CUTOUT_SHADER)
      .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
//        .setTexturingState(new RenderStateShard.OffsetTexturingStateShard(u, v))
      .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
      .setLightmapState(LIGHTMAP)
      .setOverlayState(OVERLAY)
      .createCompositeState(true);
    return RenderType.create("energy_ball", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS,
      1536, true, false, rendertype$compositestate);
  };

  public static RenderType getHighlighted(DepthTestStateShard depth) {
    return HIGHLIGHTED.apply(depth);
  }

  /**
   * 使用这个会无视层次渲染
   */
  public static RenderType getHighlighted() {
    return HIGHLIGHTED.apply(NO_DEPTH_TEST);
  }

  public static RenderType getEnergyBall(ResourceLocation rl) {
    return ENERGY_BALL.apply(rl);
  }

}
