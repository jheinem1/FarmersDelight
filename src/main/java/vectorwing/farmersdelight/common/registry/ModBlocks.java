package vectorwing.farmersdelight.common.registry;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.block.*;

public class ModBlocks
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, FarmersDelight.MODID);
	private static final ThreadLocal<ResourceKey<Block>> REGISTERING_BLOCK = new ThreadLocal<>();

	private static <B extends Block> Supplier<B> register(String name, Supplier<B> supplier) {
		return BLOCKS.register(name, (Identifier id) -> {
			REGISTERING_BLOCK.set(ResourceKey.create(Registries.BLOCK, id));
			try {
				return supplier.get();
			} finally {
				REGISTERING_BLOCK.remove();
			}
		});
	}

	public static BlockBehaviour.Properties idProps(BlockBehaviour.Properties properties) {
		ResourceKey<Block> key = REGISTERING_BLOCK.get();
		if (key != null) {
			properties.setId(key);
		}
		return properties;
	}

	private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
		return (state) -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
	}

	private static BlockBehaviour.Properties cropProps() {
		return idProps(Block.Properties.of()
				.mapColor(MapColor.PLANT)
				.noCollision()
				.randomTicks()
				.instabreak()
				.sound(SoundType.CROP)
				.pushReaction(PushReaction.DESTROY));
	}

	// Workstations
	public static final Supplier<Block> STOVE = register("stove",
			() -> new StoveBlock(idProps(Block.Properties.ofFullCopy(Blocks.BRICKS).lightLevel(litBlockEmission(13)))));
	public static final Supplier<Block> COOKING_POT = register("cooking_pot",
			() -> new CookingPotBlock(idProps(Block.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN))));
	public static final Supplier<Block> SKILLET = register("skillet",
			() -> new SkilletBlock(idProps(Block.Properties.of().mapColor(MapColor.METAL).strength(0.5F, 6.0F).sound(SoundType.LANTERN))));
	public static final Supplier<Block> BASKET = register("basket",
			() -> new BasketBlock(idProps(Block.Properties.of().strength(1.5F).sound(SoundType.BAMBOO_WOOD))));
	public static final Supplier<Block> CUTTING_BOARD = register("cutting_board",
			() -> new CuttingBoardBlock(idProps(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F).sound(SoundType.WOOD))));

	// Crop Storage
	public static final Supplier<Block> CARROT_CRATE = register("carrot_crate",
			() -> new Block(idProps(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD))));
	public static final Supplier<Block> POTATO_CRATE = register("potato_crate",
			() -> new Block(idProps(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD))));
	public static final Supplier<Block> BEETROOT_CRATE = register("beetroot_crate",
			() -> new Block(idProps(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD))));
	public static final Supplier<Block> CABBAGE_CRATE = register("cabbage_crate",
			() -> new Block(idProps(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD))));
	public static final Supplier<Block> TOMATO_CRATE = register("tomato_crate",
			() -> new Block(idProps(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD))));
	public static final Supplier<Block> ONION_CRATE = register("onion_crate",
			() -> new Block(idProps(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0F, 3.0F).sound(SoundType.WOOD))));
	public static final Supplier<Block> RICE_BALE = register("rice_bale",
			() -> new RiceBaleBlock(idProps(Block.Properties.ofFullCopy(Blocks.HAY_BLOCK))));
	public static final Supplier<Block> RICE_BAG = register("rice_bag",
			() -> new Block(idProps(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL))));
	public static final Supplier<Block> STRAW_BALE = register("straw_bale",
			() -> new StrawBaleBlock(idProps(Block.Properties.ofFullCopy(Blocks.HAY_BLOCK))));

	// Building
	public static final Supplier<Block> ROPE = register("rope",
			() -> new RopeBlock(idProps(Block.Properties.ofFullCopy(Blocks.BROWN_CARPET).noCollision().noOcclusion().strength(0.2F).sound(SoundType.WOOL))));
	public static final Supplier<Block> SAFETY_NET = register("safety_net",
			() -> new SafetyNetBlock(idProps(Block.Properties.ofFullCopy(Blocks.BROWN_CARPET).strength(0.2F).sound(SoundType.WOOL))));
	public static final Supplier<Block> OAK_CABINET = register("oak_cabinet",
			() -> new CabinetBlock(idProps(Block.Properties.ofFullCopy(Blocks.BARREL))));
	public static final Supplier<Block> SPRUCE_CABINET = register("spruce_cabinet",
			() -> new CabinetBlock(idProps(Block.Properties.ofFullCopy(Blocks.BARREL))));
	public static final Supplier<Block> BIRCH_CABINET = register("birch_cabinet",
			() -> new CabinetBlock(idProps(Block.Properties.ofFullCopy(Blocks.BARREL))));
	public static final Supplier<Block> JUNGLE_CABINET = register("jungle_cabinet",
			() -> new CabinetBlock(idProps(Block.Properties.ofFullCopy(Blocks.BARREL))));
	public static final Supplier<Block> ACACIA_CABINET = register("acacia_cabinet",
			() -> new CabinetBlock(idProps(Block.Properties.ofFullCopy(Blocks.BARREL))));
	public static final Supplier<Block> DARK_OAK_CABINET = register("dark_oak_cabinet",
			() -> new CabinetBlock(idProps(Block.Properties.ofFullCopy(Blocks.BARREL))));
	public static final Supplier<Block> MANGROVE_CABINET = register("mangrove_cabinet",
			() -> new CabinetBlock(idProps(Block.Properties.ofFullCopy(Blocks.BARREL))));
	public static final Supplier<Block> CHERRY_CABINET = register("cherry_cabinet",
			() -> new CabinetBlock(idProps(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.CHERRY_WOOD))));
	public static final Supplier<Block> BAMBOO_CABINET = register("bamboo_cabinet",
			() -> new CabinetBlock(idProps(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.BAMBOO_WOOD))));
	public static final Supplier<Block> CRIMSON_CABINET = register("crimson_cabinet",
			() -> new CabinetBlock(idProps(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.NETHER_WOOD))));
	public static final Supplier<Block> WARPED_CABINET = register("warped_cabinet",
			() -> new CabinetBlock(idProps(Block.Properties.ofFullCopy(Blocks.BARREL).sound(SoundType.NETHER_WOOD))));
	public static final Supplier<Block> CANVAS_RUG = register("canvas_rug",
			() -> new CanvasRugBlock(idProps(Block.Properties.ofFullCopy(Blocks.WHITE_CARPET).sound(SoundType.GRASS).strength(0.2F))));
	public static final Supplier<Block> TATAMI = register("tatami",
			() -> new TatamiBlock(idProps(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL))));
	public static final Supplier<Block> FULL_TATAMI_MAT = register("full_tatami_mat",
			() -> new TatamiMatBlock(idProps(Block.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(0.3F))));
	public static final Supplier<Block> HALF_TATAMI_MAT = register("half_tatami_mat",
			() -> new TatamiHalfMatBlock(idProps(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL).strength(0.3F).pushReaction(PushReaction.DESTROY))));
	public static final Supplier<Block> CANVAS_SIGN = register("canvas_sign",
			() -> new StandingCanvasSignBlock(null));
	public static final Supplier<Block> WHITE_CANVAS_SIGN = register("white_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_CANVAS_SIGN = register("orange_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_CANVAS_SIGN = register("magenta_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_CANVAS_SIGN = register("light_blue_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_CANVAS_SIGN = register("yellow_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.YELLOW));
	public static final Supplier<Block> LIME_CANVAS_SIGN = register("lime_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.LIME));
	public static final Supplier<Block> PINK_CANVAS_SIGN = register("pink_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.PINK));
	public static final Supplier<Block> GRAY_CANVAS_SIGN = register("gray_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_CANVAS_SIGN = register("light_gray_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_CANVAS_SIGN = register("cyan_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_CANVAS_SIGN = register("purple_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_CANVAS_SIGN = register("blue_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.BLUE));
	public static final Supplier<Block> BROWN_CANVAS_SIGN = register("brown_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.BROWN));
	public static final Supplier<Block> GREEN_CANVAS_SIGN = register("green_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.GREEN));
	public static final Supplier<Block> RED_CANVAS_SIGN = register("red_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.RED));
	public static final Supplier<Block> BLACK_CANVAS_SIGN = register("black_canvas_sign",
			() -> new StandingCanvasSignBlock(DyeColor.BLACK));
	public static final Supplier<Block> CANVAS_WALL_SIGN = register("canvas_wall_sign",
			() -> new WallCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), null));
	public static final Supplier<Block> WHITE_CANVAS_WALL_SIGN = register("white_canvas_wall_sign",
			() -> new WallCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_CANVAS_WALL_SIGN = register("orange_canvas_wall_sign",
			() -> new WallCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_CANVAS_WALL_SIGN = register("magenta_canvas_wall_sign",
			() -> new WallCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_CANVAS_WALL_SIGN = register("light_blue_canvas_wall_sign",
			() -> new WallCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_CANVAS_WALL_SIGN = register("yellow_canvas_wall_sign",
			() -> new WallCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.YELLOW));
	public static final Supplier<Block> LIME_CANVAS_WALL_SIGN = register("lime_canvas_wall_sign",
			() -> new WallCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.LIME));
	public static final Supplier<Block> PINK_CANVAS_WALL_SIGN = register("pink_canvas_wall_sign",
			() -> new WallCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.PINK));
	public static final Supplier<Block> GRAY_CANVAS_WALL_SIGN = register("gray_canvas_wall_sign",
			() -> new WallCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_CANVAS_WALL_SIGN = register("light_gray_canvas_wall_sign",
			() -> new WallCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_CANVAS_WALL_SIGN = register("cyan_canvas_wall_sign",
			() -> new WallCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_CANVAS_WALL_SIGN = register("purple_canvas_wall_sign",
			() -> new WallCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_CANVAS_WALL_SIGN = register("blue_canvas_wall_sign",
			() -> new WallCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.BLUE));
	public static final Supplier<Block> BROWN_CANVAS_WALL_SIGN = register("brown_canvas_wall_sign",
			() -> new WallCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.BROWN));
	public static final Supplier<Block> GREEN_CANVAS_WALL_SIGN = register("green_canvas_wall_sign",
			() -> new WallCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.GREEN));
	public static final Supplier<Block> RED_CANVAS_WALL_SIGN = register("red_canvas_wall_sign",
			() -> new WallCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.RED));
	public static final Supplier<Block> BLACK_CANVAS_WALL_SIGN = register("black_canvas_wall_sign",
			() -> new WallCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_SIGN)), DyeColor.BLACK));
	public static final Supplier<Block> HANGING_CANVAS_SIGN = register("hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(null));
	public static final Supplier<Block> WHITE_HANGING_CANVAS_SIGN = register("white_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_HANGING_CANVAS_SIGN = register("orange_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_HANGING_CANVAS_SIGN = register("magenta_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_HANGING_CANVAS_SIGN = register("light_blue_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_HANGING_CANVAS_SIGN = register("yellow_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.YELLOW));
	public static final Supplier<Block> LIME_HANGING_CANVAS_SIGN = register("lime_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.LIME));
	public static final Supplier<Block> PINK_HANGING_CANVAS_SIGN = register("pink_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.PINK));
	public static final Supplier<Block> GRAY_HANGING_CANVAS_SIGN = register("gray_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_HANGING_CANVAS_SIGN = register("light_gray_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_HANGING_CANVAS_SIGN = register("cyan_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_HANGING_CANVAS_SIGN = register("purple_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_HANGING_CANVAS_SIGN = register("blue_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.BLUE));
	public static final Supplier<Block> BROWN_HANGING_CANVAS_SIGN = register("brown_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.BROWN));
	public static final Supplier<Block> GREEN_HANGING_CANVAS_SIGN = register("green_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.GREEN));
	public static final Supplier<Block> RED_HANGING_CANVAS_SIGN = register("red_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.RED));
	public static final Supplier<Block> BLACK_HANGING_CANVAS_SIGN = register("black_hanging_canvas_sign",
			() -> new CeilingHangingCanvasSignBlock(DyeColor.BLACK));
	public static final Supplier<Block> HANGING_CANVAS_WALL_SIGN = register("wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN)), null));
	public static final Supplier<Block> WHITE_HANGING_CANVAS_WALL_SIGN = register("white_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN)), DyeColor.WHITE));
	public static final Supplier<Block> ORANGE_HANGING_CANVAS_WALL_SIGN = register("orange_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN)), DyeColor.ORANGE));
	public static final Supplier<Block> MAGENTA_HANGING_CANVAS_WALL_SIGN = register("magenta_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN)), DyeColor.MAGENTA));
	public static final Supplier<Block> LIGHT_BLUE_HANGING_CANVAS_WALL_SIGN = register("light_blue_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN)), DyeColor.LIGHT_BLUE));
	public static final Supplier<Block> YELLOW_HANGING_CANVAS_WALL_SIGN = register("yellow_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN)), DyeColor.YELLOW));
	public static final Supplier<Block> LIME_HANGING_CANVAS_WALL_SIGN = register("lime_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN)), DyeColor.LIME));
	public static final Supplier<Block> PINK_HANGING_CANVAS_WALL_SIGN = register("pink_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN)), DyeColor.PINK));
	public static final Supplier<Block> GRAY_HANGING_CANVAS_WALL_SIGN = register("gray_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN)), DyeColor.GRAY));
	public static final Supplier<Block> LIGHT_GRAY_HANGING_CANVAS_WALL_SIGN = register("light_gray_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN)), DyeColor.LIGHT_GRAY));
	public static final Supplier<Block> CYAN_HANGING_CANVAS_WALL_SIGN = register("cyan_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN)), DyeColor.CYAN));
	public static final Supplier<Block> PURPLE_HANGING_CANVAS_WALL_SIGN = register("purple_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN)), DyeColor.PURPLE));
	public static final Supplier<Block> BLUE_HANGING_CANVAS_WALL_SIGN = register("blue_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN)), DyeColor.BLUE));
	public static final Supplier<Block> BROWN_HANGING_CANVAS_WALL_SIGN = register("brown_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN)), DyeColor.BROWN));
	public static final Supplier<Block> GREEN_HANGING_CANVAS_WALL_SIGN = register("green_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN)), DyeColor.GREEN));
	public static final Supplier<Block> RED_HANGING_CANVAS_WALL_SIGN = register("red_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN)), DyeColor.RED));
	public static final Supplier<Block> BLACK_HANGING_CANVAS_WALL_SIGN = register("black_wall_hanging_canvas_sign",
			() -> new WallHangingCanvasSignBlock(idProps(Block.Properties.ofFullCopy(Blocks.SPRUCE_WALL_HANGING_SIGN)), DyeColor.BLACK));

	// Composting
	public static final Supplier<Block> BROWN_MUSHROOM_COLONY = register("brown_mushroom_colony",
			() -> new MushroomColonyBlock(Items.BROWN_MUSHROOM.builtInRegistryHolder(), idProps(Block.Properties.ofFullCopy(Blocks.BROWN_MUSHROOM))));
	public static final Supplier<Block> RED_MUSHROOM_COLONY = register("red_mushroom_colony",
			() -> new MushroomColonyBlock(Items.RED_MUSHROOM.builtInRegistryHolder(), idProps(Block.Properties.ofFullCopy(Blocks.RED_MUSHROOM))));
	public static final Supplier<Block> ORGANIC_COMPOST = register("organic_compost",
			() -> new OrganicCompostBlock(idProps(Block.Properties.ofFullCopy(Blocks.DIRT).strength(1.2F).sound(SoundType.CROP))));
	public static final Supplier<Block> RICH_SOIL = register("rich_soil",
			() -> new RichSoilBlock(idProps(Block.Properties.ofFullCopy(Blocks.DIRT).randomTicks())));
	public static final Supplier<Block> RICH_SOIL_FARMLAND = register("rich_soil_farmland",
			() -> new RichSoilFarmlandBlock(idProps(Block.Properties.ofFullCopy(Blocks.FARMLAND))));

	// Pastries
	public static final Supplier<Block> APPLE_PIE = register("apple_pie",
			() -> new PieBlock(idProps(Block.Properties.ofFullCopy(Blocks.CAKE)), ModItems.APPLE_PIE_SLICE));
	public static final Supplier<Block> SWEET_BERRY_CHEESECAKE = register("sweet_berry_cheesecake",
			() -> new PieBlock(idProps(Block.Properties.ofFullCopy(Blocks.CAKE)), ModItems.SWEET_BERRY_CHEESECAKE_SLICE));
	public static final Supplier<Block> CHOCOLATE_PIE = register("chocolate_pie",
			() -> new PieBlock(idProps(Block.Properties.ofFullCopy(Blocks.CAKE)), ModItems.CHOCOLATE_PIE_SLICE));

	// Wild Crops
	public static final Supplier<Block> SANDY_SHRUB = register("sandy_shrub",
			() -> new SandyShrubBlock(idProps(Block.Properties.ofFullCopy(Blocks.TALL_GRASS))));
	public static final Supplier<Block> WILD_CABBAGES = register("wild_cabbages",
			() -> new WildCropBlock(MobEffects.STRENGTH, 6, idProps(Block.Properties.ofFullCopy(Blocks.TALL_GRASS))));
	public static final Supplier<Block> WILD_ONIONS = register("wild_onions",
			() -> new WildCropBlock(MobEffects.FIRE_RESISTANCE, 6, idProps(Block.Properties.ofFullCopy(Blocks.TALL_GRASS))));
	public static final Supplier<Block> WILD_TOMATOES = register("wild_tomatoes",
			() -> new WildCropBlock(MobEffects.POISON, 10, idProps(Block.Properties.ofFullCopy(Blocks.TALL_GRASS))));
	public static final Supplier<Block> WILD_CARROTS = register("wild_carrots",
			() -> new WildCropBlock(MobEffects.MINING_FATIGUE, 6, idProps(Block.Properties.ofFullCopy(Blocks.TALL_GRASS))));
	public static final Supplier<Block> WILD_POTATOES = register("wild_potatoes",
			() -> new WildCropBlock(MobEffects.NAUSEA, 8, idProps(Block.Properties.ofFullCopy(Blocks.TALL_GRASS))));
	public static final Supplier<Block> WILD_BEETROOTS = register("wild_beetroots",
			() -> new WildCropBlock(MobEffects.WATER_BREATHING, 8, idProps(Block.Properties.ofFullCopy(Blocks.TALL_GRASS))));
	public static final Supplier<Block> WILD_RICE = register("wild_rice",
			() -> new WildRiceBlock(idProps(Block.Properties.ofFullCopy(Blocks.TALL_GRASS))));

	// Crops
	public static final Supplier<Block> CABBAGE_CROP = register("cabbages",
			() -> new CabbageBlock(cropProps()));
	public static final Supplier<Block> ONION_CROP = register("onions",
			() -> new OnionBlock(cropProps()));
	public static final Supplier<Block> BUDDING_TOMATO_CROP = register("budding_tomatoes",
			() -> new BuddingTomatoBlock(cropProps()));
	public static final Supplier<Block> TOMATO_CROP = register("tomatoes",
			() -> new TomatoVineBlock(cropProps()));
	public static final Supplier<Block> RICE_CROP = register("rice",
			() -> new RiceBlock(cropProps().strength(0.2F)));
	public static final Supplier<Block> RICE_CROP_PANICLES = register("rice_panicles",
			() -> new RicePaniclesBlock(cropProps()));

	// Feasts
	public static final Supplier<Block> ROAST_CHICKEN_BLOCK = register("roast_chicken_block",
			() -> new RoastChickenBlock(idProps(Block.Properties.ofFullCopy(Blocks.CAKE)), ModItems.ROAST_CHICKEN, true));
	public static final Supplier<Block> STUFFED_PUMPKIN_BLOCK = register("stuffed_pumpkin_block",
			() -> new FeastBlock(idProps(Block.Properties.ofFullCopy(Blocks.PUMPKIN)), ModItems.STUFFED_PUMPKIN, false));
	public static final Supplier<Block> HONEY_GLAZED_HAM_BLOCK = register("honey_glazed_ham_block",
			() -> new HoneyGlazedHamBlock(idProps(Block.Properties.ofFullCopy(Blocks.CAKE)), ModItems.HONEY_GLAZED_HAM, true));
	public static final Supplier<Block> SHEPHERDS_PIE_BLOCK = register("shepherds_pie_block",
			() -> new ShepherdsPieBlock(idProps(Block.Properties.ofFullCopy(Blocks.CAKE)), ModItems.SHEPHERDS_PIE, true));
	public static final Supplier<Block> RICE_ROLL_MEDLEY_BLOCK = register("rice_roll_medley_block",
			() -> new RiceRollMedleyBlock(idProps(Block.Properties.ofFullCopy(Blocks.CAKE))));
}
