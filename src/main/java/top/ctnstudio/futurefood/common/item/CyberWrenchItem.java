package top.ctnstudio.futurefood.common.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.api.capability.IUnlimitedLinkStorage;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModCapability;
import top.ctnstudio.futurefood.core.init.ModItemComponent;
import top.ctnstudio.futurefood.util.ModUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static top.ctnstudio.futurefood.datagen.tag.FfBlockTags.UNLIMITED_LAUNCH;
import static top.ctnstudio.futurefood.datagen.tag.FfBlockTags.UNLIMITED_RECEIVE;

public class CyberWrenchItem extends Item {
  public static final String BINDING_SUCCESS = FutureFood.ID + ".cyber_wrench.binding.success";
  public static final String BINDING_CANCEL = FutureFood.ID + ".cyber_wrench.binding.cancel";
  public static final String BINDING_FAILURE = FutureFood.ID + ".cyber_wrench.binding.failure";
  public static final String LINK_SUCCESS = FutureFood.ID + ".cyber_wrench.link.success";
  public static final String LINK_REMOVE = FutureFood.ID + ".cyber_wrench.link.remove";
  public static final String LINK_REMOVE_FAILURE = FutureFood.ID + ".cyber_wrench.link.remove_failure";
  public static final String LINK_FAILURE = FutureFood.ID + ".cyber_wrench.link.failure";
  public static final int SCOPE = 10; // TODO 添加配置功能

  public static final StreamCodec<ByteBuf, List<Integer>> POSITION_STREAM =
    ByteBufCodecs.<ByteBuf, Integer>list(3).apply(ByteBufCodecs.VAR_INT);

  public static final Codec<List<Integer>> POSITION_CODEC =
    Codec.list(Codec.INT, 0, 3);

  public CyberWrenchItem(Properties properties) {
    super(properties.stacksTo(1)
      .component(ModItemComponent.POSITION, new ArrayList<>()));
  }

  // TODO 处理链接动作，添加断开链接
  @Override
  public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand usedHand) {
    final ItemStack item = player.getItemInHand(usedHand);
    final DataComponentType<List<Integer>> component = ModItemComponent.POSITION.get();
    if (!item.has(component)) {
      return super.use(level, player, usedHand);
    }

    final List<Integer> diffuserPosList = item.get(component);
    if (diffuserPosList == null) {
      return super.use(level, player, usedHand);
    }

    final BlockPos diffuserPos = diffuserPosList.isEmpty() ? null :
      new BlockPos(diffuserPosList.get(0), diffuserPosList.get(1), diffuserPosList.get(2));

    // 移除绑定
    if (player.isShiftKeyDown() && diffuserPos != null) {
      sendOverlayMessage(BINDING_CANCEL, diffuserPos);
      item.set(component, new ArrayList<>());
      return InteractionResultHolder.sidedSuccess(item, level.isClientSide);
    }

    // 获取目标方块信息
    final var targetBlock = getTargetBlockPos(level, player);

    Map.Entry<BlockPos, BlockState> launchEntry;
    Map.Entry<BlockPos, BlockState> receiveEntry = null;
    if (targetBlock.isEmpty() || (
      (launchEntry = targetBlock.get("launch")) == null && (receiveEntry = targetBlock.get("receive")) == null)) {
      return super.use(level, player, usedHand);
    }

    if (receiveEntry != null) {
      return linkMode(level, player, usedHand, diffuserPos, receiveEntry.getKey(), item);
    }
    BlockPos launchEntryKey = launchEntry.getKey();
    if (diffuserPos == null || !diffuserPos.equals(launchEntryKey)) {
      return bindingMode(level, player, usedHand, launchEntryKey, item);
    }
    return super.use(level, player, usedHand);
  }

  /**
   * 链接模式
   * @param level  世界
   * @param player 玩家
   * @param usedHand 使用的Hand
   * @param diffuserPos 绑定方块位置
   * @param targetBlockPos 目标方块位置
   * @param item 玩家物品
   * @return
   */
  public @NotNull InteractionResultHolder<ItemStack> linkMode(Level level,
                                                              Player player,
                                                              InteractionHand usedHand,
                                                              BlockPos diffuserPos,
                                                              BlockPos targetBlockPos,
                                                              ItemStack item) {
    if (level.isClientSide) {
      return InteractionResultHolder.success(item);
    }
    IUnlimitedLinkStorage capability = level.getCapability(ModCapability.ModBlockCapability.UNLIMITED_LINK_STORAGE, diffuserPos);
    if (capability == null) {
      sendOverlayMessage(LINK_FAILURE, targetBlockPos);
      return super.use(level, player, usedHand);
    }

    if (capability.isLink(targetBlockPos)) {
      if (!capability.linkBlock(level, targetBlockPos)) {
        sendOverlayMessage(LINK_FAILURE, targetBlockPos);
        return super.use(level, player, usedHand);
      }
      sendOverlayMessage(LINK_SUCCESS, targetBlockPos);
    } else {
      if (!capability.removeLink(targetBlockPos)) {
        sendOverlayMessage(LINK_REMOVE_FAILURE, targetBlockPos);
        return super.use(level, player, usedHand);
      }
      sendOverlayMessage(LINK_REMOVE, targetBlockPos);
    }

    return InteractionResultHolder.consume(item);
  }

  /**
   * 绑定模式
   * @param level  世界
   * @param player 玩家
   * @param usedHand 使用的Hand
   * @param targetBlockPos 目标方块位置
   * @param item 玩家物品
   * @return
   */
  public @NotNull InteractionResultHolder<ItemStack> bindingMode(Level level,
                                                                 Player player,
                                                                 InteractionHand usedHand,
                                                                 BlockPos targetBlockPos,
                                                                 ItemStack item) {
    if (level.getCapability(ModCapability.ModBlockCapability.UNLIMITED_LINK_STORAGE, targetBlockPos) == null) {
      sendOverlayMessage(BINDING_FAILURE, targetBlockPos);
      return super.use(level, player, usedHand);
    }
    sendOverlayMessage(BINDING_SUCCESS, targetBlockPos);
    item.set(ModItemComponent.POSITION, getPositionList(targetBlockPos));

    return InteractionResultHolder.sidedSuccess(item, level.isClientSide);
  }

  public static @NotNull List<Integer> getPositionList(BlockPos blockPos) {
    return List.of(blockPos.getX(), blockPos.getY(), blockPos.getZ());
  }

  /**
   * 获取目标方块位置
   */
  @NotNull
  public Map<String, Map.Entry<BlockPos, BlockState>> getTargetBlockPos(Level level, Player player) {
    var eyePosition = player.getEyePosition();
    var lookAngle = player.getLookAngle();
    var vec3 = eyePosition.add(lookAngle.x * SCOPE, lookAngle.y * SCOPE, lookAngle.z * SCOPE);
    var context = new ClipBlockStateContext(eyePosition, vec3, blockState ->
      blockState.is(UNLIMITED_RECEIVE) || blockState.is(UNLIMITED_LAUNCH));

    int[] receiveQuantity = new int[]{0};
    int[] launchQuantity = new int[]{0};
    var map = new HashMap<String, Map.Entry<BlockPos, BlockState>>();
    return BlockGetter.traverseBlocks(context.getFrom(), context.getTo(), context,
      (c, pos) -> {
        if (receiveQuantity[0] >= 1 && (launchQuantity[0] >= 1)) {
          return map;
        }
        BlockState blockState = level.getBlockState(pos);
        if (!context.isTargetBlock().test(blockState)) {
          return null;
        }
        String key;
        if (blockState.is(UNLIMITED_RECEIVE)) {
          if (receiveQuantity[0] >= 1) {
            return null;
          }
          key = "receive";
          receiveQuantity[0]++;
        } else {
          if (launchQuantity[0] >= 1) {
            return null;
          }
          key = "launch";
          launchQuantity[0]++;
        }
        map.put(key, Map.entry(pos, blockState));
        return map;
      },
      (c) -> map);
  }

  public static void sendOverlayMessage(String text, Vec3i pos) {
    ModUtil.sendOverlayMessage(text, pos.getX(), pos.getY(), pos.getZ());
  }

  public static void sendOverlayMessage(String text, List<Integer> pos) {
    ModUtil.sendOverlayMessage(text, pos.get(0), pos.get(1), pos.get(2));
  }
}
