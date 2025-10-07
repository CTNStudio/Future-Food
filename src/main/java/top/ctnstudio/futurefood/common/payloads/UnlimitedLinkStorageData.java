package top.ctnstudio.futurefood.common.payloads;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.api.capability.IUnlimitedLinkStorage;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModCapability;
import top.ctnstudio.futurefood.util.BlockUtil;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// TODO 待完成
public record UnlimitedLinkStorageData(List<List<Integer>> linkPosSet,
                                       List<Integer> targetPos) implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<UnlimitedLinkStorageData> TYPE = new CustomPacketPayload.Type<>(FutureFood.modRL("unlimited_link_storage_data"));
  public static final StreamCodec<ByteBuf, UnlimitedLinkStorageData> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.<ByteBuf, List<Integer>>list().apply(ByteBufCodecs.<ByteBuf, Integer>list(3).apply(ByteBufCodecs.VAR_INT)),
    UnlimitedLinkStorageData::linkPosSet,
    ByteBufCodecs.<ByteBuf, Integer>list(3).apply(ByteBufCodecs.VAR_INT),
    UnlimitedLinkStorageData::targetPos,
    UnlimitedLinkStorageData::new
  );

  public UnlimitedLinkStorageData(@NotNull Collection<BlockPos> linkPosSet, @NotNull BlockPos targetPos) {
    this(List.copyOf(linkPosSet).stream().map(pos -> List.of(pos.getX(), pos.getY(), pos.getZ())).toList(),
      List.of(targetPos.getX(), targetPos.getY(), targetPos.getZ()));
  }

  /**
   * 发送到客户端
   */
  public static void toClient(final UnlimitedLinkStorageData data, final IPayloadContext context) {
    context.enqueueWork(() -> {
      IUnlimitedLinkStorage capability = context.player().level()
        .getCapability(ModCapability.ModBlockCapability.UNLIMITED_LINK_STORAGE, data.getTargetPos());
      if (capability == null) {
        return;
      }
      capability.setLinkList(data.getLinkPosSet());
    });
  }

  public Set<BlockPos> getLinkPosSet() {
    return linkPosSet.stream().map(BlockUtil::getBlockPos).collect(Collectors.toSet());
  }

  public BlockPos getTargetPos() {
    return BlockUtil.getBlockPos(targetPos);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
