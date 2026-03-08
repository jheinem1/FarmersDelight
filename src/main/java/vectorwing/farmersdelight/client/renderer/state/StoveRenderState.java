package vectorwing.farmersdelight.client.renderer.state;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec2;

import java.util.ArrayList;
import java.util.List;

public class StoveRenderState extends BlockEntityRenderState
{
	public Direction facing = Direction.NORTH;
	public int renderLight;
	public final List<Entry> items = new ArrayList<>();

	public record Entry(ItemStackRenderState item, Vec2 offset)
	{
	}
}
