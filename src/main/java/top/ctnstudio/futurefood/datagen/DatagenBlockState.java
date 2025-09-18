package top.ctnstudio.futurefood.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.core.init.ModBlock;

public class DatagenBlockState extends BlockStateProvider {
  public DatagenBlockState(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, top.ctnstudio.futurefood.core.FutureFood.ID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    directionalBlock(ModBlock.QED.get(), 180);
    directionalBlock(ModBlock.QER.get(), 180);
  }

  public void directionalBlock(@NotNull Block block, int angleOffset) {
    super.directionalBlock(block, models().getExistingFile(BuiltInRegistries.BLOCK.getKey(block))
      , angleOffset);
  }

  /*public void specialItem(Item item) {
    getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile(parse("builtin/entity")));
  }*/
}
