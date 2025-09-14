package ctn.futurefood.init;

import ctn.ctntemplate.CtnTemplate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 声音类型
 */
public class SoundEvents {
	public static final DeferredRegister<SoundEvent> SOUND_EVENT_TYPE_REGISTER = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, CtnTemplate.ID);

//	public static final Holder<SoundEvent> ARMOR_EQUIP_ZAYIN = registerForHolder("equip_zayin", "item.armor.equip_zayin");
	
	private static DeferredHolder<SoundEvent, SoundEvent> registerForHolder(String name, String location) {
		return SOUND_EVENT_TYPE_REGISTER.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(CtnTemplate.ID, location)));
	}
}
