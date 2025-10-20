package top.ctnstudio.futurefood.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredItem;
import top.ctnstudio.futurefood.client.ModItemTooltipRender;
import top.ctnstudio.futurefood.client.gui.screen.GluttonyScreen;
import top.ctnstudio.futurefood.client.gui.screen.ParticleColliderScreen;
import top.ctnstudio.futurefood.client.gui.widget.energy.EnergyBar;
import top.ctnstudio.futurefood.common.item.tool.CyberWrenchItem;
import top.ctnstudio.futurefood.common.item.tool.FoodItem;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModBlock;
import top.ctnstudio.futurefood.core.init.ModEffect;
import top.ctnstudio.futurefood.core.init.ModItem;
import top.ctnstudio.futurefood.linkage.jei.GluttonyJeiRecipe;
import top.ctnstudio.futurefood.linkage.jei.ParticleColliderJeiRecipe;

import java.util.function.Supplier;

public class DatagenI18ZhCn extends LanguageProvider {
  public DatagenI18ZhCn(PackOutput output) {
    super(output, FutureFood.ID, "zh_cn");
  }

  public static String translationKey(String string) {
    return FutureFood.ID + ".configgui." + string;
  }

  public static String commentKey(String string) {
    return FutureFood.ID + ".configgui." + string + ".tooltip";
  }

  @Override
  protected void addTranslations() {
    add("itemGroup.futurefood", "未来食物");
    add(EnergyBar.TOOLTIP, "能量存储：%s/%s");
    add(ModItemTooltipRender.ITEM_TOOLTIP_POSITION, "绑定的方块坐标：%s %s %s");
    add(ModItemTooltipRender.ITEM_TOOLTIP_POSITION_EMPTY, "没有绑定方块坐标");
    add(ModItemTooltipRender.ITEM_TOOLTIP_ENERGY_STORAGE, "能量存储：%s/%s");
    add(CyberWrenchItem.BINDING_SUCCESS, "绑定成功，坐标： x %s y %s z %s");
    add(CyberWrenchItem.BINDING_CANCEL, "已取消绑定，坐标： x %s y %s z %s");
    add(CyberWrenchItem.BINDING_FAILURE, "绑定失败，坐标： x %s y %s z %s");
    add(CyberWrenchItem.LINK_SUCCESS, "链接成功，坐标： x %s y %s z %s");
    add(CyberWrenchItem.LINK_REMOVE, "已断开链接，坐标： x %s y %s z %s");
    add(CyberWrenchItem.LINK_REMOVE_FAILURE, "断开链接失败，坐标： x %s y %s z %s");
    add(CyberWrenchItem.LINK_FAILURE, "链接失败坐标： x %s y %s z %s");
    add(ParticleColliderScreen.TOOLTIP, "剩余时间：%s");
    add(GluttonyScreen.TOOLTIP, "剩余时间：%s");
    addItem(ModItem.CYBER_WRENCH, "赛博扳手");
    addBlock(ModBlock.QED, "量子能源扩散器");
    addBlock(ModBlock.QER, "量子能源接收器");
    addBlock(ModBlock.PARTICLE_COLLIDER, "粒子对撞器");
    addBlock(ModBlock.GLUTTONY, "暴食者");
    addBlock(ModBlock.BATTERY, "储蓄方块");
    addBlock(ModBlock.INFINITE_BATTERY, "无限储蓄方块");
    addItem(ModItem.FOOD_ESSENCE, "食物源质");

    addEffect(ModEffect.RADIATION, "辐射");

    addFood(ModItem.ANTIMATTER_SNACK, "反物质小吃", "非常美味的小吃，能量密度极高。如果你一口没有吃饱，那么可能会在你的胃里膨胀。");
    addFood(ModItem.STRONGLY_INTERACTING_BREAD, "强相互作用面包", "在你吃面包的时候，面包也在吃你。");
    addFood(ModItem.WEAKLY_INTERACTING_WATER_BOTTLE, "弱相互作用水瓶", "喝起来有一种前所未有的清凉感。但是你真的在喝吗？");
    addFood(ModItem.LEYDEN_JAR, "莱顿瓶", "里面装满了电，或许不应该用来吃。");
    addFood(ModItem.SCHRODINGERS_CAN, "薛定谔的罐头", "你永远不知道你打开它会发生什么。");
    addFood(ModItem.WORMHOLE_COOKIE, "虫洞曲奇", "它会穿越。");
    addFood(ModItem.POWERED_MILK, "凝固牛奶", "它足够充实，以至于根本无从下口。");
    addFood(ModItem.ENTROPY_STEW, "增熵炖菜", "一种从未有过的混乱感。");
    addFood(ModItem.UNPREDICTABLE_CHORUS_FRUIT, "不可预测的紫颂果", "它比一般的紫颂果更危险，它会随意带你到任何地方。");
    addFood(ModItem.BLACK_HOLE_CAKE, "黑洞蛋糕", "有东西想把你吸进去。");
    addFood(ModItem.WHITE_HOLE_CAKE, "白洞蛋糕", "有东西想把你吐出来。");
    addFood(ModItem.ATOM_COLA, "原子可乐", "BONK!");

    // addFood(ModItem.NEUTRINO_SOUP, "中微子汤", "你感觉不到它，但它确实存在。");
    // addFood(ModItem.QUARKS_SALAD, "夸克沙拉", "各种夸克组成的沙拉，味道复杂且难以捉摸。");
    // addFood(ModItem.HADRON_HAMBURGER, "强子汉堡", "强大的力量，但是代价呢？");
    // addFood(ModItem.LEPTON_LASAGNA, "轻子千层面", "它就快起飞了，最好把它拴在你的背包里。");
    // addFood(ModItem.PHOTON_PIE, "光子派", "它以光速滑入你的胃。");
    // addFood(ModItem.GLUON_GUM, "胶子口香糖", "如果黏在你的鞋上，那你就有福了。");
    // addFood(ModItem.HIGGS_BOSON_BROWNIE, "希格斯玻色子布朗尼", "感受到质量了吗？");
    // addFood(ModItem.GRAVITON_GELATO, "引力子冰淇淋", "这是铁砧做的吗？");
    // addFood(ModItem.PLASMA_SOUP, "等离子体汤", "这汤热得能把你烫伤。");
    // addFood(ModItem.NEUTRONIUM_STEAK, "中子星牛排", "中子星可以轻松的吃掉你的鱼子酱，但你可没法那么容易吃掉中子星的牛排。");

    add(ParticleColliderJeiRecipe.TITLE_KEY, "粒子对撞器");
    add(ParticleColliderJeiRecipe.ENERGY_KEY, "能量消耗：%s");
    add(ParticleColliderJeiRecipe.PROCESSING_TIME_KEY, "处理时间：%s刻");
    add(GluttonyJeiRecipe.TITLE_KEY, "暴食者");
    add(GluttonyJeiRecipe.ENERGY_KEY, "能量产生：%s");
    add(GluttonyJeiRecipe.PROCESSING_TIME_KEY, "处理时间：%s刻");

    add("death.attack.radiation", "%s变异了。");
  }

  public void addConfig(String configKey, String translationDescribe, String commentDescribe) {
    add(translationKey(configKey), translationDescribe);
    add(commentKey(configKey), commentDescribe);
  }

  public void addConfig(String configKey, String translationDescribe) {
    add(translationKey(configKey), translationDescribe);
  }

  public void addFood(DeferredItem<Item> holder, String name, String tooltip) {
    if (!(holder.get() instanceof FoodItem food)) {
      throw new IllegalArgumentException("Item is not a FoodItem");
    }

    add(holder.get().getDescriptionId(), name);
    add(food.tooltipInfo.get(), tooltip);
  }

  public <T> void addAttribute(Supplier<DataComponentType<T>> dataComponentType, String name) {
    add(dataComponentType.get().toString(), name);
  }

  /**
   * 生物属性翻译
   */
  public void addAttribute(Holder<Attribute> attributeHolder, String name) {
    add(attributeHolder.value().getDescriptionId(), name);
  }

  /**
   * 死亡消息翻译
   */
  public void addDeathMessage(ResourceKey<DamageType> damageType, String name) {
    add("death.attack." + damageType.location().getPath(), name);
  }

  /**
   * 玩家死亡消息翻译
   */
  public void addPlayerDeathMessage(ResourceKey<DamageType> damageType, String name) {
    add("death.attack." + damageType.location().getPath() + ".player", name);
  }
}
