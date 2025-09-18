package top.ctnstudio.futurefood.builder;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;

public class FoodPropertiesBuilder {
  /**
   * 食物效果列表构建器。
   * 用于存储食物可能带来的效果（如中毒、治疗等）。
   */
  private final ImmutableList.Builder<FoodProperties.PossibleEffect> effects =
    ImmutableList.builder();
  /**
   * 食物的营养值。
   * 影响玩家饥饿值的恢复量。
   */
  private int nutrition;
  /**
   * 食物的饱食度。
   * 影响玩家饱和度的恢复量。
   */
  private float saturation;
  /**
   * 是否可以始终食用。
   * 如果为true，则玩家即使饥饿值满也可以食用该食物。
   */
  private boolean canAlwaysEat;
  /**
   * 食用所需时间（秒）。
   * 默认为1.6秒，某些快速食用的食物可以设置为更低的值。
   */
  private float eatSeconds = 1.6F;
  /**
   * 食用后转换成的物品栈。
   * 例如，食用后可能会留下容器（如碗、盘子等）。
   */
  private Optional<ItemStack> usingConvertsTo = Optional.empty();

  public static FoodPropertiesBuilder foodBuilder() {
    return new FoodPropertiesBuilder();
  }

  /**
   * 设置食物的营养值。
   *
   * @param nutrition 营养值，通常为正整数
   * @return 当前构建器实例，用于链式调用
   */
  public FoodPropertiesBuilder nutrition(int nutrition) {
    this.nutrition = nutrition;
    return this;
  }

  /**
   * 设置食物的饱食度。
   *
   * @param saturation 饱食度，通常为正浮点数
   * @return 当前构建器实例，用于链式调用
   */
  public FoodPropertiesBuilder saturation(float saturation) {
    this.saturation = saturation;
    return this;
  }

  /**
   * 设置食物是否可以始终食用。
   * 如果调用此方法，则食物可以被已经饱食的玩家食用。
   *
   * @return 当前构建器实例，用于链式调用
   */
  public FoodPropertiesBuilder alwaysEdible() {
    this.canAlwaysEat = true;
    return this;
  }

  /**
   * 设置食物为快速食用类型。
   * 将食用时间设置为0.8秒，比普通食物更快。
   *
   * @return 当前构建器实例，用于链式调用
   */
  public FoodPropertiesBuilder fast() {
    this.eatSeconds = 0.8F;
    return this;
  }

  /**
   * 设置食物的食用时间。
   *
   * @param eatSeconds 食用所需时间（秒）
   * @return 当前构建器实例，用于链式调用
   */
  public FoodPropertiesBuilder eatSeconds(float eatSeconds) {
    this.eatSeconds = eatSeconds;
    return this;
  }

  /**
   * 添加一个食物效果，概率默认为100%。
   * 效果将在玩家食用食物时触发。
   *
   * @param effectIn 效果的供应器
   * @return 当前构建器实例，用于链式调用
   */
  public FoodPropertiesBuilder addEffect(java.util.function.Supplier<MobEffectInstance> effectIn) {
    return addEffect(effectIn, 1);
  }

  /**
   * 添加一个食物效果，并指定触发概率。
   *
   * @param effectIn    效果的供应器
   * @param probability 效果触发的概率（0-1之间）
   * @return 当前构建器实例，用于链式调用
   */
  public FoodPropertiesBuilder addEffect(java.util.function.Supplier<MobEffectInstance> effectIn,
                                         float probability) {
    this.effects.add(new FoodProperties.PossibleEffect(effectIn, probability));
    return this;
  }

  /**
   * 添加一个食物效果，并指定触发概率。
   * 与addEffect(Supplier, float)类似，但直接接受MobEffectInstance参数。
   *
   * @param effectIn    效果实例
   * @param probability 效果触发的概率（0-1之间）
   * @return 当前构建器实例，用于链式调用
   */
  public FoodPropertiesBuilder addEffect(MobEffectInstance effectIn, float probability) {
    return addEffect(() -> effectIn, probability);
  }

  /**
   * 添加一个食物效果，概率默认为100%。
   * 与addEffect(Supplier)类似，但直接接受MobEffectInstance参数。
   *
   * @param effectIn 效果实例
   * @return 当前构建器实例，用于链式调用
   */
  public FoodPropertiesBuilder addEffect(MobEffectInstance effectIn) {
    return addEffect(() -> effectIn, 1);
  }

  /**
   * 设置食用后转换成的物品。
   * 例如，食用后可能会留下容器（如碗、盘子等）。
   *
   * @param item 转换后的物品
   * @return 当前构建器实例，用于链式调用
   */
  public FoodPropertiesBuilder usingConvertsTo(ItemLike item) {
    this.usingConvertsTo = Optional.of(new ItemStack(item));
    return this;
  }

  /**
   * 构建并返回FoodProperties实例。
   * 所有配置的属性将应用到新创建的FoodProperties对象上。
   *
   * @return 构建好的FoodProperties实例
   */
  public FoodProperties build() {
    return new FoodProperties(this.nutrition, saturation, this.canAlwaysEat, this.eatSeconds,
      this.usingConvertsTo, this.effects.build());
  }
}