package vectorwing.farmersdelight.common.utility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
public class RecipeUtils
{
	// Copyright (c) 2014-2015 mezz
	public static ItemStack getResultItem(Recipe<?> recipe) {
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		if (level == null) {
			throw new NullPointerException("level must not be null.");
		}
		RegistryAccess registryAccess = level.registryAccess();
		if (recipe instanceof vectorwing.farmersdelight.common.crafting.CookingPotRecipe cookingPotRecipe) {
			return cookingPotRecipe.getResultItem(registryAccess);
		}
		if (recipe instanceof vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe cuttingBoardRecipe) {
			return cuttingBoardRecipe.getResultItem(registryAccess);
		}
		return recipe.display().isEmpty() ? ItemStack.EMPTY : recipe.display().getFirst().result().resolveForFirstStack(SlotDisplayContext.fromLevel(level));
	}
}
