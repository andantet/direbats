package net.teamhollow.direbats.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.teamhollow.direbats.Direbats;
import net.teamhollow.direbats.entity.direbat_fang_arrow.DirebatFangArrowEntity;

public class DirebatFangArrowItem extends ArrowItem {
    public static final String id = "direbat_fang_arrow";

	public DirebatFangArrowItem() {
        super(new Item.Settings().group(Direbats.ITEM_GROUP));
    }

    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter) {
        return new DirebatFangArrowEntity(world, shooter);
    }
}
