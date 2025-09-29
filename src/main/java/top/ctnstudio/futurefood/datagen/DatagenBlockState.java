package top.ctnstudio.futurefood.datagen;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.common.block.QedEntityBlock;
import top.ctnstudio.futurefood.common.block.QedEntityBlock.Activate;
import top.ctnstudio.futurefood.common.block.QedEntityBlock.Light;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModBlock;

public class DatagenBlockState extends BlockStateProvider {
  public DatagenBlockState(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, top.ctnstudio.futurefood.core.FutureFood.ID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    qedModel();
    simpleParticleModels(ModBlock.PARTICLE_COLLIDER.get());
  }

  private void qedModel() {
    final var block = ModBlock.QED.get();
//    final var variantBuilder = getVariantBuilder(block);

    final var multiPartBuilder = getMultipartBuilder(block);

    final String baseRl = "block/quantum_energy_diffuser/base/";
    final String displayLightRl = "block/quantum_energy_diffuser/display_light/";
    final String energyBallRl = "block/quantum_energy_diffuser/energy_ball/";

    for (Direction direction : Direction.values()) {
      final int xValue = direction == Direction.DOWN ? 180 : direction.getAxis().isHorizontal() ? 90 : 0;
      final int yValue = direction.getAxis().isVertical() ? 0 : 360;
      for (Activate activate : QedEntityBlock.Activate.values()) {
        multiPartBuilder
          .part()
          .modelFile(getExistingFile(baseRl + activate.getName()))
          .rotationX(xValue)
          .rotationY(yValue)
          .addModel()
          .nestedGroup()
          .nestedGroup()
          .condition(BlockStateProperties.FACING, direction)
          .endNestedGroup()
          .end()
          .nestedGroup()
          .condition(QedEntityBlock.ACTIVATE, activate)
          .end()
          .end();
      }
      for (Light light : QedEntityBlock.Light.values()) {
        multiPartBuilder
          .part()
          .modelFile(getExistingFile(displayLightRl + light.getName()))
          .rotationX(xValue)
          .rotationY(yValue)
          .addModel()
          .nestedGroup()
          .nestedGroup()
          .condition(BlockStateProperties.FACING, direction)
          .endNestedGroup()
          .end()
          .nestedGroup()
          .condition(QedEntityBlock.LIGHT, light)
          .end()
          .end();
      }
    }
  }

  public void directionalBlock(@NotNull Block block, int angleOffset) {
    super.directionalBlock(block, getExistingFile(BuiltInRegistries.BLOCK.getKey(block))
      , angleOffset);
  }

  private void simpleParticleModels(Block block) {
    simpleParticleModels(block, FutureFood.modRL("block/particle"));
  }

  private @NotNull ModelFile.ExistingModelFile getExistingFile(String rl) {
    return getExistingFile(FutureFood.modRL(rl));
  }

  private @NotNull ModelFile.ExistingModelFile getExistingFile(ResourceLocation path) {
    return models().getExistingFile(path);
  }

  private void simpleParticleModels(Block block, ResourceLocation particleRl) {
    simpleBlock(block, models().sign(name(block), particleRl));
  }

  private String name(Block block) {
    return key(block).getPath();
  }

  private ResourceLocation key(Block block) {
    return BuiltInRegistries.BLOCK.getKey(block);
  }
}
