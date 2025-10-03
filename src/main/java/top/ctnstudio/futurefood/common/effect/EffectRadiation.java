package top.ctnstudio.futurefood.common.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.EffectCure;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import top.ctnstudio.futurefood.core.init.ModEffect;

import java.awt.*;
import java.util.Objects;
import java.util.Set;

@EventBusSubscriber
public class EffectRadiation extends MobEffect {
  public EffectRadiation() {
    super(MobEffectCategory.HARMFUL, Color.GREEN.getRGB());
  }

  @SubscribeEvent
  public static void onFinishEffect(MobEffectEvent.Expired event) {
    final var effect = event.getEffectInstance();
    if (Objects.isNull(effect)
      || !effect.is(ModEffect.RADIATION)) {
      return;
    }

    // TODO 添加一种伤害类型：辐射伤害
    final int lv = effect.getAmplifier();
    event.getEntity().addEffect(new MobEffectInstance(MobEffects.POISON,
      20 * 60 * 5, lv));
    event.getEntity().hurt(event.getEntity().damageSources().magic(), lv * 5);
  }

  @SubscribeEvent
  public static void onRemoveEffect(MobEffectEvent.Remove event) {
    if (Objects.isNull(event.getEffectInstance())
      || !event.getEffectInstance().is(ModEffect.RADIATION)) {
      return;
    }

    event.setCanceled(true);
  }

  @Override
  public void fillEffectCures(Set<EffectCure> cures, MobEffectInstance effectInstance) {
    cures.clear();
  }
}
