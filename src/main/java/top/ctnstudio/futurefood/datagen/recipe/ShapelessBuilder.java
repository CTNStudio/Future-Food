package top.ctnstudio.futurefood.datagen.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import top.ctnstudio.futurefood.FutureFood;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 无序配方构建器封装
 *
 * @author 尽
 */
public class ShapelessBuilder {
  protected final List<Ingredient> ingredients = Lists.newArrayList(); // 配方原料
  protected final Map<String, Criterion<?>> criteria = Maps.newLinkedHashMap(); // 配方条件
  protected final ResourceLocation recipesId;
  protected RecipeCategory category = RecipeCategory.MISC; // 配方分类
  protected ItemStack resultStack; // 输出物品
  @Nullable
  protected String group; // 配方分组

  public ShapelessBuilder(ResourceLocation recipesId, RecipeCategory category, ItemLike result) {
    this(recipesId, category);
    this.resultStack = new ItemStack(result);
  }

  public ShapelessBuilder(ResourceLocation recipesId, RecipeCategory category) {
    this.recipesId = recipesId;
    this.category = category;
  }

  public ShapelessBuilder(ResourceLocation recipesId, RecipeCategory category,
                          ItemLike result, int count) {
    this(recipesId, category, new ItemStack(result, count));
  }

  public ShapelessBuilder(ResourceLocation recipesId, RecipeCategory category,
                          ItemStack resultStack) {
    this(recipesId, category);
    this.resultStack = resultStack;
  }

  public ShapelessBuilder(ResourceLocation recipesId, ItemLike result) {
    this(recipesId, new ItemStack(result));
  }

  public ShapelessBuilder(ResourceLocation recipesId, ItemStack resultStack) {
    this(recipesId);
    this.resultStack = resultStack;
  }

  public ShapelessBuilder(ResourceLocation recipesId) {
    this.recipesId = recipesId;
  }

  public ShapelessBuilder(ResourceLocation recipesId, ItemLike result, int count) {
    this(recipesId, new ItemStack(result, count));
  }

  /**
   * 创建一个带有结果物品的无序配方构建器
   *
   * @param recipesId 配方资源位置
   * @param category  配方分类
   * @param result    物品
   * @return 新的ShapelessBuilder实例
   */
  public static ShapelessBuilder shaped(ResourceLocation recipesId, RecipeCategory category, ItemLike result) {
    return new ShapelessBuilder(recipesId, category, result);
  }

  /**
   * 创建一个指定ID和分类的无序配方构建器
   *
   * @param recipesId 配方资源位置
   * @param category  配方分类
   * @return 新的ShapelessBuilder实例
   */
  public static ShapelessBuilder shaped(ResourceLocation recipesId, RecipeCategory category) {
    return new ShapelessBuilder(recipesId, category);
  }

  /**
   * 创建一个带有结果物品及数量的无序配方构建器
   *
   * @param recipesId 配方资源位置
   * @param category  配方分类
   * @param result    物品
   * @param count     数量
   * @return 新的ShapelessBuilder实例
   */
  public static ShapelessBuilder shaped(ResourceLocation recipesId, RecipeCategory category,
                                        ItemLike result, int count) {
    return new ShapelessBuilder(recipesId, category, result, count);
  }

  /**
   * 创建一个带有结果物品堆的无序配方构建器
   *
   * @param recipesId   配方资源位置
   * @param category    配方分类
   * @param resultStack 输出物品堆
   * @return 新的ShapelessBuilder实例
   */
  public static ShapelessBuilder shaped(ResourceLocation recipesId, RecipeCategory category,
                                        ItemStack resultStack) {
    return new ShapelessBuilder(recipesId, category, resultStack);
  }

  /**
   * 创建一个仅指定配方ID的无序配方构建器
   *
   * @param recipesId 配方资源位置
   * @return 新的ShapelessBuilder实例
   */
  public static ShapelessBuilder shaped(ResourceLocation recipesId) {
    return new ShapelessBuilder(recipesId);
  }

  /**
   * 创建一个带有结果物品的无序配方构建器
   *
   * @param recipesId 配方资源位置
   * @param result    物品
   * @return 新的ShapelessBuilder实例
   */
  public static ShapelessBuilder shaped(ResourceLocation recipesId, ItemLike result) {
    return new ShapelessBuilder(recipesId, result);
  }

  /**
   * 创建一个带有结果物品及数量的无序配方构建器
   *
   * @param recipesId 配方资源位置
   * @param result    物品
   * @param count     数量
   * @return 新的ShapelessBuilder实例
   */
  public static ShapelessBuilder shaped(ResourceLocation recipesId, ItemLike result, int count) {
    return new ShapelessBuilder(recipesId, result, count);
  }

  /**
   * 创建一个带有结果物品堆的无序配方构建器
   *
   * @param recipesId   配方资源位置
   * @param resultStack 输出物品堆
   * @return 新的ShapelessBuilder实例
   */
  public static ShapelessBuilder shaped(ResourceLocation recipesId, ItemStack resultStack) {
    return new ShapelessBuilder(recipesId, resultStack);
  }

  /**
   * 根据配方原料自动设置解锁条件
   *
   * @return 当前实例
   */
  @SuppressWarnings({"DuplicatedCode", "unchecked", "rawtypes"})
  public ShapelessBuilder basicUnlockedBy() {
    try {
      Class<RecipeProvider> clazz = RecipeProvider.class; // 或者使用 Class.forName("完整类名")
      Method itemLikeMethod = clazz.getDeclaredMethod("has", ItemLike.class);
      // 以结果堆栈的描述ID和物品设置解锁条件
      Criterion<InventoryChangeTrigger.TriggerInstance> result = (Criterion) itemLikeMethod.invoke(null, resultStack.getItem());
      this.unlockedBy(resultStack.getDescriptionId(), result);
      // 遍历配方的键值对，设置每个键字符对应的解锁条件
      for (Ingredient ingredient : ingredients) {
        Ingredient.Value[] ingredientTag = ingredient.getValues();

        for (Ingredient.Value value : ingredientTag) {
          if (value instanceof Ingredient.TagValue(TagKey<Item> tag)) {
            Method tagMethod = clazz.getDeclaredMethod("has", TagKey.class);
            Criterion<InventoryChangeTrigger.TriggerInstance> has = (Criterion) tagMethod.invoke(null, tag);
            unlockedBy(tag.location().toString(), has);
            continue;
          }
          if (value instanceof Ingredient.ItemValue(ItemStack stack)) {
            String stackName = stack.getDescriptionId();
            Item item = stack.getItem();
            Criterion<InventoryChangeTrigger.TriggerInstance> has = (Criterion) itemLikeMethod.invoke(null, item);
            this.unlockedBy(stackName, has);
          }
        }
      }
    } catch (Exception e) {
      FutureFood.LOGGER.debug("", e);
      return this;
    }
    return this;
  }

  /**
   * 添加解锁条件
   *
   * @param name      条件名称
   * @param criterion 解锁条件
   * @return 当前实例
   */
  public ShapelessBuilder unlockedBy(String name, Criterion<?> criterion) {
    this.criteria.put(name, criterion);
    return this;
  }

  /**
   * 从原料中获取物品（如果存在）
   *
   * @param ingredient 原料
   * @return 物品或null
   */
  private Item getItemFromIngredient(Ingredient ingredient) {
    ItemStack[] stacks = ingredient.getItems();
    if (stacks.length > 0) {
      return stacks[0].getItem();
    }
    return null;
  }

  /**
   * 清除所有解锁条件
   *
   * @return 当前实例
   */
  public ShapelessBuilder clearCriteria() {
    this.criteria.clear();
    return this;
  }

  /**
   * 设置配方分类
   *
   * @param category 新的配方分类
   * @return 当前实例
   */
  public ShapelessBuilder category(RecipeCategory category) {
    this.category = category;
    return this;
  }

  /**
   * 设置配方分组
   *
   * @param groupName 分组名
   * @return 当前实例
   */
  public ShapelessBuilder group(String groupName) {
    group = groupName;
    return this;
  }

  /**
   * 设置输出物品
   *
   * @param item 新的输出物品
   * @return 当前实例
   */
  public ShapelessBuilder result(ItemLike item) {
    resultStack = new ItemStack(item);
    return this;
  }

  /**
   * 设置输出物品及数量
   *
   * @param item  新的输出物品
   * @param count 数量
   * @return 当前实例
   */
  public ShapelessBuilder result(ItemLike item, int count) {
    resultStack = new ItemStack(item, count);
    return this;
  }

  /**
   * 设置输出物品堆
   *
   * @param itemStack 新的输出物品堆
   * @return 当前实例
   */
  public ShapelessBuilder result(ItemStack itemStack) {
    resultStack = itemStack;
    return this;
  }

  /**
   * 修改输出物品数量
   *
   * @param count 新的数量
   * @return 当前实例
   */
  public ShapelessBuilder resultCount(int count) {
    resultStack.setCount(count);
    return this;
  }

  /**
   * 添加一个可以是任何物品的原料
   *
   * @param item 物品
   * @return 当前实例
   */
  public ShapelessBuilder requires(ItemLike item) {
    return this.requires(Ingredient.of(item));
  }

  /**
   * 添加一个原料
   *
   * @param ingredient 原料
   * @return 当前实例
   */
  public ShapelessBuilder requires(Ingredient ingredient) {
    return this.requires(ingredient, 1);
  }

  /**
   * 添加多个相同的原料
   *
   * @param ingredient 原料
   * @param quantity   数量
   * @return 当前实例
   */
  public ShapelessBuilder requires(Ingredient ingredient, int quantity) {
    for (int i = 0; i < quantity; i++) {
      this.ingredients.add(ingredient);
    }
    return this;
  }

  /**
   * 添加多个相同的原料
   *
   * @param item     物品
   * @param quantity 数量
   * @return 当前实例
   */
  public ShapelessBuilder requires(ItemLike item, int quantity) {
    for (int i = 0; i < quantity; i++) {
      this.requires(Ingredient.of(item));
    }
    return this;
  }

  /**
   * 清除所有原料定义
   *
   * @return 当前实例
   */
  public ShapelessBuilder clearIngredients() {
    this.ingredients.clear();
    return this;
  }

  /**
   * 保存配方到指定输出
   *
   * @param recipeOutput 配方输出目标
   */
  public void save(RecipeOutput recipeOutput) {
    builder().save(recipeOutput, recipesId);
  }

  /**
   * 导出后允许二次修改
   *
   * @return 构建好的ShapelessRecipeBuilder实例
   */
  public ShapelessRecipeBuilder builder() {
    ShapelessRecipeBuilder builder = ShapelessRecipeBuilder.shapeless(category, resultStack);
    for (Ingredient ingredient : ingredients) {
      builder.requires(ingredient);
    }
    criteria.forEach(builder::unlockedBy);
    builder.group(group);
    return builder;
  }
}
