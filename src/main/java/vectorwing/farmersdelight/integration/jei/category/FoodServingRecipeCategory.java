package vectorwing.farmersdelight.integration.jei.category;

import org.jspecify.annotations.NullMarked;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.utility.ClientRenderUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;
import vectorwing.farmersdelight.integration.jei.FDRecipeTypes;
import vectorwing.farmersdelight.integration.jei.resource.FoodServingDummy;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@NullMarked
public class FoodServingRecipeCategory implements IRecipeCategory<FoodServingDummy>
{
	private final Component title;
	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawable slot;
	private final IDrawable outputSlot;
	private final IDrawable plusSign;
	private final IDrawableAnimated arrow;

	public FoodServingRecipeCategory(IGuiHelper helper) {
		title = TextUtils.getTranslation("jei.food_serving");
		background = helper.createBlankDrawable(116, 56);
		icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.COOKING_POT.get()));
		slot = helper.getSlotDrawable();
		outputSlot = helper.getOutputSlot();
		plusSign = helper.getRecipePlusSign();
		arrow = helper.createAnimatedRecipeArrow(200);
	}

	@Override
	public RecipeType<FoodServingDummy> getRecipeType() {
		return FDRecipeTypes.FOOD_SERVING;
	}

	@Override
	public Component getTitle() {
		return title;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public int getWidth() {
		return 116;
	}

	@Override
	public int getHeight() {
		return 56;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, FoodServingDummy recipe, IFocusGroup focusGroup) {
		builder.addSlot(RecipeIngredientRole.INPUT, 4, 20)
				.setStandardSlotBackground()
				.setSlotName("loaded_pot")
				.addItemStack(recipe.getLoadedPot());
		builder.addSlot(RecipeIngredientRole.INPUT, 30, 20)
				.setStandardSlotBackground()
				.setSlotName("container")
				.addItemStack(recipe.getContainer());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 82, 16)
				.setOutputSlotBackground()
				.setSlotName("serving")
				.addItemStack(recipe.getServing());
		builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 82, 38)
				.setStandardSlotBackground()
				.setSlotName("returned_pot")
				.addItemStack(recipe.getReturnedPot())
				.addRichTooltipCallback((slotView, tooltip) -> tooltip.add(TextUtils.getTranslation("jei.food_serving.remainder")));
	}

	@Override
	public void draw(FoodServingDummy recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
		background.draw(guiGraphics, 0, 0);
		slot.draw(guiGraphics, 3, 19);
		slot.draw(guiGraphics, 29, 19);
		outputSlot.draw(guiGraphics, 77, 11);
		slot.draw(guiGraphics, 81, 37);
		plusSign.draw(guiGraphics, 55, 24);
		arrow.draw(guiGraphics, 53, 19);
	}

	@Override
	public void getTooltip(ITooltipBuilder tooltip, FoodServingDummy recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
		if (ClientRenderUtils.isCursorInsideBounds(81, 37, 18, 18, mouseX, mouseY)) {
			tooltip.add(TextUtils.getTranslation("jei.food_serving.remainder"));
		}
	}

	@Override
	public Identifier getIdentifier(FoodServingDummy recipe) {
		return recipe.getId();
	}
}
