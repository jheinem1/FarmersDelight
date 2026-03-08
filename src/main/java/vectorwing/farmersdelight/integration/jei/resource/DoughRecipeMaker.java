package vectorwing.farmersdelight.integration.jei.resource;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModItems;
import java.util.List;
public class DoughRecipeMaker
{
	public static List<RecipeHolder<CraftingRecipe>> createRecipe() {
		NonNullList<Ingredient> inputs = NonNullList.create();
		inputs.add(Ingredient.of(Items.WHEAT));
		inputs.add(Ingredient.of(Items.WATER_BUCKET));
		ItemStack output = new ItemStack(ModItems.WHEAT_DOUGH.get());
		String path = "dough";
		ResourceKey<Recipe<?>> id = ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(FarmersDelight.MODID, path));
		return List.of(new RecipeHolder<>(id, new ShapelessRecipe(path, CraftingBookCategory.MISC, output, inputs)));
	}
}
