package top.ctnstudio.futurefood.common.item.food;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import top.ctnstudio.futurefood.common.item.tool.FoodItem;
import top.ctnstudio.futurefood.core.init.ModItem;

public class BlackHoleCake extends FoodItem {
  private static final FoodProperties foodProperties = new FoodProperties.Builder()
    .alwaysEdible()
    .build();

  public BlackHoleCake() {
    super(new Item.Properties().food(foodProperties));
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity livingEntity) {
    if (!(livingEntity instanceof Player)) {
      return super.finishUsingItem(stack, world, livingEntity);
    }

    final var cake = ModItem.WHITE_HOLE_CAKE.get().getDefaultInstance();
    var data = cake.getOrDefault(
      DataComponents.CUSTOM_DATA,
      CustomData.of(new CompoundTag()));
    final var pos = livingEntity.position();

    data = data.update(nbt -> {
      nbt.putDouble("x", pos.x());
      nbt.putDouble("y", pos.y());
      nbt.putDouble("z", pos.z());
      nbt.putString("world", world.dimension().location().toString());
    });

    cake.set(DataComponents.CUSTOM_DATA, data);

    ((Player) livingEntity).addItem(cake);
    return super.finishUsingItem(stack, world, livingEntity);
  }
}
