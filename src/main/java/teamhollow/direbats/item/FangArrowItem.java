package teamhollow.direbats.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import teamhollow.direbats.entity.projectile.FangArrowEntity;

public class FangArrowItem extends ArrowItem {
    public FangArrowItem(Item.Properties settings) {
        super(settings);
    }

    public AbstractArrowEntity createArrow(World worldIn, ItemStack stack, LivingEntity shooter) {
        FangArrowEntity arrowEntity = new FangArrowEntity(worldIn, shooter);
        return arrowEntity;
    }
}
