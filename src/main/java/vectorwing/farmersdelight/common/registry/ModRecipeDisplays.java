package vectorwing.farmersdelight.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.display.CookingPotRecipeDisplay;

public final class ModRecipeDisplays
{
	public static final DeferredRegister<RecipeDisplay.Type<?>> RECIPE_DISPLAYS = DeferredRegister.create(Registries.RECIPE_DISPLAY, FarmersDelight.MODID);

	public static final DeferredHolder<RecipeDisplay.Type<?>, RecipeDisplay.Type<CookingPotRecipeDisplay>> COOKING_POT =
			RECIPE_DISPLAYS.register("cooking_pot", () -> CookingPotRecipeDisplay.TYPE);

	private ModRecipeDisplays() {
	}
}
