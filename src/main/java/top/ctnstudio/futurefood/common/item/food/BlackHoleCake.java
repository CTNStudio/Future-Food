package top.ctnstudio.futurefood.common.item.food;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BlackHoleCake extends Item {
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

    // TODO - 变成白洞蛋糕
    return super.finishUsingItem(stack, world, livingEntity);
  }
}
