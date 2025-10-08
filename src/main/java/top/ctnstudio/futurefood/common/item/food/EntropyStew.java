package top.ctnstudio.futurefood.common.item.food;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import top.ctnstudio.futurefood.common.item.tool.FoodItem;

import javax.annotation.Nonnull;

public class EntropyStew extends FoodItem {
  private static final FoodProperties foodProperties = new FoodProperties.Builder()
    .alwaysEdible()
    .usingConvertsTo(Items.BOWL)
    .build();

  public EntropyStew() {
    super(new Properties()
      .food(foodProperties)
      .stacksTo(1)
      .craftRemainder(Items.BOWL));
  }

  @Override
  @Nonnull
  public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level world,
                                   @Nonnull LivingEntity livingEntity) {
    final var list = BuiltInRegistries.MOB_EFFECT.holders().toList();

    final var odd = world.getRandom().nextInt(list.size());
    final var holder = list.get(odd);
    final var effect = BuiltInRegistries.MOB_EFFECT.get(holder.getKey());
    final var time = effect.isInstantenous() ? 1 : 20 * 30;
    final var effectInstance = new MobEffectInstance(holder, time, 0);

    livingEntity.addEffect(effectInstance);

    return super.finishUsingItem(stack, world, livingEntity);
  }
}
