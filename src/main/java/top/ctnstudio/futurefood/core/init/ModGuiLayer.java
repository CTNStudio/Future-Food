package top.ctnstudio.futurefood.core.init;

import com.mojang.datafixers.util.Function3;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.LayeredDraw.Layer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import top.ctnstudio.futurefood.core.FutureFood;

import java.util.function.Supplier;

@EventBusSubscriber
public final class ModGuiLayer {
  public static final ResourceLocation TEST_LAYER = FutureFood.modRL("test_layer");
//  public static Supplier<TestLayerDraw> testLayerDraw; 暂时完成了他的使命

  @SubscribeEvent
  public static void registerGuiLayersEvent(RegisterGuiLayersEvent event) {
//    testLayerDraw = register(event, SELECTED_ITEM_NAME, TEST_LAYER, TestLayerDraw::new);
  }

  private static <T extends LayeredDraw.Layer> Supplier<T> register(RegisterGuiLayersEvent event, ResourceLocation other,
    ResourceLocation id, Function3<GuiGraphics, DeltaTracker, Minecraft, T> layer) {
    Layer layer1 = (guiGraphics, deltaTracker) -> layer.apply(guiGraphics, deltaTracker, Minecraft.getInstance());
    event.registerBelow(other, id, layer1);
    return () -> (T) layer1;
  }
}
