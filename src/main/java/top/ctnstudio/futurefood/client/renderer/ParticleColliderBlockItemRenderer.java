package top.ctnstudio.futurefood.client.renderer;

import net.minecraft.world.level.block.Block;
import top.ctnstudio.futurefood.common.item.ModGeoBlockItem;

public class ParticleColliderBlockItemRenderer<T extends ModGeoBlockItem> extends BasicGeoBlockItemRenderer<T> {
  public ParticleColliderBlockItemRenderer(Block block) {
    super(block);
  }
}
