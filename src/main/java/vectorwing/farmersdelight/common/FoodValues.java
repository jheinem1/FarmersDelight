package vectorwing.farmersdelight.common;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import vectorwing.farmersdelight.common.registry.ModEffects;
import org.jspecify.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class FoodValues
{
	public static final int BRIEF_DURATION = 600;
	public static final int SHORT_DURATION = 1200;
	public static final int MEDIUM_DURATION = 3600;
	public static final int LONG_DURATION = 6000;

	private static final Map<FoodProperties, Consumable> CONSUMABLE_OVERRIDES = new IdentityHashMap<>();

	public static MobEffectInstance comfort(int duration) {
		return new MobEffectInstance(ModEffects.COMFORT, duration, 0, false, false);
	}

	public static MobEffectInstance nourishment(int duration) {
		return new MobEffectInstance(ModEffects.NOURISHMENT, duration, 0, false, false);
	}

	private static FoodProperties food(int nutrition, float saturationModifier) {
		return new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturationModifier).build();
	}

	private static FoodProperties alwaysEdibleFood(int nutrition, float saturationModifier) {
		return new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturationModifier).alwaysEdible().build();
	}

	private static FoodProperties withFoodConsumable(FoodProperties food, UnaryOperator<Consumable.Builder> customizer) {
		CONSUMABLE_OVERRIDES.put(food, customizer.apply(Consumables.defaultFood()).build());
		return food;
	}

	private static FoodProperties withDrinkConsumable(FoodProperties food, UnaryOperator<Consumable.Builder> customizer) {
		CONSUMABLE_OVERRIDES.put(food, customizer.apply(Consumables.defaultDrink()).build());
		return food;
	}

	private static FoodProperties fastFood(int nutrition, float saturationModifier) {
		return withFoodConsumable(food(nutrition, saturationModifier), builder -> builder.consumeSeconds(0.8F));
	}

	private static FoodProperties foodWithEffect(int nutrition, float saturationModifier, MobEffectInstance effect, float probability) {
		return withFoodConsumable(food(nutrition, saturationModifier), builder -> builder.onConsume(new ApplyStatusEffectsConsumeEffect(effect, probability)));
	}

	private static FoodProperties alwaysEdibleDrinkWithEffect(int nutrition, float saturationModifier, MobEffectInstance effect, float probability) {
		return withDrinkConsumable(alwaysEdibleFood(nutrition, saturationModifier), builder -> builder.onConsume(new ApplyStatusEffectsConsumeEffect(effect, probability)));
	}

	private static FoodProperties fastFoodWithEffect(int nutrition, float saturationModifier, MobEffectInstance effect, float probability) {
		return withFoodConsumable(food(nutrition, saturationModifier), builder -> builder.consumeSeconds(0.8F).onConsume(new ApplyStatusEffectsConsumeEffect(effect, probability)));
	}

	private static FoodProperties alwaysEdibleFastFood(int nutrition, float saturationModifier) {
		return withFoodConsumable(alwaysEdibleFood(nutrition, saturationModifier), builder -> builder.consumeSeconds(0.8F));
	}

	public static Item.Properties applyConsumable(Item.Properties properties, FoodProperties food) {
		Consumable consumable = CONSUMABLE_OVERRIDES.get(food);
		return consumable != null ? properties.food(food, consumable) : properties.food(food);
	}

	public static @Nullable Consumable getConsumable(FoodProperties food) {
		return CONSUMABLE_OVERRIDES.get(food);
	}

	public static List<MobEffectInstance> getStatusEffects(@Nullable Consumable consumable) {
		if (consumable == null) {
			return List.of();
		}
		return consumable.onConsumeEffects().stream()
				.filter(ApplyStatusEffectsConsumeEffect.class::isInstance)
				.map(ApplyStatusEffectsConsumeEffect.class::cast)
				.flatMap(effect -> effect.effects().stream())
				.toList();
	}

	public static List<ApplyStatusEffectsConsumeEffect> getStatusEffectEntries(@Nullable Consumable consumable) {
		if (consumable == null) {
			return List.of();
		}
		return consumable.onConsumeEffects().stream()
				.filter(ApplyStatusEffectsConsumeEffect.class::isInstance)
				.map(ApplyStatusEffectsConsumeEffect.class::cast)
				.toList();
	}

	public static final FoodProperties CABBAGE = food(2, 0.4F);
	public static final FoodProperties TOMATO = food(1, 0.3F);
	public static final FoodProperties ONION = food(2, 0.4F);

	public static final FoodProperties APPLE_CIDER = alwaysEdibleDrinkWithEffect(0, 0.0F, new MobEffectInstance(MobEffects.ABSORPTION, 1200, 0), 1.0F);

	public static final FoodProperties FRIED_EGG = food(4, 0.4F);
	public static final FoodProperties TOMATO_SAUCE = food(4, 0.4F);
	public static final FoodProperties WHEAT_DOUGH = foodWithEffect(2, 0.3F, new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F);
	public static final FoodProperties RAW_PASTA = foodWithEffect(2, 0.3F, new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F);
	public static final FoodProperties PIE_CRUST = food(2, 0.2F);
	public static final FoodProperties PUMPKIN_SLICE = food(3, 0.3F);
	public static final FoodProperties CABBAGE_LEAF = fastFood(1, 0.4F);
	public static final FoodProperties MINCED_BEEF = fastFood(2, 0.3F);
	public static final FoodProperties BEEF_PATTY = fastFood(4, 0.8F);
	public static final FoodProperties CHICKEN_CUTS = fastFoodWithEffect(1, 0.3F, new MobEffectInstance(MobEffects.HUNGER, 600, 0), 0.3F);
	public static final FoodProperties COOKED_CHICKEN_CUTS = fastFood(3, 0.6F);
	public static final FoodProperties BACON = fastFood(2, 0.3F);
	public static final FoodProperties COOKED_BACON = fastFood(4, 0.8F);
	public static final FoodProperties COD_SLICE = fastFood(1, 0.1F);
	public static final FoodProperties COOKED_COD_SLICE = fastFood(3, 0.5F);
	public static final FoodProperties SALMON_SLICE = fastFood(1, 0.1F);
	public static final FoodProperties COOKED_SALMON_SLICE = fastFood(3, 0.8F);
	public static final FoodProperties MUTTON_CHOPS = fastFood(1, 0.3F);
	public static final FoodProperties COOKED_MUTTON_CHOPS = fastFood(3, 0.8F);
	public static final FoodProperties HAM = food(5, 0.3F);
	public static final FoodProperties SMOKED_HAM = food(10, 0.8F);

	public static final FoodProperties POPSICLE = alwaysEdibleFastFood(3, 0.2F);
	public static final FoodProperties COOKIES = fastFood(2, 0.1F);
	public static final FoodProperties CAKE_SLICE = fastFoodWithEffect(2, 0.1F, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 0, false, false), 1.0F);
	public static final FoodProperties PIE_SLICE = fastFoodWithEffect(3, 0.3F, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 0, false, false), 1.0F);
	public static final FoodProperties FRUIT_SALAD = foodWithEffect(6, 0.6F, new MobEffectInstance(MobEffects.REGENERATION, 100, 0), 1.0F);
	public static final FoodProperties GLOW_BERRY_CUSTARD = withFoodConsumable(alwaysEdibleFood(7, 0.6F), builder -> builder.onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0), 1.0F)));

	public static final FoodProperties MIXED_SALAD = foodWithEffect(6, 0.6F, new MobEffectInstance(MobEffects.REGENERATION, 100, 0), 1.0F);
	public static final FoodProperties NETHER_SALAD = foodWithEffect(5, 0.4F, new MobEffectInstance(MobEffects.NAUSEA, 240, 0), 0.3F);
	public static final FoodProperties BARBECUE_STICK = food(8, 0.9F);
	public static final FoodProperties EGG_SANDWICH = food(8, 0.8F);
	public static final FoodProperties CHICKEN_SANDWICH = food(10, 0.8F);
	public static final FoodProperties HAMBURGER = food(11, 0.8F);
	public static final FoodProperties BACON_SANDWICH = food(10, 0.8F);
	public static final FoodProperties MUTTON_WRAP = food(10, 0.8F);
	public static final FoodProperties DUMPLINGS = food(8, 0.8F);
	public static final FoodProperties STUFFED_POTATO = food(10, 0.7F);
	public static final FoodProperties CABBAGE_ROLLS = food(5, 0.5F);
	public static final FoodProperties SALMON_ROLL = food(7, 0.6F);
	public static final FoodProperties COD_ROLL = food(7, 0.6F);
	public static final FoodProperties KELP_ROLL = food(12, 2.4F);
	public static final FoodProperties KELP_ROLL_SLICE = fastFood(6, 0.5F);

	public static final FoodProperties COOKED_RICE = foodWithEffect(6, 0.4F, comfort(BRIEF_DURATION), 1.0F);
	public static final FoodProperties BONE_BROTH = foodWithEffect(8, 0.7F, comfort(SHORT_DURATION), 1.0F);
	public static final FoodProperties BEEF_STEW = foodWithEffect(12, 0.8F, comfort(MEDIUM_DURATION), 1.0F);
	public static final FoodProperties VEGETABLE_SOUP = foodWithEffect(12, 0.8F, comfort(MEDIUM_DURATION), 1.0F);
	public static final FoodProperties FISH_STEW = foodWithEffect(12, 0.8F, comfort(MEDIUM_DURATION), 1.0F);
	public static final FoodProperties CHICKEN_SOUP = foodWithEffect(14, 0.75F, comfort(LONG_DURATION), 1.0F);
	public static final FoodProperties FRIED_RICE = foodWithEffect(14, 0.75F, comfort(LONG_DURATION), 1.0F);
	public static final FoodProperties PUMPKIN_SOUP = foodWithEffect(14, 0.75F, comfort(LONG_DURATION), 1.0F);
	public static final FoodProperties BAKED_COD_STEW = foodWithEffect(14, 0.75F, comfort(LONG_DURATION), 1.0F);
	public static final FoodProperties NOODLE_SOUP = foodWithEffect(14, 0.75F, comfort(LONG_DURATION), 1.0F);

	public static final FoodProperties BACON_AND_EGGS = foodWithEffect(10, 0.6F, nourishment(SHORT_DURATION), 1.0F);
	public static final FoodProperties RATATOUILLE = foodWithEffect(10, 0.6F, nourishment(SHORT_DURATION), 1.0F);
	public static final FoodProperties STEAK_AND_POTATOES = foodWithEffect(12, 0.8F, nourishment(MEDIUM_DURATION), 1.0F);
	public static final FoodProperties PASTA_WITH_MEATBALLS = foodWithEffect(12, 0.8F, nourishment(MEDIUM_DURATION), 1.0F);
	public static final FoodProperties PASTA_WITH_MUTTON_CHOP = foodWithEffect(12, 0.8F, nourishment(MEDIUM_DURATION), 1.0F);
	public static final FoodProperties MUSHROOM_RICE = foodWithEffect(12, 0.8F, nourishment(MEDIUM_DURATION), 1.0F);
	public static final FoodProperties ROASTED_MUTTON_CHOPS = foodWithEffect(14, 0.75F, nourishment(LONG_DURATION), 1.0F);
	public static final FoodProperties VEGETABLE_NOODLES = foodWithEffect(14, 0.75F, nourishment(LONG_DURATION), 1.0F);
	public static final FoodProperties SQUID_INK_PASTA = foodWithEffect(14, 0.75F, nourishment(LONG_DURATION), 1.0F);
	public static final FoodProperties GRILLED_SALMON = foodWithEffect(14, 0.75F, nourishment(MEDIUM_DURATION), 1.0F);

	public static final FoodProperties ROAST_CHICKEN = foodWithEffect(14, 0.75F, nourishment(LONG_DURATION), 1.0F);
	public static final FoodProperties STUFFED_PUMPKIN = foodWithEffect(14, 0.75F, comfort(LONG_DURATION), 1.0F);
	public static final FoodProperties HONEY_GLAZED_HAM = foodWithEffect(14, 0.75F, nourishment(LONG_DURATION), 1.0F);
	public static final FoodProperties SHEPHERDS_PIE = foodWithEffect(14, 0.75F, nourishment(LONG_DURATION), 1.0F);
	public static final FoodProperties DOG_FOOD = food(4, 0.2F);

	public static final Map<Item, List<MobEffectInstance>> VANILLA_SOUP_EFFECTS = ImmutableMap.<Item, List<MobEffectInstance>>builder()
			.put(net.minecraft.world.item.Items.MUSHROOM_STEW, List.of(comfort(MEDIUM_DURATION)))
			.put(net.minecraft.world.item.Items.BEETROOT_SOUP, List.of(comfort(MEDIUM_DURATION)))
			.put(net.minecraft.world.item.Items.RABBIT_STEW, List.of(comfort(LONG_DURATION)))
			.build();

	public static final FoodProperties RABBIT_STEW_BUFF = food(14, 0.75F);
	public static final Consumable RABBIT_STEW_BUFF_CONSUMABLE = Consumables.defaultFood()
			.onConsume(new ApplyStatusEffectsConsumeEffect(comfort(LONG_DURATION), 1.0F))
			.build();

	static {
		CONSUMABLE_OVERRIDES.put(RABBIT_STEW_BUFF, RABBIT_STEW_BUFF_CONSUMABLE);
	}
}
