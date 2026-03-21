package vectorwing.farmersdelight.integration.jei;
import com.google.common.collect.Lists;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.client.event.ClientRecipeEvents;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.item.component.ItemStackWrapper;
import vectorwing.farmersdelight.common.registry.ModDataComponents;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.utility.RecipeUtils;
import vectorwing.farmersdelight.integration.jei.resource.FoodServingDummy;
import java.util.List;
public class FDRecipes
{
	private final RecipeMap recipeMap;
	public FDRecipes() {
		this.recipeMap = ClientRecipeEvents.getRecipeMap();
		if (recipeMap == RecipeMap.EMPTY) {
			throw new IllegalStateException("Client recipe map must not be empty when JEI registers recipes.");
		}
	}
	public List<RecipeHolder<CookingPotRecipe>> getCookingPotRecipes() {
		return List.copyOf(recipeMap.byType(ModRecipeTypes.COOKING.get()));
	}
	public List<RecipeHolder<CuttingBoardRecipe>> getCuttingBoardRecipes() {
		return List.copyOf(recipeMap.byType(ModRecipeTypes.CUTTING.get()));
	}
	public List<FoodServingDummy> getFoodServingRecipes() {
		List<FoodServingDummy> recipes = Lists.newArrayList();
		for (RecipeHolder<CookingPotRecipe> holder : recipeMap.byType(ModRecipeTypes.COOKING.get())) {
			CookingPotRecipe recipe = holder.value();
			ItemStack servingStack = RecipeUtils.getResultItem(recipe).copy();
			ItemStack containerStack = recipe.getOutputContainer().copy();
			if (servingStack.isEmpty() || containerStack.isEmpty()) {
				continue;
			}
			ItemStack loadedPot = new ItemStack(ModItems.COOKING_POT.get());
			loadedPot.set(ModDataComponents.MEAL, new ItemStackWrapper(servingStack.copy()));
			loadedPot.set(ModDataComponents.CONTAINER.get(), new ItemStackWrapper(containerStack.copy()));
			ItemStack returnedPot = loadedPot.copy();
			CookingPotBlockEntity.takeServingFromItem(returnedPot);
			Identifier baseId = holder.id().identifier();
			Identifier servingId = Identifier.fromNamespaceAndPath(baseId.getNamespace(), baseId.getPath() + "_food_serving");
			recipes.add(new FoodServingDummy(servingId, loadedPot, containerStack, servingStack.copyWithCount(1), returnedPot));
		}
		return recipes;
	}
	public List<RecipeHolder<CraftingRecipe>> getSpecialWheatDoughRecipe() {
		List<RecipeHolder<CraftingRecipe>> recipes = Lists.newArrayList();
		RecipeHolder<?> specialRecipe = recipeMap.byKey(ResourceKey.create(Registries.RECIPE,
				Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "wheat_dough_from_water")));
		if (specialRecipe != null) {
			NonNullList<Ingredient> inputs = NonNullList.create();
			inputs.add(Ingredient.of(Items.WHEAT));
			inputs.add(Ingredient.of(Items.WATER_BUCKET));
			ItemStack output = new ItemStack(ModItems.WHEAT_DOUGH.get());
			ResourceKey<Recipe<?>> id = specialRecipe.id();
			CraftingRecipe newRecipe = new ShapelessRecipe("fd_dough", CraftingBookCategory.MISC, output, inputs);
			recipes.add(new RecipeHolder<>(id, newRecipe));
		}
		return recipes;
	}
}
