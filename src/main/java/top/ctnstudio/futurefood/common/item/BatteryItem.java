package top.ctnstudio.futurefood.common.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.api.adapter.InfiniteModEnergyStorage;
import top.ctnstudio.futurefood.api.capability.IModEnergyStorage;
import top.ctnstudio.futurefood.common.block.BatteryEntityBlock;
import top.ctnstudio.futurefood.common.data_component.ModComponentEnergyStorage;
import top.ctnstudio.futurefood.core.init.ModDataComponent;
import top.ctnstudio.futurefood.util.EnergyUtil;

import java.util.function.Supplier;

public class BatteryItem extends BlockItem {
  private static final Supplier<ModComponentEnergyStorage.EnergyStorageData> ENERGY_STORAGE =
    () -> new ModComponentEnergyStorage.EnergyStorageData(1024000);
  private static final Supplier<ModComponentEnergyStorage.EnergyStorageData> INFINITE_ENERGY_STORAGE =
    () -> new ModComponentEnergyStorage.EnergyStorageData(new InfiniteModEnergyStorage());

  public BatteryItem(BatteryEntityBlock block, Properties properties) {
    super(block, properties.component(ModDataComponent.ENERGY_STORAGE,
      block.isInfinite() ? INFINITE_ENERGY_STORAGE.get() : ENERGY_STORAGE.get()));
  }

  public BatteryItem(BatteryEntityBlock block) {
    this(block, new Properties());
  }

  @Override
  public @NotNull ItemStack getDefaultInstance() {
    ItemStack itemStack = new ItemStack(this);
    // 保证每个物品的能源对象唯一
    itemStack.set(ModDataComponent.ENERGY_STORAGE, ((BatteryEntityBlock) getBlock()).isInfinite() ?
      INFINITE_ENERGY_STORAGE.get() :
      ENERGY_STORAGE.get());
    return itemStack;
  }


  @Override
  protected boolean placeBlock(@NotNull BlockPlaceContext context, BlockState state) {
    if (!super.placeBlock(context, state)) {
      return false;
    }

    if (context.getLevel().getCapability(Capabilities.EnergyStorage.BLOCK, context.getClickedPos(), null) instanceof IModEnergyStorage energyStorage &&
      context.getItemInHand().getCapability(Capabilities.EnergyStorage.ITEM) instanceof IModEnergyStorage itemEnergyStorage) {
      EnergyUtil.copyEnergy(energyStorage, itemEnergyStorage);
    }

    return true;
  }
}
