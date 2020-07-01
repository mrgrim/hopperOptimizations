package hopperOptimizations.mixins.cacheInventories.cacheInventoryBlocks;

import hopperOptimizations.features.cacheInventories.IValidInventoryUntilBlockUpdate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.block.ComposterBlock$FullComposterInventory")
public class ComposterBlockFullComposterInventoryMixin implements IValidInventoryUntilBlockUpdate {
    @Shadow
    private boolean dirty;

    @Override
    public boolean isValid() {
        return !dirty;
    }
}