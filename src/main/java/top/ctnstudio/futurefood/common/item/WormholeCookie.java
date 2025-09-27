package top.ctnstudio.futurefood.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WormholeCookie extends Item {
  private static final FoodProperties foodProperties = new FoodProperties.Builder()
    .alwaysEdible()
    .build();

  public WormholeCookie() {
    super((new Item.Properties()).food(foodProperties));
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity livingEntity) {
    if (!(livingEntity instanceof Player player)) {
      return super.finishUsingItem(stack, world, livingEntity);
    }

    if (world.getRandom().nextBoolean()) {
      player.getFoodData().eat(2, 0.2f);
    }

    return super.finishUsingItem(stack, world, livingEntity);
  }

  @Override
  public int getUseDuration(ItemStack stack, LivingEntity entity) {
    return 8;
  }
}
