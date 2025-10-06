package top.ctnstudio.futurefood.common.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.api.capability.IModEnergyStorage;
import top.ctnstudio.futurefood.common.item.data_component.ItemEnergyStorageData;
import top.ctnstudio.futurefood.core.init.ModDataComponent;

public class BatteryItem extends BlockItem {
  public BatteryItem(Block block, Properties properties) {
    super(block, properties.component(ModDataComponent.ENERGY_STORAGE, new ItemEnergyStorageData.Data(1024000)));
  }

  public BatteryItem(Block block) {
    this(block, new Properties());
  }

  @Override
  public @NotNull ItemStack getDefaultInstance() {
    ItemStack itemStack = new ItemStack(this);
    // 保证每个物品的能源对象唯一
    itemStack.set(ModDataComponent.ENERGY_STORAGE, new ItemEnergyStorageData.Data(1024000));
    return itemStack;
  }

  @Override
  protected boolean placeBlock(@NotNull BlockPlaceContext context, BlockState state) {
    if (!super.placeBlock(context, state)) {
      return false;
    }
    if (!(context.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, context.getClickedPos(), null) instanceof IModEnergyStorage energyStorage)) {
      return true;
    }
    ItemEnergyStorageData.Data data = getDefaultInstance().get(ModDataComponent.ENERGY_STORAGE);
    if (data == null) {
      return true;
    }
    energyStorage.setEnergy(data.getEnergyStored());
    energyStorage.setMaxEnergyStored(data.getMaxEnergyStored());
    energyStorage.setMaxExtract(data.getMaxExtract());
    energyStorage.setMaxReceive(data.getMaxReceive());
    return true;
  }
}
