package top.ctnstudio.futurefood.core.init;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponentType.Builder;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import static top.ctnstudio.futurefood.common.item.CyberWrenchItem.POSITION_CODEC;
import static top.ctnstudio.futurefood.common.item.CyberWrenchItem.POSITION_STREAM;

public final class ModItemComponent {
  public static final DeferredRegister<DataComponentType<?>> ITEM_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, FutureFood.ID);

  public static final Supplier<DataComponentType<List<Integer>>> POSITION = register("position", listBuilder ->
    listBuilder.persistent(POSITION_CODEC).networkSynchronized(POSITION_STREAM));

  private static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<Builder<T>> builder) {
    return ITEM_COMPONENTS.register(name, () -> builder.apply(DataComponentType.builder()).build());
  }
}
