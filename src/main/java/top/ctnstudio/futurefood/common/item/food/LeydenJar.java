package top.ctnstudio.futurefood.common.item.food;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;
import top.ctnstudio.futurefood.common.item.tool.FoodItem;

public class LeydenJar extends FoodItem {
  private static final FoodProperties foodProperties = new FoodProperties.Builder()
    .alwaysEdible()
    .usingConvertsTo(Items.GLASS_BOTTLE)
    .build();

  public LeydenJar() {
    super(new Properties()
      .food(foodProperties)
      .stacksTo(1)
      .craftRemainder(Items.GLASS_BOTTLE)
    );
  }
}
