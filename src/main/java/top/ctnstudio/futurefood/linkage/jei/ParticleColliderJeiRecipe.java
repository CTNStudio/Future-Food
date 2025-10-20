package top.ctnstudio.futurefood.linkage.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModBlock;
import top.ctnstudio.futurefood.core.recipe.ParticleColliderRecipe;

import java.util.Arrays;

public class ParticleColliderJeiRecipe implements IRecipeCategory<ParticleColliderRecipe> {
  public static final RecipeType<ParticleColliderRecipe> TYPE = RecipeType.create(
    FutureFood.ID, "particle_collider", ParticleColliderRecipe.class);

  public static final ResourceLocation BACKGROUND = FutureFood.modRL("textures/gui/jei/particle_collider.png");
  public static final String TITLE_KEY = "recipe.particle_collider.jei";
  public static final String ENERGY_KEY = "recipe.particle_collider.energy";
  public static final String PROCESSING_TIME_KEY = "recipe.particle_collider.processingTime";

  private final IGuiHelper guiHelper;

  public ParticleColliderJeiRecipe(IGuiHelper guiHelper) {
    this.guiHelper = guiHelper;
  }

  @Override
  public RecipeType<ParticleColliderRecipe> getRecipeType() {
    return TYPE;
  }

  @Override
  public Component getTitle() {
    return Component.translatable(TITLE_KEY);
  }

  @Override
  public int getWidth() {
    return 153;
  }

  @Override
  public int getHeight() {
    return 67;
  }

  @SuppressWarnings("removal")
  @Override
  public @Nullable IDrawable getBackground() {
    return guiHelper.createDrawable(BACKGROUND, 0, 0, this.getWidth(), this.getHeight());
  }

  @Override
  public @Nullable IDrawable getIcon() {
    return this.guiHelper.createDrawableItemLike(ModBlock.PARTICLE_COLLIDER);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, ParticleColliderRecipe recipe, IFocusGroup focuses) {
    builder.addInputSlot(21, 30).addItemStacks(Arrays.stream(recipe.input1().getItems())
      .map(it -> it.copyWithCount(recipe.input1Count())).toList());
    builder.addInputSlot(125, 30).addItemStacks(Arrays.stream(recipe.input2().getItems())
      .map(it -> it.copyWithCount(recipe.input2Count())).toList());

    builder.addOutputSlot(73, 30).addItemStack(recipe.output());
  }

  @Override
  public void getTooltip(ITooltipBuilder tooltip, ParticleColliderRecipe recipe,
                         IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
    IRecipeCategory.super.getTooltip(tooltip, recipe, recipeSlotsView, mouseX, mouseY);

    if (mouseX > 1 && mouseX < 12 && mouseY > 15 && mouseY < 53) {
      tooltip.add(Component.translatable(ENERGY_KEY, recipe.energyCost()));
    } else if (mouseX > 57 && mouseX < 105 && mouseY > 4 && mouseY < 15) {
      tooltip.add(Component.translatable(PROCESSING_TIME_KEY, recipe.processingTime()));
    }
  }
}
