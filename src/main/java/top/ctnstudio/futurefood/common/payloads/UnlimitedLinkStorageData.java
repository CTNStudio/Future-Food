package top.ctnstudio.futurefood.common.payloads;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import top.ctnstudio.futurefood.api.capability.IUnlimitedLinkStorage;
import top.ctnstudio.futurefood.common.item.CyberWrenchItem;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModCapability;
import top.ctnstudio.futurefood.datagen.tag.FfBlockTags;
import top.ctnstudio.futurefood.util.ModUtil;

import java.util.*;
import java.util.stream.Collectors;

// TODO 待完成
public record UnlimitedLinkStorageData(Optional<List<List<Integer>>> linkPosSet,
                                       Optional<List<Integer>> targetPos) implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<UnlimitedLinkStorageData> TYPE = new CustomPacketPayload.Type<>(FutureFood.modRL("unlimited_link_storage_data"));
  public static final StreamCodec<ByteBuf, UnlimitedLinkStorageData> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.optional(ByteBufCodecs.<ByteBuf, List<Integer>>list().apply(ByteBufCodecs.<ByteBuf, Integer>list(3).apply(ByteBufCodecs.VAR_INT))),
    UnlimitedLinkStorageData::linkPosSet,
    ByteBufCodecs.optional(ByteBufCodecs.<ByteBuf, Integer>list(3).apply(ByteBufCodecs.VAR_INT)),
    UnlimitedLinkStorageData::targetPos,
    UnlimitedLinkStorageData::new
  );

  public UnlimitedLinkStorageData(Collection<BlockPos> linkPosSet, BlockPos targetPos) {
    this(
      linkPosSet != null ?
        Optional.of(List.copyOf(linkPosSet).stream().map(pos -> List.of(pos.getX(), pos.getY(), pos.getZ())).toList()) : Optional.empty(),
      targetPos != null ?
        Optional.of(List.of(targetPos.getX(), targetPos.getY(), targetPos.getZ())) : Optional.empty());
  }

  public Optional<Set<BlockPos>> getLinkPosSet() {
    return linkPosSet.map(list -> list.stream().map(ModUtil::getBlockPos).collect(Collectors.toSet()));
  }

  public Optional<BlockPos> getTargetPos() {
    return targetPos.map(ModUtil::getBlockPos);
  }

  /**
   * 客户端请求 该方法由玩家发送
   */
  public static void requestToServer(final UnlimitedLinkStorageData data, final IPayloadContext context) {
    context.enqueueWork(() -> {
      ServerPlayer player = (ServerPlayer) context.player();
      ServerLevel level = (ServerLevel) player.level();
      Optional<List<Integer>> targetPos = data.targetPos;
      if (targetPos.isEmpty()) {
        ModUtil.rangePos(level, BlockPos.containing(player.getEyePosition()), CyberWrenchItem.SCOPE, (entry) ->
            entry.getValue().is(FfBlockTags.UNLIMITED_LAUNCH)).keySet().stream()
          .map(pos -> Map.entry(pos, Optional.ofNullable(level.getCapability(ModCapability.ModBlockCapability.UNLIMITED_LINK_STORAGE, pos))))
          .filter(entry -> entry.getValue().isPresent())
          .forEach(entry ->
            ModPayloadUtil.sendToPlayer(player, new UnlimitedLinkStorageData(entry.getValue().get().getLinkPosList(), entry.getKey())));
      } else {
        BlockPos target = ModUtil.getBlockPos(targetPos.get());
        IUnlimitedLinkStorage capability = level.getCapability(ModCapability.ModBlockCapability.UNLIMITED_LINK_STORAGE, target);
        if (capability == null) {
          return;
        }
        ModPayloadUtil.sendToPlayer(player, new UnlimitedLinkStorageData(capability.getLinkPosList(), target));
      }
    });
  }

  /**
   * 发送到客户端
   */
  public static void toClient(final UnlimitedLinkStorageData data, final IPayloadContext context) {
    context.enqueueWork(() -> {
      IUnlimitedLinkStorage capability = context.player().level()
        .getCapability(ModCapability.ModBlockCapability.UNLIMITED_LINK_STORAGE, ModUtil.getBlockPos(data.targetPos().get()));
      if (capability == null) {
        return;
      }
      capability.setLinkList(data.linkPosSet.get().stream().map(ModUtil::getBlockPos).toList());
    });
  }


  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
