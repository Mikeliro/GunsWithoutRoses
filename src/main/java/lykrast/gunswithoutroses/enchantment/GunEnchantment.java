package lykrast.gunswithoutroses.enchantment;

import lykrast.gunswithoutroses.item.GunItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class GunEnchantment extends Enchantment {
	private int maxLevel, minCost, levelCost, levelCostSpan;

	public GunEnchantment(Rarity rarityIn, int maxLevel, int minCost, int levelCost, int levelCostSpan) {
		super(rarityIn, EnchantmentType.BOW, new EquipmentSlotType[] { EquipmentSlotType.MAINHAND });
		this.maxLevel = maxLevel;
		this.minCost = minCost;
		this.levelCost = levelCost;
		this.levelCostSpan = levelCostSpan;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack) {
		return stack.getItem() instanceof GunItem || super.canApplyAtEnchantingTable(stack);
	}

	@Override
	public int getMaxLevel() {
		return maxLevel;
	}

	@Override
	public int getMinEnchantability(int enchantmentLevel) {
		return minCost + (enchantmentLevel - 1) * levelCost;
	}

	@Override
	public int getMaxEnchantability(int enchantmentLevel) {
		return getMinEnchantability(enchantmentLevel) + levelCostSpan;
	}
	
	@Override
	public int calcModifierDamage(int level, DamageSource source) {
		return 0;
	}

}
