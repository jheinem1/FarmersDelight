package vectorwing.farmersdelight.common.block;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import vectorwing.farmersdelight.common.block.entity.BasketBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;
import org.jspecify.annotations.Nullable;
@SuppressWarnings("deprecation")
public class BasketBlock extends BaseEntityBlock implements SimpleWaterloggedBlock
{
	public static final MapCodec<BasketBlock> CODEC = simpleCodec(BasketBlock::new);
	public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
	public static final BooleanProperty ENABLED = BlockStateProperties.ENABLED;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final VoxelShape OUT_SHAPE = Shapes.block();
	public static final VoxelShape RENDER_SHAPE = box(1.0D, 1.0D, 1.0D, 15.0D, 15.0D, 15.0D);
	@SuppressWarnings("UnstableApiUsage")
	public static final ImmutableMap<Direction, VoxelShape> COLLISION_SHAPE_FACING =
			Maps.immutableEnumMap(ImmutableMap.<Direction, VoxelShape>builder()
					.put(Direction.DOWN, makeHollowCubeShape(box(2.0D, 0.0D, 2.0D, 14.0D, 14.0D, 14.0D)))
					.put(Direction.UP, makeHollowCubeShape(box(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D)))
					.put(Direction.NORTH, makeHollowCubeShape(box(2.0D, 2.0D, 0.0D, 14.0D, 14.0D, 14.0D)))
					.put(Direction.SOUTH, makeHollowCubeShape(box(2.0D, 2.0D, 2.0D, 14.0D, 14.0D, 16.0D)))
					.put(Direction.WEST, makeHollowCubeShape(box(0.0D, 2.0D, 2.0D, 14.0D, 14.0D, 14.0D)))
					.put(Direction.EAST, makeHollowCubeShape(box(2.0D, 2.0D, 2.0D, 16.0D, 14.0D, 14.0D)))
					.build());
	private static VoxelShape makeHollowCubeShape(VoxelShape cutout) {
		return Shapes.joinUnoptimized(OUT_SHAPE, cutout, BooleanOp.ONLY_FIRST).optimize();
	}
	public BasketBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.UP).setValue(WATERLOGGED, false));
	}
	@Override
	public MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return COLLISION_SHAPE_FACING.get(state.getValue(FACING));
	}
	@Override
	protected VoxelShape getOcclusionShape(BlockState state) {
		return RENDER_SHAPE;
	}
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, ENABLED, WATERLOGGED);
	}
	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (!level.isClientSide()) {
			BlockEntity tileEntity = level.getBlockEntity(pos);
			if (tileEntity instanceof BasketBlockEntity) {
				player.openMenu((BasketBlockEntity) tileEntity);
			}
		}
		return InteractionResult.SUCCESS;
	}
	@Override
	protected void affectNeighborsAfterRemoval(BlockState state, net.minecraft.server.level.ServerLevel level, BlockPos pos, boolean movedByPiston) {
		Containers.updateNeighboursAfterDestroy(state, level, pos);
	}
	@Override
	public BlockState updateShape(BlockState state, net.minecraft.world.level.LevelReader level, net.minecraft.world.level.ScheduledTickAccess scheduledTickAccess, BlockPos currentPos, Direction facing, BlockPos facingPos, BlockState facingState, net.minecraft.util.RandomSource random) {
		if (state.getValue(WATERLOGGED)) {
			scheduledTickAccess.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}
		return super.updateShape(state, level, scheduledTickAccess, currentPos, facing, facingPos, facingState, random);
	}
	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}
	// --- HOPPER STUFF ---
	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, net.minecraft.world.level.redstone.Orientation orientation, boolean isMoving) {
		boolean isPowered = !level.hasNeighborSignal(pos);
		if (isPowered != state.getValue(ENABLED)) {
			level.setBlock(pos, state.setValue(ENABLED, isPowered), 4);
		}
	}
	// --- BARREL STUFF ---
	@Override
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}
	@Override
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
		return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
	}
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
		return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
	}
	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}
	@Override
	protected boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}
	@Override
	public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
		return OUT_SHAPE;
	}
	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType type) {
		return false;
	}
	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BasketBlockEntity(pos, state);
	}
	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return level.isClientSide() ? null : createTickerHelper(blockEntityType, ModBlockEntityTypes.BASKET.get(), BasketBlockEntity::pushItemsTick);
	}
}
