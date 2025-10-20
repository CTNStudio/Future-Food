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
import top.ctnstudio.futurefood.common.block.tile.GluttonyBlockEntity;
import top.ctnstudio.futurefood.core.FutureFood;
import top.ctnstudio.futurefood.core.init.ModBlock;
import top.ctnstudio.futurefood.core.recipe.GluttonyRecipe;

public class GluttonyJeiRecipe implements IRecipeCategory<GluttonyRecipe> {
  public static final RecipeType<GluttonyRecipe> TYPE = RecipeType.create(
    FutureFood.ID, "gluttony", GluttonyRecipe.class);
  public static final String TITLE_KEY = "recipe.gluttony.jei";
  public static final ResourceLocation BACKGROUND = FutureFood.modRL("textures/gui/jei/gluttony.png");
  public static final String ENERGY_KEY = "recipe.gluttony.energy";
  public static final String PROCESSING_TIME_KEY = "recipe.gluttony.processingTime";

  private final IGuiHelper guiHelper;

  public GluttonyJeiRecipe(IGuiHelper guiHelper) {
    this.guiHelper = guiHelper;
  }

  @SuppressWarnings("removal")
  @Override
  public @Nullable IDrawable getBackground() {
    return guiHelper.createDrawable(BACKGROUND, 0, 0, this.getWidth(), this.getHeight());
  }

  @Override
  public RecipeType<GluttonyRecipe> getRecipeType() {
    return TYPE;
  }

  @Override
  public Component getTitle() {
    return Component.translatable(TITLE_KEY);
  }

  @Override
  public @Nullable IDrawable getIcon() {
    return this.guiHelper.createDrawableItemLike(ModBlock.GLUTTONY);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, GluttonyRecipe recipe, IFocusGroup focuses) {
    builder.addInputSlot(39, 26).addItemStack(recipe.inputItem());
    builder.addOutputSlot(98, 26).addItemStack(recipe.outputItem());
  }

  @Override
  public int getWidth() {
    return 153;
  }

  @Override
  public int getHeight() {
    return 67;
  }

  @Override
  public void getTooltip(ITooltipBuilder tooltip, GluttonyRecipe recipe,
                         IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
    IRecipeCategory.super.getTooltip(tooltip, recipe, recipeSlotsView, mouseX, mouseY);

    if (mouseX > 1 && mouseX < 12 && mouseY > 15 && mouseY < 53) {
      tooltip.add(Component.translatable(ENERGY_KEY, recipe.outputEnergy()));
    } else if (mouseX > 60 && mouseX < 92 && mouseY > 27 && mouseY < 40) {
      tooltip.add(Component.translatable(PROCESSING_TIME_KEY, GluttonyBlockEntity.MAX_WORK_TICK));
    }
  }
}
