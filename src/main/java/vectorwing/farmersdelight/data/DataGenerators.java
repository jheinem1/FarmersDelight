package vectorwing.farmersdelight.data;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.data.loot.FDBlockLoot;
import vectorwing.farmersdelight.data.tools.StructureUpdater;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
@SuppressWarnings("unused")
@EventBusSubscriber(modid = FarmersDelight.MODID)
public class DataGenerators
{
	@SubscribeEvent
	public static void gatherServerData(GatherDataEvent.Server event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder()
				.add(Registries.ENCHANTMENT, ModEnchantments::bootstrap);
		event.createBlockAndItemTags(BlockTags::new, ItemTags::new);
		event.addProvider(new EntityTags(output, lookupProvider));
		event.addProvider(new DamageTypeTags(output, lookupProvider, FarmersDelight.MODID));
		event.createDatapackRegistryObjects(registrySetBuilder, Set.of(FarmersDelight.MODID));
		CompletableFuture<HolderLookup.Provider> builtinLookupProvider = event.getLookupProvider();
		event.addProvider(new EnchantmentTags(output, builtinLookupProvider));
		event.addProvider(new Recipes(output, lookupProvider));
		event.addProvider(new DataMaps(output, lookupProvider));
		event.addProvider(new Advancements(output, lookupProvider));
		event.addProvider(new LootTableProvider(output, Collections.emptySet(), List.of(
				new LootTableProvider.SubProviderEntry(FDBlockLoot::new, LootContextParamSets.BLOCK)
		), lookupProvider));
		event.addProvider(new StructureUpdater("structures/village/houses", FarmersDelight.MODID, event.getResourceManager(PackType.SERVER_DATA), output));
	}
	@SubscribeEvent
	public static void gatherClientData(GatherDataEvent.Client event) {
		PackOutput output = event.getGenerator().getPackOutput();
		event.addProvider(new BlockStates(output));
		event.addProvider(new ItemModels(output));
	}
}
