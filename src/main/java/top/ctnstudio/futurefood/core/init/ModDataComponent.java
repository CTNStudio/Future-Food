package top.ctnstudio.futurefood.core.init;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponentType.Builder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import top.ctnstudio.futurefood.common.item.data_component.ItemBlockPosData;
import top.ctnstudio.futurefood.common.item.data_component.ItemEnergyStorageData;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public final class ModDataComponent {
  public static final DeferredRegister<DataComponentType<?>> ITEM_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, FutureFood.ID);
  public static final ResourceLocation ENERGY_STORAGE_NAME = FutureFood.modRL("energy_storage");

  public static final Supplier<DataComponentType<ItemBlockPosData>> BLOCK_POS = register(
    "block_pos", ItemBlockPosData.CODEC, ItemBlockPosData.STREAM);
  public static final Supplier<DataComponentType<ItemEnergyStorageData.Data>> ENERGY_STORAGE = register(
    ENERGY_STORAGE_NAME.getPath(), ItemEnergyStorageData.Data.CODEC, ItemEnergyStorageData.Data.STREAM);

  private static <T> Supplier<DataComponentType<T>> register(String name, Codec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
    return register(name, builder -> builder.persistent(codec).networkSynchronized(streamCodec));
  }

  private static <T> Supplier<DataComponentType<T>> register(String name, UnaryOperator<Builder<T>> builder) {
    return ITEM_COMPONENTS.register(name, () -> builder.apply(DataComponentType.builder()).build());
  }
}
