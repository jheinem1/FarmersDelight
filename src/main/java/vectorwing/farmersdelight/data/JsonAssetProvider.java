package vectorwing.farmersdelight.data;

import com.google.gson.JsonElement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

abstract class JsonAssetProvider implements DataProvider
{
	private final String name;
	protected final PackOutput.PathProvider blockStatePathProvider;
	protected final PackOutput.PathProvider blockModelPathProvider;
	protected final PackOutput.PathProvider itemModelPathProvider;

	protected JsonAssetProvider(String name, PackOutput output) {
		this.name = name;
		this.blockStatePathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
		this.blockModelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/block");
		this.itemModelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/item");
	}

	protected abstract void collect(Map<Path, JsonElement> files);

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		Map<Path, JsonElement> files = new LinkedHashMap<>();
		this.collect(files);
		CompletableFuture<?>[] writes = files.entrySet().stream()
				.map(entry -> DataProvider.saveStable(output, entry.getValue(), entry.getKey()))
				.toArray(CompletableFuture[]::new);
		return CompletableFuture.allOf(writes);
	}

	@Override
	public String getName() {
		return this.name;
	}
}
