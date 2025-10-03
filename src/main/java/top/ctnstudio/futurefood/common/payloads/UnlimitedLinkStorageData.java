package top.ctnstudio.futurefood.common.payloads;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import top.ctnstudio.futurefood.api.capability.IUnlimitedLinkStorage;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModCapability;
import top.ctnstudio.futurefood.util.ModUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

// TODO 待完成
public record UnlimitedLinkStorageData(List<List<Integer>> linkPosSet, List<Integer> targetPos,
                                       Optional<UUID> playerUUID) implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<UnlimitedLinkStorageData> TYPE = new CustomPacketPayload.Type<>(FutureFood.modRL("unlimited_link_storage_data"));
  public static final StreamCodec<ByteBuf, UnlimitedLinkStorageData> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.<ByteBuf, List<Integer>>list().apply(ByteBufCodecs.<ByteBuf, Integer>list(3).apply(ByteBufCodecs.VAR_INT)),
    UnlimitedLinkStorageData::linkPosSet,
    ByteBufCodecs.<ByteBuf, Integer>list(3).apply(ByteBufCodecs.VAR_INT),
    UnlimitedLinkStorageData::targetPos,
    ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC),
    UnlimitedLinkStorageData::playerUUID,
    UnlimitedLinkStorageData::new
  );

  public UnlimitedLinkStorageData(Set<BlockPos> linkPosSet, BlockPos targetPos, @Nullable Player player) {
    this(linkPosSet.stream().map(pos -> List.of(pos.getX(), pos.getY(), pos.getZ())).toList(),
      List.of(targetPos.getX(), targetPos.getY(), targetPos.getZ()),
      player == null ? Optional.empty() : Optional.of(player.getUUID()));
  }

  /**
   * 客户端请求 该方法由玩家发送
   */
  public static void requestToServer(final UnlimitedLinkStorageData data, final IPayloadContext context) {
    // TODO 待填写
  }

  /**
   * 发送到客户端
   */
  public static void toClient(final UnlimitedLinkStorageData data, final IPayloadContext context) {
    context.enqueueWork(() -> {
      IUnlimitedLinkStorage capability = context.player().level()
        .getCapability(ModCapability.ModBlockCapability.UNLIMITED_LINK_STORAGE, ModUtil.getBlockPos(data.targetPos()));
      if (capability == null) {
        return;
      }
      capability.setLinkList(data.linkPosSet.stream().map(ModUtil::getBlockPos).toList());
    });
  }


  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
