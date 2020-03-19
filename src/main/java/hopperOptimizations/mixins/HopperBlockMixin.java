package hopperOptimizations.mixins;

import hopperOptimizations.annotation.Feature;
import hopperOptimizations.settings.Settings;
import hopperOptimizations.utils.IHopper;
import hopperOptimizations.workarounds.WorldInterface;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HopperBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Feature("inventoryCheckOnBlockupdate")
@Mixin(HopperBlock.class)
public class HopperBlockMixin {
    @Inject(method = "neighborUpdate(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;Z)V", at = @At(value = "HEAD"))
    private void updateBlockEntity(BlockState myBlockState, World world, BlockPos myPos, Block block, BlockPos neighborPos, boolean moved, CallbackInfo ci) {
        if (!Settings.inventoryCheckOnBlockUpdate) return;

        if (neighborPos.getY() == myPos.getY() + 1 || neighborPos.equals(myPos.offset(myBlockState.get(HopperBlock.FACING)))) {
            BlockEntity hopper = ((WorldInterface) world).getExistingBlockEntity(myPos);
            if (hopper instanceof IHopper)
                ((IHopper) hopper).onBlockUpdate();
        }
    }
}
