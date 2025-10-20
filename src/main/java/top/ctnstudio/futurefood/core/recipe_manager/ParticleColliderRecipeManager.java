package top.ctnstudio.futurefood.core.recipe_manager;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import top.ctnstudio.futurefood.core.init.ModItem;
import top.ctnstudio.futurefood.core.recipe.ParticleColliderRecipe;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ParticleColliderRecipeManager {
  private static final Set<ParticleColliderRecipe> RECIPES = Sets.newHashSet();
  private static boolean needsInitialization = true;

  /*
   * 添加配方（硬匹配），输入无序
   */
  public static void initRecipes(){
    // 示例(测试用):
    addRecipe(new ParticleColliderRecipe(
      Ingredient.of(Items.BREAD),
      Ingredient.of(Items.BREAD),
      10,10,
      new ItemStack(ModItem.STRONGLY_INTERACTING_BREAD.get(),1),
      2000, // 能量消耗
      100   // 处理时间 (ticks)
    ));
    addRecipe(new ParticleColliderRecipe(
      Ingredient.of(Items.POTION),
      Ingredient.of(Items.ENDER_PEARL),
      1,1,
      new ItemStack(ModItem.WEAKLY_INTERACTING_WATER_BOTTLE.get(),1),
      2000,
      100
    ));
    addRecipe(new ParticleColliderRecipe(
      Ingredient.of(ModItem.STRONGLY_INTERACTING_BREAD),
      Ingredient.of(ModItem.WEAKLY_INTERACTING_WATER_BOTTLE),
      1,1,
      new ItemStack(ModItem.ANTIMATTER_SNACK.get(),1),
      5000,
      100
    ));
    addRecipe(new ParticleColliderRecipe(
      Ingredient.of(ModItem.WEAKLY_INTERACTING_WATER_BOTTLE),
      Ingredient.of(ModItem.FOOD_ESSENCE),
      1,5,
      new ItemStack(ModItem.LEYDEN_JAR.get(),1),
      10000,
      100
    ));
    addRecipe(new ParticleColliderRecipe(
      Ingredient.of(ModItem.STRONGLY_INTERACTING_BREAD),
      Ingredient.of(Items.LAVA_BUCKET),
      1,1,
      new ItemStack(ModItem.SCHRODINGERS_CAN.get(),1),
      5000,
      100
    ));
    addRecipe(new ParticleColliderRecipe(
      Ingredient.of(Items.MILK_BUCKET),
      Ingredient.of(Items.WOODEN_SHOVEL),
      1,1,
      new ItemStack(ModItem.POWERED_MILK.get(),1),
      1000,
      100
    ));
    addRecipe(new ParticleColliderRecipe(
      Ingredient.of(ModItem.POWERED_MILK),
      Ingredient.of(Items.COOKIE),
      1,1,
      new ItemStack(ModItem.WORMHOLE_COOKIE.get(),2),
      1000,
      100
    ));
    addRecipe(new ParticleColliderRecipe(
      Ingredient.of(ModItem.POWERED_MILK),
      Ingredient.of(Items.BEETROOT_SOUP),
      1,1,
      new ItemStack(ModItem.ENTROPY_STEW.get(),1),
      2000,
      100
    ));
    addRecipe(new ParticleColliderRecipe(
      Ingredient.of(Items.POPPED_CHORUS_FRUIT),
      Ingredient.of(ModItem.LEYDEN_JAR),
      1,1,
      new ItemStack(ModItem.UNPREDICTABLE_CHORUS_FRUIT.get(),1),
      2000,
      100
    ));
    addRecipe(new ParticleColliderRecipe(
      Ingredient.of(ModItem.ANTIMATTER_SNACK),
      Ingredient.of(Items.CAKE),
      1,1,
      new ItemStack(ModItem.BLACK_HOLE_CAKE.get(),1),
      5000,
      100
    ));
    addRecipe(new ParticleColliderRecipe(
      Ingredient.of(ModItem.ANTIMATTER_SNACK),
      Ingredient.of(ModItem.LEYDEN_JAR),
      1,5,
      new ItemStack(ModItem.ATOM_COLA.get(),1),
      20000,
      200
    ));
  }

  public static void checkAndInit() {
    if (!needsInitialization) {
      return;
    }
    initRecipes();
    needsInitialization = false;
  }

  public static void addRecipe(ParticleColliderRecipe recipe) {
    RECIPES.add(recipe);
  }

  public static Optional<ParticleColliderRecipe> findRecipe(ItemStack input1, ItemStack input2){
    checkAndInit();
    return RECIPES.stream()
      .filter(recipe -> recipe.matches(input1, input2))// 检查两个输入是否匹配（无序）
      .findFirst();
  }

  public static List<ParticleColliderRecipe> getALLRecipes(){
    checkAndInit();
    return ImmutableList.copyOf(RECIPES);
  }
}
