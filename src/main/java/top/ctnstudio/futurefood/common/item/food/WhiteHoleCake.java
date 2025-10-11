package top.ctnstudio.futurefood.common.item.food;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
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
    super(new Item.Properties()
      .food(foodProperties)
      .stacksTo(1)
    );
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity livingEntity) {
    final var callback = super.finishUsingItem(stack, world, livingEntity);
    if (!(livingEntity instanceof Player) || world.isClientSide) {
      return callback;
    }

    final var data = stack.get(DataComponents.CUSTOM_DATA);
    if (Objects.isNull(data)) {
      return callback;
    }

    final var nbt = data.copyTag();

    final var x = nbt.getDouble("x");
    final var y = nbt.getDouble("y");
    final var z = nbt.getDouble("z");
    final var dim = nbt.getString("world");

    if (world.dimension().location().toString().equals(dim)) {
      livingEntity.teleportTo(x, y, z);
      return callback;
    }

    world.getServer().forgeGetWorldMap().keySet().stream()
      .filter(it -> it.location().toString().equals(dim))
      .findFirst()
      .ifPresent(it -> {
        final var level = world.getServer().getLevel(it);
        if (Objects.isNull(level)) {
          return;
        }

        livingEntity.teleportTo(level, x, y ,z,
          RelativeMovement.ROTATION,
          livingEntity.getXRot(), livingEntity.getYRot());
      });

    return callback;
  }
}
