package vectorwing.farmersdelight.client.event;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.FoodValues;

import java.util.List;

@EventBusSubscriber(modid = FarmersDelight.MODID, value = Dist.CLIENT)
public class TooltipEvents
{
	@SubscribeEvent
	public static void addTooltipToVanillaSoups(ItemTooltipEvent event) {
		if (!Configuration.VANILLA_SOUP_EXTRA_EFFECTS.get() || !Configuration.FOOD_EFFECT_TOOLTIP.get()) {
			return;
		}
		Item food = event.getItemStack().getItem();
		List<MobEffectInstance> effects = FoodValues.VANILLA_SOUP_EFFECTS.get(food);
		if (effects != null) {
			List<Component> tooltip = event.getToolTip();
			Player player = event.getEntity();
			for (MobEffectInstance effectInstance : effects) {
				MutableComponent effectText = Component.translatable(effectInstance.getDescriptionId());
				if (effectInstance.getDuration() > 20) {
					effectText = Component.translatable("potion.withDuration", effectText, MobEffectUtil.formatDuration(effectInstance, 1.0F, player == null ? 20.0F : player.level().tickRateManager().tickrate()));
				}
				tooltip.add(effectText.withStyle(effectInstance.getEffect().value().getCategory().getTooltipFormatting()));
			}
		}
	}
}
