package top.ctnstudio.futurefood.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;

public class AntimatterSnack extends Item {
  private static final FoodProperties foodProperties = new FoodProperties.Builder()
    .alwaysEdible()
    .build();

  public AntimatterSnack() {
    super(new Properties().food(foodProperties));
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity livingEntity) {
    final var callback = super.finishUsingItem(stack, world, livingEntity);

    if (!(livingEntity instanceof Player player)) {
      return callback;
    }

    final int hunger = world.getRandom().nextIntBetweenInclusive(2, 6);
    final float saturation = hunger / 3f;

    final var foodData = player.getFoodData();

    foodData.eat(hunger, saturation);

    if (foodData.needsFood()) {
      world.explode(player, player.getX(), player.getY(), player.getZ(),
        20 - foodData.getFoodLevel(), ExplosionInteraction.BLOCK);
    }

    return callback;
  }
}
