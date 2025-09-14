package ctn.futurefood.init;

import ctn.futurefood.FutureFood;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class FfEntitys {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPE_REGISTER = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, FutureFood.ID);
	
	public static <I extends Entity> Supplier<EntityType<I>> registerEntity(final String name, final EntityType.Builder<I> sup) {
		return register(name, () -> sup.build(name));
	}
	
	public static <I extends EntityType<?>> DeferredHolder<EntityType<?>, I> register(final String name, final Supplier<? extends I> sup) {
		return ENTITY_TYPE_REGISTER.register(name, sup);
	}
}
