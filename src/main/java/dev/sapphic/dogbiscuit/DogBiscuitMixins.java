package dev.sapphic.dogbiscuit;

import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.connect.IMixinConnector;

public final class DogBiscuitMixins implements IMixinConnector {
  @Override
  public void connect() {
    Mixins.addConfiguration("mixins/" + DogBiscuit.NAMESPACE + "/mixins.json");
  }
}
