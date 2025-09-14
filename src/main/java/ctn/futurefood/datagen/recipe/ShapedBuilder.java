package ctn.futurefood.datagen.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static ctn.futurefood.datagen.recipe.DatagenRecipeProvider.getItemName;
import static ctn.futurefood.datagen.recipe.RecipeTool.getIngredient;
import static net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion.MOD_ID;

/**
 * 有序配方构建器封装
 *
 * @author 尽
 */
public class ShapedBuilder {
	protected final List<String>               rows             = Lists.newArrayList(); // 配方排版
	protected final Map<Character, Ingredient> key              = Maps.newLinkedHashMap(); // 配方键
	protected final Map<String, Criterion<?>>  criteria         = new LinkedHashMap<>(); // 配方条件
	protected final ResourceLocation           recipesId;
	protected       RecipeCategory             category         = RecipeCategory.MISC; // 配方分类
	protected       ItemStack                  resultStack; // 输出物品
	@Nullable
	protected       String                     group; // 配方分组
	protected       boolean                    showNotification = true; // 是否显示配方获得提示通知
	
	public ShapedBuilder(ResourceLocation recipesId, RecipeCategory category, ItemLike result) {
		this(recipesId, category);
		this.resultStack = new ItemStack(result);
	}
	
	/**
	 * 构造函数，指定配方ID、分类
	 *
	 * @param recipesId 配方资源位置
	 * @param category  配方分类
	 */
	public ShapedBuilder(ResourceLocation recipesId, RecipeCategory category) {
		this.recipesId = recipesId;
		this.category  = category;
	}
	
	/**
	 * 构造函数，指定配方ID、分类、物品及数量
	 *
	 * @param recipesId 配方资源位置
	 * @param category  配方分类
	 * @param result    物品
	 * @param count     数量
	 */
	public ShapedBuilder(ResourceLocation recipesId, RecipeCategory category,
			ItemLike result, int count) {
		this(recipesId, category, new ItemStack(result, count));
	}
	
	/**
	 * 构造函数，指定配方ID、分类及输出物品堆
	 *
	 * @param recipesId   配方资源位置
	 * @param resultStack 输出物品堆
	 */
	public ShapedBuilder(ResourceLocation recipesId, RecipeCategory category,
			ItemStack resultStack) {
		this(recipesId, category);
		this.resultStack = resultStack;
	}
	
	/**
	 * 构造函数，仅指定配方ID和物品
	 *
	 * @param recipesId 配方资源位置
	 * @param result    物品
	 */
	public ShapedBuilder(ResourceLocation recipesId, ItemLike result) {
		this(recipesId, new ItemStack(result));
	}
	
	/**
	 * 构造函数，指定配方ID和物品堆
	 *
	 * @param recipesId   配方资源位置
	 * @param resultStack 输出物品堆
	 */
	public ShapedBuilder(ResourceLocation recipesId, ItemStack resultStack) {
		this(recipesId);
		this.resultStack = resultStack;
	}
	
	/**
	 * 构造函数，仅指定配方ID
	 *
	 * @param recipesId 配方资源位置
	 */
	public ShapedBuilder(ResourceLocation recipesId) {
		this.recipesId = recipesId;
	}
	
	/**
	 * 构造函数，指定配方ID和物品及数量
	 *
	 * @param recipesId 配方资源位置
	 * @param result    物品
	 * @param count     数量
	 */
	public ShapedBuilder(ResourceLocation recipesId, ItemLike result, int count) {
		this(recipesId, new ItemStack(result, count));
	}
	
	/**
	 * 创建一个指定ID和分类的有序配方构建器
	 *
	 * @param recipesId 配方资源位置
	 * @param category  配方分类
	 * @return 新的BuilderShapedRecipe实例
	 */
	public static ShapedBuilder shaped(ResourceLocation recipesId, RecipeCategory category) {
		return new ShapedBuilder(recipesId, category);
	}
	
	/**
	 * 创建一个带有结果物品及数量的有序配方构建器
	 *
	 * @param recipesId 配方资源位置
	 * @param category  配方分类
	 * @param result    物品
	 * @param count     数量
	 * @return 新的BuilderShapedRecipe实例
	 */
	public static ShapedBuilder shaped(ResourceLocation recipesId, RecipeCategory category,
			ItemLike result, int count) {
		return new ShapedBuilder(recipesId, category, result, count);
	}
	
	/**
	 * 创建一个带有结果物品堆的有序配方构建器
	 *
	 * @param recipesId   配方资源位置
	 * @param category    配方分类
	 * @param resultStack 输出物品堆
	 * @return 新的BuilderShapedRecipe实例
	 */
	public static ShapedBuilder shaped(ResourceLocation recipesId, RecipeCategory category,
			ItemStack resultStack) {
		return new ShapedBuilder(recipesId, category, resultStack);
	}
	
	/**
	 * 创建一个仅指定配方ID的有序配方构建器
	 *
	 * @param recipesId 配方资源位置
	 * @return 新的BuilderShapedRecipe实例
	 */
	public static ShapedBuilder shaped(ResourceLocation recipesId) {
		return new ShapedBuilder(recipesId);
	}
	
	/**
	 * 创建一个带有结果物品的有序配方构建器
	 *
	 * @param recipesId 配方资源位置
	 * @param result    物品
	 * @return 新的BuilderShapedRecipe实例
	 */
	public static ShapedBuilder shaped(ResourceLocation recipesId, ItemLike result) {
		return new ShapedBuilder(recipesId, result);
	}
	
	/**
	 * 创建一个带有结果物品及数量的有序配方构建器
	 *
	 * @param recipesId 配方资源位置
	 * @param result    物品
	 * @param count     数量
	 * @return 新的BuilderShapedRecipe实例
	 */
	public static ShapedBuilder shaped(ResourceLocation recipesId, ItemLike result, int count) {
		return new ShapedBuilder(recipesId, result, count);
	}
	
	/**
	 * 创建一个带有结果物品堆的有序配方构建器
	 *
	 * @param recipesId   配方资源位置
	 * @param resultStack 输出物品堆
	 * @return 新的BuilderShapedRecipe实例
	 */
	public static ShapedBuilder shaped(ResourceLocation recipesId, ItemStack resultStack) {
		return new ShapedBuilder(recipesId, resultStack);
	}
	
	public static void basicBuilder(RecipeOutput output, ItemLike result, RecipeCategory category,
			Function<ShapedBuilder, ShapedBuilder> additional) {
		basicBuilder(output, result, category, null, additional);
	}
	
	public static void basicBuilder(RecipeOutput output, ItemLike result, RecipeCategory category, String group,
			Function<ShapedBuilder, ShapedBuilder> additional) {
		var builder = ShapedBuilder.shaped(getLocation(getItemName(result)),
				RecipeCategory.MISC, result);
		additional.apply(builder)
				.basicUnlockedBy()
				.group(group)
				.save(output);
	}
	
	/**
	 * 创建一个带有结果物品的有序配方构建器
	 *
	 * @param recipesId 配方资源位置
	 * @param category  配方分类
	 * @param result    物品
	 * @return 新的BuilderShapedRecipe实例
	 */
	public static ShapedBuilder shaped(ResourceLocation recipesId, RecipeCategory category, ItemLike result) {
		return new ShapedBuilder(recipesId, category, result);
	}
	
	private static ResourceLocation getLocation(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
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
	 * 设置配方分组
	 *
	 * @param groupName 分组名
	 * @return 当前实例
	 */
	public ShapedBuilder group(String groupName) {
		group = groupName;
		return this;
	}
	
	/**
	 * 根据配方的键字符和对应物品或标签来设置解锁条件
	 *
	 * @return 当前实例
	 */
	@SuppressWarnings("DuplicatedCode")
	public ShapedBuilder basicUnlockedBy() {
		// 以结果堆栈的描述ID和物品设置解锁条件
		this.unlockedBy(resultStack.getDescriptionId(), DatagenRecipeProvider.has(resultStack.getItem()));
		// 遍历配方的键值对，设置每个键字符对应的解锁条件
		for (Map.Entry<Character, Ingredient> entry : key.entrySet()) {
			Ingredient ingredient = entry.getValue();
			
			Ingredient.Value[] ingredientTag = ingredient.getValues();
			
			for (Ingredient.Value value : ingredientTag) {
				if (value instanceof Ingredient.TagValue(TagKey<Item> tag)) {
					Criterion<InventoryChangeTrigger.TriggerInstance> has = DatagenRecipeProvider.has(tag);
					unlockedBy(tag.location().toString(), has);
					continue;
				}
				if (value instanceof Ingredient.ItemValue(ItemStack stack)) {
					String stackName = stack.getDescriptionId();
					Item item = stack.getItem();
					Criterion<InventoryChangeTrigger.TriggerInstance> has = DatagenRecipeProvider.has(item);
					this.unlockedBy(stackName, has);
				}
			}
		}
		return this;
	}
	
	/**
	 * 导出后允许二次修改
	 *
	 * @return 构建好的ShapedRecipeBuilder实例
	 */
	public ShapedRecipeBuilder builder() {
		ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(category, resultStack);
		for (String row : rows) {
			builder.pattern(row);
		}
		key.forEach(builder::define);
		criteria.forEach(builder::unlockedBy);
		builder.showNotification(showNotification);
		builder.group(group);
		return builder;
	}
	
	/**
	 * 添加解锁条件
	 *
	 * @param name      条件名称
	 * @param criterion 解锁条件
	 * @return 当前实例
	 */
	public ShapedBuilder unlockedBy(String name, Criterion<?> criterion) {
		this.criteria.put(name, criterion);
		return this;
	}
	
	/**
	 * 清除所有解锁条件
	 *
	 * @return 当前实例
	 */
	public ShapedBuilder clearCriteria() {
		this.criteria.clear();
		return this;
	}
	
	/**
	 * 关闭配方获得提示通知
	 *
	 * @return 当前实例
	 */
	public ShapedBuilder closeShowNotification() {
		showNotification = false;
		return this;
	}
	
	/**
	 * 设置配方分类
	 *
	 * @param category 新的配方分类
	 * @return 当前实例
	 */
	public ShapedBuilder category(RecipeCategory category) {
		this.category = category;
		return this;
	}
	
	/**
	 * 设置输出物品
	 *
	 * @param item 新的输出物品
	 * @return 当前实例
	 */
	public ShapedBuilder result(ItemLike item) {
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
	public ShapedBuilder result(ItemLike item, int count) {
		resultStack = new ItemStack(item, count);
		return this;
	}
	
	/**
	 * 设置输出物品堆
	 *
	 * @param itemStack 新的输出物品堆
	 * @return 当前实例
	 */
	public ShapedBuilder result(ItemStack itemStack) {
		resultStack = itemStack;
		return this;
	}
	
	/**
	 * 修改输出物品数量
	 *
	 * @param count 新的数量
	 * @return 当前实例
	 */
	public ShapedBuilder resultCount(int count) {
		resultStack.setCount(count);
		return this;
	}
	
	/**
	 * 定义配方符号与标签的映射
	 *
	 * @param symbol 符号
	 * @param tag    标签
	 * @return 当前实例
	 */
	public ShapedBuilder define(Character symbol, TagKey<Item> tag) {
		return this.define(symbol, getIngredient(tag));
	}
	
	/**
	 * 定义配方符号与原料的映射
	 *
	 * @param symbol     符号
	 * @param ingredient 原料
	 * @return 当前实例
	 */
	public ShapedBuilder define(Character symbol, Ingredient ingredient) {
		if (this.key.containsKey(symbol)) {
			throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
		} else if (symbol == ' ') {
			throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined");
		} else {
			this.key.put(symbol, ingredient);
			return this;
		}
	}
	
	/**
	 * 定义配方符号与物品的映射
	 *
	 * @param symbol 符号
	 * @param item   物品
	 * @return 当前实例
	 */
	public ShapedBuilder define(Character symbol, ItemLike item) {
		return this.define(symbol, getIngredient(item));
	}
	
	/**
	 * 清除所有定义的符号映射
	 *
	 * @return 当前实例
	 */
	public ShapedBuilder clearDefine() {
		this.key.clear();
		return this;
	}
	
	/**
	 * 添加一行图案模式
	 *
	 * @param pattern 图案模式字符串
	 * @return 当前实例
	 */
	public ShapedBuilder pattern(String pattern) {
		if (!this.rows.isEmpty() && pattern.length() != this.rows.getFirst().length()) {
			throw new IllegalArgumentException("Pattern must be the same width on every line!");
		} else {
			this.rows.add(pattern);
			return this;
		}
	}
	
	/**
	 * 移除指定索引处的图案模式
	 *
	 * @param index 要移除的索引
	 * @return 当前实例
	 */
	public ShapedBuilder removePattern(int index) {
		if (index < 0 || index >= this.rows.size()) {
			throw new IllegalArgumentException("Pattern index out of bounds!");
		}
		this.rows.remove(index);
		return this;
	}
	
	/**
	 * 清除所有图案模式
	 *
	 * @return 当前实例
	 */
	public ShapedBuilder clearPattern() {
		this.rows.clear();
		return this;
	}
}