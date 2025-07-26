package com.hbm.tileentity.machine;

import api.hbm.energymk2.IEnergyProviderMK2;
import com.hbm.tileentity.TileEntityLoadedBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

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
		if (isAbleToProvidePower()) {
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

			if (isAbleToProvidePower()) {
				power = 0;
				power += 100;

				if (power > maxPower) {
					power = maxPower;
				}
			}
		}
	}

	public boolean isAbleToProvidePower() {
		int y = yCoord;
		if (y > 70 || !worldObj.canBlockSeeTheSky(xCoord, yCoord + 1, zCoord)){
			return false;
		}
		return true;
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
