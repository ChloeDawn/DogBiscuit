package dev.sapphic.dogbiscuit.mixin;

import dev.sapphic.dogbiscuit.DogBiscuit;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WolfEntity.class)
abstract class WolfMixin extends TameableEntity implements Angerable {
  WolfMixin() {
    //noinspection ConstantConditions
    super(null, null);
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
    return (item == DogBiscuit.BISCUIT_ITEM) ? Items.BONE : item;
  }
}
