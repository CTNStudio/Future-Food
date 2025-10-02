package top.ctnstudio.futurefood.client.gui.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import top.ctnstudio.futurefood.common.menu.ParticleColliderMenu;
import top.ctnstudio.futurefood.core.FutureFood;

public class ParticleColliderScreen extends EnergyScreen<ParticleColliderMenu> {
  public static final ResourceLocation BG = FutureFood.modRL("textures/gui/container/particle_collider.png");
  public static final ResourceLocation PROGRESSO = FutureFood.modRL("container/particle_collider/progresso.png");

  public ParticleColliderScreen(ParticleColliderMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title, BG);
  }

  // TODO 完成粒子加速器界面
}
