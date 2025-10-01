package top.ctnstudio.futurefood.common.payloads;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

/// 数据包集
public class ModPayloadUtil {
  /// 同步玩家数据-服务端到客户端 ///

  /**
   * 发送文本提示
   *
   * @param serverPlayer 发送的玩家
   * @param text         发送的文本
   * @param clientPlayer 接收的玩家
   */
  public static void textPromptData(ServerPlayer serverPlayer, String text, ServerPlayer clientPlayer) {
    sendToPlayer(serverPlayer, new TextPromptData(text, clientPlayer));
  }

  /// 同步玩家数据-客户端到服务端 ///


  /// 打开界面 ///


  /// 工具 ///

  /// 发送玩家数据包（服务端到客户端）
  public static void sendToPlayer(ServerPlayer serverPlayer, CustomPacketPayload customPacketPayload) {
    PacketDistributor.sendToPlayer(serverPlayer, customPacketPayload);
  }

  /// 发送玩家数据包（客户端到服务端）
  public static void sendToServer(CustomPacketPayload customPacketPayload) {
    PacketDistributor.sendToServer(customPacketPayload);
  }
}
