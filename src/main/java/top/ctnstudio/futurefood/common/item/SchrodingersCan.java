package top.ctnstudio.futurefood.common.item;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class SchrodingersCan extends Item {
  private static final FoodProperties foodProperties = new FoodProperties.Builder()
    .alwaysEdible()
    .build();


  // TODO - 迁移到 Config。

  final int addAnEffect = 1;
  final int removeAnEffect = 2;
  final int none = 5;

  public SchrodingersCan() {
    super(new Item.Properties().food(foodProperties).stacksTo(16));
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity livingEntity) {
    final int sum = addAnEffect + removeAnEffect + none;
    final int odd = world.getRandom().nextInt(sum);

    if (odd < addAnEffect) {
      upEffect(livingEntity);
    } else if (odd < removeAnEffect) {
      dowEffect(livingEntity);
    }

    return super.finishUsingItem(stack, world, livingEntity);
  }

  private static void upEffect(LivingEntity entity) {
    final var list = entity.getActiveEffects().stream()
      .filter(it -> {
        final MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(it.getEffect().getKey());
        return Objects.nonNull(effect)
          && effect.getCategory() == MobEffectCategory.BENEFICIAL
          && !effect.isInstantenous()
          && it.isVisible();
      }).toList();
    final int odd = entity.level().getRandom().nextInt(list.size());
    final var effect = list.get(odd);
    final var newEffect = new MobEffectInstance(effect.getEffect(), effect.getDuration(),
      effect.getAmplifier() + 1);
    effect.update(newEffect);
  }

  private static void dowEffect(LivingEntity entity) {
    final var list = entity.getActiveEffects().stream()
      .filter(it -> {
        final MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(it.getEffect().getKey());
        return Objects.nonNull(effect)
          && effect.getCategory() == MobEffectCategory.HARMFUL
          && !effect.isInstantenous()
          && it.isVisible();
      }).toList();
    final int odd = entity.level().getRandom().nextInt(list.size());
    final var effect = list.get(odd);
    final var newEffect = new MobEffectInstance(effect.getEffect(), effect.getDuration(),
      effect.getAmplifier() + 1);
    effect.update(newEffect);
  }
}
