package dev.sapphic.dogbiscuit.client.mixin;

import dev.sapphic.dogbiscuit.DogBiscuit;
import dev.sapphic.dogbiscuit.PrideWolf;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.WolfCollarFeatureRenderer;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfCollarFeatureRenderer.class)
abstract class WolfCollarLayerMixin extends FeatureRenderer<WolfEntity, WolfEntityModel<WolfEntity>> {
  @Unique private static final Identifier PRIDE_COLLAR =
    new Identifier(DogBiscuit.NAMESPACE, "textures/entity/pride_collar.png");

  WolfCollarLayerMixin() {
    super(null);
  }

  /**
   * If the wolf is wearing a pride collar, we render it instead of the original collar texture. We
   * insert out own render call here rather than hooking the texture argument as we need to ensure
   * that the render color is always white (no tinting)
   */
  @Inject(
    method = "render",
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/entity/passive/WolfEntity;getCollarColor()Lnet/minecraft/util/DyeColor;",
      shift = Shift.BEFORE),
    cancellable = true)
  private void renderPrideCollar(
    final MatrixStack stack, final VertexConsumerProvider vertices, final int light, final WolfEntity wolf,
    final float limbAngle, final float limbDistance, final float tickDelta, final float animationProgress,
    final float headYaw, final float headPitch, final CallbackInfo ci
  ) {
    //noinspection CastToIncompatibleInterface
    if (((PrideWolf) wolf).dogbiscuit$hasPrideCollar()) {
      renderModel(this.getContextModel(), PRIDE_COLLAR, stack, vertices, light, wolf, 1.0F, 1.0F, 1.0F);
      ci.cancel();
    }
  }
}
