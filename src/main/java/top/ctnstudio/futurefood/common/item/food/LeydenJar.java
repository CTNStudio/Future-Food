package top.ctnstudio.futurefood.common.item.food;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class LeydenJar extends Item {
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
