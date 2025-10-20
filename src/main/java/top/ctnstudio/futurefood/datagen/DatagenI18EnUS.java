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

public class DatagenI18EnUS extends LanguageProvider {
  public DatagenI18EnUS(PackOutput output) {
    super(output, FutureFood.ID, "en_us");
  }

  public static String translationKey(String string) {
    return FutureFood.ID + ".configgui." + string;
  }

  public static String commentKey(String string) {
    return FutureFood.ID + ".configgui." + string + ".tooltip";
  }

  @Override
  protected void addTranslations() {
    add("itemGroup.futurefood", "Future Food");
    add(EnergyBar.TOOLTIP, "Energy：%s/%s");
    add(ModItemTooltipRender.ITEM_TOOLTIP_POSITION, "Pos to：%s %s %s");
    add(ModItemTooltipRender.ITEM_TOOLTIP_POSITION_EMPTY, "Non block pos.");
    add(ModItemTooltipRender.ITEM_TOOLTIP_ENERGY_STORAGE, "Energy：%s/%s");
    add(CyberWrenchItem.BINDING_SUCCESS, "Successful! Block pos： x %s y %s z %s");
    add(CyberWrenchItem.BINDING_CANCEL, "Success cancel! Pos ： x %s y %s z %s");
    add(CyberWrenchItem.BINDING_FAILURE, "Fail, pos: x %s y %s z %s");
    add(CyberWrenchItem.LINK_SUCCESS, "Success linked! Pos： x %s y %s z %s");
    add(CyberWrenchItem.LINK_REMOVE, "Discontinue，pos： x %s y %s z %s");
    add(CyberWrenchItem.LINK_REMOVE_FAILURE, "Failed discontinue，pos： x %s y %s z %s");
    add(CyberWrenchItem.LINK_FAILURE, "Fail, pos： x %s y %s z %s");
    add(ParticleColliderScreen.TOOLTIP, "Timeless：%s");
    add(GluttonyScreen.TOOLTIP, "Timeless：%s");
    addItem(ModItem.CYBER_WRENCH, "Cyber Wrench");
    addBlock(ModBlock.QED, "Quantum Energy Diffuser");
    addBlock(ModBlock.QER, "Quantum Energy Receiver");
    addBlock(ModBlock.PARTICLE_COLLIDER, "Particle Collider");
    addBlock(ModBlock.GLUTTONY, "Gluttony");
    addBlock(ModBlock.BATTERY, "Battery");
    addBlock(ModBlock.INFINITE_BATTERY, "Infinite Battery");
    addItem(ModItem.FOOD_ESSENCE, "Food Essence");

    addEffect(ModEffect.RADIATION, "Radiation");

    addFood(ModItem.ANTIMATTER_SNACK, "Antimatter Snack", "Delicious snack with extremely high energy density. If you don't eat it in one bite, it may expand in your stomach.");
    addFood(ModItem.STRONGLY_INTERACTING_BREAD, "Strongly Interacting Bread", "If you eat the bread, the bread is also eating you.");
    addFood(ModItem.WEAKLY_INTERACTING_WATER_BOTTLE, "Weakly Interacting Water Bottle", "Drinking it gives you an unprecedented sense of coolness. But are you really drinking it?");
    addFood(ModItem.LEYDEN_JAR, "Leyden Jar", "Fulled with electricity, maybe not for eating.");
    addFood(ModItem.SCHRODINGERS_CAN, "Schrodingers Can", "We never know what will happen when you open it.");
    addFood(ModItem.WORMHOLE_COOKIE, "Wormhole Cookie", "It can travel through space.");
    addFood(ModItem.POWERED_MILK, "Powered Milk", "It is so solid that you can't even take a bite.");
    addFood(ModItem.ENTROPY_STEW, "Entropy Stew", "A kind of unprecedented sense of chaos.");
    addFood(ModItem.UNPREDICTABLE_CHORUS_FRUIT, "Unpredictable Chorus Fruit", "It is more dangerous than ordinary chorus fruit, it can take you anywhere at will.");
    addFood(ModItem.BLACK_HOLE_CAKE, "Black Hole Cake", "Something wants to suck you in.");
    addFood(ModItem.WHITE_HOLE_CAKE, "White Hole Cake", "Something wants to spit you out.");
    addFood(ModItem.ATOM_COLA, "Atom Cola", "BONK!");

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

    add(ParticleColliderJeiRecipe.TITLE_KEY, "Particle Collider");
    add(ParticleColliderJeiRecipe.ENERGY_KEY, "energy cost：%s");
    add(ParticleColliderJeiRecipe.PROCESSING_TIME_KEY, "processing time：%s");
    add(GluttonyJeiRecipe.TITLE_KEY, "Gluttony");
    add(GluttonyJeiRecipe.ENERGY_KEY, "energy generation：%s");
    add(GluttonyJeiRecipe.PROCESSING_TIME_KEY, "processing time：%s");
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
