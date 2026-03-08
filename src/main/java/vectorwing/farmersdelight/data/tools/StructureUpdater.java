package vectorwing.farmersdelight.data.tools;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jspecify.annotations.NonNull;
import java.util.concurrent.CompletableFuture;

public class StructureUpdater implements DataProvider
{
	private final String basePath;
	@SuppressWarnings("unused")
	private final String modid;
	@SuppressWarnings("unused")
	private final PackOutput output;
	@SuppressWarnings("unused")
	private final ResourceManager resources;

	public StructureUpdater(
			String basePath, String modid, ResourceManager resources, PackOutput output
	) {
		this.basePath = basePath;
		this.modid = modid;
		this.resources = resources;
		this.output = output;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public @NonNull String getName() {
		return "Update structure files in " + basePath;
	}
}
