package vectorwing.farmersdelight.client.gui;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.GhostSlots;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.recipebook.CookingPotSearchRecipeBookCategory;
import vectorwing.farmersdelight.common.block.entity.container.CookingPotMenu;
import vectorwing.farmersdelight.common.crafting.display.CookingPotRecipeDisplay;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeBookCategories;

import java.util.List;

public class CookingPotRecipeBookComponent extends RecipeBookComponent<CookingPotMenu>
{
	private static final WidgetSprites FILTER_BUTTON_SPRITES = new WidgetSprites(
			Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_enabled"),
			Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_disabled"),
			Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_enabled_highlighted"),
			Identifier.fromNamespaceAndPath(FarmersDelight.MODID, "recipe_book/cooking_pot_disabled_highlighted")
	);
	private static final Component FILTER_NAME = Component.translatable("container.recipe_book.cookable");
	private static final List<TabInfo> TABS = List.of(
			new TabInfo(new ItemStack(ModItems.COOKING_POT.get()), java.util.Optional.empty(), CookingPotSearchRecipeBookCategory.COOKING_SEARCH),
			new TabInfo(ModItems.VEGETABLE_NOODLES.get(), ModRecipeBookCategories.COOKING_MEALS.get()),
			new TabInfo(ModItems.APPLE_CIDER.get(), ModRecipeBookCategories.COOKING_DRINKS.get()),
			new TabInfo(ModItems.DUMPLINGS.get(), ModItems.TOMATO_SAUCE.get(), ModRecipeBookCategories.COOKING_MISC.get())
	);

	public CookingPotRecipeBookComponent(CookingPotMenu menu) {
		super(menu, TABS);
	}

	@Override
	protected WidgetSprites getFilterButtonTextures() {
		return FILTER_BUTTON_SPRITES;
	}

	@Override
	protected boolean isCraftingSlot(Slot slot) {
		return slot.index >= 0 && slot.index <= 8;
	}

	@Override
	protected void fillGhostRecipe(GhostSlots ghostSlots, RecipeDisplay recipeDisplay, ContextMap contextMap) {
		if (!(recipeDisplay instanceof CookingPotRecipeDisplay cookingPotDisplay)) {
			return;
		}

		ghostSlots.setResult(this.menu.slots.get(CookingPotMenu.RESULT_SLOT), contextMap, cookingPotDisplay.result());
		for (int i = 0; i < Math.min(cookingPotDisplay.ingredients().size(), CookingPotMenu.INPUT_SLOT_COUNT); i++) {
			ghostSlots.setInput(this.menu.slots.get(i), contextMap, cookingPotDisplay.ingredients().get(i));
		}
	}

	@Override
	protected Component getRecipeFilterName() {
		return FILTER_NAME;
	}

	@Override
	protected void selectMatchingRecipes(RecipeCollection recipeCollection, StackedItemContents stackedItemContents) {
		recipeCollection.selectRecipes(stackedItemContents, recipeDisplay -> recipeDisplay instanceof CookingPotRecipeDisplay);
	}
}
