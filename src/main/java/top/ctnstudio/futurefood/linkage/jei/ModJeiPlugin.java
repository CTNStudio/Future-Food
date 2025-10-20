package top.ctnstudio.futurefood.linkage.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModBlock;
import top.ctnstudio.futurefood.core.recipe_manager.GluttonyRecipeManager;
import top.ctnstudio.futurefood.core.recipe_manager.ParticleColliderRecipeManager;

@JeiPlugin
public class ModJeiPlugin implements IModPlugin {
  @Override
  public ResourceLocation getPluginUid() {
    return FutureFood.modRL("jei_plugin");
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registration) {
    registration.addRecipeCategories(new ParticleColliderJeiRecipe(registration.getJeiHelpers().getGuiHelper()));
    registration.addRecipeCategories(new GluttonyJeiRecipe(registration.getJeiHelpers().getGuiHelper()));
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    registration.addRecipes(ParticleColliderJeiRecipe.TYPE, ParticleColliderRecipeManager.getALLRecipes());
    registration.addRecipes(GluttonyJeiRecipe.TYPE, GluttonyRecipeManager.getALLRecipes());
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(ModBlock.PARTICLE_COLLIDER, ParticleColliderJeiRecipe.TYPE);
    registration.addRecipeCatalyst(ModBlock.GLUTTONY, GluttonyJeiRecipe.TYPE);
  }

}
