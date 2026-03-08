package vectorwing.farmersdelight.data.builder;
import org.jspecify.annotations.NullMarked;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import org.jspecify.annotations.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.LinkedHashMap;
import java.util.Map;
@ParametersAreNonnullByDefault
@NullMarked
public class CookingPotRecipeBuilder implements RecipeBuilder
{
	private CookingPotRecipeBookTab tab;
	private final NonNullList<Ingredient> ingredients = NonNullList.create();
	private final Item result;
	private final ItemStack resultStack;
	private final int cookingTime;
	private final float experience;
	private final ItemStack container;
	private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
	public CookingPotRecipeBuilder(ItemLike result, int count, int cookingTime, float experience, @Nullable ItemLike container) {
		this(new ItemStack(result, count), cookingTime, experience, container);
	}
	public CookingPotRecipeBuilder(ItemStack resultIn, int cookingTime, float experience, @Nullable ItemLike container) {
		this.result = resultIn.getItem();
		this.resultStack = resultIn;
		this.cookingTime = cookingTime;
		this.experience = experience;
		this.container = container != null ? new ItemStack(container) : ItemStack.EMPTY;
		this.tab = null;
	}
	public static CookingPotRecipeBuilder cookingPotRecipe(ItemLike mainResult, int count, int cookingTime, float experience) {
		return new CookingPotRecipeBuilder(mainResult, count, cookingTime, experience, null);
	}
	public static CookingPotRecipeBuilder cookingPotRecipe(ItemLike mainResult, int count, int cookingTime, float experience, ItemLike container) {
		return new CookingPotRecipeBuilder(mainResult, count, cookingTime, experience, container);
	}
	public CookingPotRecipeBuilder addIngredient(TagKey<Item> tagIn) {
		throw new UnsupportedOperationException("Use addIngredient(Ingredient) with a datagen registry-backed tag ingredient");
	}
	public CookingPotRecipeBuilder addIngredient(ItemLike itemIn) {
		return addIngredient(itemIn, 1);
	}
	public CookingPotRecipeBuilder addIngredient(ItemLike itemIn, int quantity) {
		for (int i = 0; i < quantity; ++i) {
			addIngredient(Ingredient.of(itemIn));
		}
		return this;
	}
	public CookingPotRecipeBuilder addIngredient(Ingredient ingredientIn) {
		return addIngredient(ingredientIn, 1);
	}
	public CookingPotRecipeBuilder addIngredient(Ingredient ingredientIn, int quantity) {
		for (int i = 0; i < quantity; ++i) {
			ingredients.add(ingredientIn);
		}
		return this;
	}
	@Override
	public RecipeBuilder group(@org.jetbrains.annotations.Nullable String p_176495_) {
		return this;
	}
	public CookingPotRecipeBuilder setRecipeBookTab(CookingPotRecipeBookTab tab) {
		this.tab = tab;
		return this;
	}
	@Override
	public Item getResult() {
		return this.result;
	}
	@Override
	public CookingPotRecipeBuilder unlockedBy(String criterionName, Criterion<?> criterionTrigger) {
		this.criteria.put(criterionName, criterionTrigger);
		return this;
	}
	public CookingPotRecipeBuilder unlockedByItems(String criterionName, ItemLike... items) {
		return unlockedBy(criterionName, InventoryChangeTrigger.TriggerInstance.hasItems(items));
	}
	public CookingPotRecipeBuilder unlockedByAnyIngredient(ItemLike... items) {
		this.criteria.put("has_any_ingredient", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(BuiltInRegistries.ITEM, items).build()));
		return this;
	}
	public void build(RecipeOutput output) {
		Identifier location = BuiltInRegistries.ITEM.getKey(result);
		save(output, ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(FarmersDelight.MODID, location.getPath())));
	}
	public void build(RecipeOutput outputIn, String save) {
		Identifier resourcelocation = BuiltInRegistries.ITEM.getKey(result);
		if ((Identifier.parse(save)).equals(resourcelocation)) {
			throw new IllegalStateException("Cooking Recipe " + save + " should remove its 'save' argument");
		} else {
			save(outputIn, ResourceKey.create(Registries.RECIPE, Identifier.parse(save)));
		}
	}
	@Override
	public void save(RecipeOutput output, ResourceKey<net.minecraft.world.item.crafting.Recipe<?>> id) {
		ResourceKey<net.minecraft.world.item.crafting.Recipe<?>> recipeId = ResourceKey.create(Registries.RECIPE, id.identifier().withPrefix("cooking/"));
		Advancement.Builder advancementBuilder = output.advancement()
				.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeId))
				.rewards(AdvancementRewards.Builder.recipe(recipeId))
				.requirements(AdvancementRequirements.Strategy.OR);
		this.criteria.forEach(advancementBuilder::addCriterion);
		CookingPotRecipe recipe = new CookingPotRecipe(
				"",
				this.tab,
				this.ingredients,
				this.resultStack,
				this.container,
				this.experience,
				this.cookingTime
		);
		output.accept(recipeId, recipe, advancementBuilder.build(id.identifier().withPrefix("recipes/cooking/")));
	}
}
