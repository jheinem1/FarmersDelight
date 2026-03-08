package vectorwing.farmersdelight.common.block;

import net.minecraft.world.level.block.*;

public final class PlantSupport
{
	private PlantSupport() {
	}

	public static boolean isWaterPlant(Block block) {
		return block instanceof SeagrassBlock
				|| block instanceof KelpBlock
				|| block instanceof WaterlilyBlock;
	}

	public static boolean isNetherPlant(Block block) {
		return block == Blocks.CRIMSON_FUNGUS
				|| block == Blocks.WARPED_FUNGUS
				|| block == Blocks.CRIMSON_ROOTS
				|| block == Blocks.WARPED_ROOTS
				|| block == Blocks.NETHER_SPROUTS
				|| block == Blocks.WEEPING_VINES
				|| block == Blocks.WEEPING_VINES_PLANT
				|| block == Blocks.TWISTING_VINES
				|| block == Blocks.TWISTING_VINES_PLANT;
	}

	public static boolean isCropLike(Block block) {
		return block instanceof CropBlock
				|| block instanceof StemBlock
				|| block instanceof AttachedStemBlock
				|| block instanceof PitcherCropBlock
				|| block instanceof TorchflowerCropBlock
				|| block instanceof SweetBerryBushBlock;
	}

	public static boolean isPlainsPlant(Block block) {
		return block instanceof BushBlock
				|| block instanceof SaplingBlock
				|| block instanceof MangrovePropaguleBlock
				|| block instanceof BambooSaplingBlock;
	}
}
