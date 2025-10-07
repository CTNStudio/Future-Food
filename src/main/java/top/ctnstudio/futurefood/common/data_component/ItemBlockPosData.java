package top.ctnstudio.futurefood.common.data_component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public record ItemBlockPosData(List<Integer> posList) {
  public static final ItemBlockPosData EMPTY = new ItemBlockPosData(List.of());

  public static final StreamCodec<ByteBuf, ItemBlockPosData> STREAM = StreamCodec.composite(
    ByteBufCodecs.<ByteBuf, Integer>list(3).apply(ByteBufCodecs.VAR_INT), ItemBlockPosData::posList,
    ItemBlockPosData::new);

  private static final Codec<List<Integer>> POS_CODEC = Codec.list(Codec.INT, 0, 3);
  private static final Codec<ItemBlockPosData> FULL_CODEC = RecordCodecBuilder.create(instance ->
    instance.group(POS_CODEC.fieldOf("pos").forGetter(ItemBlockPosData::posList))
      .apply(instance, ItemBlockPosData::new));

  public static final Codec<ItemBlockPosData> CODEC = Codec.withAlternative(
    FULL_CODEC, POS_CODEC, ItemBlockPosData::new);

  public ItemBlockPosData(int x, int y, int z) {
    this(List.of(x, y, z));
  }

  public ItemBlockPosData(int[] poss) {
    this(poss[0], poss[1], poss[2]);
  }

  public ItemBlockPosData(Vec3i v3i) {
    this(v3i.getX(), v3i.getY(), v3i.getZ());
  }

  public ItemBlockPosData(double x, double y, double z) {
    this((int) x, (int) y, (int) z);
  }

  public int[] getPosArray() {
    return new int[]{getX(), getY(), getZ()};
  }

  public BlockPos getBlockPos() {
    return new BlockPos(getX(), getY(), getZ());
  }

  public Vec3i getPosVec3i() {
    return new Vec3i(getX(), getY(), getZ());
  }

  public boolean isEmpty() {
    return posList.isEmpty();
  }

  public int getX() {
    return posList.get(0);
  }

  public int getY() {
    return posList.get(1);
  }

  public int getZ() {
    return posList.get(2);
  }

  @Override
  public boolean equals(Object o) {
    return switch (o) {
      case int[] array -> Arrays.equals(getPosArray(), array);
      case List list -> posList.equals(list);
      case BlockPos pos -> getBlockPos().equals(pos);
      case Vec3i pos -> getPosVec3i().equals(pos);
      case ItemBlockPosData(List<Integer> list) -> posList.equals(list);
      case null, default -> false;
    };
  }

  @Override
  public int hashCode() {
    return posList().hashCode();
  }

  @Override
  public @NotNull String toString() {
    return "ItemBlockPosData{" + "x=" + getZ() + "y=" + getY() + "z=" + getZ() + '}';
  }
}
