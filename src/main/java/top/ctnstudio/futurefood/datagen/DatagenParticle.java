package top.ctnstudio.futurefood.datagen;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;
import top.ctnstudio.futurefood.FutureFood;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/// 粒子
public class DatagenParticle extends ParticleDescriptionProvider {
  public DatagenParticle(PackOutput output, ExistingFileHelper fileHelper) {
    super(output, fileHelper);
  }

  private static ResourceLocation getPath(String name) {
    return ResourceLocation.fromNamespaceAndPath(FutureFood.ID, name);
  }

  @Override
  protected void addDescriptions() {
  }

  private <p extends ParticleOptions> void createSprite(Supplier<ParticleType<p>> type, String name) {
    sprite(type.get(), ResourceLocation.fromNamespaceAndPath(FutureFood.ID, name));
  }

  private <p extends ParticleOptions> void createSprite(Supplier<ParticleType<p>> type, String... names) {
    List<ResourceLocation> list = new ArrayList<>();
    for (String name : names) {
      list.add(getPath(name));
    }
    spriteSet(type.get(), list);
  }

}
