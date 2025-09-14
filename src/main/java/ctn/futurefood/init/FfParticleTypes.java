package ctn.futurefood.init;

import com.mojang.serialization.MapCodec;
import ctn.futurefood.FutureFood;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * 粒子类型
 */
public class FfParticleTypes {
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPE_REGISTER = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, FutureFood.ID);
	
	private static <T extends ParticleOptions> Supplier<ParticleType<T>> register(String id,
			boolean overrideLimiter,
			MapCodec<T> mapCodec,
			StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
		return PARTICLE_TYPE_REGISTER.register(
				id, () -> new ParticleType<>(overrideLimiter) {
					@Override
					
					public MapCodec<T> codec() {
						return mapCodec;
					}
					
					@Override
					
					public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
						return streamCodec;
					}
				});
	}
}
