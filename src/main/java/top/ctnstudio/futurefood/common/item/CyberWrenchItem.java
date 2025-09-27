package top.ctnstudio.futurefood.common.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
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
import net.minecraft.world.phys.Vec3;
import top.ctnstudio.futurefood.api.capability.IUnlimitedLinkStorage;
import top.ctnstudio.futurefood.common.block.QedEntityBlock;
import top.ctnstudio.futurefood.core.init.ModCapabilitys.Block;
import top.ctnstudio.futurefood.core.init.ModItemComponent;

import java.util.ArrayList;
import java.util.List;

import static top.ctnstudio.futurefood.datagen.tag.FfBlockTags.UNLIMITED_RECEIVE;

public class CyberWrenchItem extends Item {
  public static final int SCOPE = 10;

  public static final StreamCodec<ByteBuf, List<Integer>> POSITION_STREAM =
    ByteBufCodecs.<ByteBuf, Integer>list(3).apply(ByteBufCodecs.VAR_INT);

  public static final Codec<List<Integer>> POSITION_CODEC =
    Codec.list(Codec.INT, 0, 3);

  public CyberWrenchItem(Properties properties) {
    super(properties.stacksTo(1)
      .component(ModItemComponent.POSITION, new ArrayList<>()));
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
    Vec3 eyePosition = player.getEyePosition();
    Vec3 lookAngle = player.getLookAngle();
    Vec3 vec3 = eyePosition.add(lookAngle.x * SCOPE, lookAngle.y * SCOPE, lookAngle.z * SCOPE);

    ItemStack item = player.getItemInHand(usedHand);
    DataComponentMap itemComponents = item.getComponents();

    final boolean isLinkMode;
    List<Integer> itemStorageBlockPos;

    DataComponentType<List<Integer>> component = ModItemComponent.POSITION.get();
    if (!itemComponents.has(component)) {
      return super.use(level, player, usedHand);
    }
    itemStorageBlockPos = itemComponents.get(component);
    isLinkMode = (itemStorageBlockPos != null && !itemStorageBlockPos.isEmpty());

    ClipBlockStateContext context = new ClipBlockStateContext(eyePosition, vec3, blockState ->
      isLinkMode ? blockState.is(UNLIMITED_RECEIVE) : blockState.getBlock() instanceof QedEntityBlock);

    final var blockPos = BlockGetter.traverseBlocks(context.getFrom(), context.getTo(), context,
      (c, pos) -> {
        BlockState state = level.getBlockState(pos);
        boolean test = context.isTargetBlock().test(state);
        return test ? pos : null;
      }, (c) -> null);
    if (blockPos == null) {
      return super.use(level, player, usedHand);
    }

    if (isLinkMode) {
      BlockPos bePos = new BlockPos(itemStorageBlockPos.get(0), itemStorageBlockPos.get(1), itemStorageBlockPos.get(2));
      IUnlimitedLinkStorage capability = level.getCapability(Block.UNLIMITED_LINK_STORAGE, bePos);
      if (capability == null) {
        return super.use(level, player, usedHand);
      }
      return capability.linkBlock(level, blockPos) ?
        InteractionResultHolder.sidedSuccess(item, level.isClientSide) : super.use(level, player, usedHand);
    }

    if (itemStorageBlockPos == null || level.getCapability(Block.UNLIMITED_LINK_STORAGE, blockPos) == null) {
      return super.use(level, player, usedHand);
    }

    itemStorageBlockPos.clear();
    itemStorageBlockPos.addAll(List.of(blockPos.getX(), blockPos.getY(), blockPos.getZ()));

    return InteractionResultHolder.sidedSuccess(item, level.isClientSide);
  }
}
