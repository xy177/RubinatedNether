package net.artienia.rubinated_nether;

import net.artienia.rubinated_nether.content.RNBlocks;
import net.artienia.rubinated_nether.content.RNParticleTypes;
import net.artienia.rubinated_nether.content.RNTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;

public final class RNEvents {

    private RNEvents() {}

    public static InteractionResult interactBlock(Player player, Level level, InteractionHand hand, BlockHitResult hit) {
        BlockPos pos = hit.getBlockPos();
        ItemStack stack = player.getItemInHand(hand);
        if(stack.is(RNTags.Items.OBSIDIAN_CONVERTERS) && level.getBlockState(pos).is(Blocks.CRYING_OBSIDIAN)) {
            // TODO: Figure out sound and particles
            level.setBlockAndUpdate(pos, RNBlocks.BLEEDING_OBSIDIAN.getDefaultState());
            level.playLocalSound(pos, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 1.0f, 0.4f, true);

            ParticleUtils.spawnParticlesOnBlockFaces(level, pos, RNParticleTypes.RUBY_AURA.get(), ConstantInt.of(6));

            if(!player.getAbilities().instabuild) stack.shrink(1);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }
}
