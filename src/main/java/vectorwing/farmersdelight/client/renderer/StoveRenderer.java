package vectorwing.farmersdelight.client.renderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.renderer.texture.OverlayTexture;
import vectorwing.farmersdelight.client.renderer.state.StoveRenderState;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.StoveBlockEntity;
import org.jspecify.annotations.Nullable;

public class StoveRenderer implements BlockEntityRenderer<StoveBlockEntity, StoveRenderState>
{
	private final ItemModelResolver itemModelResolver;

	public StoveRenderer(BlockEntityRendererProvider.Context context) {
		this.itemModelResolver = context.itemModelResolver();
	}

	@Override
	public StoveRenderState createRenderState() {
		return new StoveRenderState();
	}

	@Override
	public void extractRenderState(StoveBlockEntity stoveEntity, StoveRenderState renderState, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay overlay) {
		BlockEntityRenderer.super.extractRenderState(stoveEntity, renderState, partialTick, cameraPos, overlay);
		renderState.facing = stoveEntity.getBlockState().getValue(StoveBlock.FACING).getOpposite();
		renderState.renderLight = stoveEntity.getLevel() != null ? LevelRenderer.getLightColor(stoveEntity.getLevel(), stoveEntity.getBlockPos().above()) : renderState.lightCoords;
		renderState.items.clear();
		int seedBase = (int) stoveEntity.getBlockPos().asLong();
		for (int i = 0; i < stoveEntity.getInventory().getSlots(); ++i) {
			ItemStackRenderState item = new ItemStackRenderState();
			this.itemModelResolver.updateForTopItem(item, stoveEntity.getInventory().getStackInSlot(i), ItemDisplayContext.FIXED, stoveEntity.getLevel(), null, seedBase + i);
			if (!item.isEmpty()) {
				renderState.items.add(new StoveRenderState.Entry(item, stoveEntity.getStoveItemOffset(i)));
			}
		}
	}

	@Override
	public void submit(StoveRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		for (StoveRenderState.Entry entry : renderState.items) {
			poseStack.pushPose();
			poseStack.translate(0.5D, 1.02D, 0.5D);
			poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.facing.toYRot()));
			poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
			Vec2 itemOffset = entry.offset();
			poseStack.translate(itemOffset.x, itemOffset.y, 0.0D);
			poseStack.scale(0.375F, 0.375F, 0.375F);
			entry.item().submit(poseStack, submitNodeCollector, renderState.renderLight, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}
	}
}
