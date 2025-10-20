package top.ctnstudio.futurefood.common.item.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import top.ctnstudio.futurefood.common.item.tool.FoodItem;
import top.ctnstudio.futurefood.core.init.ModEffect;

public class AtomCola extends FoodItem {
  private static final FoodProperties foodProperties = new FoodProperties.Builder()
    .nutrition(2)
    .saturationModifier(0.2f)
    .effect(() ->
      new MobEffectInstance(ModEffect.RADIATION, 20 * 60 * 3, 0), 1.0f)
    .effect(() ->
      new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20 * 60, 4), 1.0f)
    .effect(() ->
      new MobEffectInstance(MobEffects.DIG_SPEED, 20 * 60, 4), 1.0f)
    .alwaysEdible()
    .build();

  public AtomCola() {
    super(new Properties().food(foodProperties));
  }

  @Override
  public UseAnim getUseAnimation(ItemStack stack) {
    return UseAnim.DRINK;
  }
}
