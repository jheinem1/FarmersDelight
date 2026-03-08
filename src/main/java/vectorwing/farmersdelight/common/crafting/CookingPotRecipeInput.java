package vectorwing.farmersdelight.common.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record CookingPotRecipeInput(List<ItemStack> items) implements RecipeInput
{
	@Override
	public ItemStack getItem(int index) {
		return this.items.get(index);
	}

	@Override
	public int size() {
		return this.items.size();
	}
}
