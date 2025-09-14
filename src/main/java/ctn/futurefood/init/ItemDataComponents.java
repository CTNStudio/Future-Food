package ctn.futurefood.init;

import com.mojang.serialization.Codec;
import ctn.ctntemplate.CtnTemplate;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.EncoderCache;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * 物品组件
 */
public class ItemDataComponents {
	public static final DeferredRegister<DataComponentType<?>> ITEM_DATA_COMPONENT_REGISTER = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, CtnTemplate.ID);

//	public static final Supplier<DataComponentType<Boolean>>           MODE_BOOLEAN         = recordBoolean("mode_boolean");
	
	private static final EncoderCache ENCODER_CACHE = new EncoderCache(512);
	
	public static Supplier<DataComponentType<Boolean>> recordBoolean(String name) {
		return register(name, builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL));
	}
	
	public static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builder) {
		return register(name, () -> builder.apply(DataComponentType.builder()).build());
	}
	
	public static <B extends DataComponentType<?>> DeferredHolder<DataComponentType<?>, B> register(String name, Supplier<? extends B> builder) {
		return ITEM_DATA_COMPONENT_REGISTER.register("data_components." + name, builder);
	}
	
	public static Supplier<DataComponentType<String>> recordComponent(String name) {
		return register(name, builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));
	}
}
