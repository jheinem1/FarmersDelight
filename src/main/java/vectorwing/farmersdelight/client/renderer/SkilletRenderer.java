package vectorwing.farmersdelight.client.renderer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.client.renderer.state.SkilletRenderState;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;
import java.util.Random;

public class SkilletRenderer implements BlockEntityRenderer<SkilletBlockEntity, SkilletRenderState>
{
	private final ItemModelResolver itemModelResolver;
	private final Random random = new Random();

	public SkilletRenderer(BlockEntityRendererProvider.Context context) {
		this.itemModelResolver = context.itemModelResolver();
	}

	@Override
	public SkilletRenderState createRenderState() {
		return new SkilletRenderState();
	}

	@Override
	public void extractRenderState(SkilletBlockEntity skilletEntity, SkilletRenderState renderState, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay overlay) {
		BlockEntityRenderer.super.extractRenderState(skilletEntity, renderState, partialTick, cameraPos, overlay);
		renderState.facing = skilletEntity.getBlockState().getValue(StoveBlock.FACING);
		Item stackItem = skilletEntity.getStoredStack().getItem();
		renderState.randomSeed = skilletEntity.getStoredStack().isEmpty() ? 187 : Item.getId(stackItem) + skilletEntity.getStoredStack().getDamageValue();
		renderState.itemRenderCount = skilletEntity.getStoredStack().isEmpty() ? 0 : this.getModelCount(skilletEntity.getStoredStack());
		this.itemModelResolver.updateForTopItem(renderState.item, skilletEntity.getStoredStack(), ItemDisplayContext.FIXED, skilletEntity.getLevel(), null, (int) skilletEntity.getBlockPos().asLong());
	}

	@Override
	public void submit(SkilletRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		this.random.setSeed(renderState.randomSeed);
		if (!renderState.item.isEmpty()) {
			for (int i = 0; i < renderState.itemRenderCount; i++) {
				poseStack.pushPose();
				float xOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
				float zOffset = (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
				poseStack.translate(0.5D + xOffset, 0.1D + 0.03 * (i + 1), 0.5D + zOffset);
				poseStack.mulPose(Axis.YP.rotationDegrees(-renderState.facing.toYRot()));
				poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
				poseStack.scale(0.5F, 0.5F, 0.5F);
				renderState.item.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
				poseStack.popPose();
			}
		}
	}

	protected int getModelCount(ItemStack stack) {
		if (stack.getCount() > 48) {
			return 5;
		} else if (stack.getCount() > 32) {
			return 4;
		} else if (stack.getCount() > 16) {
			return 3;
		} else if (stack.getCount() > 1) {
			return 2;
		}
		return 1;
	}
}
