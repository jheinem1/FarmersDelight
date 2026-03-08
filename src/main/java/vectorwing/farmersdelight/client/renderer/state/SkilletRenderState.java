package vectorwing.farmersdelight.client.renderer.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;

public class SkilletRenderState extends BlockEntityRenderState
{
	public Direction facing = Direction.NORTH;
	public final ItemStackRenderState item = new ItemStackRenderState();
	public int itemRenderCount;
	public int randomSeed = 187;
}
