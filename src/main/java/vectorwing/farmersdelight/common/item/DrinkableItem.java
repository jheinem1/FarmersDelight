package vectorwing.farmersdelight.common.item;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.Level;
import net.minecraft.core.component.DataComponents;
public class DrinkableItem extends ConsumableItem
{
	public DrinkableItem(Properties properties) {
		super(properties);
	}
	public DrinkableItem(Properties properties, boolean hasFoodEffectTooltip) {
		super(properties, hasFoodEffectTooltip);
	}
	public DrinkableItem(Properties properties, boolean hasPotionEffectTooltip, boolean hasCustomTooltip) {
		super(properties, hasPotionEffectTooltip, hasCustomTooltip);
	}
	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.DRINK;
	}
	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 32;
	}
	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack heldStack = player.getItemInHand(hand);
		FoodProperties food = heldStack.get(DataComponents.FOOD);
		if (food != null) {
			if (player.canEat(food.canAlwaysEat())) {
				player.startUsingItem(hand);
				return InteractionResult.CONSUME;
			} else {
				return InteractionResult.FAIL;
			}
		}
		return ItemUtils.startUsingInstantly(level, player, hand);
	}
}
