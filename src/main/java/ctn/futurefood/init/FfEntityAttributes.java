package ctn.futurefood.init;

import ctn.futurefood.FutureFood;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.neoforge.common.BooleanAttribute;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FfEntityAttributes {
	public static final DeferredRegister<Attribute> ATTRIBUTE_REGISTER = DeferredRegister.create(Registries.ATTRIBUTE, FutureFood.ID);
	
	public static Holder<Attribute> registerRangedAttribute(String name, String descriptionId, double defaultValue, double min, double max) {
		return ATTRIBUTE_REGISTER.register(name, () -> new RangedAttribute(FutureFood.ID + "." + descriptionId, defaultValue, min, max).setSyncable(true));
	}
	
	public static Holder<Attribute> registerBooleanAttribute(String name, String descriptionId, boolean defaultValue) {
		return ATTRIBUTE_REGISTER.register(name, () -> new BooleanAttribute(FutureFood.ID + "." + descriptionId, defaultValue).setSyncable(true));
	}
}
