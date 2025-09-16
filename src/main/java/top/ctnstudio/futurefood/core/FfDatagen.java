package top.ctnstudio.futurefood.core;

import club.someoneice.json.Nodes;
import club.someoneice.json.node.MapNode;
import club.someoneice.json.processor.JsonBuilder;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Files;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static top.ctnstudio.futurefood.core.FutureFood.LOGGER;

/**
 * 数据生成主类
 */
@EventBusSubscriber()
public class FfDatagen {
  @SubscribeEvent
  public static void gatherData(final GatherDataEvent event) throws IOException {
    LOGGER.info("Start generator data.");

    final File root = new File("../build/resources/assets/futurefood/");
    if (!root.exists() || !root.isDirectory()) {
      root.mkdirs();
    }

    FutureFood.LOGGER.info(root.getAbsoluteFile());

    genLanguageFile(root);

    // TODO - 用我们自己的方式处理 DataGenerator。
  }

  /**
   * Generate the language files.
   */
  public static void genLanguageFile(File root) throws IOException {
    LOGGER.info("Start language file generation.");

    final MapNode node = new MapNode();

    findData(BuiltInRegistries.CREATIVE_MODE_TAB).forEach(it -> {
      final String name = ((TranslatableContents) it.getDisplayName().getContents()).getKey();
      node.put(name, Nodes.as(""));
    });

    findData(BuiltInRegistries.ITEM).forEach(it -> {
      final String name = it.getDescriptionId();
      node.put(name, Nodes.as(""));
    });

    findData(BuiltInRegistries.BLOCK).forEach(it -> {
      final String name = it.getDescriptionId();
      node.put(name, Nodes.as(""));
    });

    final byte[] out = JsonBuilder.prettyPrint(node).getBytes(StandardCharsets.UTF_8);
    final File language = new File(root, "lang");

    if (!language.exists() || !language.isDirectory()) {
      language.mkdirs();
    }

    final File cn = new File(language, "zh_cn.json");
    final File en = new File(language, "en_us.json");

    if (!cn.exists() || !cn.isFile()) {
      cn.createNewFile();
    }

    if (!en.exists() || !en.isFile()) {
      en.createNewFile();
    }

    Files.write(out, cn);
    Files.write(out, en);
  }

  private static <T> Set<T> findData(Registry<T> reg) {
    final ImmutableSet.Builder<T> builder = ImmutableSet.builder();

    reg.stream()
      .filter(it -> reg.getKey(it).getNamespace().equals(FutureFood.ID))
      .forEach(builder::add);

    return builder.build();
  }
}
