package dev.sapphic.dogbiscuit;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(DogBiscuit.NAMESPACE)
public final class DogBiscuit {
  static final String NAMESPACE = "dogbiscuit";

  private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NAMESPACE);
  private static final Food BISCUIT_FOOD = new Food.Builder().hunger(2).saturation(0.1F).meat().build();
  private static final RegistryObject<Item> BISCUIT_ITEM = ITEMS.register("biscuit", () ->
    new Item(new Item.Properties().group(ItemGroup.FOOD).food(BISCUIT_FOOD))
  );

  public DogBiscuit() {
    ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
  }

  public static Item biscuit() {
    return BISCUIT_ITEM.get();
  }
}
