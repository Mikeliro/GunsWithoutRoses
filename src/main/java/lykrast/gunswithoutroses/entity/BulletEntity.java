package lykrast.gunswithoutroses.entity;

import lykrast.gunswithoutroses.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BulletEntity extends AbstractFireballEntity {
	private double damage = 1;
	private boolean ignoreInvulnerability = false;

	public BulletEntity(EntityType<? extends BulletEntity> p_i50160_1_, World p_i50160_2_) {
		super(p_i50160_1_, p_i50160_2_);
	}

	public BulletEntity(World worldIn, LivingEntity shooter) {
		this(worldIn, shooter, 0, 0, 0);
		setPosition(shooter.getPosX(), shooter.getPosYEye() - 0.1, shooter.getPosZ());
	}

	public BulletEntity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
		super(ModEntities.BULLET, shooter, accelX, accelY, accelZ, worldIn);
	}

	public BulletEntity(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
		super(ModEntities.BULLET, x, y, z, accelX, accelY, accelZ, worldIn);
	}

	@Override
	protected void onEntityHit(EntityRayTraceResult raytrace) {
		super.onEntityHit(raytrace);
		if (!world.isRemote) {
			Entity target = raytrace.getEntity();
			Entity shooter = func_234616_v_();
			if (isBurning()) target.setFire(5);
			int lastHurtResistant = target.hurtResistantTime;
			if (ignoreInvulnerability) target.hurtResistantTime = 0;
			boolean damaged = target.attackEntityFrom((new IndirectEntityDamageSource("arrow", this, shooter)).setProjectile(), (float)damage);
			if (damaged && shooter instanceof LivingEntity) {
				applyEnchantments((LivingEntity) shooter, target);
			}
			else if (!damaged && ignoreInvulnerability) target.hurtResistantTime = lastHurtResistant;
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		super.onImpact(result);
		if (!world.isRemote) remove();
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putDouble("damage", damage);
		compound.putBoolean("ignoreinv", ignoreInvulnerability);
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		damage = compound.getDouble("damage");
		ignoreInvulnerability = compound.getBoolean("ignoreinv");
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public double getDamage() {
		return damage;
	}
	
	public void setIgnoreInvulnerability(boolean ignoreInvulnerability) {
		this.ignoreInvulnerability = ignoreInvulnerability;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}

	@Override
	protected boolean isFireballFiery() {
		return false;
	}

	@Override
	protected float getMotionFactor() {
		return 1;
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

}
