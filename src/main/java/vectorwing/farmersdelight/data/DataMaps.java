package vectorwing.farmersdelight.data;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import java.util.concurrent.CompletableFuture;
public class DataMaps implements DataProvider
{
	protected DataMaps(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
	}
	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public String getName() {
		return "Farmer's Delight Data Maps";
	}
}
