package top.ctnstudio.futurefood.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.VanillaGameEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import top.ctnstudio.futurefood.common.block.tile.QedBlockEntity;
import top.ctnstudio.futurefood.core.init.ModItem;

import java.util.List;

@EventBusSubscriber
public final class ModBlockEvent {
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void onBlockBreakEvent(final BlockEvent.BreakEvent event) {
    LevelAccessor level = event.getLevel();
    BlockPos pos = event.getPos();
    final var tile = level.getBlockEntity(pos);
    if (!(tile instanceof IEnergyStorage)) {
      return;
    }

    handle(level, pos);
  }

  private static void handle(final LevelAccessor world, final BlockPos pos) {
    if (world.isClientSide()) {
      return;
    }

    final AABB aabb = AABB.encapsulatingFullBlocks(
      pos.offset(-5, -5, -5),
      pos.offset(5, 5, 5));

    BlockPos.betweenClosedStream(aabb).forEach((pos1) -> {
      final var tile = world.getBlockEntity(pos1);
      if (tile instanceof QedBlockEntity qed) {
        qed.getUnlimitedStorage().addLinkCache(pos);
      }
    });
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void onBlockPlaceEvent(final BlockEvent.EntityPlaceEvent event) {
    LevelAccessor level = event.getLevel();
    BlockPos pos = event.getPos();
    final var tile = level.getBlockEntity(pos);
    if (!(tile instanceof IEnergyStorage)) {
      return;
    }

    handle(level, pos);
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void onBlockChangedEvent(final VanillaGameEvent event) {
    if (event.getVanillaEvent() != GameEvent.BLOCK_CHANGE) {
      return;
    }

    final var vec3 = event.getEventPosition();
    final var blockPos = new BlockPos(Mth.floor(vec3.x), Mth.floor(vec3.y), Mth.floor(vec3.z));
    Level level = event.getLevel();
    if (!(level.getBlockEntity(blockPos) instanceof IEnergyStorage)) {
      return;
    }

    handle(level, blockPos);
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void onBlockTickEvent(final LevelTickEvent.Post event) {
    if (!(event.getLevel() instanceof ServerLevel level)) {
      return;
    }
    List<ServerPlayer> players = level.getPlayers(serverPlayer -> {
      if (!serverPlayer.isAlive()) return false;
      ItemStack item = serverPlayer.getMainHandItem();
      return !item.isEmpty() && item.is(ModItem.CYBER_WRENCH.get());
    });
  }
}
