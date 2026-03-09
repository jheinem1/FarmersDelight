package vectorwing.farmersdelight.common.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.FoodValues;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.List;

@EventBusSubscriber(modid = FarmersDelight.MODID)
public class CommonEvents
{
	@SubscribeEvent
	public static void onDatapackSync(OnDatapackSyncEvent event) {
		event.sendRecipes(ModRecipeTypes.COOKING.get(), ModRecipeTypes.CUTTING.get());
	}

	@SubscribeEvent
	public static void handleVanillaSoupEffects(LivingEntityUseItemEvent.Finish event) {
		Item food = event.getItem().getItem();
		LivingEntity entity = event.getEntity();
		if (Configuration.RABBIT_STEW_BUFF.get() && food.equals(Items.RABBIT_STEW)) {
			return;
		}
		if (Configuration.VANILLA_SOUP_EXTRA_EFFECTS.get()) {
			for (var effect : FoodValues.VANILLA_SOUP_EFFECTS.getOrDefault(food, List.of())) {
				entity.addEffect(effect);
			}
		}
	}
}
