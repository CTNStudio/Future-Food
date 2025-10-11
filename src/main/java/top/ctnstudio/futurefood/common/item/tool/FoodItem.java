package top.ctnstudio.futurefood.common.item.tool;

import com.google.common.base.Suppliers;
import com.google.common.collect.Queues;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

public class FoodItem extends Item {
  public static final Queue<Item> FOODS = Queues.newArrayDeque();

  public FoodItem(Item.Properties properties) {
    super(properties);

    FOODS.add(this);
  }

  public Supplier<String> tooltipInfo = Suppliers.memoize(() -> {
    final var path = BuiltInRegistries.ITEM.getKey(this).getPath();
    return "food.%s.%s.info".formatted(FutureFood.ID, path);
  });

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list,
                              TooltipFlag flag) {
    super.appendHoverText(stack, context, list, flag);
    list.add(Component.translatable(this.tooltipInfo.get()));
  }
}
