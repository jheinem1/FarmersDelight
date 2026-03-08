package vectorwing.farmersdelight.client.renderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.client.renderer.state.CuttingBoardRenderState;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.tag.ModTags;

public class CuttingBoardRenderer implements BlockEntityRenderer<CuttingBoardBlockEntity, CuttingBoardRenderState>
{
	private final ItemModelResolver itemModelResolver;

	public CuttingBoardRenderer(BlockEntityRendererProvider.Context pContext) {
		this.itemModelResolver = pContext.itemModelResolver();
	}

	@Override
	public CuttingBoardRenderState createRenderState() {
		return new CuttingBoardRenderState();
	}

	@Override
	public void extractRenderState(CuttingBoardBlockEntity cuttingBoardEntity, CuttingBoardRenderState renderState, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay overlay) {
		BlockEntityRenderer.super.extractRenderState(cuttingBoardEntity, renderState, partialTick, cameraPos, overlay);
		ItemStack boardStack = cuttingBoardEntity.getStoredItem();
		renderState.facing = cuttingBoardEntity.getBlockState().getValue(CuttingBoardBlock.FACING).getOpposite();
		renderState.itemCarvingBoard = cuttingBoardEntity.isItemCarvingBoard();
		renderState.blockItem = boardStack.getItem() instanceof BlockItem;
		renderState.flatItem = boardStack.is(ModTags.FLAT_ON_CUTTING_BOARD);
		renderState.carvedPoseAngle = getCarvedPoseAngle(boardStack.getItem());
		this.itemModelResolver.updateForTopItem(renderState.item, boardStack, ItemDisplayContext.FIXED, cuttingBoardEntity.getLevel(), null, (int) cuttingBoardEntity.getBlockPos().asLong());
	}

	@Override
	public void submit(CuttingBoardRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		if (!renderState.item.isEmpty()) {
			poseStack.pushPose();
			if (renderState.itemCarvingBoard) {
				renderItemCarved(poseStack, renderState.facing, renderState.carvedPoseAngle);
			} else if (renderState.blockItem && !renderState.flatItem) {
				renderBlock(poseStack, renderState.facing);
			} else {
				renderItemLayingDown(poseStack, renderState.facing);
			}
			renderState.item.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		}
	}

	public void renderItemLayingDown(PoseStack matrixStackIn, Direction direction) {
		// Center item above the cutting board
		matrixStackIn.translate(0.5D, 0.08D, 0.5D);
		// Rotate item to face the cutting board's front side
		float f = -direction.toYRot();
		matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));
		// Rotate item flat on the cutting board. Use X and Y from now on
		matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
		// Resize the item
		matrixStackIn.scale(0.6F, 0.6F, 0.6F);
	}
	public void renderBlock(PoseStack matrixStackIn, Direction direction) {
		// Center block above the cutting board
		matrixStackIn.translate(0.5D, 0.27D, 0.5D);
		// Rotate block to face the cutting board's front side
		float f = -direction.toYRot();
		matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));
		// Resize the block
		matrixStackIn.scale(0.8F, 0.8F, 0.8F);
	}
	public void renderItemCarved(PoseStack matrixStackIn, Direction direction, ItemStack itemStack) {
		renderItemCarved(matrixStackIn, direction, getCarvedPoseAngle(itemStack.getItem()));
	}

	public void renderItemCarved(PoseStack matrixStackIn, Direction direction, float poseAngle) {
		// Center item above the cutting board
		matrixStackIn.translate(0.5D, 0.23D, 0.5D);
		// Rotate item to face the cutting board's front side
		float f = -direction.toYRot() + 180;
		matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));
		matrixStackIn.mulPose(Axis.ZP.rotationDegrees(poseAngle));
		// Resize the item
		matrixStackIn.scale(0.6F, 0.6F, 0.6F);
	}

	private static float getCarvedPoseAngle(Item toolItem) {
		if (toolItem instanceof HoeItem) {
			return 225.0F;
		}
		if (toolItem instanceof TridentItem) {
			return 135.0F;
		}
		return 180.0F;
	}
}
