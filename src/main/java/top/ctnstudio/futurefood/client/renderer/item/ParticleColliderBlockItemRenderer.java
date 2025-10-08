package top.ctnstudio.futurefood.client.renderer.item;

import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.ctnstudio.futurefood.client.model.BasicGeoModel;
import top.ctnstudio.futurefood.common.item.ModGeoBlockItem;

@OnlyIn(Dist.CLIENT)
public class ParticleColliderBlockItemRenderer<T extends ModGeoBlockItem> extends BasicGeoBlockItemRenderer<T> {
  public ParticleColliderBlockItemRenderer(Block block) {
    super(new BasicGeoModel.BlockModel<>("particle_collider_activate", "particle_collider"));
  }
}
