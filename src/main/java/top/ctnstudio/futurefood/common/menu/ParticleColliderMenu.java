package top.ctnstudio.futurefood.common.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import top.ctnstudio.futurefood.common.block.tile.ParticleColliderBlockEntity;
import top.ctnstudio.futurefood.core.init.ModMenu;

public class ParticleColliderMenu extends BasicEnergyMenu {
  protected ParticleColliderBlockEntity.WorkProgress workProgress;

  public ParticleColliderMenu(int containerId, Inventory container, FriendlyByteBuf buf) {
    super(ModMenu.PARTICLE_COLLIDER_MENU.get(), containerId, container, new ItemStackHandler(4),
      new SimpleContainerData(2), new SimpleContainerData(2), buf);
    this.workProgress = new ParticleColliderBlockEntity.WorkProgress(new ParticleColliderBlockEntity.WorkTick());
  }

  public ParticleColliderMenu(int containerId, Inventory container, IItemHandler dataInventory,
                              EnergyData energyData, ParticleColliderBlockEntity.WorkProgress workProgress) {
    super(ModMenu.PARTICLE_COLLIDER_MENU.get(), containerId, container, dataInventory, energyData, workProgress);
    this.workProgress = workProgress;
  }

  @Override
  protected void addOtherSlot(IItemHandler dataInventory, ContainerData data) {
    super.addOtherSlot(dataInventory, data);
    addSlot(new SlotItemHandler(dataInventory, 1, 28, 35));
    addSlot(new SlotItemHandler(dataInventory, 2, 123, 35));
    addSlot(new SlotItemHandler(dataInventory, 3, 80, 35) {
      // 仅允许输出
      @Override
      public boolean mayPlace(ItemStack stack) {
        return false;
      }
    });
  }

  public int getRemainingTick() {
    return workProgress.get(0);
  }

  public void setRemainingTick(int remainingTick) {
    this.workProgress.set(0, remainingTick);
  }

  public int getMaxWorkTick() {
    return workProgress.get(1);
  }

  public void setMaxWorkTick(int maxWorkTick) {
    this.workProgress.set(1, maxWorkTick);
  }
}
