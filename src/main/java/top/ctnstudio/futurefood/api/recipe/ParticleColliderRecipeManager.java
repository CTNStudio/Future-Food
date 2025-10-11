package top.ctnstudio.futurefood.api.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ParticleColliderRecipeManager {
  private static final List<ParticleColliderRecipe> RECIPES = new ArrayList<>();

  /*
   * 添加配方（硬匹配），输入无序
   */
  public static void initRecipes(){
    // 示例(测试用):
    addRecipe(new ParticleColliderRecipe(
      Ingredient.of(Items.IRON_INGOT),
      Ingredient.of(Items.GOLD_INGOT),
      1,1,
      new ItemStack(Items.DIAMOND, 1),
      0, // 能量消耗
      200   // 处理时间 (ticks)
    ));
    addRecipe(new ParticleColliderRecipe(
      Ingredient.of(Items.GOLD_INGOT),
      Ingredient.of(Items.GOLD_INGOT),
      1,1,
      new ItemStack(Items.DIAMOND, 2),
      100, // 能量消耗
      100   // 处理时间 (ticks)
    ));
  }
  public static void addRecipe(ParticleColliderRecipe recipe) {
    RECIPES.add(recipe);
  }

  public static Optional<ParticleColliderRecipe> findRecipe(ItemStack input1, ItemStack input2){
    return RECIPES.stream()
      .filter(recipe -> recipe.matches(input1, input2))// 检查两个输入是否匹配（无序）
      .findFirst();
  }

  public static List<ParticleColliderRecipe> getALLRecipes(){
    return new ArrayList<>(RECIPES);
  }
}
