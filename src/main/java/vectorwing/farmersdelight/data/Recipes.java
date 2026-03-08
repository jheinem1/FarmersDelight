package vectorwing.farmersdelight.data;
import org.jspecify.annotations.NullMarked;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;
@ParametersAreNonnullByDefault
@NullMarked
public class Recipes implements DataProvider
{
	public Recipes(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
	}
	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public String getName() {
		return "Farmer's Delight Recipes";
	}
}
