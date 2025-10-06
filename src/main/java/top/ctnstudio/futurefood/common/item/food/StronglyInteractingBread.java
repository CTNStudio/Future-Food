package top.ctnstudio.futurefood.common.item.food;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class StronglyInteractingBread extends Item {
  private static final FoodProperties foodProperties = new FoodProperties.Builder()
    .nutrition(2)
    .saturationModifier(0.1f)
    .build();

  public StronglyInteractingBread() {
    super(new Item.Properties().food(foodProperties).stacksTo(1));
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
    if (!(livingEntity instanceof Player player)) {
      return stack;
    }
    player.hurt(livingEntity.damageSources().playerAttack(player), 1);

    return stack;
  }
}
