package top.ctnstudio.futurefood.event;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.VanillaGameEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import top.ctnstudio.futurefood.common.block.tile.QedBlockEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@EventBusSubscriber
public final class ModBlockEvent {
  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void onBlockBreakEvent(final BlockEvent.BreakEvent event) {
    final var tile = event.getLevel().getBlockEntity(event.getPos());
    if (!(tile instanceof IEnergyStorage)) {
      return;
    }

    handle(event.getLevel(), event.getPos());
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void onBlockPlaceEvent(final BlockEvent.EntityPlaceEvent event) {
    final var tile = event.getLevel().getBlockEntity(event.getPos());
    if (!(tile instanceof IEnergyStorage)) {
      return;
    }

    handle(event.getLevel(), event.getPos());
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public static void onBlockChangedEvent(final VanillaGameEvent event) {
    if (event.getVanillaEvent() != GameEvent.BLOCK_CHANGE) {
      return;
    }

    final var vec3 = event.getEventPosition();
    final var blockPos = new BlockPos(Mth.floor(vec3.x), Mth.floor(vec3.y), Mth.floor(vec3.z));
    if (!(event.getLevel().getBlockEntity(blockPos) instanceof IEnergyStorage)) {
      return;
    }

    handle(event.getLevel(), blockPos);
  }

  private static void handle(final LevelAccessor world, final BlockPos pos) {
    if (world.isClientSide()) {
      return;
    }

    final Set<Integer> posYSet = Sets.newHashSet();

    for (int y = -5; y <= 5; y++) {
      final int iY = pos.getY() + y;
      if (QedBlockEntity.CACHES.containsRow(y)) {
        posYSet.add(iY);
      }
    }

    if (posYSet.isEmpty()) {
      return;
    }

    final var set = QedBlockEntity.CACHES.rowMap().entrySet();
    for (Entry<Integer, Map<Integer, Integer>> it : set) {
      if (posYSet.contains(it.getKey())) {
        // TODO
      }
    }
  }

  private static void checkPos(final LevelAccessor world, final BlockPos pos, final BlockPos state) {

  }
}
