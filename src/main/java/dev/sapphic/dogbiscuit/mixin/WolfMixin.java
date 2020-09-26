package dev.sapphic.dogbiscuit.mixin;

import dev.sapphic.dogbiscuit.DogBiscuit;
import dev.sapphic.dogbiscuit.PrideWolf;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WolfEntity.class)
abstract class WolfMixin extends TameableEntity implements Angerable, PrideWolf {
  @Unique private static final TrackedData<Boolean> PRIDE_COLLAR =
    DataTracker.registerData(WolfEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

  WolfMixin() {
    //noinspection ConstantConditions
    super(null, null);
  }

  @Shadow
  public abstract DyeColor getCollarColor();

  @Override
  public final boolean dogbiscuit$hasPrideCollar() {
    return this.dataTracker.get(PRIDE_COLLAR);
  }

  @Override
  public final void dogbiscuit$setPrideCollar(final boolean value) {
    this.dataTracker.set(PRIDE_COLLAR, value);
  }

  @Inject(method = "initDataTracker", at = @At("RETURN"))
  private void registerPrideCollarAttribute(final CallbackInfo ci) {
    this.dataTracker.startTracking(PRIDE_COLLAR, false);
  }

  @Inject(method = "writeCustomDataToTag", at = @At("RETURN"))
  private void savePrideCollar(final CompoundTag tag, final CallbackInfo ci) {
    tag.putBoolean(PrideWolf.PRIDE_COLLAR, this.dogbiscuit$hasPrideCollar());
  }

  @Inject(method = "readCustomDataFromTag", at = @At("RETURN"))
  private void loadPrideCollar(final CompoundTag tag, final CallbackInfo ci) {
    if (tag.contains(PrideWolf.PRIDE_COLLAR, NbtType.BYTE)) {
      this.dogbiscuit$setPrideCollar(tag.getBoolean(PrideWolf.PRIDE_COLLAR));
    }
  }

  /**
   * If the player is holding a pride collar, we will try to equip it. It will only equip and consume
   * the item if the wolf does not already have a pride collar, following the behavior of collar dyeing.
   * The collar item will only be consumed if the player does not have creative abilities
   */
  @Inject(
    method = "interactMob",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/util/ActionResult;isAccepted()Z",
      shift = At.Shift.BEFORE),
    allow = 1,
    cancellable = true)
  private void trySetPrideCollar(
    final PlayerEntity player, final Hand hand, final CallbackInfoReturnable<ActionResult> cir
  ) {
    final ItemStack stack = player.getStackInHand(hand); // LVT stack gets popped so we can't capture
    if ((stack.getItem() == DogBiscuit.PRIDE_COLLAR_ITEM) && !this.dogbiscuit$hasPrideCollar()) {
      this.dogbiscuit$setPrideCollar(true);
      if (!player.abilities.creativeMode) {
        stack.decrement(1);
      }
      cir.setReturnValue(ActionResult.SUCCESS);
    }
  }

  /**
   * Vanilla logic will fail as it will see an equal dye color and short-circuit. We need to account
   * for equal dye colors whilst a pride collar is present, and the safest approach is to mimic it
   */
  @Inject(
    method = "interactMob",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/entity/passive/WolfEntity;getCollarColor()Lnet/minecraft/util/DyeColor;",
      shift = At.Shift.BEFORE),
    cancellable = true)
  private void allowDyeAfterPrideCollar(
    final PlayerEntity player, final Hand hand, final CallbackInfoReturnable<ActionResult> cir
  ) {
    final ItemStack stack = player.getStackInHand(hand);
    // Not sure why the dye local is out of scope, but okay.
    if ((((DyeItem) stack.getItem()).getColor() == this.getCollarColor()) && this.dogbiscuit$hasPrideCollar()) {
      this.dogbiscuit$setPrideCollar(false);
      if (!player.abilities.creativeMode) {
        stack.decrement(1);
      }
      cir.setReturnValue(ActionResult.SUCCESS);
    }
  }

  /**
   * Masks the dog biscuit item as a bone, to have the game defer to vanilla taming behaviours
   *
   * @param item The interacting player's held item
   * @return The bone item if the given item is our dog biscuit
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
