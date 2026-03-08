package vectorwing.farmersdelight.common.crafting.display;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;

import java.util.List;

public record CookingPotRecipeDisplay(List<SlotDisplay> ingredients, SlotDisplay result, SlotDisplay craftingStation) implements RecipeDisplay
{
	public static final MapCodec<CookingPotRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			SlotDisplay.CODEC.listOf().fieldOf("ingredients").forGetter(CookingPotRecipeDisplay::ingredients),
			SlotDisplay.CODEC.fieldOf("result").forGetter(CookingPotRecipeDisplay::result),
			SlotDisplay.CODEC.fieldOf("crafting_station").forGetter(CookingPotRecipeDisplay::craftingStation)
	).apply(instance, CookingPotRecipeDisplay::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, CookingPotRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
			SlotDisplay.STREAM_CODEC.apply(ByteBufCodecs.list()),
			CookingPotRecipeDisplay::ingredients,
			SlotDisplay.STREAM_CODEC,
			CookingPotRecipeDisplay::result,
			SlotDisplay.STREAM_CODEC,
			CookingPotRecipeDisplay::craftingStation,
			CookingPotRecipeDisplay::new
	);

	public static final RecipeDisplay.Type<CookingPotRecipeDisplay> TYPE = new RecipeDisplay.Type<>(MAP_CODEC, STREAM_CODEC);

	@Override
	public RecipeDisplay.Type<CookingPotRecipeDisplay> type() {
		return TYPE;
	}
}
