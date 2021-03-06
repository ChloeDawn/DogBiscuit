package dev.sapphic.dogbiscuit;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class DogBiscuit implements ModInitializer {
  public static final String NAMESPACE = "dogbiscuit";

  public static final Item BISCUIT_ITEM = new Item(new Item.Settings().group(ItemGroup.FOOD)
    .food(new FoodComponent.Builder().hunger(2).saturationModifier(0.1F).meat().build()));

  public static final Item PRIDE_COLLAR_ITEM = new Item(new Item.Settings().group(ItemGroup.MISC));

  @Override
  public void onInitialize() {
    Registry.register(Registry.ITEM, new Identifier(NAMESPACE, "biscuit"), BISCUIT_ITEM);
    Registry.register(Registry.ITEM, new Identifier(NAMESPACE, "pride_collar"), PRIDE_COLLAR_ITEM);
  }
}
