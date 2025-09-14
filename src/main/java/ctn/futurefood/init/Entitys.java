package ctn.futurefood.init;

import ctn.ctntemplate.CtnTemplate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class Entitys {
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPE_REGISTER = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, CtnTemplate.ID);

//	public static final Supplier<EntityType<TrainingRabbits>> TRAINING_RABBITS = registerEntity(
//			"training_rabbits",
//			EntityType.Builder.of(TrainingRabbits::new, MobCategory.MISC)
//			                  .sized(0.625F, 1.375F)
//			                  .eyeHeight(1F)
//			                  .clientTrackingRange(8)
//			                  .updateInterval(2)
//			                  .canSpawnFarFromPlayer());
	
	public static <I extends Entity> Supplier<EntityType<I>> registerEntity(final String name, final EntityType.Builder<I> sup) {
		return register(name, () -> sup.build(name));
	}
	
	public static <I extends EntityType<?>> DeferredHolder<EntityType<?>, I> register(final String name, final Supplier<? extends I> sup) {
		return ENTITY_TYPE_REGISTER.register(name, sup);
	}
}
