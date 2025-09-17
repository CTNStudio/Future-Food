package top.ctnstudio.futurefood.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModBlock;
import top.ctnstudio.futurefood.core.init.ModCreativeModeTab;

import java.util.function.Supplier;

public class DatagenI18ZhCn extends LanguageProvider {
  public DatagenI18ZhCn(PackOutput output) {
    super(output, FutureFood.ID, "zh_cn");
  }

  @Override
  protected void addTranslations() {
    add(ModCreativeModeTab.TAB, "未来食物");

    addBlock(ModBlock.QED, "量子能源扩散器");
    addBlock(ModBlock.QER, "量子能源接收器");
    addBlock(ModBlock.PARTICLE_COLLIDER, "粒子对撞器");
  }

  /**
   * 创造模式物品栏名称翻译
   */
  public <R, T extends R> void add(DeferredHolder<R, T> itemGroup, String name) {
    add("itemGroup." + itemGroup.getId().toString().replace(":", "."), name);
  }

  public void addConfig(String configKey, String translationDescribe, String commentDescribe) {
    add(translationKey(configKey), translationDescribe);
    add(commentKey(configKey), commentDescribe);
  }

  public static String translationKey(String string) {
    return FutureFood.ID + ".configgui." + string;
  }

  public static String commentKey(String string) {
    return FutureFood.ID + ".configgui." + string + ".tooltip";
  }

  public void addConfig(String configKey, String translationDescribe) {
    add(translationKey(configKey), translationDescribe);
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
