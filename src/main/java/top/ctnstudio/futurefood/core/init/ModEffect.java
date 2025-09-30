package top.ctnstudio.futurefood.core.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.ctnstudio.futurefood.common.effect.EffectRadiation;
import top.ctnstudio.futurefood.core.FutureFood;

public final class ModEffect {
  public static final DeferredRegister<MobEffect> EFFECT =
    DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, FutureFood.ID);

  public static final DeferredHolder<MobEffect, EffectRadiation> RADIATION =
    EFFECT.register("radiation", EffectRadiation::new);

}
