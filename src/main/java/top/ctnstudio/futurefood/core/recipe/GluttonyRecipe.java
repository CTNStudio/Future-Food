package top.ctnstudio.futurefood.core.recipe;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import top.ctnstudio.futurefood.core.init.ModItem;

import java.util.Objects;

public record GluttonyRecipe(ItemStack inputItem, int outputEnergy, ItemStack outputItem) {
  public boolean matches(ItemStack inputItem) {
    return ItemStack.isSameItemSameComponents(this.inputItem, inputItem);
  }

  /**
   * 计算产物
   *
   * @return 产物
   */
  public static GluttonyRecipe getRecipe(ItemStack item) {
    var food = item.get(DataComponents.FOOD);
    if (food == null) {
      throw new NullPointerException(String.format("%s ItemStack do not have %s DataComponents",
        item,
        BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(DataComponents.FOOD)));
    }
    int nutritionFactor = food.nutrition();
    int saturationFactor = (int) (food.saturation() * 5);
    int effectFactor = food.effects().stream()
      .map(e -> e.probability() >= 1 ? e.effect() : null)
      .filter(Objects::nonNull)
      .mapToInt(e -> (int) (Math.max(1, (e.getDuration() / 20.0)) * e.getAmplifier() + 1)).sum();

    ItemStack outputItem = ModItem.FOOD_ESSENCE.get().getDefaultInstance();
    int count = Math.max(1, Math.min(outputItem.getMaxStackSize(), (int) (((nutritionFactor * saturationFactor) / 3.5f + effectFactor / 5.0f) / 10.0f)));
    outputItem.setCount(count);
    int outputEnergy = ((nutritionFactor * saturationFactor) * 5 + effectFactor * 10) * 2;
    return new GluttonyRecipe(item, outputEnergy, outputItem);
  }
}
