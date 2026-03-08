package vectorwing.farmersdelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.item.component.ItemStackWrapper;
import vectorwing.farmersdelight.common.registry.ModDataComponents;

import java.util.function.Consumer;

public class SkilletSpecialRenderer implements SpecialModelRenderer<ItemStack>
{
	private final ItemModelResolver itemModelResolver = new ItemModelResolver(Minecraft.getInstance().getModelManager());

	@Override
	public void submit(@Nullable ItemStack ingredientStack, ItemDisplayContext displayContext, PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, int packedOverlay, boolean hasFoil, int outlineColor) {
		if (ingredientStack == null || ingredientStack.isEmpty()) {
			return;
		}

		ItemStackRenderState renderState = new ItemStackRenderState();
		this.itemModelResolver.updateForTopItem(renderState, ingredientStack, ItemDisplayContext.FIXED, Minecraft.getInstance().level, null, 0);
		if (renderState.isEmpty()) {
			return;
		}

		poseStack.pushPose();
		poseStack.translate(0.5D, 1.0D / 16.0D, 0.5D);
		poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
		poseStack.scale(0.5F, 0.5F, 0.5F);
		renderState.submit(poseStack, nodeCollector, packedLight, packedOverlay, outlineColor);
		poseStack.popPose();
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
	}

	@Override
	public @Nullable ItemStack extractArgument(ItemStack stack) {
		ItemStackWrapper wrapper = stack.getOrDefault(ModDataComponents.SKILLET_INGREDIENT, ItemStackWrapper.EMPTY);
		ItemStack ingredient = wrapper.getStack();
		return ingredient.isEmpty() ? null : ingredient;
	}

	public record Unbaked() implements SpecialModelRenderer.Unbaked
	{
		public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(new Unbaked());

		@Override
		public @Nullable SpecialModelRenderer<?> bake(BakingContext context) {
			return new SkilletSpecialRenderer();
		}

		@Override
		public MapCodec<? extends SpecialModelRenderer.Unbaked> type() {
			return MAP_CODEC;
		}
	}
}
