package top.ctnstudio.futurefood.client.renderer;

import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import top.ctnstudio.futurefood.common.item.ModGeoBlockItem;

@OnlyIn(Dist.CLIENT)
public class ParticleColliderBlockItemRenderer<T extends ModGeoBlockItem> extends BasicGeoBlockItemRenderer<T> {
  public ParticleColliderBlockItemRenderer(Block block) {
    super(block);
  }
}
