package com.toma.pubgmc.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockWindow extends PMCBlock {

    public static final PropertyEnum<EnumWindowAxis> AXIS = PropertyEnum.create("axis", EnumWindowAxis.class);
    public static final PropertyEnum<EnumWindowPart> PART = PropertyEnum.create("part", EnumWindowPart.class);
    public static final PropertyBool BROKEN = PropertyBool.create("broken");
    private static final AxisAlignedBB[] BOUNDING_BOX = {new AxisAlignedBB(0, 0, 0.35, 1, 1, 0.65),new AxisAlignedBB(0.35, 0, 0, 0.65, 1, 1)};
    private static final AxisAlignedBB[] COLLISION_BOX = {new AxisAlignedBB(0, 0, 0.4, 1, 1, 0.6),new AxisAlignedBB(0.4, 0, 0, 0.6, 1, 1)};
    private final WindowType windowType;

    public BlockWindow(String name, WindowType windowType) {
        super(name, Material.GLASS);
        this.setSoundType(SoundType.GLASS);
        this.setHardness(0.75f);
        this.windowType = windowType;
        this.setDefaultState(blockState.getBaseState().withProperty(BROKEN, false).withProperty(AXIS, EnumWindowAxis.NS).withProperty(PART, EnumWindowPart.LOWER_LEFT));
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if(!state.getValue(BROKEN)) {
            worldIn.setBlockState(pos, state.withProperty(BROKEN, true));
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDING_BOX[state.getValue(AXIS).ordinal()];
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        if (blockState.getValue(BROKEN)) {
            return NULL_AABB;
        }
        return COLLISION_BOX[blockState.getValue(AXIS).ordinal()];
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        switch(windowType) {
            case WINDOW_1X2: {
                EnumWindowPart part = state.getValue(PART);
                if (part == EnumWindowPart.LOWER_LEFT) {
                    worldIn.setBlockToAir(pos.up());
                } else {
                    worldIn.setBlockToAir(pos.down());
                }
                break;
            }
            case WINDOW_2X1: {
                if(state.getValue(AXIS) == EnumWindowAxis.NS) {
                    IBlockState state1 = worldIn.getBlockState(pos.offset(EnumFacing.WEST));
                    if(state1.getBlock() instanceof BlockWindow && state1.getValue(PART) == state.getValue(PART).getOpposite()) {
                        worldIn.setBlockToAir(pos.offset(EnumFacing.WEST));
                    } else {
                        worldIn.setBlockToAir(pos.offset(EnumFacing.EAST));
                    }
                } else {
                    IBlockState state1 = worldIn.getBlockState(pos.offset(EnumFacing.NORTH));
                    if(state1.getBlock() instanceof BlockWindow && state1.getValue(PART) == state.getValue(PART).getOpposite()) {
                        worldIn.setBlockToAir(pos.offset(EnumFacing.NORTH));
                    } else {
                        worldIn.setBlockToAir(pos.offset(EnumFacing.SOUTH));
                    }
                }
                break;
            }
            case WINDOW_2X2: {
                if(state.getValue(PART).isLower()) {
                    worldIn.setBlockToAir(pos.up());
                    if(state.getValue(AXIS) == EnumWindowAxis.NS) {
                        IBlockState state1 = worldIn.getBlockState(pos.offset(EnumFacing.WEST));
                        if(state1.getBlock() instanceof BlockWindow && state1.getValue(PART) == state.getValue(PART).getOpposite()) {
                            worldIn.setBlockToAir(pos.offset(EnumFacing.WEST));
                            worldIn.setBlockToAir(pos.offset(EnumFacing.WEST).up());
                        } else {
                            worldIn.setBlockToAir(pos.offset(EnumFacing.EAST));
                            worldIn.setBlockToAir(pos.offset(EnumFacing.EAST).up());
                        }
                    } else {
                        IBlockState state1 = worldIn.getBlockState(pos.offset(EnumFacing.NORTH));
                        if(state1.getBlock() instanceof BlockWindow && state1.getValue(PART) == state.getValue(PART).getOpposite()) {
                            worldIn.setBlockToAir(pos.offset(EnumFacing.NORTH));
                            worldIn.setBlockToAir(pos.offset(EnumFacing.NORTH).up());
                        } else {
                            worldIn.setBlockToAir(pos.offset(EnumFacing.SOUTH));
                            worldIn.setBlockToAir(pos.offset(EnumFacing.SOUTH).up());
                        }
                    }
                } else {
                    worldIn.setBlockToAir(pos.down());
                    if(state.getValue(AXIS) == EnumWindowAxis.NS) {
                        IBlockState state1 = worldIn.getBlockState(pos.offset(EnumFacing.WEST));
                        if(state1.getBlock() instanceof BlockWindow && state1.getValue(PART) == state.getValue(PART).getOpposite()) {
                            worldIn.setBlockToAir(pos.offset(EnumFacing.WEST));
                            worldIn.setBlockToAir(pos.offset(EnumFacing.WEST).down());
                        } else {
                            worldIn.setBlockToAir(pos.offset(EnumFacing.EAST));
                            worldIn.setBlockToAir(pos.offset(EnumFacing.EAST).down());
                        }
                    } else {
                        IBlockState state1 = worldIn.getBlockState(pos.offset(EnumFacing.NORTH));
                        if(state1.getBlock() instanceof BlockWindow && state1.getValue(PART) == state.getValue(PART).getOpposite()) {
                            worldIn.setBlockToAir(pos.offset(EnumFacing.NORTH));
                            worldIn.setBlockToAir(pos.offset(EnumFacing.NORTH).down());
                        } else {
                            worldIn.setBlockToAir(pos.offset(EnumFacing.SOUTH));
                            worldIn.setBlockToAir(pos.offset(EnumFacing.SOUTH).down());
                        }
                    }
                }
                break;
            }
            default: case WINDOW_1X1: {
                super.breakBlock(worldIn, pos, state);
                break;
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        EnumFacing f = placer.getHorizontalFacing();
        IBlockState iBlockState = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(AXIS, EnumWindowAxis.getAxisFromFacing(f)).withProperty(PART, EnumWindowPart.LOWER_LEFT);
        switch (windowType) {
            case WINDOW_1X1: {
                return iBlockState;
            }
            case WINDOW_1X2: {
                if (world.getBlockState(pos.up()).getBlock().isReplaceable(world, pos.up())) {
                    world.setBlockState(pos.up(), iBlockState.withProperty(PART, EnumWindowPart.UPPER_LEFT));
                    return iBlockState;
                }
                return Blocks.AIR.getDefaultState();
            }
            case WINDOW_2X1: {
                BlockPos neighbor = pos.offset(f.rotateY());
                if(world.getBlockState(neighbor).getBlock().isReplaceable(world, neighbor)) {
                    world.setBlockState(neighbor, iBlockState.withProperty(PART, EnumWindowPart.LOWER_RIGHT));
                    return iBlockState;
                }
                return Blocks.AIR.getDefaultState();
            }
            case WINDOW_2X2: {
                BlockPos neighbor = pos.offset(f.rotateY());
                IBlockState lr = world.getBlockState(neighbor);
                IBlockState ul = world.getBlockState(pos.up());
                IBlockState ur = world.getBlockState(neighbor.up());
                if(world.getBlockState(pos.up()).getBlock().isReplaceable(world, pos.up()) &&
                    world.getBlockState(neighbor).getBlock().isReplaceable(world, neighbor) &&
                    world.getBlockState(neighbor.up()).getBlock().isReplaceable(world, neighbor.up())) {
                    world.setBlockState(pos.up(), iBlockState.withProperty(PART, EnumWindowPart.UPPER_LEFT));
                    world.setBlockState(neighbor, iBlockState.withProperty(PART, EnumWindowPart.LOWER_RIGHT));
                    world.setBlockState(neighbor.up(), iBlockState.withProperty(PART, EnumWindowPart.UPPER_RIGHT));
                    return iBlockState;
                }
                return Blocks.AIR.getDefaultState();
            }
            default: return Blocks.AIR.getDefaultState();
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = state.getValue(PART).ordinal();
        meta = state.getValue(BROKEN) ? meta | 4 : meta;
        meta |= state.getValue(AXIS).ordinal() << 3;
        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(PART, EnumWindowPart.values()[meta%4]).withProperty(BROKEN, (meta & 4) > 0).withProperty(AXIS, EnumWindowAxis.values()[meta >> 3]);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PART, AXIS, BROKEN);
    }

    public enum WindowType {
        WINDOW_1X1,
        WINDOW_1X2,
        WINDOW_2X1,
        WINDOW_2X2
    }

    public enum EnumWindowAxis implements IStringSerializable {
        NS,
        WE;

        @Override
        public String getName() {
            return this.name().toLowerCase();
        }

        public static EnumWindowAxis getAxisFromFacing(EnumFacing facing) {
            switch (facing) {
                case EAST: case WEST: return WE;
                case NORTH: case SOUTH: default: return NS;
            }
        }
    }

    public enum EnumWindowPart implements IStringSerializable {
        UPPER_RIGHT,
        UPPER_LEFT,
        LOWER_RIGHT,
        LOWER_LEFT;

        @Override
        public String getName() {
            return this.name().toLowerCase();
        }

        public boolean isLower() {
            return this.ordinal() > 1;
        }

        public EnumWindowPart getOpposite() {
            return getOpposite(this);
        }

        public static EnumWindowPart getOpposite(EnumWindowPart part) {
            switch (part) {
                case LOWER_LEFT: return LOWER_RIGHT;
                case LOWER_RIGHT: default: return LOWER_LEFT;
                case UPPER_LEFT: return UPPER_RIGHT;
                case UPPER_RIGHT: return UPPER_LEFT;
            }
        }
    }
}