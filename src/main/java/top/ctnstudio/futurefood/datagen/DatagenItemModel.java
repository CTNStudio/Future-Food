package top.ctnstudio.futurefood.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;
import static net.minecraft.resources.ResourceLocation.parse;

public class DatagenItemModel extends ItemModelProvider {
  public DatagenItemModel(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, FutureFood.ID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
  }

  private ModelFile.UncheckedModelFile getParent(String name) {
    return new ModelFile.UncheckedModelFile(ResourceLocation.withDefaultNamespace(name));
  }

  /// 多模型物品
  public void createModelFile(Item item, Map<Float, String> texture,
                              ResourceLocation... predicates) {
    var mod = basicItem(item);
    var predicate = predicates[0];
    Iterator<Float> iteratorKey = texture.keySet().iterator();
    Float key;
    String value;
    for (int i = 0; i < texture.size(); i++) {
      key = iteratorKey.next();
      value = texture.get(key);
      if (predicates.length > 1) {
        predicate = predicates[i];
      }
      mod.override().model(createModelFile(item, value)).predicate(predicate, key).end();
      specialItem(item, value);
    }
  }

  public ModelFile.UncheckedModelFile createModelFile(Item item, String name) {
    return new ModelFile.UncheckedModelFile(getItemResourceLocation(item, name).withPrefix("item" +
      "/"));
  }

  public ItemModelBuilder specialItem(Item item, String name) {
    return basicItem(getItemResourceLocation(item, name));
  }

  private ResourceLocation getItemResourceLocation(Item item, String name) {
    return Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)).withSuffix("_" + name);
  }

  public void createModelFile(Item item, Map<Float, String> texture, ModelFile parent,
                              ResourceLocation... predicates) {
    var mod = basicItem(item).parent(parent);
    var predicate = predicates[0];
    Iterator<Float> iteratorKey = texture.keySet().iterator();
    Float key;
    String value;
    for (int i = 0; i < texture.size(); i++) {
      key = iteratorKey.next();
      value = texture.get(key);
      if (predicates.length > 1) {
        predicate = predicates[i];
      }
      mod.override().model(createModelFile(item, value)).predicate(predicate, key).end();
      specialItem(item, value).parent(parent);
    }
  }

  public ItemModelBuilder createModelItem(Item item, ModelFile parent) {
    ResourceLocation resourceLocation = Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
    return getBuilder(item.toString())
      .parent(parent)
      .texture("layer0", fromNamespaceAndPath(resourceLocation.getNamespace(),
        "item/" + resourceLocation.getPath()));
  }

  /**
   * 用于给于特殊渲染模型生成的
   */
  public void specialItem(Item item) {
    getBuilder(item.toString()).parent(new ModelFile.UncheckedModelFile(parse("builtin/entity")));
  }

  public ItemModelBuilder basicItem(Item item, String name) {
    return basicItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)), name);
  }

  public ItemModelBuilder basicItem(ResourceLocation item, String name) {
    return getBuilder(item.toString())
      .parent(customModelFile("models/item/" + name))
      .texture("layer0", fromNamespaceAndPath(item.getNamespace(), "item/" + item.getPath()));
  }

  public ModelFile customModelFile(String name) {
    return new ModelFile.UncheckedModelFile(fromNamespaceAndPath(FutureFood.ID, name));
  }
}
