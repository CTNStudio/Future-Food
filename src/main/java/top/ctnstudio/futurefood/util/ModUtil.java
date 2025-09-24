package top.ctnstudio.futurefood.util;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;

import java.util.Set;
import java.util.function.Predicate;

public class ModUtil {
  public static Set<BlockPos> rangePos(BlockPos start, int range,
                                       Predicate<BlockPos> predicate) {
    ImmutableSet.Builder<BlockPos> builder = ImmutableSet.builder();
    for (int x = -range; x <= range; x++)
      for (int y = -range; y <= range; y++)
        for (int z = -range; z <= range; z++) {
      final BlockPos pos = start.offset(x, y, z);
      if (predicate.test(pos))
        builder.add(pos);
    }

    return builder.build();
  }
}
