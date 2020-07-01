package hopperOptimizations.mixins.inventoryOptimizer.inventories;

import hopperOptimizations.utils.inventoryOptimizer.InventoryListOptimized;
import hopperOptimizations.utils.inventoryOptimizer.InventoryOptimizer;
import hopperOptimizations.utils.inventoryOptimizer.OptimizedInventory;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Arrays;

@Mixin(ShulkerBoxBlockEntity.class)
public abstract class ShulkerBoxBlockEntityMixin extends LootableContainerBlockEntity implements OptimizedInventory {
    @Shadow
    private DefaultedList<ItemStack> inventory;
    @Shadow
    private int viewerCount;

    protected ShulkerBoxBlockEntityMixin(BlockEntityType<?> beType) {
        super(beType);
    }

    //Redirects and Injects to replace the inventory with an optimized Inventory
    @Redirect(method = "<init>(Lnet/minecraft/util/DyeColor;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;ofSize(ILjava/lang/Object;)Lnet/minecraft/util/collection/DefaultedList;"))
    private DefaultedList<ItemStack> createInventory(int int_1, Object object_1) {
        return InventoryListOptimized.ofSize(int_1, (ItemStack) object_1);
    }

    @Inject(method = "setInvStackList", at = @At("RETURN"))
    private void onSetStackList(DefaultedList<ItemStack> stackList, CallbackInfo ci) {
        if (!(inventory instanceof InventoryListOptimized))
            inventory = new InventoryListOptimized(Arrays.asList((ItemStack[]) inventory.toArray()), ItemStack.EMPTY);
    }

    @Redirect(method = "deserializeInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/collection/DefaultedList;ofSize(ILjava/lang/Object;)Lnet/minecraft/util/collection/DefaultedList;"))
    private DefaultedList<ItemStack> createInventory2(int int_1, Object object_1) {
        return InventoryListOptimized.ofSize(int_1, (ItemStack) object_1);
    }

    @Override
    @Nullable
    public InventoryOptimizer getOptimizer(boolean create) {
        return (((ShulkerBoxBlockEntity) (Object) this).getClass() == ShulkerBoxBlockEntity.class) && this.world != null && !this.world.isClient && this.inventory instanceof InventoryListOptimized ? ((InventoryListOptimized) inventory).getCreateOrRemoveOptimizer(this, create) : null;
    }
}
