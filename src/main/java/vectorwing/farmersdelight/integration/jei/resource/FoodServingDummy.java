package vectorwing.farmersdelight.integration.jei.resource;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class FoodServingDummy
{
	private final Identifier id;
	private final ItemStack loadedPot;
	private final ItemStack container;
	private final ItemStack serving;
	private final ItemStack returnedPot;

	public FoodServingDummy(Identifier id, ItemStack loadedPot, ItemStack container, ItemStack serving, ItemStack returnedPot) {
		this.id = id;
		this.loadedPot = loadedPot;
		this.container = container;
		this.serving = serving;
		this.returnedPot = returnedPot;
	}

	public Identifier getId() {
		return id;
	}

	public ItemStack getLoadedPot() {
		return loadedPot;
	}

	public ItemStack getContainer() {
		return container;
	}

	public ItemStack getServing() {
		return serving;
	}

	public ItemStack getReturnedPot() {
		return returnedPot;
	}
}
