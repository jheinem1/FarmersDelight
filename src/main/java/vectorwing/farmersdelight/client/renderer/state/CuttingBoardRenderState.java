package vectorwing.farmersdelight.client.renderer.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;

public class CuttingBoardRenderState extends BlockEntityRenderState
{
	public Direction facing = Direction.NORTH;
	public final ItemStackRenderState item = new ItemStackRenderState();
	public boolean itemCarvingBoard;
	public boolean blockItem;
	public boolean flatItem;
	public float carvedPoseAngle = 180.0F;
}
