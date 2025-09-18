package top.ctnstudio.futurefood.linkage.jade;

import net.minecraft.world.level.block.Block;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import top.ctnstudio.futurefood.common.block.ModEnergyStorageBlock;


@WailaPlugin
public class ModPlugin implements IWailaPlugin {

	@Override
	public void register(IWailaCommonRegistration registration) {
//		registration.registerBlockDataProvider(ModEnergyStorageComponentProvider.get(), ModEnergyStorageBlock.class);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
//    registration.registerBlockComponent(ModEnergyStorageComponentProvider.get(), Block.class);
	}
}