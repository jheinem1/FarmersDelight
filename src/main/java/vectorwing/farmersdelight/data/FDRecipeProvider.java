package vectorwing.farmersdelight.data;

import org.jspecify.annotations.NullMarked;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@NullMarked
public abstract class FDRecipeProvider extends RecipeProvider implements RecipeOutput
{
	private final RecipeOutput output;

	protected FDRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
		super(registries, output);
		this.output = output;
	}

	public Ingredient ingredient(TagKey<Item> tag) {
		HolderSet<Item> items = this.registries.lookupOrThrow(Registries.ITEM).getOrThrow(tag);
		return Ingredient.of(items);
	}

	public ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike result) {
		return ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), category, result);
	}

	public ShapedRecipeBuilder shaped(RecipeCategory category, ItemLike result, int count) {
		return ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), category, result, count);
	}

	public ShapedRecipeBuilder shaped(RecipeCategory category, ItemStack result) {
		return ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), category, result);
	}

	public ShapelessRecipeBuilder shapeless(RecipeCategory category, ItemLike result) {
		return ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), category, result);
	}

	public ShapelessRecipeBuilder shapeless(RecipeCategory category, ItemLike result, int count) {
		return ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), category, result, count);
	}

	public ShapelessRecipeBuilder shapeless(RecipeCategory category, ItemStack result) {
		return ShapelessRecipeBuilder.shapeless(this.registries.lookupOrThrow(Registries.ITEM), category, result);
	}

	@Override
	public Advancement.Builder advancement() {
		return this.output.advancement();
	}

	@Override
	public void accept(ResourceKey<Recipe<?>> id, Recipe<?> recipe, @Nullable AdvancementHolder advancement, ICondition... conditions) {
		this.output.accept(id, recipe, advancement, conditions);
	}

	@Override
	public void includeRootAdvancement() {
		this.output.includeRootAdvancement();
	}
}
