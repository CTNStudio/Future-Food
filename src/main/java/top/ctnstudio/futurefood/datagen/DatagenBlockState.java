package top.ctnstudio.futurefood.datagen;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.common.block.GluttonyEntityBlock;
import top.ctnstudio.futurefood.common.block.QedEntityBlock;
import top.ctnstudio.futurefood.common.block.QedEntityBlock.Light;
import top.ctnstudio.futurefood.common.block.QerEntityBlock;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModBlock;

public class DatagenBlockState extends BlockStateProvider {
  public DatagenBlockState(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, top.ctnstudio.futurefood.core.FutureFood.ID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    qedModel();
    qerModel();
    batteryModel();
    gluttonyModel();
    simpleParticleModels(ModBlock.PARTICLE_COLLIDER.get());
  }

  private void gluttonyModel() {
    Block block = ModBlock.GLUTTONY.get();
    var modelActivate = getExistingFile(blockTexture(block).withSuffix("_activate"));
    var model = getExistingFile(blockTexture(block));
    getVariantBuilder(block)
      .forAllStates(state -> ConfiguredModel.builder()
        .modelFile(state.getValue(GluttonyEntityBlock.ACTIVATE) ? modelActivate : model)
        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
        .build());
  }

  private void batteryModel() {
    Block block = ModBlock.BATTERY.get();
    var modelFunc = getExistingFile(blockTexture(block));
    getVariantBuilder(block)
      .forAllStates(state -> ConfiguredModel.builder()
        .modelFile(modelFunc)
        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
        .build());
  }

  private void simpleBlockExistingFile(Block block) {
    simpleBlock(block, getExistingFile(blockTexture(ModBlock.BATTERY.get())));
  }

  private void qerModel() {
    final var block = ModBlock.QER.get();
    final var multiPartBuilder = getMultipartBuilder(block);

    final String rl = "block/quantum_energy_receiver/";
    final String displayLightRl = "block/quantum_energy_receiver/display_light/";


    for (Direction direction : Direction.values()) {
      final int xValue = direction == Direction.DOWN ? 180 : direction.getAxis().isHorizontal() ? 90 : 0;
      final int yValue = direction.getAxis().isVertical() ? 0 : (((int) direction.toYRot()) + 180) % 360;
      for (QerEntityBlock.Activate activate : QerEntityBlock.Activate.values()) {
        multiPartBuilder
          .part()
          .modelFile(getExistingFile(rl + activate.getName()))
          .rotationX(xValue)
          .rotationY(yValue)
          .addModel()
          .condition(BlockStateProperties.FACING, direction)
          .condition(QerEntityBlock.ACTIVATE, activate)
          .end();
      }
      for (Light light : QedEntityBlock.Light.values()) {
        multiPartBuilder
          .part()
          .modelFile(getExistingFile(displayLightRl + light.getName()))
          .rotationX(xValue)
          .rotationY(yValue)
          .addModel()
          .condition(BlockStateProperties.FACING, direction)
          .condition(QedEntityBlock.LIGHT, light)
          .end();
      }
    }
  }

  private void qedModel() {
    final var block = ModBlock.QED.get();
    final var multiPartBuilder = getMultipartBuilder(block);

    final String baseRl = "block/quantum_energy_diffuser/base/";
    final String displayLightRl = "block/quantum_energy_diffuser/display_light/";

    for (Direction direction : Direction.values()) {
      final int xValue = direction == Direction.DOWN ? 180 : direction.getAxis().isHorizontal() ? 90 : 0;
      final int yValue = direction.getAxis().isVertical() ? 0 : (((int) direction.toYRot()) + 180) % 360;
      for (QedEntityBlock.Activate activate : QedEntityBlock.Activate.values()) {
        multiPartBuilder
          .part()
          .modelFile(getExistingFile(baseRl + activate.getName()))
          .rotationX(xValue)
          .rotationY(yValue)
          .addModel()
          .condition(BlockStateProperties.FACING, direction)
          .condition(QedEntityBlock.ACTIVATE, activate)
          .end();
      }
      for (Light light : QedEntityBlock.Light.values()) {
        multiPartBuilder
          .part()
          .modelFile(getExistingFile(displayLightRl + light.getName()))
          .rotationX(xValue)
          .rotationY(yValue)
          .addModel()
          .condition(BlockStateProperties.FACING, direction)
          .condition(QedEntityBlock.LIGHT, light)
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
