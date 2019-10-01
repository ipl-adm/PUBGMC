package com.toma.pubgmc.client.layers;

import com.toma.pubgmc.Pubgmc;
import com.toma.pubgmc.init.PMCRegistry;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LayerGhillie implements LayerRenderer<EntityLivingBase> {

    public static final ResourceLocation TEXTURE_MAIN = new ResourceLocation(Pubgmc.MOD_ID + ":textures/models/ghillie_main.java");
    public static final ResourceLocation TEXTURE_OVERLAY = new ResourceLocation(Pubgmc.MOD_ID + ":textures/models/ghillie_overlay.java");

    protected final RenderLivingBase<?> renderLivingBase;
    protected final ModelBiped baseLayer;
    protected final ModelBiped overlay;

    public LayerGhillie(RenderLivingBase<?> renderLivingBase) {
        this.renderLivingBase = renderLivingBase;
        this.baseLayer = new ModelBiped(1.2F);
        this.overlay = new ModelBiped(1.25F);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        ItemStack stack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        if(!stack.isEmpty() && stack.getItem() == PMCRegistry.PMCItems.GHILLIE_SUIT) {
            this.baseLayer.setModelAttributes(this.renderLivingBase.getMainModel());
            this.overlay.setModelAttributes(this.renderLivingBase.getMainModel());
            this.baseLayer.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            this.overlay.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks);
            int color = stack.hasTagCompound() && stack.getTagCompound().hasKey("ghillieColor") ? stack.getTagCompound().getInteger("ghillieColor") : 0x359E35;
            float red = (color >> 16 & 255) / 255.0F;
            float green = (color >> 8 & 255) / 255.0F;
            float blue = (color & 255) / 255.0F;
            this.renderLivingBase.bindTexture(TEXTURE_MAIN);
            GlStateManager.color(red, green, blue);
            this.baseLayer.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            this.renderLivingBase.bindTexture(TEXTURE_OVERLAY);
            this.overlay.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            GlStateManager.color(1f, 1f, 1f);
        }
    }
}
