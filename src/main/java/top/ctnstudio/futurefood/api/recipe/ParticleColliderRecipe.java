package top.ctnstudio.futurefood.api.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record ParticleColliderRecipe(Ingredient input1, Ingredient input2,int input1Count,int input2Count, ItemStack output,
                                     int energyCost, int processingTime) {
  public ParticleColliderRecipe(Ingredient input1, Ingredient input2,int input1Count,int input2Count, ItemStack output, int energyCost, int processingTime) {
    this.input1 = input1;
    this.input2 = input2;
    this.input1Count = input1Count;
    this.input2Count = input2Count;
    this.output = output.copy();
    this.energyCost = energyCost;
    this.processingTime = processingTime;
  }

  public boolean matches(ItemStack inputStack1, ItemStack inputStack2) {
    // 检查两个输入是否匹配（顺序）
    return this.input1.test(inputStack1) && this.input2.test(inputStack2) && inputStack1.getCount() >= this.input1Count && inputStack2.getCount() >= this.input2Count;
  }

  public int getInputCount(int index) {
    return switch (index) {
      case 1 -> this.input1Count;
      case 2 -> this.input2Count;
      default -> 0;
    };
  }

  @Override
  public ItemStack output() {
    return this.output.copy();
  }

  public int getEnergyPerTick() {
    return this.energyCost / this.processingTime;
  }
}
