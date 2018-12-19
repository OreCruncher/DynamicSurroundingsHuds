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

package org.orecruncher.dshuds.keyboard;

import javax.annotation.Nonnull;

import org.lwjgl.input.Keyboard;
import org.orecruncher.dshuds.Environment;
import org.orecruncher.dshuds.ModInfo;
import org.orecruncher.dshuds.ModOptions;
import org.orecruncher.dshuds.hud.LightLevelHUD;
import org.orecruncher.dshuds.hud.LightLevelHUD.Mode;
import org.orecruncher.lib.Localization;
import org.orecruncher.lib.compat.EntityRendererUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KeyHandler {

	private static final String SECTION_NAME = ModInfo.MOD_NAME;

	private static KeyBinding SELECTIONBOX_KEY;
	private static KeyBinding LIGHTLEVEL_KEY;
	private static KeyBinding CHUNKBORDER_KEY;

	public static void init() {
		SELECTIONBOX_KEY = new KeyBinding("dshuds.keybind.SelectionBox", Keyboard.KEY_B, SECTION_NAME);
		ClientRegistry.registerKeyBinding(SELECTIONBOX_KEY);

		LIGHTLEVEL_KEY = new KeyBinding("dshuds.keybind.LightLevel", Keyboard.KEY_F7, SECTION_NAME);
		ClientRegistry.registerKeyBinding(LIGHTLEVEL_KEY);

		CHUNKBORDER_KEY = new KeyBinding("dshuds.keybind.ChunkBorders", Keyboard.KEY_F9, SECTION_NAME);
		ClientRegistry.registerKeyBinding(CHUNKBORDER_KEY);

		MinecraftForge.EVENT_BUS.register(KeyHandler.class);
	}

	private static String getOnOff(final boolean flag) {
		return Localization.format(flag ? "dshuds.keybind.msg.ON" : "dshuds.keybind.msg.OFF");
	}

	private static final String chatPrefix = TextFormatting.BLUE + "[" + TextFormatting.GREEN + ModInfo.MOD_NAME
			+ TextFormatting.BLUE + "] " + TextFormatting.RESET;

	private static void sendPlayerMessage(final String fmt, final Object... parms) {
		if (ModOptions.logging.hideChatNotices)
			return;

		final EntityPlayer player = Environment.getPlayer();
		if (player != null) {
			final String txt = chatPrefix + Localization.format(fmt, parms);
			player.sendMessage(new TextComponentString(txt));
		}
	}

	private static boolean shouldHandle(@Nonnull final KeyBinding binding) {
		return binding != null && binding.isPressed();
	}

	@SubscribeEvent(receiveCanceled = false)
	public static void onKeyboard(@Nonnull InputEvent.KeyInputEvent event) {

		if (shouldHandle(SELECTIONBOX_KEY)) {
			final EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
			final boolean result = !EntityRendererUtil.getDrawBlockOutline(renderer);
			EntityRendererUtil.setDrawBlockOutline(renderer, result);
			sendPlayerMessage("dshuds.keybind.msg.Fencing", getOnOff(result));
		}

		if (shouldHandle(CHUNKBORDER_KEY)) {
			final boolean result = Minecraft.getMinecraft().debugRenderer.toggleChunkBorders();
			sendPlayerMessage("dshuds.keybind.msg.ChunkBorder", getOnOff(result));
		}

		if (shouldHandle(LIGHTLEVEL_KEY)) {
			if (GuiScreen.isCtrlKeyDown()) {
				// Only change mode when visible
				if (LightLevelHUD.showHUD) {
					ModOptions.lightLevelHUD.displayMode++;
					if (ModOptions.lightLevelHUD.displayMode >= Mode.values().length)
						ModOptions.lightLevelHUD.displayMode = 0;
					sendPlayerMessage("dshuds.keybind.msg.LLDisplayMode",
							Mode.getMode(ModOptions.lightLevelHUD.displayMode).name());
				}
			} else if (GuiScreen.isShiftKeyDown()) {
				if (LightLevelHUD.showHUD) {
					ModOptions.lightLevelHUD.hideSafe = !ModOptions.lightLevelHUD.hideSafe;
					sendPlayerMessage("dshuds.keybind.msg.LLSafeBlocks", getOnOff(ModOptions.lightLevelHUD.hideSafe));
				}
			} else {
				LightLevelHUD.showHUD = !LightLevelHUD.showHUD;
				sendPlayerMessage("dshuds.keybind.msg.LLDisplay", getOnOff(LightLevelHUD.showHUD));
			}
		}

	}

}
