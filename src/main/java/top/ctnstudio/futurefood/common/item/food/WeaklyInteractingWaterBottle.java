package top.ctnstudio.futurefood.common.item.food;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class WeaklyInteractingWaterBottle extends Item {
  private static final FoodProperties foodProperties = new FoodProperties.Builder()
    .alwaysEdible()
    .usingConvertsTo(Items.GLASS_BOTTLE)
    .build();

  public WeaklyInteractingWaterBottle() {
    super(new Item.Properties()
      .food(foodProperties)
      .stacksTo(1)
      .craftRemainder(Items.GLASS_BOTTLE));
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
    livingEntity.clearFire();
    return super.finishUsingItem(stack, level, livingEntity);
  }

  @Override
  public int getUseDuration(ItemStack stack, LivingEntity entity) {
    return 8;
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.DRINK;
  }
}
