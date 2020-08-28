package lykrast.gunswithoutroses.item;

import java.util.function.Predicate;

import lykrast.gunswithoutroses.ModItems;
import lykrast.gunswithoutroses.entity.BulletEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShootableItem;
import net.minecraft.item.UseAction;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class GunItem extends ShootableItem {
	private int enchantability;
	private int fireDelay;
	private double inaccuracy;

	public GunItem(Properties properties, int fireDelay, double inaccuracy, int enchantability) {
		super(properties);
		this.enchantability = enchantability;
		this.fireDelay = fireDelay;
		this.inaccuracy = inaccuracy;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack gun = player.getHeldItem(hand);
		ItemStack ammo = player.findAmmo(gun);

		if (!ammo.isEmpty() || player.abilities.isCreativeMode) {
			if (ammo.isEmpty() || ammo.getItem() == Items.ARROW) {
				ammo = new ItemStack(ModItems.flintBullet);
			}

			float speed = 3;
			BulletItem bulletItem = (BulletItem) (ammo.getItem() instanceof BulletItem ? ammo.getItem() : ModItems.flintBullet);
			if (!world.isRemote) {
				boolean bulletFree = player.abilities.isCreativeMode || !shouldConsumeAmmo(gun, player);
				BulletEntity shot = bulletItem.createProjectile(world, ammo, player);
				shot.func_234612_a_(player, player.rotationPitch, player.rotationYaw, 0, speed, (float)getInaccuracy(gun, player));

				gun.damageItem(1, player, (p) -> p.sendBreakAnimation(player.getActiveHand()));
				world.addEntity(shot);

				if (!bulletFree) bulletItem.consume(ammo, player);
			}

			world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + speed * 0.5F);
			player.addStat(Stats.ITEM_USED.get(this));

			player.getCooldownTracker().setCooldown(this, fireDelay);
			return ActionResult.resultConsume(gun);
		}
		else return ActionResult.resultFail(gun);
	}
	
	public boolean shouldConsumeAmmo(ItemStack stack, PlayerEntity player) {
		//TODO enchant
		return true;
	}
	
	public double getInaccuracy(ItemStack stack, PlayerEntity player) {
		//TODO enchant
		return inaccuracy;
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getItemEnchantability() {
		return enchantability;
	}

	private static final Predicate<ItemStack> BULLETS = (stack) -> stack.getItem() instanceof BulletItem;

	@Override
	public Predicate<ItemStack> getInventoryAmmoPredicate() {
		return BULLETS;
	}

	@Override
	public int func_230305_d_() {
		// No idea what this is yet, so using the Bow value (Crossbow is 8)
		return 15;
	}

}