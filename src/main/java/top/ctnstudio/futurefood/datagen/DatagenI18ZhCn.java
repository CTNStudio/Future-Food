package top.ctnstudio.futurefood.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.neoforge.common.data.LanguageProvider;
import top.ctnstudio.futurefood.client.core.ModRender;
import top.ctnstudio.futurefood.common.item.CyberWrenchItem;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModBlock;
import top.ctnstudio.futurefood.core.init.ModItem;

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
    add("futurefood.gui.energy.tooltip", "能量存储：%s/%s");
    add(ModRender.ITEM_TOOLTIP_POSITION, "绑定的方块坐标：%s %s %s");
    add(ModRender.ITEM_TOOLTIP_POSITION_EMPTY, "没有绑定方块坐标");
    add(CyberWrenchItem.BINDING_SUCCESS, "绑定成功，坐标： x %s y %s z %s");
    add(CyberWrenchItem.BINDING_CANCEL, "已取消绑定，坐标： x %s y %s z %s");
    add(CyberWrenchItem.BINDING_FAILURE, "绑定失败，坐标： x %s y %s z %s");
    add(CyberWrenchItem.LINK_SUCCESS, "链接成功，坐标： x %s y %s z %s");
    add(CyberWrenchItem.LINK_REMOVE, "已断开链接，坐标： x %s y %s z %s");
    add(CyberWrenchItem.LINK_REMOVE_FAILURE, "断开链接失败，坐标： x %s y %s z %s");
    add(CyberWrenchItem.LINK_FAILURE, "链接失败坐标： x %s y %s z %s");
    addItem(ModItem.CYBER_WRENCH, "赛博扳手");
    addBlock(ModBlock.QED, "量子能源扩散器");
    addBlock(ModBlock.QER, "量子能源接收器");
    addBlock(ModBlock.PARTICLE_COLLIDER, "粒子对撞器");
  }

  public void addConfig(String configKey, String translationDescribe, String commentDescribe) {
    add(translationKey(configKey), translationDescribe);
    add(commentKey(configKey), commentDescribe);
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
