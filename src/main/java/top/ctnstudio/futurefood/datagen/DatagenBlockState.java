package top.ctnstudio.futurefood.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.ctnstudio.futurefood.FutureFood;


public class DatagenBlockState extends BlockStateProvider {
  public DatagenBlockState(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, FutureFood.ID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {

  }
}
