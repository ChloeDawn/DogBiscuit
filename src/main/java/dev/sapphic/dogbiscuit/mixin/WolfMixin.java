package dev.sapphic.dogbiscuit.mixin;

import dev.sapphic.dogbiscuit.DogBiscuit;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WolfEntity.class)
abstract class WolfMixin extends TameableEntity implements Angerable {
  WolfMixin(final EntityType<? extends TameableEntity> type, final World level) {
    super(type, level);
  }

  /**
   * Masks the dog biscuit item as a bone, to have the game defer to vanilla taming behaviours
   *
   * @param item The interacting player's held item
   * @return The bone item if the given item is a dog biscuit
   */
  @ModifyVariable(
    method = "interactMob",
    at = @At(
      value = "FIELD",
      target = "Lnet/minecraft/item/Items;BONE:Lnet/minecraft/item/Item;",
      shift = At.Shift.BEFORE))
  private Item interactBiscuitAsBone(final Item item) {
    return (item == DogBiscuit.biscuit()) ? Items.BONE : item;
  }
}
