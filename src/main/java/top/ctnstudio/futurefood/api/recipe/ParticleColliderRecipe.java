package top.ctnstudio.futurefood.api.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record ParticleColliderRecipe(Ingredient input1, Ingredient input2, ItemStack output,
                                     int energyCost, int processingTime) {
  public ParticleColliderRecipe(Ingredient input1, Ingredient input2, ItemStack output, int energyCost, int processingTime) {
    this.input1 = input1;
    this.input2 = input2;
    this.output = output.copy();
    this.energyCost = energyCost;
    this.processingTime = processingTime;
  }

  public boolean matches(ItemStack inputStack1, ItemStack inputStack2) {
    // 检查两个输入是否匹配（顺序）
    return this.input1.test(inputStack1) && this.input2.test(inputStack2);
  }

  public boolean matchesReverse(ItemStack inputStack1, ItemStack inputStack2) {
    // 检查两个输入是否匹配（无序）
    return matches(inputStack1, inputStack2) || matches(inputStack2, inputStack1);
  }

  @Override
  public ItemStack output() {
    return this.output.copy();
  }

  public int getEnergyPerTick() {
    return this.energyCost / this.processingTime;
  }
}
