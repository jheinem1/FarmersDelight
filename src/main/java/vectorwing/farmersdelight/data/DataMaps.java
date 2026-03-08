package vectorwing.farmersdelight.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.concurrent.CompletableFuture;

public class DataMaps extends DataMapProvider
{
	protected DataMaps(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void gather(HolderLookup.Provider provider) {
		var compostables = this.builder(NeoForgeDataMaps.COMPOSTABLES);

		addCompostable(compostables, ModItems.APPLE_PIE.get(), 1.0F);
		addCompostable(compostables, ModItems.APPLE_PIE_SLICE.get(), 0.85F);
		addCompostable(compostables, ModItems.BROWN_MUSHROOM_COLONY.get(), 1.0F);
		addCompostable(compostables, ModItems.CABBAGE.get(), 0.65F);
		addCompostable(compostables, ModItems.CABBAGE_LEAF.get(), 0.5F);
		addCompostable(compostables, ModItems.CABBAGE_SEEDS.get(), 0.3F);
		addCompostable(compostables, ModItems.CAKE_SLICE.get(), 0.85F);
		addCompostable(compostables, ModItems.CHOCOLATE_PIE.get(), 1.0F);
		addCompostable(compostables, ModItems.CHOCOLATE_PIE_SLICE.get(), 0.85F);
		addCompostable(compostables, ModItems.DUMPLINGS.get(), 1.0F);
		addCompostable(compostables, ModItems.HONEY_COOKIE.get(), 0.85F);
		addCompostable(compostables, ModItems.KELP_ROLL.get(), 0.85F);
		addCompostable(compostables, ModItems.KELP_ROLL_SLICE.get(), 0.5F);
		addCompostable(compostables, ModItems.ONION.get(), 0.65F);
		addCompostable(compostables, ModItems.PIE_CRUST.get(), 0.65F);
		addCompostable(compostables, ModItems.PUMPKIN_SLICE.get(), 0.5F);
		addCompostable(compostables, ModItems.RAW_PASTA.get(), 0.85F);
		addCompostable(compostables, ModItems.RED_MUSHROOM_COLONY.get(), 1.0F);
		addCompostable(compostables, ModItems.RICE.get(), 0.3F);
		addCompostable(compostables, ModItems.RICE_BALE.get(), 0.85F);
		addCompostable(compostables, ModItems.RICE_PANICLE.get(), 0.3F);
		addCompostable(compostables, ModItems.ROTTEN_TOMATO.get(), 0.85F);
		addCompostable(compostables, ModItems.SANDY_SHRUB.get(), 0.3F);
		addCompostable(compostables, ModItems.STRAW.get(), 0.3F);
		addCompostable(compostables, ModItems.STUFFED_PUMPKIN_BLOCK.get(), 1.0F);
		addCompostable(compostables, ModItems.SWEET_BERRY_CHEESECAKE.get(), 1.0F);
		addCompostable(compostables, ModItems.SWEET_BERRY_CHEESECAKE_SLICE.get(), 0.85F);
		addCompostable(compostables, ModItems.SWEET_BERRY_COOKIE.get(), 0.85F);
		addCompostable(compostables, ModItems.TOMATO.get(), 0.65F);
		addCompostable(compostables, ModItems.TOMATO_SEEDS.get(), 0.3F);
		addCompostable(compostables, ModItems.TREE_BARK.get(), 0.3F);
		addCompostable(compostables, ModItems.WILD_BEETROOTS.get(), 0.65F);
		addCompostable(compostables, ModItems.WILD_CABBAGES.get(), 0.65F);
		addCompostable(compostables, ModItems.WILD_CARROTS.get(), 0.65F);
		addCompostable(compostables, ModItems.WILD_ONIONS.get(), 0.65F);
		addCompostable(compostables, ModItems.WILD_POTATOES.get(), 0.65F);
		addCompostable(compostables, ModItems.WILD_RICE.get(), 0.65F);
		addCompostable(compostables, ModItems.WILD_TOMATOES.get(), 0.65F);
	}

	private static void addCompostable(DataMapProvider.Builder<Compostable, Item> builder, Item item, float chance) {
		builder.add(BuiltInRegistries.ITEM.getKey(item), new Compostable(chance), false);
	}
}
