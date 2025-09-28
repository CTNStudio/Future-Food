package top.ctnstudio.futurefood.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
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
public class ModRenderType {
  public static final Function<DepthTestStateShard, RenderType> HIGHLIGHTED = depth -> RenderType.create(
    "highlighted",
    DefaultVertexFormat.POSITION_COLOR,
    QUADS,
    1536,
    RenderType.CompositeState.builder()
      .setShaderState(RENDERTYPE_ENERGY_SWIRL_SHADER)
      .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
      .setOutputState(ITEM_ENTITY_TARGET)
      .setWriteMaskState(COLOR_DEPTH_WRITE)
      .setCullState(NO_CULL)

      .setDepthTestState(depth)
      .createCompositeState(AFFECTS_OUTLINE));

  public static final Function<ResourceLocation, RenderType> ENERGY_BALL = texture -> RenderType.create(
    "energy_ball",
    DefaultVertexFormat.BLOCK,
    QUADS,
    1536,
    false,
    true,
    RenderType.CompositeState.builder()
      .setShaderState(POSITION_TEX_SHADER) // RENDERTYPE_ENERGY_SWIRL_SHADER
      .setLightmapState(NO_LIGHTMAP)
      .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
      .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
      .setTexturingState(DEFAULT_TEXTURING)
      .setOutputState(ITEM_ENTITY_TARGET)
      .setCullState(CULL)
      .createCompositeState(false)
  );

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

  private static RenderType createEnergyBall(ResourceLocation location) {
    return RenderType.create(
      "energy_ball",
      DefaultVertexFormat.POSITION_TEX,
      QUADS,
      256,
      false,
      true,
      RenderType.CompositeState.builder()
        .setShaderState(POSITION_TEX_SHADER)
        .setTextureState(new RenderStateShard.TextureStateShard(location, false, false))
        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
        .setWriteMaskState(COLOR_WRITE)
        .createCompositeState(false)
    );
  }

}
