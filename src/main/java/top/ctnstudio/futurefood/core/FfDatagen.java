package top.ctnstudio.futurefood.core;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;
import top.ctnstudio.futurefood.datagen.*;
import top.ctnstudio.futurefood.datagen.tag.FfBlockTags;
import top.ctnstudio.futurefood.datagen.tag.FfEntityTags;
import top.ctnstudio.futurefood.datagen.tag.FfItemTags;

import java.util.concurrent.CompletableFuture;

/**
 * 数据生成主类
 */
@EventBusSubscriber
@SuppressWarnings("all")
public class FfDatagen {
  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    DataGenerator generator = event.getGenerator();
    PackOutput output = generator.getPackOutput();
    ExistingFileHelper exFileHelper = event.getExistingFileHelper();
    CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

    // 客户端数据生成
    buildClient(event, generator, new DatagenI18ZhCn(output));
    buildClient(event, generator, new DatagenItemModel(output, exFileHelper));
    buildClient(event, generator, new DatagenBlockState(output, exFileHelper));
    buildClient(event, generator, new DatagenParticle(output, exFileHelper));

    // 服务端数据生成
    buildServer(event, generator, new DatagenDatapackBuiltinEntries(output, lookupProvider));
    FfBlockTags pmBlockTags = buildServer(event, generator, new FfBlockTags(output, lookupProvider, exFileHelper));
    buildServer(event, generator, new FfItemTags(output, lookupProvider, pmBlockTags.contentsGetter(), exFileHelper));
    buildServer(event, generator, new FfEntityTags(output, lookupProvider, exFileHelper));
  }

  private static <T extends DataProvider> @NotNull T buildClient(GatherDataEvent event, DataGenerator generator, T provider) {
    return generator.addProvider(event.includeClient(), provider);
  }

  private static <T extends DataProvider> @NotNull T buildServer(GatherDataEvent event, DataGenerator generator, T provider) {
    return generator.addProvider(event.includeServer(), provider);
  }
}
