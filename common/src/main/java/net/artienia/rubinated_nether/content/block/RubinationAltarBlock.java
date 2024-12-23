//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.artienia.rubinated_nether.content.block;

import java.util.Iterator;
import java.util.List;

import net.artienia.rubinated_nether.content.RNBlockEntities;
import net.artienia.rubinated_nether.content.RNBlocks;
import net.artienia.rubinated_nether.content.RNParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import uwu.serenity.critter.utils.BEBlock;

public class RubinationAltarBlock extends BaseEntityBlock implements BEBlock<RubinationAltarBlockEntity> {

	protected static final VoxelShape SHAPE_BOTTOM = Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);
	protected static final VoxelShape SHAPE_TOP = Block.box(0.0, 4.0, 0.0, 16.0, 14.0, 16.0);
	protected static final VoxelShape SHAPE = Shapes.or(SHAPE_BOTTOM, SHAPE_TOP);

	public static final List<BlockPos> RUNESTONE_OFFSETS = BlockPos.betweenClosedStream(-3, 0, -3, 3, 1, 3).filter((blockPos) -> {
		return Math.abs(blockPos.getX()) > 1 || Math.abs(blockPos.getZ()) > 1;
	}).map(BlockPos::immutable).toList();


	public RubinationAltarBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	public static boolean isValidBookShelf(Level level, BlockPos tablePos, BlockPos offsetPos) {
		return level.getBlockState(tablePos.offset(offsetPos)).is(RNBlocks.RUNESTONE.get()) && level.getBlockState(tablePos.offset(offsetPos.getX() / 2, offsetPos.getY(), offsetPos.getZ() / 2)).is(BlockTags.ENCHANTMENT_POWER_TRANSMITTER);
	}

	public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("tooltip.rubinated_nether.wip.tooltip"));
		super.appendHoverText(stack, level, tooltip, flag);
	}

	public boolean useShapeForLightOcclusion(BlockState state) {
		return false;
	}

	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		super.animateTick(state, level, pos, random);
		Iterator<BlockPos> var5 = RUNESTONE_OFFSETS.iterator();

		while(var5.hasNext()) {
			BlockPos blockPos = (BlockPos)var5.next();
			if (random.nextInt(1) == 0 && isValidBookShelf(level, pos, blockPos)) {
				level.addParticle(RNParticleTypes.RUBINATE.get(), (double)pos.getX() + 0.5, (double)pos.getY() + 2.0, (double)pos.getZ() + 0.5, (double)((float)blockPos.getX() + random.nextFloat()) - 0.5, (double)((float)blockPos.getY() - random.nextFloat() - 0.5F), (double)((float)blockPos.getZ() + random.nextFloat()) - 0.5);
			}
		}

	}

	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new EnchantmentTableBlockEntity(pos, state);
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return level.isClientSide ? createTickerHelper(blockEntityType, BlockEntityType.ENCHANTING_TABLE, EnchantmentTableBlockEntity::bookAnimationTick) : null;
	}

	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			player.openMenu(state.getMenuProvider(level, pos));
			return InteractionResult.CONSUME;
		}
	}

	@Nullable
	public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		BlockEntity blockEntity = level.getBlockEntity(pos);

		Component component = ((Nameable)blockEntity).getDisplayName();
		return new SimpleMenuProvider((i, inventory, player) -> {
			return new EnchantmentMenu(i, inventory, ContainerLevelAccess.create(level, pos));
		},
				component);
	}

	public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
		return false;
	}

	@Override
	public BlockEntityType<? extends RubinationAltarBlockEntity> getBlockEntityType() {
		return RNBlockEntities.RUBINATION_ALTAR.get();
	}

	@Override
	public Class<? extends RubinationAltarBlockEntity> getBlockEntityClass() {
		return RubinationAltarBlockEntity.class;
	}
}
