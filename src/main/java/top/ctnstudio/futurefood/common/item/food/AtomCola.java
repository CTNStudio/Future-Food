package top.ctnstudio.futurefood.common.item.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import top.ctnstudio.futurefood.common.item.tool.FoodItem;
import top.ctnstudio.futurefood.core.init.ModEffect;

public class AtomCola extends FoodItem {
  private static final FoodProperties foodProperties = new FoodProperties.Builder()
    .nutrition(2)
    .saturationModifier(0.2f)
    .effect(() ->
      new MobEffectInstance(ModEffect.RADIATION, 20 * 60 * 5, 0),
      1.0f)
    .alwaysEdible()
    .build();

  public AtomCola() {
    super(new Properties().food(foodProperties));
  }
}
