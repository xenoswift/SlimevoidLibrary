package net.slimevoid.library.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.slimevoid.library.items.ItemBlockBase;
import net.slimevoid.library.sounds.SlimevoidStepSound;
import net.slimevoid.library.tileentity.TileEntityBase;
import net.slimevoid.library.util.helpers.BlockHelper;

public abstract class BlockBase extends BlockContainer {

    //protected IIcon[] bottom;
    //protected IIcon[] top;
    //protected IIcon[] front;
    //protected IIcon[] side;
    protected Class[] tileEntityMap;

    protected BlockBase(Material material, int maxTiles) {
        super(material);
        this.tileEntityMap = new Class[maxTiles];
        this.setCreativeTab(this.getCreativeTab());
        this.setStepSound(new SlimevoidStepSound("blockbase", 1.0F, 1.0F));
        this.setHardness(1.0F);
    }

    //@Override
    //public void registerBlockIcons(IIconRegister iconregister) {
    //    this.registerIcons(iconregister);
    //}

    /**
     * Registers custom icons
     * 
     * @param iconRegister
     */
    //public void registerIcons(IIconRegister iconRegister) {
    //    this.bottom = this.registerBottomIcons(iconRegister);
    //    this.top = this.registerTopIcons(iconRegister);
    //    this.front = this.registerFrontIcons(iconRegister);
    //    this.side = this.registerSideIcons(iconRegister);
    //}

    //public abstract IIcon[] registerBottomIcons(IIconRegister iconRegister);

    //public abstract IIcon[] registerTopIcons(IIconRegister iconRegister);

    //public abstract IIcon[] registerFrontIcons(IIconRegister iconRegister);

    //public abstract IIcon[] registerSideIcons(IIconRegister iconRegister);

    //@Override
    //public IIcon getIcon(int side, int metadata) {
    //    switch (side) {
    //    case 0:
    //        return this.bottom != null && metadata < this.tileEntityMap.length ? this.bottom[metadata] : this.blockIcon;
    //    case 1:
    //        return this.top != null && metadata < this.tileEntityMap.length ? this.top[metadata] : this.blockIcon;
    //    case 3:
    //        return this.front != null && metadata < this.tileEntityMap.length ? this.front[metadata] : this.blockIcon;
    //    default:
    //        return this.side != null && metadata < this.tileEntityMap.length ? this.side[metadata] : this.blockIcon;
    //    }
    //}

    public abstract CreativeTabs getCreativeTab();

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    //@Override
    //public boolean renderAsNormalBlock() {
    //    return false;
    //}

    public boolean isCube() {
        return false;
    }

    @Override
    public int damageDropped(IBlockState blockstate) {
        return blockstate.hashCode();
    }

    @Override
    public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        IBlockState blockState = world.getBlockState(pos);
        
        TileEntityBase tileentitybase = (TileEntityBase) BlockHelper.getTileEntity(world,
                                                                                   pos,
                                                                                   this.getTileMapData(blockState));
        if (tileentitybase != null) {
            return tileentitybase.removeBlockByPlayer(player,
                                                      this,
                                                      willHarvest);
        } else {
            return super.removedByPlayer(world,
            							 pos,
                                         player,
                                         willHarvest);
        }
    }

    public boolean superRemoveBlockByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        return super.removedByPlayer(world,
                					 pos,
                                     player,
                                     willHarvest);
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos pos) {
        TileEntityBase tileentitybase = (TileEntityBase) BlockHelper.getTileEntity(world,
                                                                                   pos,
                                                                                   this.getTileMapData(world.getBlockState(pos)));
        if (tileentitybase != null) {
            return tileentitybase.getPickBlock(target,
                                               this);
        } else {
            return super.getPickBlock(target,
                                      world,
                                      pos);
        }
    }

    public final ItemStack superGetPickBlock(MovingObjectPosition target, World world, BlockPos pos) {
        return super.getPickBlock(target,
                                  world,
                                  pos);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, BlockPos pos) {
        IBlockState state = iblockaccess.getBlockState(pos);
        TileEntityBase tileentitybase = (TileEntityBase) BlockHelper.getTileEntity(iblockaccess,
                                                                                   pos,
                                                                                   this.getTileMapData(state));
        if (tileentitybase != null) {
            tileentitybase.setBlockBoundsBasedOnState(this);
        } else {
            super.setBlockBoundsBasedOnState(iblockaccess,
                                             pos);
        }
    }

    /**
    public void setBlockBoundsForItemRender(int metadata) {
        TileEntityBase tileentitybase = (TileEntityBase) this.createTileEntity(null,
                                                                               metadata);
        if (tileentitybase != null) {
            tileentitybase.setBlockBoundsForItemRender(this);
        }
    }**/

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.0F,
                            0.0F,
                            0.0F,
                            1.0F,
                            1.0F,
                            1.0F);
    }

    @Override
    public MovingObjectPosition collisionRayTrace(World world, BlockPos pos, Vec3 startVec, Vec3 endVec) {
        IBlockState blockState = world.getBlockState(pos);
        TileEntityBase tileentitybase = (TileEntityBase) BlockHelper.getTileEntity(world,
                                                                                   pos,
                                                                                   this.getTileMapData(blockState));
        if (tileentitybase != null) {
            return tileentitybase.collisionRayTrace(this,
                                                    startVec,
                                                    endVec);
        } else {
            return super.collisionRayTrace(world,
                                           pos,
                                           startVec,
                                           endVec);
        }
    }

    @Override
    public void addCollisionBoxesToList(World world, BlockPos pos, IBlockState blockState, AxisAlignedBB axisAlignedBB, List aList, Entity anEntity) {
        TileEntityBase tileentitybase = (TileEntityBase) BlockHelper.getTileEntity(world,
                                                                                   pos,
                                                                                   this.getTileMapData(blockState));
        if (tileentitybase != null) {
            tileentitybase.addCollisionBoxesToList(this,
            									   blockState,
                                                   axisAlignedBB,
                                                   aList,
                                                   anEntity);
        } else {
            super.addCollisionBoxesToList(world,
                                          pos,
                                          blockState,
                                          axisAlignedBB,
                                          aList,
                                          anEntity);
        }
    }

    public final void superSetBlockBoundsBasedOnState(World worldObj, BlockPos pos) {
        this.setBlockBounds(0.0F,
                            0.0F,
                            0.0F,
                            1.0F,
                            1.0F,
                            1.0F);
    }

    public final MovingObjectPosition superCollisionRayTrace(World world, BlockPos pos, Vec3 startVec, Vec3 endVec) {
        return super.collisionRayTrace(world,
                                       pos,
                                       startVec,
                                       endVec);
    }

    public final void superAddCollisionBoxesToList(World world, BlockPos pos, IBlockState blockState, AxisAlignedBB axisAlignedBB, List aList, Entity anEntity) {
        super.addCollisionBoxesToList(world,
                                      pos,
                                      blockState,
                                      axisAlignedBB,
                                      aList,
                                      anEntity);
    }

    @Override
    public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
        IBlockState blockState = world.getBlockState(pos);
        TileEntityBase tileentitybase = (TileEntityBase) BlockHelper.getTileEntity(world,
                                                                                   pos,
                                                                                   this.getTileMapData(blockState));
        if (tileentitybase != null) {
            return tileentitybase.isSideSolid(this,
                                              side);
        } else {
            return super.isSideSolid(world,
                                     pos,
                                     side);
        }
    }

    public boolean superSideSolid(World world, BlockPos pos, EnumFacing side) {
        return super.isSideSolid(world,
                                 pos,
                                 side);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        IBlockState blockState = world.getBlockState(pos);
        TileEntityBase tileentitybase = (TileEntityBase) BlockHelper.getTileEntity(world,
                                                                                   pos,
                                                                                   this.getTileMapData(blockState));
        if (tileentitybase != null) {
            return tileentitybase.getExplosionResistance(this,
            											 exploder,
                                                         explosion);
        } else {
            return super.getExplosionResistance(world,
                                                pos,
                                                exploder,
                                                explosion);
        }
    }

    public final float superGetExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
        return super.getExplosionResistance(world,
                                            pos,
                                            exploder,
                                            explosion);
    }

    @Override
    public float getBlockHardness(World world, BlockPos pos) {
        IBlockState blockState = world.getBlockState(pos);
        TileEntityBase tileentitybase = (TileEntityBase) BlockHelper.getTileEntity(world,
                                                                                   pos,
                                                                                   this.getTileMapData(blockState));
        if (tileentitybase != null) {
            return tileentitybase.getBlockHardness(this);
        } else {
            return super.getBlockHardness(world,
                                          pos);
        }
    }

    public float superBlockHardness(World world, BlockPos pos) {
        return super.getBlockHardness(world,
                                      pos);
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer entityplayer, World world, BlockPos pos) {
        IBlockState blockState = world.getBlockState(pos);
        TileEntityBase tileentitybase = (TileEntityBase) BlockHelper.getTileEntity(world,
                                                                                   pos,
                                                                                   this.getTileMapData(blockState));
        if (tileentitybase != null) {
            return tileentitybase.getPlayerRelativeBlockHardness(entityplayer,
                                                                 blockState);
        } else {
            return super.getPlayerRelativeBlockHardness(entityplayer,
                                                        world,
                                                        pos);
        }
    }

    @Override
    public int colorMultiplier(IBlockAccess iblockaccess, BlockPos pos, int renderPass) {
        IBlockState blockState = iblockaccess.getBlockState(pos);
        TileEntityBase tileentitybase = (TileEntityBase) BlockHelper.getTileEntity(iblockaccess,
                                                                                   pos,
                                                                                   this.getTileMapData(blockState));
        if (tileentitybase != null) {
            return tileentitybase.colorMultiplier(this, renderPass);
        } else {
            return super.colorMultiplier(iblockaccess,
                                         pos,
                                         renderPass);
        }
    }

    public int superColorMultiplier(World world, BlockPos pos, int renderPass) {
        return super.colorMultiplier(world,
                                     pos,
                                     renderPass);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState blockState, int fortune) {
        ArrayList<ItemStack> harvestList = new ArrayList<ItemStack>();
        harvestList.add(new ItemStack(this, 1, blockState.getBlock().getMetaFromState(blockState)));
        return harvestList;
    }

    @Override
    public int quantityDroppedWithBonus(int i, Random random) {
        return 0;
    }

    @Override
    public void onNeighborChange(IBlockAccess iblockaccess, BlockPos pos, BlockPos posNeighbor) {
        IBlockState blockState = iblockaccess.getBlockState(pos);
        TileEntityBase tileentitybase = (TileEntityBase) BlockHelper.getTileEntity(iblockaccess,
                                                                                   pos,
                                                                                   this.getTileMapData(blockState));
        if (tileentitybase != null) {
            tileentitybase.onNeighborChange(posNeighbor);
        } else {
            ((World) iblockaccess).setBlockToAir(pos);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState blockState, EntityLivingBase entityplayer, ItemStack itemstack) {
        TileEntityBase tileentitybase = (TileEntityBase) BlockHelper.getTileEntity(world,
                                                                                   pos,
                                                                                   this.getTileMapData(blockState));
        if (tileentitybase != null) {
            tileentitybase.onBlockPlacedBy(itemstack,
                                           entityplayer);
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState blockState) {
        TileEntityBase tileentity = (TileEntityBase) BlockHelper.getTileEntity(world,
                                                                               pos,
                                                                               this.getTileMapData(blockState));
        if (tileentity != null) {
            tileentity.breakBlock(blockState);
        }
        super.breakBlock(world,
                         pos,
                         blockState);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState blockState, EntityPlayer entityplayer, EnumFacing side, float xHit, float yHit, float zHit) {
        TileEntityBase tileentity = (TileEntityBase) BlockHelper.getTileEntity(world,
                                                                               pos,
                                                                               this.getTileMapData(blockState));
        if (tileentity != null) {
            return tileentity.onBlockActivated(blockState, entityplayer, side, xHit, yHit, zHit);
        } else {
            return false;
        }
    }

    /**@Override
    public IIcon getIcon(IBlockAccess iblockaccess, int x, int y, int z, int side) {
        int metadata = iblockaccess.getBlockMetadata(x,
                                                     y,
                                                     z);
        TileEntityBase tileentity = (TileEntityBase) BlockHelper.getTileEntity(iblockaccess,
                                                                               x,
                                                                               y,
                                                                               z,
                                                                               this.getTileMapData(metadata));
        if (tileentity != null) {
            return tileentity.getBlockTexture(x,
                                              y,
                                              z,
                                              metadata,
                                              side);
        } else {
            return this.getIcon(side,
                                metadata);
        }
    }**/

    @Override
    public int getLightValue(IBlockAccess iblockaccess, BlockPos pos) {
        IBlockState blockState = iblockaccess.getBlockState(pos);
        TileEntityBase tileentity = (TileEntityBase) BlockHelper.getTileEntity(iblockaccess,
                                                                               pos,
                                                                               this.getTileMapData(blockState));
        if (tileentity != null) {
            return tileentity.getLightValue();
        } else {
            return super.getLightValue(iblockaccess,
                                       pos);
        }
    }

    public boolean superDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer) {
        return super.addDestroyEffects(world,
                                       pos,
                                       effectRenderer);
    }

    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, EffectRenderer effectRenderer) {
        IBlockState blockState = world.getBlockState(pos);
        TileEntityBase tileentity = (TileEntityBase) BlockHelper.getTileEntity(world,
                                                                               pos,
                                                                               this.getTileMapData(blockState));
        if (tileentity != null) {
            return tileentity.addBlockDestroyEffects(this,
                                                     effectRenderer);
        } else {
            return super.addDestroyEffects(world,
                                           pos,
                                           effectRenderer);
        }
    }

    public boolean superHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer) {
        return super.addHitEffects(world,
                                   target,
                                   effectRenderer);
    }

    @Override
    public boolean addHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer) {
        BlockPos pos = target.func_178782_a();
        IBlockState blockState = world.getBlockState(pos);
        TileEntityBase tileentity = (TileEntityBase) BlockHelper.getTileEntity(world,
                                                                               pos,
                                                                               this.getTileMapData(blockState));
        if (tileentity != null) {
            return tileentity.addBlockHitEffects(this,
                                                 target,
                                                 effectRenderer);
        } else {
            return super.addHitEffects(world,
                                       target,
                                       effectRenderer);
        }
    }

    public Class getTileMapData(IBlockState blockState) {
    	int metadata = blockState.getBlock().getMetaFromState(blockState);
        if (metadata < this.tileEntityMap.length) {
            return this.tileEntityMap[metadata];
        } else {
            return null;
        }
    }

    public void addMapping(int metadata, Class<? extends TileEntity> tileEntityClass, String unlocalizedName) {
        this.tileEntityMap[metadata] = tileEntityClass;
        GameRegistry.registerTileEntity(tileEntityClass,
                                        unlocalizedName);
        this.setItemName(metadata,
                         unlocalizedName);
    }

    public void setItemName(int metadata, String name) {
        Item item = Item.getItemFromBlock(this);
        if (item != null) {
            ((ItemBlockBase) item).setMetaName(metadata,
                                               (new StringBuilder()).append("tile.").append(name).toString());
        }
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState blockState) {
        try {
            return (TileEntity) this.getTileMapData(blockState).newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}
