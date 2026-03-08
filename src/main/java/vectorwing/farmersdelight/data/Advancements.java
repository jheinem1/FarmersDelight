package vectorwing.farmersdelight.data;
import org.jspecify.annotations.NullMarked;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.PackOutput;
import vectorwing.farmersdelight.data.advancement.FDAdvancementGenerator;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.CompletableFuture;
@ParametersAreNonnullByDefault
@NullMarked
public class Advancements extends AdvancementProvider
{
	public Advancements(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, List.of(new FDAdvancementGenerator()));
	}
}
