package vectorwing.farmersdelight.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

public class ItemModels implements DataProvider
{
	public ItemModels(PackOutput output) {
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public String getName() {
		return "Farmer's Delight Item Models";
	}
}
