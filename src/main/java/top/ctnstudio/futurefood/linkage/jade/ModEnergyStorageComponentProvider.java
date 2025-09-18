package top.ctnstudio.futurefood.linkage.jade;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import snownee.jade.addon.universal.EnergyStorageProvider;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.ui.ProgressStyle;
import snownee.jade.api.view.ClientViewGroup;
import snownee.jade.api.view.EnergyView;
import snownee.jade.api.view.ViewGroup;
import snownee.jade.impl.WailaCommonRegistration;
import snownee.jade.impl.ui.ElementHelper;
import snownee.jade.util.CommonProxy;
import top.ctnstudio.futurefood.capability.ModEnergyStorage;
import top.ctnstudio.futurefood.common.block.tile.BasicEnergyStorageBlockEntity;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ModEnergyStorageComponentProvider<T extends BlockAccessor> implements IComponentProvider<T>, IServerDataProvider<T> {
  public static ModEnergyStorageComponentProvider<BlockAccessor> get(){
    return new ModEnergyStorageComponentProvider<>();
  }

  @Override
  public void appendServerData(CompoundTag data, BlockAccessor accessor) {
    CompoundTag tag = new CompoundTag();
    BasicEnergyStorageBlockEntity<?> blockEntity = (BasicEnergyStorageBlockEntity<?>) accessor.getBlockEntity();
    ModEnergyStorage energyStorage = blockEntity.getEnergyStorage();
    int maxExtract = energyStorage.getMaxExtract();
    if (maxExtract == 0) {
      return;
    }
    int energy = energyStorage.getEnergyStored();
    int maxReceive = energyStorage.getMaxReceive();
    int maxEnergyStored = energyStorage.getMaxEnergyStored();

    tag.putInt("Cur", energy);
    tag.putInt("Capacity", maxExtract);
    tag.putInt("MaxReceive", maxReceive);
    tag.putInt("MaxEnergyStored", maxEnergyStored);
    data.put("FfEnergyStorage", tag);
  }

  @Override
  public void appendTooltip(ITooltip tooltip, T accessor, IPluginConfig config) {
    CompoundTag nbt = accessor.getServerData();
    if (!nbt.contains("FfEnergyStorage")) {
      return;
    }
    CompoundTag energyStorage = nbt.getCompound("FfEnergyStorage");

    List<ClientViewGroup<EnergyView>> groups = new ArrayList<>();
    List<EnergyView> energyViews = new ArrayList<>();
    energyViews.add(EnergyView.read(energyStorage, ""));

    ClientViewGroup<EnergyView> clientViewGroup = new ClientViewGroup<>(energyViews);
    groups.add(clientViewGroup);

    boolean renderGroup = groups.size() > 1 || groups.getFirst().shouldRenderGroup();
    ClientViewGroup.tooltip(
      tooltip, groups, true, (theTooltip, group) -> {
        if (renderGroup) {
          group.renderHeader(theTooltip);
        }
        for (var view : group.views) {
          IWailaConfig.HandlerDisplayStyle style = config.getEnum(JadeIds.UNIVERSAL_ENERGY_STORAGE_STYLE);
          Component text;
          if (view.overrideText != null) {
            text = view.overrideText;
          } else {
            String current = view.current;
            if (style == IWailaConfig.HandlerDisplayStyle.PROGRESS_BAR) {
              current = ChatFormatting.WHITE + current;
            }
            text = Component.translatable("jade.fe", current, view.max);
          }

          IElementHelper helper = IElementHelper.get();
          switch (style) {
            case PLAIN_TEXT -> theTooltip.add(Component.translatable("jade.energy.text", text));
            case ICON -> {
              theTooltip.add(helper.sprite(JadeIds.JADE("energy"), 10, 10)
                .size(ElementHelper.SMALL_ITEM_SIZE)
                .translate(ElementHelper.SMALL_ITEM_OFFSET));
              theTooltip.append(text);
            }
            case PROGRESS_BAR -> {
              ProgressStyle progressStyle = helper.progressStyle().color(0xFFAA0000, 0xFF660000);
              theTooltip.add(helper.progress(view.ratio, text, progressStyle, BoxStyle.getNestedBox(), true));
            }
          }
        }
      });
  }

  @Override
  public ResourceLocation getUid() {
    return FutureFood.modRL("energy_storage");
  }
}