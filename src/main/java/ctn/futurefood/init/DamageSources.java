package ctn.futurefood.init;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

/**
 * 伤害来源
 */
public class DamageSources extends net.minecraft.world.damagesource.DamageSources {
//	private final DamageSource physics;
	
	public DamageSources(RegistryAccess registry) {
		super(registry);
//		physics     = source(PHYSICS);
	}

//	public static BiFunction<LivingEntity, LivingEntity, ? extends DamageSource> physicsDamage() {
//		return (attacker, target) -> getDamageSource().apply(attacker, target).physics();
//	}

//	public static DamageSource physicsDamage(Entity causer) {
//		return createDamage(causer, PHYSICS);
//	}
	
	private static @NotNull DamageSource createDamage(Entity causer, ResourceKey<DamageType> damageTypes) {
		return new DamageSource(causer.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypes), causer);
	}

//	public DamageSource physics() {
//		return physics;
//	}
}
