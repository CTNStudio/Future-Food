package top.ctnstudio.futurefood.core.recipe_manager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.ctnstudio.futurefood.core.recipe.GluttonyRecipe;

import java.util.List;
import java.util.Set;

public class GluttonyRecipeManager {
  private static final Set<GluttonyRecipe> RECIPES = Sets.newHashSet();
  private static boolean needsInitialization = true;

  public static void initRecipes() {
    BuiltInRegistries.ITEM.stream()
      .map(Item::getDefaultInstance)
      .filter(item -> item.has(DataComponents.FOOD))
      .map(GluttonyRecipe::getRecipe)
      .forEach(GluttonyRecipeManager::addRecipe);
  }

  public static void checkAndInit() {
    if (!needsInitialization) {
      return;
    }
    initRecipes();
    needsInitialization = false;
  }

  public static void addRecipe(GluttonyRecipe recipe) {
    RECIPES.add(recipe);
  }

  public static GluttonyRecipe findRecipe(ItemStack itemStack) {
    checkAndInit();
    return RECIPES.stream()
      .filter(recipe -> recipe.matches(itemStack))
      .findFirst()
      .orElseGet(() -> {
        var recipe = GluttonyRecipe.getRecipe(itemStack);
        // 添加进缓存
        addRecipe(recipe);
        return recipe;
      });
  }

  public static List<GluttonyRecipe> getALLRecipes() {
    checkAndInit();
    return ImmutableList.copyOf(RECIPES);
  }
}
