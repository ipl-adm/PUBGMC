package com.toma.pubgmc.common;

import com.toma.pubgmc.common.blocks.PMCBlockHorizontal;
import com.toma.pubgmc.util.IBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class HorizontalBlockBuilder implements IBuilder<PMCBlockHorizontal>
{
	private String name;
	private Material material;
	private SoundType soundType;
	private BlockRenderLayer renderLayer;
	private MapColor mapColor;
	private boolean opaque, fullCube;
	private int lightValue;
	private AxisAlignedBB[] boxes;
	private BlockFaceShape faceShape;
	
	private HorizontalBlockBuilder() {}
	
	private void initialize()
	{
		renderLayer = BlockRenderLayer.SOLID;
		opaque = true;
		fullCube = true;
		boxes = new AxisAlignedBB[] {Block.FULL_BLOCK_AABB, Block.FULL_BLOCK_AABB};
		faceShape = BlockFaceShape.UNDEFINED;
		lightValue = 0;
	}
	
	public static HorizontalBlockBuilder create(String name, Material material)
	{
		HorizontalBlockBuilder builder = new HorizontalBlockBuilder();
		builder.name = name;
		builder.material = material;
		builder.initialize();
		return builder;
	}
	
	public HorizontalBlockBuilder renderType(BlockRenderLayer layerRender)
	{
		this.renderLayer = layerRender;
		return this;
	}
	
	public HorizontalBlockBuilder mapColor(MapColor color)
	{
		this.mapColor = color;
		return this;
	}
	
	public HorizontalBlockBuilder transparency(boolean opaque, boolean fullCube)
	{
		this.opaque = opaque;
		this.fullCube = fullCube;
		return this;
	}
	
	public HorizontalBlockBuilder light(int light)
	{
		this.lightValue = light;
		return this;
	}
	
	public HorizontalBlockBuilder aabb(AxisAlignedBB aabb)
	{
		this.boxes = new AxisAlignedBB[] {aabb, aabb};
		return this;
	}
	
	public HorizontalBlockBuilder aabb(AxisAlignedBB bounding, AxisAlignedBB collision)
	{
		this.boxes = new AxisAlignedBB[] {bounding, collision};
		return this;
	}
	
	/**
	 * 0 - S; 1 - W; 2 - N; 3 - E
	 */
	public HorizontalBlockBuilder aabb(AxisAlignedBB... horizontalBoxes)
	{
		this.boxes = horizontalBoxes;
		return this;
	}
	
	public HorizontalBlockBuilder soundType(SoundType type)
	{
		this.soundType = type;
		return this;
	}
	
	public HorizontalBlockBuilder faceShape(BlockFaceShape shape)
	{
		this.faceShape = shape;
		return this;
	}
	
	public HorizontalBlockBuilder setTransparent()
	{
		opaque = false;
		fullCube = false;
		return this;
	}
	
	public HorizontalBlockBuilder setProp()
	{
		aabb(Block.FULL_BLOCK_AABB, Block.NULL_AABB);
		setTransparent();
		renderLayer = BlockRenderLayer.CUTOUT;
		
		return this;
	}
	
	public HorizontalBlockBuilder setPassable()
	{
		aabb(Block.FULL_BLOCK_AABB, Block.NULL_AABB);
		return this;
	}
	
	@Override
	public PMCBlockHorizontal build()
	{
		checkNotNull(material);
		checkInt(lightValue, 0, 15);
		checkNotNull(soundType);
		
		PMCBlockHorizontal builtBlock = new PMCBlockHorizontal(name, material)
		{
			@Override
			public boolean isOpaqueCube(IBlockState state)
			{
				return opaque;
			}
			
			@Override
			public boolean isFullCube(IBlockState state)
			{
				return fullCube;
			}
			
			@Override
			public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
			{
				return mapColor;
			}
			
			@Override
			public BlockRenderLayer getBlockLayer()
			{
				return renderLayer;
			}
			
			@Override
			public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) 
			{
				return boxes.length == 4 ? boxes[getBoundingBoxFromRotation(state)] : boxes[0];
			}
			
			@Override
			public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos)
			{
				return boxes.length == 4 ? boxes[getBoundingBoxFromRotation(state)] : boxes[1];
			}
			
			@Override
			public SoundType getSoundType(IBlockState state, World world, BlockPos pos, Entity entity)
			{
				return soundType;
			}
			
			@Override
			public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
			{
				return faceShape;
			}
		};
		
		return builtBlock;
	}
}