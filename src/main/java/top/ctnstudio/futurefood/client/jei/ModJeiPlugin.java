package top.ctnstudio.futurefood.client.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.ParticleColliderRecipeManager;
import top.ctnstudio.futurefood.core.init.ModBlock;

@JeiPlugin
public class ModJeiPlugin implements IModPlugin {
  @Override
  public ResourceLocation getPluginUid() {
    return FutureFood.modRL("jei_plugin");
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registration) {
    registration.addRecipeCategories(new ParticleColliderJeiRecipe(registration.getJeiHelpers().getGuiHelper()));
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    registration.addRecipes(ParticleColliderJeiRecipe.TYPE, ParticleColliderRecipeManager.getALLRecipes());
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(ModBlock.PARTICLE_COLLIDER, ParticleColliderJeiRecipe.TYPE);
  }

}
