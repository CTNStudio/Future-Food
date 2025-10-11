package top.ctnstudio.futurefood.common.item.food;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.ctnstudio.futurefood.common.item.tool.FoodItem;

public class UnpredictableChorusFruit extends FoodItem {
  private static final FoodProperties foodProperties = new FoodProperties.Builder()
    .nutrition(2)
    .saturationModifier(0.1f)
    .alwaysEdible()
    .build();

  public UnpredictableChorusFruit() {
    super(new Item.Properties().food(foodProperties));
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity livingEntity) {
    final var x = livingEntity.getX() + world.getRandom().nextIntBetweenInclusive(-20, 20);
    final var y = livingEntity.getY() + world.getRandom().nextIntBetweenInclusive(-20, 20);
    final var z = livingEntity.getZ() + world.getRandom().nextIntBetweenInclusive(-20, 20);

    livingEntity.setPos(x, Math.max(-60, y), z);

    return super.finishUsingItem(stack, world, livingEntity);
  }
}
