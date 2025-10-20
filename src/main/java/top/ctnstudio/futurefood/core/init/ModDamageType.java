package top.ctnstudio.futurefood.core.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import top.ctnstudio.futurefood.core.FutureFood;

public class ModDamageType {
  public static final ResourceKey<DamageType> DAMAGE_TYPE_RADIATION =
    ResourceKey.create(Registries.DAMAGE_TYPE, FutureFood.modRL("radiation"));

  public static DamageSource getDamageBy(LivingEntity entity, ResourceKey<DamageType> type) {
    final var holder = entity.damageSources()
      .damageTypes.getHolderOrThrow(DAMAGE_TYPE_RADIATION);
    return new DamageSource(holder);
  }
}
