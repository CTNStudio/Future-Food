package top.ctnstudio.futurefood.common.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.api.capability.IUnlimitedLinkStorage;
import top.ctnstudio.futurefood.common.block.QedEntityBlock;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModCapability;
import top.ctnstudio.futurefood.core.init.ModItemComponent;

import java.util.ArrayList;
import java.util.List;

import static top.ctnstudio.futurefood.datagen.tag.FfBlockTags.UNLIMITED_RECEIVE;
import static top.ctnstudio.futurefood.util.ModUtil.sendOverlayMessage;

public class CyberWrenchItem extends Item {
  public static final String BINDING_SUCCESS = FutureFood.ID + ".cyber_wrench.binding.success";
  public static final String BINDING_CANCEL = FutureFood.ID + ".cyber_wrench.binding.cancel";
  public static final String BINDING_FAILURE = FutureFood.ID + ".cyber_wrench.binding.failure";
  public static final String LINK_SUCCESS = FutureFood.ID + ".cyber_wrench.link.success";
  public static final String LINK_CANCEL = FutureFood.ID + ".cyber_wrench.link.cancel";
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

    final List<Integer> diffuserPos = item.get(component);
    final boolean isLinkMode = diffuserPos != null && !diffuserPos.isEmpty();
    // 获取目标方块位置
    final BlockPos blockPos = getTargetBlockPos(level, player, isLinkMode);
    if ((player.isShiftKeyDown() || (blockPos == null && player.isShiftKeyDown())) && isLinkMode) {
      sendOverlayMessage(BINDING_CANCEL, diffuserPos.get(0), diffuserPos.get(1), diffuserPos.get(2));
      item.set(component, new ArrayList<>());
      return InteractionResultHolder.sidedSuccess(item, level.isClientSide);
    }
    if (blockPos == null) {
      return super.use(level, player, usedHand);
    }

    // 选择操作方式
    if (isLinkMode) {
      // 链接模式
      return linkMode(level, player, usedHand, diffuserPos, blockPos, item);
    }
    // 绑定模式
    return bindingMode(level, player, usedHand, diffuserPos, blockPos, item);
  }

  /**
   * 链接模式
   */
  public @NotNull InteractionResultHolder<ItemStack> linkMode(Level level, Player player, InteractionHand usedHand, List<Integer> diffuserPos, BlockPos blockPos, ItemStack item) {
    BlockPos bePos = new BlockPos(diffuserPos.get(0), diffuserPos.get(1), diffuserPos.get(2));
    IUnlimitedLinkStorage capability = level.getCapability(ModCapability.ModBlockCapability.UNLIMITED_LINK_STORAGE, bePos);
    int x = blockPos.getX();
    int y = blockPos.getY();
    int z = blockPos.getZ();
    if (capability == null) {
      sendOverlayMessage(BINDING_FAILURE, x, y, z);
      return super.use(level, player, usedHand);
    }
    if (!capability.linkBlock(level, blockPos)) {
      sendOverlayMessage(BINDING_FAILURE, x, y, z);
      return super.use(level, player, usedHand);
    }
    sendOverlayMessage(LINK_SUCCESS, x, y, z);
    return InteractionResultHolder.sidedSuccess(item, level.isClientSide);
  }

  /**
   * 绑定模式
   */
  public @NotNull InteractionResultHolder<ItemStack> bindingMode(Level level, Player player, InteractionHand usedHand, List<Integer> diffuserPos, BlockPos blockPos, ItemStack item) {
    if (diffuserPos == null || level.getCapability(ModCapability.ModBlockCapability.UNLIMITED_LINK_STORAGE, blockPos) == null) {
      return super.use(level, player, usedHand);
    }
    int x = blockPos.getX();
    int y = blockPos.getY();
    int z = blockPos.getZ();
    List<Integer> pos = List.of(x, y, z);
    sendOverlayMessage(BINDING_SUCCESS, x, y, z);
    item.set(ModItemComponent.POSITION, pos);

    return InteractionResultHolder.sidedSuccess(item, level.isClientSide);
  }

  /**
   * 获取目标方块位置
   */
  @Nullable
  public BlockPos getTargetBlockPos(Level level, Player player, boolean isLinkMode) {
    var eyePosition = player.getEyePosition();
    var lookAngle = player.getLookAngle();
    var vec3 = eyePosition.add(lookAngle.x * SCOPE, lookAngle.y * SCOPE, lookAngle.z * SCOPE);
    var context = new ClipBlockStateContext(eyePosition, vec3, blockState ->
      isLinkMode ? blockState.is(UNLIMITED_RECEIVE) : blockState.getBlock() instanceof QedEntityBlock);

    return BlockGetter.traverseBlocks(context.getFrom(), context.getTo(), context,
      (c, pos) -> context.isTargetBlock().test(level.getBlockState(pos)) ? pos : null,
      (c) -> null);
  }
}
