package ctn.futurefood;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static ctn.futurefood.init.FfBlockEntityTypes.BLOCK_ENTITY_TYPE_REGISTER;
import static ctn.futurefood.init.FfBlocks.BLOCK_REGISTER;
import static ctn.futurefood.init.FfCreativeModeTab.PROJECT_MOON_TAB_REGISTER;
import static ctn.futurefood.init.FfItems.ITEM_REGISTER;

@Mod(FutureFood.ID)
public class FutureFood {
	public static final String ID     = "futurefood";
	public static final Logger LOGGER = LogManager.getLogger();
	
	public FutureFood(IEventBus modEventBus, ModContainer modContainer) {
		modEventBus.addListener(this::commonSetup);
		
		NeoForge.EVENT_BUS.register(this);
		
		BLOCK_REGISTER.register(modEventBus);
		ITEM_REGISTER.register(modEventBus);
		BLOCK_ENTITY_TYPE_REGISTER.register(modEventBus);
		PROJECT_MOON_TAB_REGISTER.register(modEventBus);
		
		modContainer.registerConfig(ModConfig.Type.COMMON, FfConfig.SPEC);
	}
	
	private void commonSetup(FMLCommonSetupEvent event) {
		// Some common setup code
		LOGGER.info("HELLO FROM COMMON SETUP");
		
		if (FfConfig.LOG_DIRT_BLOCK.getAsBoolean()) {
			LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
		}
		
		LOGGER.info("{}{}", FfConfig.MAGIC_NUMBER_INTRODUCTION.get(), FfConfig.MAGIC_NUMBER.getAsInt());
		
		FfConfig.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
	}
	
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		LOGGER.info("HELLO from server starting");
	}
}
