package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.client.renderer.blockentity.state.SignRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.block.state.CanvasSign;
import vectorwing.farmersdelight.common.registry.ModAtlases;

public class CanvasSignRenderer extends SignRenderer
{
	@Nullable
	protected DyeColor currentDye;

	public CanvasSignRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void submit(SignRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
		this.currentDye = getBackgroundColor(renderState.blockState);
		try {
			super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
		} finally {
			this.currentDye = null;
		}
	}

	@Override
	protected Material getSignMaterial(WoodType woodType) {
		return ModAtlases.getCanvasSignMaterial(this.currentDye);
	}

	@Nullable
	protected DyeColor getBackgroundColor(BlockState state) {
		return state.getBlock() instanceof CanvasSign canvasSign ? canvasSign.getBackgroundColor() : null;
	}
}
