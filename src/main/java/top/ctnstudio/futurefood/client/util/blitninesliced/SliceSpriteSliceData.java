package top.ctnstudio.futurefood.client.util.blitninesliced;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/// 切片精灵片段数据
@OnlyIn(Dist.CLIENT)
public record SliceSpriteSliceData(int width, int height,
                                   int uOffset, int vOffset,
                                   int uWidth, int vHeight) {
  public void blit(ResourceLocation texture, GuiGraphics guiGraphics, int x, int y) {
    this.blit(texture, guiGraphics, x, y, 256, 256);
  }

  public void blit(ResourceLocation texture, GuiGraphics guiGraphics, int x, int y,
                   int textureWidth, int textureHeight) {
    guiGraphics.blit(texture, x, y, width, height, uOffset, vOffset, uWidth, vHeight,
      textureWidth, textureHeight);
  }
}
