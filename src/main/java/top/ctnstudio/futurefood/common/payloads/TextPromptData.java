package top.ctnstudio.futurefood.common.payloads;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.util.ModUtil;

import java.util.UUID;

public record TextPromptData(String text, UUID playerUUID) implements CustomPacketPayload {
  public static final CustomPacketPayload.Type<TextPromptData> TYPE = new CustomPacketPayload.Type<>(FutureFood.modRL("text_prompt_data"));
  public static final StreamCodec<ByteBuf, TextPromptData> STREAM_CODEC = StreamCodec.composite(
    ByteBufCodecs.STRING_UTF8,
    TextPromptData::text,
    UUIDUtil.STREAM_CODEC,
    TextPromptData::playerUUID,
    TextPromptData::new
  );

  public TextPromptData(String text, ServerPlayer player) {
    this(text, player.getUUID());
  }

  /**
   * 发送到客户端
   */
  public static void toClient(final TextPromptData data, final IPayloadContext context) {
    context.enqueueWork(() -> {
      Player player = context.player();
      if (player.getUUID().equals(data.playerUUID())) {
        ModUtil.sendOverlayMessage(data.text());
      }
    });
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
