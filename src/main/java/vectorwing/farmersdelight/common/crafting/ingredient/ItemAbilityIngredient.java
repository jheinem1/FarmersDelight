package vectorwing.farmersdelight.common.crafting.ingredient;
import org.jspecify.annotations.NullMarked;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import vectorwing.farmersdelight.common.registry.ModIngredientTypes;
import org.jspecify.annotations.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Stream;
/**
 * Ingredient that checks if the given stack can perform a ItemAbility from Forge.
 */
@ParametersAreNonnullByDefault
@NullMarked
public class ItemAbilityIngredient implements ICustomIngredient
{
	public static final MapCodec<ItemAbilityIngredient> CODEC = RecordCodecBuilder.mapCodec(inst ->
			inst.group(ItemAbility.CODEC.fieldOf("action").forGetter(ItemAbilityIngredient::getItemAbility)
			).apply(inst, ItemAbilityIngredient::new));
	protected final ItemAbility itemAbility;
	protected List<Holder<Item>> items;
	public ItemAbilityIngredient(ItemAbility itemAbility) {
		this.itemAbility = itemAbility;
	}
	protected void dissolve() {
		if (this.items == null) {
			items = BuiltInRegistries.ITEM.stream()
					.filter(item -> new ItemStack(item).canPerformAction(itemAbility))
					.map(BuiltInRegistries.ITEM::wrapAsHolder)
					.toList();
		}
	}
	@Override
	public boolean test(@Nullable ItemStack stack) {
		return stack != null && stack.canPerformAction(itemAbility);
	}
	@Override
	public Stream<Holder<Item>> items() {
		dissolve();
		return items.stream();
	}
	@Override
	public boolean isSimple() {
		return false;
	}
	public ItemAbility getItemAbility() {
		return itemAbility;
	}
	@Override
	public IngredientType<?> getType() {
		return ModIngredientTypes.ITEM_ABILITY_INGREDIENT.get();
	}
}
