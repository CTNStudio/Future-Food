package top.ctnstudio.futurefood.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WhiteHoleCake extends Item {
  private static final FoodProperties foodProperties = new FoodProperties.Builder()
    .alwaysEdible()
    .build();

  public WhiteHoleCake() {
    super(new Item.Properties().food(foodProperties));
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity livingEntity) {
    if (!(livingEntity instanceof Player)) {
      return super.finishUsingItem(stack, world, livingEntity);
    }

    // TODO - 回到目标
    return super.finishUsingItem(stack, world, livingEntity);
  }
}
