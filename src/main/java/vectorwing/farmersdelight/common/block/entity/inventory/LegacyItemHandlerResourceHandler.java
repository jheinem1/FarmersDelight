package vectorwing.farmersdelight.common.block.entity.inventory;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

/**
 * Bridge the deprecated item handler API onto NeoForge's item transfer capability
 * while the backing block entities are still being migrated to container-backed storage.
 */
@SuppressWarnings("removal")
public class LegacyItemHandlerResourceHandler implements ResourceHandler<ItemResource>
{
	private final IItemHandler itemHandler;

	public LegacyItemHandlerResourceHandler(IItemHandler itemHandler) {
		this.itemHandler = itemHandler;
	}

	@Override
	public int size() {
		return this.itemHandler.getSlots();
	}

	@Override
	public ItemResource getResource(int index) {
		return ItemResource.of(this.itemHandler.getStackInSlot(index));
	}

	@Override
	public long getAmountAsLong(int index) {
		return this.itemHandler.getStackInSlot(index).getCount();
	}

	@Override
	public long getCapacityAsLong(int index, ItemResource resource) {
		ItemStack stack = resource.toStack();
		return Math.min(this.itemHandler.getSlotLimit(index), stack.getMaxStackSize());
	}

	@Override
	public boolean isValid(int index, ItemResource resource) {
		return this.itemHandler.isItemValid(index, resource.toStack());
	}

	@Override
	public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
		ItemStack remaining = this.itemHandler.insertItem(index, resource.toStack(amount), false);
		return amount - remaining.getCount();
	}

	@Override
	public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
		return this.itemHandler.extractItem(index, amount, false).getCount();
	}
}
