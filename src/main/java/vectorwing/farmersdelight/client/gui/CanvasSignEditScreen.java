package vectorwing.farmersdelight.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import org.jspecify.annotations.Nullable;

public class CanvasSignEditScreen extends SignEditScreen
{
	@Nullable
	protected Model.Simple signModel;

	public CanvasSignEditScreen(SignBlockEntity signBlockEntity, boolean isFront, boolean isTextFilteringEnabled) {
		super(signBlockEntity, isFront, isTextFilteringEnabled);
	}

	@Override
	protected void init() {
		super.init();
		boolean standing = this.sign.getBlockState().getBlock() instanceof StandingSignBlock;
		this.signModel = SignRenderer.createSignModel(this.minecraft.getEntityModels(), this.woodType, standing);
	}

	@Override
	protected void renderSignBackground(GuiGraphics guiGraphics) {
		if (this.signModel != null) {
			int centerX = this.width / 2;
			guiGraphics.submitSignRenderState(this.signModel, MAGIC_SCALE_NUMBER, this.woodType, centerX - 48, 66, centerX + 48, 168);
		}
	}
}
