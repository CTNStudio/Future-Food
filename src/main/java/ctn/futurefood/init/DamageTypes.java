package ctn.futurefood.init;

import ctn.ctntemplate.CtnTemplate;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public interface DamageTypes extends net.minecraft.world.damagesource.DamageTypes {
//	ResourceKey<DamageType> PHYSICS  = create("physics");
	
	
	/**
	 * 创建伤害类型
	 */
	static ResourceKey<DamageType> register(final String name) {
		return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(CtnTemplate.ID, name));
	}
}
