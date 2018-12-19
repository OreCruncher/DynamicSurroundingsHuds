/*
 * Licensed under the MIT License (MIT).
 *
 * Copyright (c) OreCruncher
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.orecruncher.dshuds.hud;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nonnull;

import org.orecruncher.dshuds.Environment;
import org.orecruncher.dshuds.ModBase;
import org.orecruncher.dshuds.ModInfo;
import org.orecruncher.dshuds.ModOptions;
import org.orecruncher.lib.ForgeUtils;
import org.orecruncher.lib.ItemStackUtil;
import org.orecruncher.lib.Localization;
import org.orecruncher.lib.MinecraftClock;
import org.orecruncher.lib.PlayerUtils;
import org.orecruncher.lib.gui.Panel.Reference;
import org.orecruncher.lib.gui.TextPanel;
import org.orecruncher.lib.math.MathStuff;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CompassHUD extends GuiOverlay {

	private static final int BAND_WIDTH = 65;
	private static final int BAND_HEIGHT = 12;
	private static final int ROSE_DIM = 256;

	private static final float TEXT_LINE_START = 1.5F;

	private static enum Style {
		//@formatter:off
		BAND_0(false, "textures/compass.png", BAND_WIDTH, BAND_HEIGHT),
		BAND_1(false, "textures/compass.png", BAND_WIDTH, BAND_HEIGHT),
		BAND_2(false, "textures/compass.png", BAND_WIDTH, BAND_HEIGHT),
		BAND_3(false, "textures/compass.png", BAND_WIDTH, BAND_HEIGHT),
		ROSE_1(true, "textures/compassrose1.png", ROSE_DIM, ROSE_DIM),
		ROSE_2(true, "textures/compassrose2.png", ROSE_DIM, ROSE_DIM),
		ROSE_3(true, "textures/compassrose3.png", ROSE_DIM, ROSE_DIM);
		//@formatter:on

		private final boolean isRose;
		private final ResourceLocation texture;
		private final int width;
		private final int height;

		private Style(final boolean isRose, @Nonnull final String texture, final int w, final int h) {
			this.isRose = isRose;
			this.texture = new ResourceLocation(ModInfo.MOD_ID, texture);
			this.width = w;
			this.height = h;
		}

		public boolean isRose() {
			return this.isRose;
		}

		public ResourceLocation getTextureResource() {
			return this.texture;
		}

		public int getWidth() {
			return this.width;
		}

		public int getHeight() {
			return this.height;
		}

		public static Style getStyle(final int index) {
			if (index < 0 || index >= values().length)
				return BAND_0;
			return values()[index];
		}
	}

	private static final Item COMPASS = ForgeUtils.getItem("minecraft:compass");
	private static final Item CLOCK = ForgeUtils.getItem("minecraft:clock");
	private static final Item SEASON_CLOCK = ForgeUtils.getItem("sereneseasons:season_clock");

	private final TextPanel textPanel;
	private boolean showCompass = false;

	public CompassHUD() {
		this.textPanel = new TextPanel();
	}

	@Nonnull
	protected String getLocationString() {
		final BlockPos pos = Environment.getPlayerPosition();
		return TextFormatting.AQUA + String.format(Locale.getDefault(), ModOptions.compassHUD.coordFormat,
				pos.getX(), pos.getY(), pos.getZ());
	}

	@Nonnull
	protected String getBiomeName() {
		return TextFormatting.GOLD + Environment.getBiomeName();
	}

	protected boolean showCompass() {
		return ModOptions.compassHUD.enable && PlayerUtils.isHolding(Environment.getPlayer(), COMPASS);
	}

	protected static boolean holdingOrItemFrameItem(final Item item) {
		if (item == null)
			return false;
		if (PlayerUtils.isHolding(Environment.getPlayer(), item))
			return true;
		final Entity e = PlayerUtils.entityImLookingAt(Environment.getPlayer());
		if (e instanceof EntityItemFrame) {
			final ItemStack stack = ((EntityItemFrame) e).getDisplayedItem();
			return ItemStackUtil.isValidItemStack(stack) && stack.getItem() == item;
		}
		return false;
	}

	protected boolean showClock() {
		return ModOptions.clockHUD.enable ? holdingOrItemFrameItem(CLOCK) : false;
	}

	protected boolean showSeason() {
		return ModOptions.clockHUD.enable ? holdingOrItemFrameItem(SEASON_CLOCK) : false;
	}

	@Override
	public void doTick(final int tickRef) {
		if (tickRef != 0 && tickRef % 4 == 0) {

			this.textPanel.resetText();

			final List<String> text = new ArrayList<>();

			if (this.showCompass = showCompass()) {
				text.add(getLocationString());
				text.add(getBiomeName());
			}

			if (showSeason()) {
				text.add(Environment.getSeasonString());
			}
			
			if (showClock()) {
				if (text.size() > 0)
					text.add("");

				final MinecraftClock clock = Environment.getClock();
				text.add(clock.getFormattedTime());
				text.add(clock.getTimeOfDay());
				
				long time = ModBase.proxy().currentSessionDuration();
				final int elapsedHours = (int) (time / 3600000);
				time -= elapsedHours * 3600000;
				final int elapsedMinutes = (int) (time / 60000);
				time -= elapsedMinutes * 60000;
				final int elapsedSeconds = (int) (time / 1000);

				text.add(Localization.format("dshuds.format.SessionTime", elapsedHours, elapsedMinutes,
						elapsedSeconds));

			}

			if (text.size() > 0)
				this.textPanel.setText(text);
		}
	}

	@Override
	public void doRender(@Nonnull final RenderGameOverlayEvent.Pre event) {

		if (event.getType() != ElementType.CROSSHAIRS || !this.textPanel.hasText())
			return;

		final Minecraft mc = Minecraft.getMinecraft();
		final FontRenderer font = mc.fontRenderer;

		final ScaledResolution resolution = event.getResolution();
		final int centerX = (resolution.getScaledWidth() + 1) / 2;
		final int centerY = (resolution.getScaledHeight() + 1) / 2;

		this.textPanel.setAlpha(ModOptions.compassHUD.transparency);
		this.textPanel.render(centerX, centerY + (int) (font.FONT_HEIGHT * TEXT_LINE_START), Reference.TOP_CENTER);

		final Style style = Style.getStyle(ModOptions.compassHUD.style);
		mc.getTextureManager().bindTexture(style.getTextureResource());

		if (this.showCompass) {
			GlStateManager.color(1F, 1F, 1F, ModOptions.compassHUD.transparency);
			if (!style.isRose()) {

				final int direction = MathStuff.floor(((mc.player.rotationYaw * 256F) / 360F) + 0.5D) & 255;
				final int x = (resolution.getScaledWidth() - style.getWidth() + 1) / 2;
				final int y = (resolution.getScaledHeight() - style.getHeight() + 1) / 2 - style.getHeight();

				if (direction < 128)
					drawTexturedModalRect(x, y, direction,
							(ModOptions.compassHUD.style * (style.getHeight() * 2)), style.getWidth(),
							style.getHeight());
				else
					drawTexturedModalRect(x, y, direction - 128,
							(ModOptions.compassHUD.style * (style.getHeight() * 2)) + style.getHeight(),
							style.getWidth(), style.getHeight());
			} else {
				GlStateManager.pushMatrix();
				GlStateManager.translate(centerX, centerY - BAND_HEIGHT * 2.5F, 0);
				GlStateManager.rotate(70, 1F, 0F, 0F);
				GlStateManager.rotate(-MathStuff.wrapDegrees(mc.player.rotationYaw + 180F), 0F, 0F, 1F);
				final int x = -(style.getWidth() + 1) / 2;
				final int y = -(style.getHeight() + 1) / 2;
				drawTexturedModalRect(x, y, 0, 0, style.getWidth(), style.getHeight());
				GlStateManager.popMatrix();
			}
		}

		GlStateManager.color(1F, 1F, 1F, 1F);
	}
}
