package vectorwing.farmersdelight.data;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ItemModels extends JsonAssetProvider
{
	private static final String GENERATED = "minecraft:item/generated";
	private static final String HANDHELD = "minecraft:item/handheld";
	private static final String MUG = FarmersDelight.MODID + ":item/mug";

	public ItemModels(PackOutput output) {
		super("Farmer's Delight Item Models", output);
	}

	@Override
	protected void collect(Map<Path, JsonElement> files) {
		Set<Item> items = BuiltInRegistries.ITEM.stream()
				.filter(item -> FarmersDelight.MODID.equals(BuiltInRegistries.ITEM.getKey(item).getNamespace()))
				.collect(Collectors.toSet());

		items.remove(ModItems.SKILLET.get());

		itemGeneratedModel(files, ModItems.WILD_RICE.get(), resourceBlock(itemName(ModItems.WILD_RICE.get()) + "_top"));
		items.remove(ModItems.WILD_RICE.get());

		itemGeneratedModel(files, ModItems.BROWN_MUSHROOM_COLONY.get(), resourceBlock(itemName(ModItems.BROWN_MUSHROOM_COLONY.get()) + "_stage3"));
		items.remove(ModItems.BROWN_MUSHROOM_COLONY.get());

		itemGeneratedModel(files, ModItems.RED_MUSHROOM_COLONY.get(), resourceBlock(itemName(ModItems.RED_MUSHROOM_COLONY.get()) + "_stage3"));
		items.remove(ModItems.RED_MUSHROOM_COLONY.get());

		blockBasedModel(files, ModItems.TATAMI.get(), "_half");
		items.remove(ModItems.TATAMI.get());

		blockBasedModel(files, ModItems.ORGANIC_COMPOST.get(), "_0");
		items.remove(ModItems.ORGANIC_COMPOST.get());

		Set<Item> mugItems = Sets.newHashSet(ModItems.HOT_COCOA.get(), ModItems.APPLE_CIDER.get(), ModItems.MELON_JUICE.get());
		takeAll(items, mugItems.toArray(Item[]::new)).forEach(item -> itemModel(files, item, MUG, resourceItem(itemName(item))));

		Set<Item> spriteBlockItems = Sets.newHashSet(
				ModItems.FULL_TATAMI_MAT.get(), ModItems.HALF_TATAMI_MAT.get(), ModItems.ROPE.get(),
				ModItems.CANVAS_SIGN.get(), ModItems.HANGING_CANVAS_SIGN.get(),
				ModItems.WHITE_CANVAS_SIGN.get(), ModItems.WHITE_HANGING_CANVAS_SIGN.get(),
				ModItems.ORANGE_CANVAS_SIGN.get(), ModItems.ORANGE_HANGING_CANVAS_SIGN.get(),
				ModItems.MAGENTA_CANVAS_SIGN.get(), ModItems.MAGENTA_HANGING_CANVAS_SIGN.get(),
				ModItems.LIGHT_BLUE_CANVAS_SIGN.get(), ModItems.LIGHT_BLUE_HANGING_CANVAS_SIGN.get(),
				ModItems.YELLOW_CANVAS_SIGN.get(), ModItems.YELLOW_HANGING_CANVAS_SIGN.get(),
				ModItems.LIME_CANVAS_SIGN.get(), ModItems.LIME_HANGING_CANVAS_SIGN.get(),
				ModItems.PINK_CANVAS_SIGN.get(), ModItems.PINK_HANGING_CANVAS_SIGN.get(),
				ModItems.GRAY_CANVAS_SIGN.get(), ModItems.GRAY_HANGING_CANVAS_SIGN.get(),
				ModItems.LIGHT_GRAY_CANVAS_SIGN.get(), ModItems.LIGHT_GRAY_HANGING_CANVAS_SIGN.get(),
				ModItems.CYAN_CANVAS_SIGN.get(), ModItems.CYAN_HANGING_CANVAS_SIGN.get(),
				ModItems.PURPLE_CANVAS_SIGN.get(), ModItems.PURPLE_HANGING_CANVAS_SIGN.get(),
				ModItems.BLUE_CANVAS_SIGN.get(), ModItems.BLUE_HANGING_CANVAS_SIGN.get(),
				ModItems.BROWN_CANVAS_SIGN.get(), ModItems.BROWN_HANGING_CANVAS_SIGN.get(),
				ModItems.GREEN_CANVAS_SIGN.get(), ModItems.GREEN_HANGING_CANVAS_SIGN.get(),
				ModItems.RED_CANVAS_SIGN.get(), ModItems.RED_HANGING_CANVAS_SIGN.get(),
				ModItems.BLACK_CANVAS_SIGN.get(), ModItems.BLACK_HANGING_CANVAS_SIGN.get(),
				ModItems.APPLE_PIE.get(), ModItems.SWEET_BERRY_CHEESECAKE.get(), ModItems.CHOCOLATE_PIE.get(),
				ModItems.CABBAGE_SEEDS.get(), ModItems.TOMATO_SEEDS.get(), ModItems.ONION.get(), ModItems.RICE.get(),
				ModItems.ROAST_CHICKEN_BLOCK.get(), ModItems.STUFFED_PUMPKIN_BLOCK.get(), ModItems.HONEY_GLAZED_HAM_BLOCK.get(),
				ModItems.SHEPHERDS_PIE_BLOCK.get(), ModItems.RICE_ROLL_MEDLEY_BLOCK.get()
		);
		takeAll(items, spriteBlockItems.toArray(Item[]::new)).forEach(item -> itemModel(files, item, GENERATED, resourceItem(itemName(item))));

		Set<Item> flatBlockItems = Sets.newHashSet(
				ModItems.SAFETY_NET.get(), ModItems.SANDY_SHRUB.get(), ModItems.WILD_BEETROOTS.get(), ModItems.WILD_CABBAGES.get(),
				ModItems.WILD_CARROTS.get(), ModItems.WILD_ONIONS.get(), ModItems.WILD_POTATOES.get(), ModItems.WILD_TOMATOES.get()
		);
		takeAll(items, flatBlockItems.toArray(Item[]::new)).forEach(item -> itemGeneratedModel(files, item, resourceBlock(itemName(item))));

		takeAll(items, item -> item instanceof BlockItem).forEach(item -> blockBasedModel(files, item, ""));

		Set<Item> handheldItems = Sets.newHashSet(
				ModItems.BARBECUE_STICK.get(), ModItems.HAM.get(), ModItems.SMOKED_HAM.get(),
				ModItems.FLINT_KNIFE.get(), ModItems.IRON_KNIFE.get(), ModItems.DIAMOND_KNIFE.get(),
				ModItems.GOLDEN_KNIFE.get(), ModItems.NETHERITE_KNIFE.get()
		);
		takeAll(items, handheldItems.toArray(Item[]::new)).forEach(item -> itemModel(files, item, HANDHELD, resourceItem(itemName(item))));

		items.forEach(item -> itemGeneratedModel(files, item, resourceItem(itemName(item))));
	}

	private void blockBasedModel(Map<Path, JsonElement> files, Item item, String suffix) {
		writeItemModel(files, itemName(item), parentModel(resourceBlock(itemName(item) + suffix).toString()));
	}

	private void itemGeneratedModel(Map<Path, JsonElement> files, Item item, String texture) {
		itemModel(files, item, GENERATED, texture);
	}

	private void itemModel(Map<Path, JsonElement> files, Item item, String parent, String texture) {
		JsonObject json = new JsonObject();
		json.addProperty("parent", parent);
		JsonObject textures = new JsonObject();
		textures.addProperty("layer0", texture);
		json.add("textures", textures);
		writeItemModel(files, itemName(item), json);
	}

	private JsonObject parentModel(String parent) {
		JsonObject json = new JsonObject();
		json.addProperty("parent", parent);
		return json;
	}

	private void writeItemModel(Map<Path, JsonElement> files, String name, JsonObject json) {
		files.put(this.itemModelPathProvider.json(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, name)), json);
	}

	private String itemName(Item item) {
		return BuiltInRegistries.ITEM.getKey(item).getPath();
	}

	private String resourceBlock(String path) {
		return FarmersDelight.MODID + ":block/" + path;
	}

	private String resourceItem(String path) {
		return FarmersDelight.MODID + ":item/" + path;
	}

	@SafeVarargs
	private static <T> Collection<T> takeAll(Set<? extends T> src, T... items) {
		Collection<T> selected = Arrays.asList(items);
		src.removeAll(selected);
		return selected;
	}

	private static <T> Collection<T> takeAll(Set<T> src, Predicate<T> predicate) {
		Collection<T> selected = new ArrayList<>();
		Iterator<T> iterator = src.iterator();
		while (iterator.hasNext()) {
			T item = iterator.next();
			if (predicate.test(item)) {
				iterator.remove();
				selected.add(item);
			}
		}
		return selected;
	}
}
