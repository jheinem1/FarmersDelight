package vectorwing.farmersdelight.integration.jei;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import java.util.List;
import java.util.Optional;
public class FDRecipes
{
	private final RecipeManager recipeManager;
	public FDRecipes() {
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		if (level != null) {
			this.recipeManager = (RecipeManager) level.recipeAccess();
		} else {
			throw new NullPointerException("minecraft world must not be null.");
		}
	}
	public List<RecipeHolder<CookingPotRecipe>> getCookingPotRecipes() {
		return recipeManager.getRecipes().stream()
				.filter(holder -> holder.value() instanceof CookingPotRecipe)
				.map(holder -> (RecipeHolder<CookingPotRecipe>) holder)
				.toList();
	}
	public List<RecipeHolder<CuttingBoardRecipe>> getCuttingBoardRecipes() {
		return recipeManager.getRecipes().stream()
				.filter(holder -> holder.value() instanceof CuttingBoardRecipe)
				.map(holder -> (RecipeHolder<CuttingBoardRecipe>) holder)
				.toList();
	}
	public List<RecipeHolder<CraftingRecipe>> getSpecialWheatDoughRecipe() {
		Optional<RecipeHolder<?>> specialRecipe = recipeManager.byKey(ResourceKey.create(Registries.RECIPE,
				Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "wheat_dough_from_water")));
		List<RecipeHolder<CraftingRecipe>> recipes = Lists.newArrayList();
		specialRecipe.ifPresent((recipe) -> {
			NonNullList<Ingredient> inputs = NonNullList.create();
			inputs.add(Ingredient.of(Items.WHEAT));
			inputs.add(Ingredient.of(Items.WATER_BUCKET));
			ItemStack output = new ItemStack(ModItems.WHEAT_DOUGH.get());
			ResourceKey<Recipe<?>> id = recipe.id();
			CraftingRecipe newRecipe = new ShapelessRecipe("fd_dough", CraftingBookCategory.MISC, output, inputs);
			recipes.add(new RecipeHolder<>(id, newRecipe));
		});
		return recipes;
	}
}
