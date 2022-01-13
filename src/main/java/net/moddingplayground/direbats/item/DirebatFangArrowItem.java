package net.moddingplayground.direbats.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.moddingplayground.direbats.entity.DirebatFangArrowEntity;

public class DirebatFangArrowItem extends ArrowItem {
	public DirebatFangArrowItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new DirebatFangArrowEntity(world, shooter);
    }
}
