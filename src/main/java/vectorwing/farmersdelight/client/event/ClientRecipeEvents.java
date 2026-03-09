package vectorwing.farmersdelight.client.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.minecraft.world.item.crafting.RecipeMap;
import vectorwing.farmersdelight.FarmersDelight;

@EventBusSubscriber(modid = FarmersDelight.MODID, value = Dist.CLIENT)
public final class ClientRecipeEvents {
	private static RecipeMap recipeMap = RecipeMap.EMPTY;

	private ClientRecipeEvents() {
	}

	public static RecipeMap getRecipeMap() {
		return recipeMap;
	}

	@SubscribeEvent
	public static void onRecipesReceived(RecipesReceivedEvent event) {
		recipeMap = event.getRecipeMap();
	}

	@SubscribeEvent
	public static void onPlayerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
		recipeMap = RecipeMap.EMPTY;
	}
}
