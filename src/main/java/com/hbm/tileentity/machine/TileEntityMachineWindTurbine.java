package com.hbm.tileentity.machine;

import api.hbm.energymk2.IEnergyProviderMK2;
import com.hbm.dim.CelestialBody;
import com.hbm.dim.orbit.WorldProviderOrbit;
import com.hbm.dim.trait.CBT_Atmosphere;
import com.hbm.handler.atmosphere.AtmosphereBlob;
import com.hbm.handler.atmosphere.ChunkAtmosphereManager;
import com.hbm.inventory.fluid.Fluids;
import com.hbm.tileentity.TileEntityLoadedBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class TileEntityMachineWindTurbine extends TileEntityLoadedBase implements IEnergyProviderMK2 {
	private long power;
	private long maxPower = 1000;
	public float spin;
	public float prevSpin;

	@Override
	public long getPower() {
		return power;
	}

	@Override
	public void setPower(long power) {
		this.power = power;
	}

	@Override
	public long getMaxPower() {
		return maxPower;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return TileEntity.INFINITE_EXTENT_AABB;
	}

	@Override
	public void updateEntity() {
		this.prevSpin = this.spin;
		if (isAbleToProvidePower() == ErrorCode.SUCCESS) {
			this.spin += 6.0F;
			if(this.spin >= 360.0F) {
				this.prevSpin -= 360.0F;
				this.spin -= 360.0F;
			}
		}
		if (!worldObj.isRemote) {

			for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				tryProvide(worldObj, xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ, dir);
			}

			if (isAbleToProvidePower() == ErrorCode.SUCCESS) {
				power = 0;
				power += 100;
				if (power > maxPower) {
					power = maxPower;
				}
			}
		}
	}

	public enum ErrorCode{
		TOO_HIGH,
		VACUUM,
		SKY_BLOCKED,
		SUCCESS
	}
	public ErrorCode isAbleToProvidePower(){
		int amount = 0;
		if (yCoord < 70){
			return ErrorCode.TOO_HIGH;
		}
		if (!breatheAir(worldObj, xCoord, yCoord, zCoord, amount)){
			return ErrorCode.VACUUM;
		}
		if (!worldObj.canBlockSeeTheSky(xCoord, yCoord + 1, zCoord)){
			return ErrorCode.SKY_BLOCKED;
		}
		return ErrorCode.SUCCESS;
	}

	public static boolean breatheAir(World world, int x, int y, int z, int amount) {
		CBT_Atmosphere atmosphere = world.provider instanceof WorldProviderOrbit ? null : CelestialBody.getTrait(world, CBT_Atmosphere.class);
		if(atmosphere != null) {
			if(atmosphere.hasFluid(Fluids.AIR, 0.19) || atmosphere.hasFluid(Fluids.OXYGEN, 0.09)) {
				return true;
			}
		}
		List<AtmosphereBlob> blobs = ChunkAtmosphereManager.proxy.getBlobs(world, x, y, z);
		for(AtmosphereBlob blob : blobs) {
			if(blob.hasFluid(Fluids.AIR, 0.19) || blob.hasFluid(Fluids.OXYGEN, 0.09)) {
				blob.consume(amount);
				return true;
			}
		}
		return false;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.power = nbt.getLong("power");
		this.maxPower = nbt.getLong("maxPower");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setLong("power", power);
		nbt.setLong("maxPower", maxPower);
	}
}
