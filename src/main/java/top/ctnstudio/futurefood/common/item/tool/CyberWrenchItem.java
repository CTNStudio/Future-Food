package top.ctnstudio.futurefood.common.item.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.common.item.data_component.ItemBlockPosData;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModCapability;
import top.ctnstudio.futurefood.core.init.ModDataComponent;
import top.ctnstudio.futurefood.datagen.tag.FfBlockTags;
import top.ctnstudio.futurefood.util.ModUtil;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static top.ctnstudio.futurefood.datagen.tag.FfBlockTags.UNLIMITED_RECEIVE;

public class CyberWrenchItem extends Item {
  // 文本翻译键
  public static final String BINDING_SUCCESS = FutureFood.ID + ".cyber_wrench.binding.success";
  public static final String BINDING_CANCEL = FutureFood.ID + ".cyber_wrench.binding.cancel";
  public static final String BINDING_FAILURE = FutureFood.ID + ".cyber_wrench.binding.failure";
  public static final String LINK_SUCCESS = FutureFood.ID + ".cyber_wrench.link.success";
  public static final String LINK_REMOVE = FutureFood.ID + ".cyber_wrench.link.remove";
  public static final String LINK_REMOVE_FAILURE = FutureFood.ID + ".cyber_wrench.link.remove_failure";
  public static final String LINK_FAILURE = FutureFood.ID + ".cyber_wrench.link.failure";

  // 绑定范围
  public static final int SCOPE = 10; // TODO 添加配置功能

  // 可以进行链接绑定方块判断的函数
  private static final Predicate<BlockState> PREDICATE = blockState ->
    blockState.is(FfBlockTags.UNLIMITED_RECEIVE) || blockState.is(FfBlockTags.UNLIMITED_LAUNCH);

  public CyberWrenchItem(Properties properties) {
    super(properties.stacksTo(1)
      .component(ModDataComponent.BLOCK_POS, ItemBlockPosData.EMPTY));
  }

  /**
   * 提示方法,将文本发送给玩家屏幕
   */
  public static void sendOverlayMessage(String text, Vec3i pos) {
    ModUtil.sendOverlayMessage(text, pos.getX(), pos.getY(), pos.getZ());
  }

  /**
   * 提示方法,将文本发送给玩家屏幕
   */
  public static void sendOverlayMessage(String text, List<Integer> pos) {
    ModUtil.sendOverlayMessage(text, pos.get(0), pos.get(1), pos.get(2));
  }

  /**
   * 物品使用时触发的方法
   */
  @Override
  public @NotNull InteractionResultHolder<ItemStack> use(final @NotNull Level level, final Player player, final @NotNull InteractionHand usedHand) {
    // 先判断是否带有物品物品组件
    final var item = player.getItemInHand(usedHand);
    if (!item.has(ModDataComponent.BLOCK_POS)) {
      return super.use(level, player, usedHand);
    }

    // 获取组件参数
    final var diffuserPos = item.get(ModDataComponent.BLOCK_POS);
    if (diffuserPos == null) {
      return super.use(level, player, usedHand);
    }

    // 获取目标方块信息
    final var targetBlock = getTargetBlockPos(level, player);

    // 如果没有判断目标方块，则进行移除绑定
    if (targetBlock.isEmpty() || (targetBlock.containsKey("targetLinkBlock") && targetBlock.containsKey("targetBindingBlock"))) {
      // 如果没有绑定方块，则返回
      if (!player.isShiftKeyDown() || diffuserPos.isEmpty()) {
        return super.use(level, player, usedHand);
      }

      // 如果按了Shift键，则进行移除绑定
      unbind(diffuserPos.getBlockPos(), item);
      return InteractionResultHolder.sidedSuccess(item, level.isClientSide);
    }

    // 在目标方块中获取要链接的方块
    if (targetBlock.containsKey("targetLinkBlock") && !diffuserPos.isEmpty()) {
      if (!linkMode(level, diffuserPos.getBlockPos(), targetBlock.get("targetLinkBlock").getKey())) {
        return super.use(level, player, usedHand);
      }

      return InteractionResultHolder.sidedSuccess(item, level.isClientSide);
    }

    // 在目标方块中获取要绑定的方块
    if (!targetBlock.containsKey("targetBindingBlock")) {
      return super.use(level, player, usedHand);
    }

    // 判断绑定方块是否与原来绑定方块相同
    final var targetBindingBlock = targetBlock.get("targetBindingBlock").getKey();
    if (!diffuserPos.isEmpty() && diffuserPos.getBlockPos().equals(targetBindingBlock)) {
      return super.use(level, player, usedHand);
    }

    if (bindingMode(level, targetBindingBlock, item)) {
      return InteractionResultHolder.sidedSuccess(item, level.isClientSide);
    }

    return super.use(level, player, usedHand);
  }

  /**
   * 移除绑定
   */
  public void unbind(@NotNull BlockPos diffuserPos, @NotNull ItemStack item) {
    sendOverlayMessage(BINDING_CANCEL, diffuserPos);
    // 将组件参数设置为空以表示移除绑定
    item.set(ModDataComponent.BLOCK_POS, ItemBlockPosData.EMPTY);
  }

  /**
   * 点击方块前触发
   */
  @Override
  public @NotNull InteractionResult onItemUseFirst(ItemStack stack, @NotNull UseOnContext context) {
    // 判断组件
    if (!stack.has(ModDataComponent.BLOCK_POS)) {
      return super.onItemUseFirst(stack, context);
    }

    // 获取参数
    final var diffuserPos = stack.get(ModDataComponent.BLOCK_POS);

    if (diffuserPos == null) {
      return super.onItemUseFirst(stack, context);
    }

    final var targetBlockPos = context.getClickedPos();
    final var level = context.getLevel();
    final var blockState = level.getBlockState(targetBlockPos);
    final var player = context.getPlayer();

    // 判断点击方块是否符合条件
    if (blockState.isEmpty() || !PREDICATE.test(blockState)) {
      if (player == null || !player.isShiftKeyDown()) {
        return super.onItemUseFirst(stack, context);
      }

      // 如果按了Shift键，则进行移除绑定
      if (!diffuserPos.isEmpty()) {
        unbind(diffuserPos.getBlockPos(), stack);
      }
      return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
    }

    // 判断是否是可链接方块
    if (blockState.is(FfBlockTags.UNLIMITED_RECEIVE)) {
      // 无论如何都挥动
      if (!diffuserPos.isEmpty()) {
        linkMode(level, diffuserPos.getBlockPos(), targetBlockPos);
      }
      return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
    }

    // 判断是否是可绑定方块, 是否按了Shift键, 绑定方块是否与原来绑定方块相同
    if (blockState.is(FfBlockTags.UNLIMITED_LAUNCH) &&
      player != null && !player.isShiftKeyDown() ||
      (diffuserPos.isEmpty() || diffuserPos.equals(targetBlockPos))) {
      bindingMode(level, targetBlockPos, stack);
    }

    return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
  }

  /**
   * 链接模式
   *
   * @param level          世界
   * @param diffuserPos    绑定方块位置
   * @param targetBlockPos 目标方块位置
   * @return 是否成功
   */
  public boolean linkMode(@NotNull Level level, @NotNull BlockPos diffuserPos, @NotNull BlockPos targetBlockPos) {
    // 不在客户端处理
    if (level.isClientSide) {
      return true;
    }
    // 获取目标方块的自定义的链接能力
    var capability = level.getCapability(ModCapability.ModBlockCapability.UNLIMITED_LINK_STORAGE, diffuserPos);
    if (capability == null) {
      sendOverlayMessage(LINK_FAILURE, targetBlockPos);
      return false;
    }

    // 通过链接能力进行链接
    if (capability.isLink(targetBlockPos)) {
      if (!capability.linkBlock(level, targetBlockPos)) {
        sendOverlayMessage(LINK_FAILURE, targetBlockPos);
        return false;
      }

      sendOverlayMessage(LINK_SUCCESS, targetBlockPos);
      return true;
    }

    // 通过链接能力进行移除链接
    if (!capability.removeLink(targetBlockPos)) {
      sendOverlayMessage(LINK_REMOVE_FAILURE, targetBlockPos);
      return false;
    }

    sendOverlayMessage(LINK_REMOVE, targetBlockPos);
    return true;
  }

  /**
   * 绑定模式
   *
   * @param level          世界
   * @param targetBlockPos 目标方块位置
   * @param item           玩家物品
   * @return 是否成功
   */
  public boolean bindingMode(@NotNull Level level, @NotNull BlockPos targetBlockPos, @NotNull ItemStack item) {
    // 获取要进行绑定的方块的能力
    if (level.getCapability(ModCapability.ModBlockCapability.UNLIMITED_LINK_STORAGE, targetBlockPos) == null) {
      sendOverlayMessage(BINDING_FAILURE, targetBlockPos);
      return false;
    }

    // 如果不为空，则进行进行绑定
    sendOverlayMessage(BINDING_SUCCESS, targetBlockPos);
    item.set(ModDataComponent.BLOCK_POS, new ItemBlockPosData(targetBlockPos));
    return true;
  }

  /**
   * 获取目标方块位置
   */
  @NotNull
  public static Map<String, Map.Entry<BlockPos, BlockState>> getTargetBlockPos(@NotNull Level level, @NotNull Player player) {
    // 获取玩家眼睛高度
    final var eyePosition = player.getEyePosition();
    // 获取玩家视线向量
    final var lookAngle = player.getLookAngle();
    // 获取玩家视线范围
    final var vec3 = eyePosition.add(lookAngle.x * SCOPE, lookAngle.y * SCOPE, lookAngle.z * SCOPE);
    // 创建判断条件
    final var context = new ClipBlockStateContext(eyePosition, vec3, PREDICATE);

    // 按照路径遍历方块
    return BlockGetter.traverseBlocks(context.getFrom(), context.getTo(), context,
      (c, pos) -> {
        // 获取方块状态并判断是否是可链接方块或者可绑定方块
        BlockState blockState = level.getBlockState(pos);
        if (!context.isTargetBlock().test(blockState)) {
          // 返回null以让方法继续遍历
          return null;
        }

        // 设置名称标注为可链接方块, 为可绑定方块
        return Map.of(blockState.is(UNLIMITED_RECEIVE) ?
            "targetLinkBlock" :
            "targetBindingBlock",
          Map.entry(pos, blockState));
      },
      // 如果上面的函数返回失败则返回结果
      (c) -> Map.of());
  }
}
