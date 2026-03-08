package vectorwing.farmersdelight.data;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.BuddingTomatoBlock;
import vectorwing.farmersdelight.common.block.CabbageBlock;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.block.MushroomColonyBlock;
import vectorwing.farmersdelight.common.block.OnionBlock;
import vectorwing.farmersdelight.common.block.PieBlock;
import vectorwing.farmersdelight.common.block.RicePaniclesBlock;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BlockStates extends JsonAssetProvider
{
	private static final int DEFAULT_ANGLE_OFFSET = 180;

	public BlockStates(PackOutput output) {
		super("Farmer's Delight Block States", output);
	}

	@Override
	protected void collect(Map<Path, JsonElement> files) {
		registerStatesAndModels(files);
	}

	private void registerStatesAndModels(Map<Path, JsonElement> files) {
		simpleBlock(files, ModBlocks.RICH_SOIL.get(), cubeRandomRotation(files, ModBlocks.RICH_SOIL.get(), ""));
		simpleBlock(files, ModBlocks.SAFETY_NET.get(), modelRef(blockName(ModBlocks.SAFETY_NET.get())));

		for (Block sign : canvasSigns()) {
			simpleBlock(files, sign, modelRef(blockName(ModBlocks.CANVAS_SIGN.get())));
		}

		String riceBag = blockName(ModBlocks.RICE_BAG.get());
		writeBlockModel(files, riceBag, cubeModel("minecraft:block/cube", Map.of(
				"particle", textureBlock(riceBag + "_top"),
				"down", textureBlock(riceBag + "_bottom"),
				"up", textureBlock(riceBag + "_top"),
				"north", textureBlock(riceBag + "_side_tied"),
				"south", textureBlock(riceBag + "_side_tied"),
				"east", textureBlock(riceBag + "_side"),
				"west", textureBlock(riceBag + "_side")
		), null));
		simpleBlock(files, ModBlocks.RICE_BAG.get(), modelRef(riceBag));

		directionalBlock(files, ModBlocks.BASKET.get(), blockName(ModBlocks.BASKET.get()));
		directionalBlock(files, ModBlocks.RICE_BALE.get(), blockName(ModBlocks.RICE_BALE.get()));
		horizontalBlock(files, ModBlocks.CUTTING_BOARD.get(), blockName(ModBlocks.CUTTING_BOARD.get()));
		horizontalBlock(files, ModBlocks.HALF_TATAMI_MAT.get(), "tatami_mat_half");
		stoveBlock(files, ModBlocks.STOVE.get());

		stageBlock(files, ModBlocks.BROWN_MUSHROOM_COLONY.get(), MushroomColonyBlock.COLONY_AGE, null, "cross", List.of());
		stageBlock(files, ModBlocks.RED_MUSHROOM_COLONY.get(), MushroomColonyBlock.COLONY_AGE, null, "cross", List.of());
		stageBlock(files, ModBlocks.RICE_CROP_PANICLES.get(), RicePaniclesBlock.RICE_AGE, null, "cross", List.of());
		stageBlock(files, ModBlocks.CABBAGE_CROP.get(), CabbageBlock.AGE, "farmersdelight:block/crop_cross", "cross", List.of());
		stageBlock(files, ModBlocks.ONION_CROP.get(), OnionBlock.AGE, "minecraft:block/crop", "crop", Arrays.asList(0, 0, 1, 1, 2, 2, 2, 3));
		stageBlock(files, ModBlocks.BUDDING_TOMATO_CROP.get(), BuddingTomatoBlock.AGE, "farmersdelight:block/crop_cross", "cross", Arrays.asList(0, 1, 2, 3, 3));

		crateBlock(files, ModBlocks.CARROT_CRATE.get(), "carrot");
		crateBlock(files, ModBlocks.POTATO_CRATE.get(), "potato");
		crateBlock(files, ModBlocks.BEETROOT_CRATE.get(), "beetroot");
		crateBlock(files, ModBlocks.CABBAGE_CRATE.get(), "cabbage");
		crateBlock(files, ModBlocks.TOMATO_CRATE.get(), "tomato");
		crateBlock(files, ModBlocks.ONION_CRATE.get(), "onion");

		axisBlock(files, (RotatedPillarBlock) ModBlocks.STRAW_BALE.get());

		cabinetBlock(files, ModBlocks.OAK_CABINET.get(), "oak");
		cabinetBlock(files, ModBlocks.BIRCH_CABINET.get(), "birch");
		cabinetBlock(files, ModBlocks.SPRUCE_CABINET.get(), "spruce");
		cabinetBlock(files, ModBlocks.JUNGLE_CABINET.get(), "jungle");
		cabinetBlock(files, ModBlocks.ACACIA_CABINET.get(), "acacia");
		cabinetBlock(files, ModBlocks.DARK_OAK_CABINET.get(), "dark_oak");
		cabinetBlock(files, ModBlocks.MANGROVE_CABINET.get(), "mangrove");
		cabinetBlock(files, ModBlocks.CHERRY_CABINET.get(), "cherry");
		cabinetBlock(files, ModBlocks.BAMBOO_CABINET.get(), "bamboo");
		cabinetBlock(files, ModBlocks.CRIMSON_CABINET.get(), "crimson");
		cabinetBlock(files, ModBlocks.WARPED_CABINET.get(), "warped");

		pieBlock(files, ModBlocks.APPLE_PIE.get());
		pieBlock(files, ModBlocks.CHOCOLATE_PIE.get());
		pieBlock(files, ModBlocks.SWEET_BERRY_CHEESECAKE.get());

		feastBlock(files, (FeastBlock) ModBlocks.STUFFED_PUMPKIN_BLOCK.get());
		feastBlock(files, (FeastBlock) ModBlocks.ROAST_CHICKEN_BLOCK.get());
		feastBlock(files, (FeastBlock) ModBlocks.HONEY_GLAZED_HAM_BLOCK.get());
		feastBlock(files, (FeastBlock) ModBlocks.SHEPHERDS_PIE_BLOCK.get());
		feastBlock(files, (FeastBlock) ModBlocks.RICE_ROLL_MEDLEY_BLOCK.get());

		wildCropBlock(files, ModBlocks.SANDY_SHRUB.get());
		wildCropBlock(files, ModBlocks.WILD_BEETROOTS.get());
		wildCropBlock(files, ModBlocks.WILD_CABBAGES.get());
		wildCropBlock(files, ModBlocks.WILD_POTATOES.get());
		wildCropBlock(files, ModBlocks.WILD_TOMATOES.get());
		wildCropBlock(files, ModBlocks.WILD_CARROTS.get());
		wildCropBlock(files, ModBlocks.WILD_ONIONS.get());

		doublePlantBlock(files, ModBlocks.WILD_RICE.get());
	}

	private Set<Block> canvasSigns() {
		return Sets.newHashSet(
				ModBlocks.CANVAS_SIGN.get(), ModBlocks.HANGING_CANVAS_SIGN.get(),
				ModBlocks.WHITE_CANVAS_SIGN.get(), ModBlocks.WHITE_HANGING_CANVAS_SIGN.get(),
				ModBlocks.ORANGE_CANVAS_SIGN.get(), ModBlocks.ORANGE_HANGING_CANVAS_SIGN.get(),
				ModBlocks.MAGENTA_CANVAS_SIGN.get(), ModBlocks.MAGENTA_HANGING_CANVAS_SIGN.get(),
				ModBlocks.LIGHT_BLUE_CANVAS_SIGN.get(), ModBlocks.LIGHT_BLUE_HANGING_CANVAS_SIGN.get(),
				ModBlocks.YELLOW_CANVAS_SIGN.get(), ModBlocks.YELLOW_HANGING_CANVAS_SIGN.get(),
				ModBlocks.LIME_CANVAS_SIGN.get(), ModBlocks.LIME_HANGING_CANVAS_SIGN.get(),
				ModBlocks.PINK_CANVAS_SIGN.get(), ModBlocks.PINK_HANGING_CANVAS_SIGN.get(),
				ModBlocks.GRAY_CANVAS_SIGN.get(), ModBlocks.GRAY_HANGING_CANVAS_SIGN.get(),
				ModBlocks.LIGHT_GRAY_CANVAS_SIGN.get(), ModBlocks.LIGHT_GRAY_HANGING_CANVAS_SIGN.get(),
				ModBlocks.CYAN_CANVAS_SIGN.get(), ModBlocks.CYAN_HANGING_CANVAS_SIGN.get(),
				ModBlocks.PURPLE_CANVAS_SIGN.get(), ModBlocks.PURPLE_HANGING_CANVAS_SIGN.get(),
				ModBlocks.BLUE_CANVAS_SIGN.get(), ModBlocks.BLUE_HANGING_CANVAS_SIGN.get(),
				ModBlocks.BROWN_CANVAS_SIGN.get(), ModBlocks.BROWN_HANGING_CANVAS_SIGN.get(),
				ModBlocks.GREEN_CANVAS_SIGN.get(), ModBlocks.GREEN_HANGING_CANVAS_SIGN.get(),
				ModBlocks.RED_CANVAS_SIGN.get(), ModBlocks.RED_HANGING_CANVAS_SIGN.get(),
				ModBlocks.BLACK_CANVAS_SIGN.get(), ModBlocks.BLACK_HANGING_CANVAS_SIGN.get(),
				ModBlocks.CANVAS_WALL_SIGN.get(), ModBlocks.HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.WHITE_CANVAS_WALL_SIGN.get(), ModBlocks.WHITE_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.ORANGE_CANVAS_WALL_SIGN.get(), ModBlocks.ORANGE_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.MAGENTA_CANVAS_WALL_SIGN.get(), ModBlocks.MAGENTA_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.LIGHT_BLUE_CANVAS_WALL_SIGN.get(), ModBlocks.LIGHT_BLUE_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.YELLOW_CANVAS_WALL_SIGN.get(), ModBlocks.YELLOW_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.LIME_CANVAS_WALL_SIGN.get(), ModBlocks.LIME_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.PINK_CANVAS_WALL_SIGN.get(), ModBlocks.PINK_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.GRAY_CANVAS_WALL_SIGN.get(), ModBlocks.GRAY_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.LIGHT_GRAY_CANVAS_WALL_SIGN.get(), ModBlocks.LIGHT_GRAY_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.CYAN_CANVAS_WALL_SIGN.get(), ModBlocks.CYAN_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.PURPLE_CANVAS_WALL_SIGN.get(), ModBlocks.PURPLE_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.BLUE_CANVAS_WALL_SIGN.get(), ModBlocks.BLUE_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.BROWN_CANVAS_WALL_SIGN.get(), ModBlocks.BROWN_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.GREEN_CANVAS_WALL_SIGN.get(), ModBlocks.GREEN_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.RED_CANVAS_WALL_SIGN.get(), ModBlocks.RED_HANGING_CANVAS_WALL_SIGN.get(),
				ModBlocks.BLACK_CANVAS_WALL_SIGN.get(), ModBlocks.BLACK_HANGING_CANVAS_WALL_SIGN.get()
		);
	}

	private JsonArray cubeRandomRotation(Map<Path, JsonElement> files, Block block, String suffix) {
		String formattedName = blockName(block) + (suffix.isEmpty() ? "" : "_" + suffix);
		writeBlockModel(files, formattedName, cubeModel("minecraft:block/cube_all", Map.of("all", textureBlock(formattedName)), null));
		JsonArray variants = new JsonArray();
		variants.add(variant(modelRef(formattedName), null, null));
		variants.add(variant(modelRef(formattedName), null, 90));
		variants.add(variant(modelRef(formattedName), null, 180));
		variants.add(variant(modelRef(formattedName), null, 270));
		return variants;
	}

	private void simpleBlock(Map<Path, JsonElement> files, Block block, String model) {
		writeBlockState(files, blockName(block), blockState(singleVariantObject(model)));
	}

	private void simpleBlock(Map<Path, JsonElement> files, Block block, JsonArray variants) {
		JsonObject variantsObject = new JsonObject();
		variantsObject.add("", variants);
		writeBlockState(files, blockName(block), blockState(variantsObject));
	}

	private void directionalBlock(Map<Path, JsonElement> files, Block block, String model) {
		JsonObject variants = new JsonObject();
		for (Direction dir : Direction.values()) {
			Integer x = null;
			if (dir == Direction.DOWN) {
				x = 180;
			} else if (dir.getAxis().isHorizontal()) {
				x = 90;
			}

			Integer y = null;
			if (!dir.getAxis().isVertical()) {
				y = (int) ((dir.toYRot() + DEFAULT_ANGLE_OFFSET) % 360);
			}

			variants.add("facing=" + dir.getName(), variant(modelRef(model), x, y));
		}
		writeBlockState(files, blockName(block), blockState(variants));
	}

	private void horizontalBlock(Map<Path, JsonElement> files, Block block, String model) {
		JsonObject variants = new JsonObject();
		for (Direction dir : horizontalDirections()) {
			Integer y = (int) ((dir.toYRot() + DEFAULT_ANGLE_OFFSET) % 360);
			variants.add("facing=" + dir.getName(), variant(modelRef(model), null, y == 0 ? null : y));
		}
		writeBlockState(files, blockName(block), blockState(variants));
	}

	private void stoveBlock(Map<Path, JsonElement> files, Block block) {
		String name = blockName(block);
		writeBlockModel(files, name, cubeModel("minecraft:block/orientable_with_bottom", Map.of(
				"side", textureBlock(name + "_side"),
				"front", textureBlock(name + "_front"),
				"bottom", textureBlock(name + "_bottom"),
				"top", textureBlock(name + "_top")
		), null));
		writeBlockModel(files, name + "_on", cubeModel("minecraft:block/orientable_with_bottom", Map.of(
				"side", textureBlock(name + "_side"),
				"front", textureBlock(name + "_front_on"),
				"bottom", textureBlock(name + "_bottom"),
				"top", textureBlock(name + "_top_on")
		), null));

		JsonObject variants = new JsonObject();
		for (Direction dir : horizontalDirections()) {
			Integer y = (int) ((dir.toYRot() + DEFAULT_ANGLE_OFFSET) % 360);
			variants.add("facing=" + dir.getName() + ",lit=false", variant(modelRef(name), null, y == 0 ? null : y));
			variants.add("facing=" + dir.getName() + ",lit=true", variant(modelRef(name + "_on"), null, y == 0 ? null : y));
		}
		writeBlockState(files, name, blockState(variants));
	}

	private void stageBlock(Map<Path, JsonElement> files, Block block, IntegerProperty ageProperty, String parent, String textureKey, List<Integer> suffixes) {
		JsonObject variants = new JsonObject();
		for (Integer age : ageProperty.getPossibleValues()) {
			int suffix = suffixes.isEmpty() ? age : suffixes.get(Math.min(suffixes.size() - 1, age));
			String stageName = blockName(block) + "_stage" + suffix;
			if (!suffixes.isEmpty() || age == suffix) {
				writeBlockModel(files, stageName, stageModel(parent, textureKey, stageName));
			} else {
				writeBlockModel(files, stageName, stageModel(parent, textureKey, stageName));
			}
			variants.add("age=" + age, variant(modelRef(stageName), null, null));
		}
		writeBlockState(files, blockName(block), blockState(variants));
	}

	private void crateBlock(Map<Path, JsonElement> files, Block block, String cropName) {
		String name = blockName(block);
		writeBlockModel(files, name, cubeModel("minecraft:block/cube_bottom_top", Map.of(
				"side", textureBlock(cropName + "_crate_side"),
				"bottom", textureBlock("crate_bottom"),
				"top", textureBlock(cropName + "_crate_top")
		), null));
		simpleBlock(files, block, modelRef(name));
	}

	private void axisBlock(Map<Path, JsonElement> files, RotatedPillarBlock block) {
		String name = blockName(block);
		writeBlockModel(files, name, cubeModel("minecraft:block/cube_column", Map.of(
				"end", textureBlock(name + "_end"),
				"side", textureBlock(name + "_side")
		), null));
		writeBlockModel(files, name + "_horizontal", cubeModel("minecraft:block/cube_column_horizontal", Map.of(
				"end", textureBlock(name + "_end"),
				"side", textureBlock(name + "_side")
		), null));

		JsonObject variants = new JsonObject();
		variants.add("axis=x", variant(modelRef(name + "_horizontal"), 90, 90));
		variants.add("axis=y", variant(modelRef(name), null, null));
		variants.add("axis=z", variant(modelRef(name + "_horizontal"), 90, null));
		writeBlockState(files, name, blockState(variants));
	}

	private void cabinetBlock(Map<Path, JsonElement> files, Block block, String woodType) {
		String name = blockName(block);
		writeBlockModel(files, name, cubeModel("minecraft:block/orientable", Map.of(
				"side", textureBlock(woodType + "_cabinet_side"),
				"front", textureBlock(woodType + "_cabinet_front"),
				"top", textureBlock(woodType + "_cabinet_top")
		), null));
		writeBlockModel(files, name + "_open", cubeModel("minecraft:block/orientable", Map.of(
				"side", textureBlock(woodType + "_cabinet_side"),
				"front", textureBlock(woodType + "_cabinet_front_open"),
				"top", textureBlock(woodType + "_cabinet_top")
		), null));

		JsonObject variants = new JsonObject();
		for (Direction dir : horizontalDirections()) {
			Integer y = (int) ((dir.toYRot() + DEFAULT_ANGLE_OFFSET) % 360);
			variants.add("facing=" + dir.getName() + ",open=false", variant(modelRef(name), null, y == 0 ? null : y));
			variants.add("facing=" + dir.getName() + ",open=true", variant(modelRef(name + "_open"), null, y == 0 ? null : y));
		}
		writeBlockState(files, name, blockState(variants));
	}

	private void feastBlock(Map<Path, JsonElement> files, FeastBlock block) {
		JsonObject variants = new JsonObject();
		IntegerProperty servingsProperty = block.getServingsProperty();
		int maxServings = block.getMaxServings();
		int fallbackStage = servingsProperty.getPossibleValues().size() - 2;
		for (Direction dir : horizontalDirections()) {
			Integer y = (int) ((dir.toYRot() + DEFAULT_ANGLE_OFFSET) % 360);
			for (Integer servings : servingsProperty.getPossibleValues()) {
				String suffix = servings == 0
						? (block.hasLeftovers ? "_leftover" : "_stage" + fallbackStage)
						: "_stage" + (maxServings - servings);
				variants.add("facing=" + dir.getName() + ",servings=" + servings,
						variant(modelRef(blockName(block) + suffix), null, y == 0 ? null : y));
			}
		}
		writeBlockState(files, blockName(block), blockState(variants));
	}

	private void pieBlock(Map<Path, JsonElement> files, Block block) {
		JsonObject variants = new JsonObject();
		for (Direction dir : horizontalDirections()) {
			Integer y = (int) ((dir.toYRot() + DEFAULT_ANGLE_OFFSET) % 360);
			for (Integer bites : PieBlock.BITES.getPossibleValues()) {
				String suffix = bites > 0 ? "_slice" + bites : "";
				variants.add("bites=" + bites + ",facing=" + dir.getName(),
						variant(modelRef(blockName(block) + suffix), null, y == 0 ? null : y));
			}
		}
		writeBlockState(files, blockName(block), blockState(variants));
	}

	private void wildCropBlock(Map<Path, JsonElement> files, Block block) {
		String name = blockName(block);
		writeBlockModel(files, name, stageModel("minecraft:block/cross", "cross", name));
		simpleBlock(files, block, modelRef(name));
	}

	private void doublePlantBlock(Map<Path, JsonElement> files, Block block) {
		String name = blockName(block);
		writeBlockModel(files, name + "_bottom", stageModel("minecraft:block/cross", "cross", name + "_bottom"));
		writeBlockModel(files, name + "_top", stageModel("minecraft:block/cross", "cross", name + "_top"));

		JsonObject variants = new JsonObject();
		variants.add("half=" + DoubleBlockHalf.LOWER.getSerializedName(), variant(modelRef(name + "_bottom"), null, null));
		variants.add("half=" + DoubleBlockHalf.UPPER.getSerializedName(), variant(modelRef(name + "_top"), null, null));
		writeBlockState(files, name, blockState(variants));
	}

	private JsonObject stageModel(String parent, String textureKey, String stageName) {
		String resolvedParent = parent == null ? "minecraft:block/cross" : parent;
		return cubeModel(resolvedParent, Map.of(textureKey, textureBlock(stageName)), "minecraft:cutout");
	}

	private JsonObject cubeModel(String parent, Map<String, String> textures, String renderType) {
		JsonObject json = new JsonObject();
		json.addProperty("parent", parent);
		if (renderType != null) {
			json.addProperty("render_type", renderType);
		}
		JsonObject texturesJson = new JsonObject();
		textures.forEach(texturesJson::addProperty);
		json.add("textures", texturesJson);
		return json;
	}

	private JsonObject blockState(JsonObject variants) {
		JsonObject json = new JsonObject();
		json.add("variants", variants);
		return json;
	}

	private JsonObject singleVariantObject(String model) {
		JsonObject variants = new JsonObject();
		variants.add("", variant(model, null, null));
		return variants;
	}

	private JsonObject variant(String model, Integer x, Integer y) {
		JsonObject variant = new JsonObject();
		variant.addProperty("model", model);
		if (x != null) {
			variant.addProperty("x", x);
		}
		if (y != null) {
			variant.addProperty("y", y);
		}
		return variant;
	}

	private void writeBlockState(Map<Path, JsonElement> files, String name, JsonObject json) {
		files.put(this.blockStatePathProvider.json(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, name)), json);
	}

	private void writeBlockModel(Map<Path, JsonElement> files, String name, JsonObject json) {
		files.put(this.blockModelPathProvider.json(Identifier.fromNamespaceAndPath(FarmersDelight.MODID, name)), json);
	}

	private List<Direction> horizontalDirections() {
		return Arrays.asList(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
	}

	private String blockName(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block).getPath();
	}

	private String modelRef(String path) {
		return FarmersDelight.MODID + ":block/" + path;
	}

	private String textureBlock(String path) {
		return FarmersDelight.MODID + ":block/" + path;
	}
}
