package ctn.futurefood.init;

import ctn.futurefood.FutureFood;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 声音类型
 */
public class FfSoundEvents {
	public static final DeferredRegister<SoundEvent> SOUND_EVENT_TYPE_REGISTER = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, FutureFood.ID);
	
	private static DeferredHolder<SoundEvent, SoundEvent> registerForHolder(String name, String location) {
		return SOUND_EVENT_TYPE_REGISTER.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(FutureFood.ID, location)));
	}
}
