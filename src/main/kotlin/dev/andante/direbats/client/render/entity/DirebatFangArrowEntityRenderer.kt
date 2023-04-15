package dev.andante.direbats.client.render.entity

import dev.andante.direbats.Direbats
import dev.andante.direbats.entity.DirebatFangArrowEntity
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.render.entity.ProjectileEntityRenderer
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.util.Identifier

/**
 * Represents the renderer for a [DirebatFangArrowEntity].
 */
@Environment(EnvType.CLIENT)
class DirebatFangArrowEntityRenderer<T : PersistentProjectileEntity>(ctx: EntityRendererFactory.Context) : ProjectileEntityRenderer<T>(ctx) {
    override fun getTexture(entity: T): Identifier {
        return TEXTURE
    }

    companion object {
        val TEXTURE = Identifier(Direbats.MOD_ID, "textures/entity/arrow/direbat_fang_arrow.png")
    }
}
