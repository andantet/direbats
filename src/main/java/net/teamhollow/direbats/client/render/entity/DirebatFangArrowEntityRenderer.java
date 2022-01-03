package net.teamhollow.direbats.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;

import static net.teamhollow.direbats.client.util.ClientUtil.entityTexture;

@Environment(EnvType.CLIENT)
public class DirebatFangArrowEntityRenderer<T extends PersistentProjectileEntity> extends ProjectileEntityRenderer<T> {
    public static final Identifier TEXTURE = entityTexture("arrow/direbat_fang_arrow");

    public DirebatFangArrowEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(T entity) {
        return TEXTURE;
    }
}
