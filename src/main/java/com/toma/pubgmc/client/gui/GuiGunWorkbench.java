package com.toma.pubgmc.client.gui;

import java.io.IOException;
import java.util.List;

import com.toma.pubgmc.Pubgmc;
import com.toma.pubgmc.client.util.PageButton;
import com.toma.pubgmc.client.util.RecipeButton;
import com.toma.pubgmc.common.container.ContainerGunWorkbench;
import com.toma.pubgmc.common.tileentity.TileEntityGunWorkbench;
import com.toma.pubgmc.util.recipes.PMCRecipe;
import com.toma.pubgmc.util.recipes.PMCRecipe.CraftingCategory;
import com.toma.pubgmc.util.recipes.RecipeRegistry;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiGunWorkbench extends GuiContainer
{
	private static final ResourceLocation TEXTURES = new ResourceLocation(Pubgmc.MOD_ID + ":textures/gui/gun_workbench.png");
	private static final ResourceLocation INFO = new ResourceLocation(Pubgmc.MOD_ID + ":textures/overlay/info.png");
	private final InventoryPlayer player;
	private final TileEntityGunWorkbench tileentity;
	
	public GuiGunWorkbench(TileEntityGunWorkbench te, InventoryPlayer playerInv)
	{
		super(new ContainerGunWorkbench(te, playerInv));
		this.tileentity = te;
		this.player = playerInv;
		xSize = 176;
		ySize = 193;
	}
	
	@Override
	public void initGui()
	{	
		super.initGui();
		buttonList.clear();
		List<PMCRecipe> list = tileentity.RECIPES.get(tileentity.selectedCat.ordinal());
		for(int i = tileentity.selectedIndex*4+4; i > tileentity.selectedIndex*4; i--) {
			if(i < list.size()) {
				this.buttonList.add(new RecipeButton(buttonList.size(), guiLeft + 45, guiTop + i*18 - 10, list.get(i), player));
			}
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{	
		mc.getTextureManager().bindTexture(TEXTURES);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if(button instanceof RecipeButton) {
			RecipeButton btn = (RecipeButton)button;
			PMCRecipe recipe = btn.recipe;
			btn.craft();
		} else if(button instanceof PageButton) {
			PageButton btn = (PageButton)button;
			switch(btn.id) {
				// category
				case 4: case 5: {
					CraftingCategory cat = btn.isRight ? CraftingCategory.getNextCategory(tileentity.selectedCat) : CraftingCategory.getPrevCategory(tileentity.selectedCat);
					int page = 0;
					// TODO update tileentity
					break;
				}
				
				case 6: case 7: {
					List<PMCRecipe> list = TileEntityGunWorkbench.RECIPES.get(tileentity.selectedCat.ordinal());
					int currentPage = btn.visible ? btn.isRight ? tileentity.selectedIndex + 1 : tileentity.selectedIndex - 1 : tileentity.selectedIndex;
					// TODO update tileentity
					break;
				}
			}
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if(mouseButton == 0) {
			for(GuiButton button : this.buttonList) {
				if(button.mousePressed(mc, mouseX, mouseY)) {
					if(button instanceof RecipeButton) {
						if(((RecipeButton)button).active) {
							this.actionPerformed(button);
							button.playPressSound(mc.getSoundHandler());
						}
					} else if(button instanceof PageButton) {
						if(((PageButton)button).visible) {
							button.playPressSound(mc.getSoundHandler());
							this.actionPerformed(button);
						}
					} else {
						button.playPressSound(mc.getSoundHandler());
						this.actionPerformed(button);
					}
				}
			}
		}
	}
	
	private PageButton createPageButton(int index, int x, int y, boolean right, List<PMCRecipe> recipeCollection) {
		PageButton button = new PageButton(index, x, y, right);
		int i = tileentity.selectedIndex + 1;
		boolean flag = right ? recipeCollection.size() > i*4 : tileentity.selectedIndex > 0;
		button.visible = flag;
		return button;
	}
	
	private void update(TileEntityGunWorkbench te, int page, CraftingCategory category) {
		// TODO packet
		tileentity.selectedIndex = page;
		tileentity.selectedCat = category;
	}
}
