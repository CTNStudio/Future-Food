package top.ctnstudio.futurefood.common.payloads;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;

/**
 * @author 尽
 */
public interface OpenMenuData extends CustomPacketPayload {
  ItemStack carried();
}
