package top.ctnstudio.futurefood.common.item.food;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.ctnstudio.futurefood.common.item.tool.FoodItem;

import java.util.Objects;

public class WhiteHoleCake extends FoodItem {
  private static final FoodProperties foodProperties = new FoodProperties.Builder()
    .alwaysEdible()
    .build();

  public WhiteHoleCake() {
    super(new Item.Properties().food(foodProperties));
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity livingEntity) {
    final var callback = super.finishUsingItem(stack, world, livingEntity);
    if (!(livingEntity instanceof Player)) {
      return callback;
    }

    final var data = stack.get(DataComponents.CUSTOM_DATA);
    if (Objects.isNull(data)) {
      return callback;
    }

    final var pos = BlockPos.of(data.getUnsafe().getLong("pos"));
    livingEntity.teleportTo(pos.getX() + 0.5, pos.getY() + 0.1, pos.getZ() + 0.5);
    return callback;
  }
}
