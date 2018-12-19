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

import org.orecruncher.dshuds.Environment;
import org.orecruncher.dshuds.ModInfo;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(value = Side.CLIENT, modid = ModInfo.MOD_ID)
public final class GuiHUDHandler {

	private static final List<GuiOverlay> overlays = new ArrayList<>();
	static {
		overlays.add(new PotionHUD());
		overlays.add(new CompassHUD());
		overlays.add(new LightLevelHUD());
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onRenderGameOverlayEvent(final RenderGameOverlayEvent.Pre event) {
		for (int i = 0; i < overlays.size(); i++)
			overlays.get(i).doRender(event);
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onRenderGameOverlayEvent(final RenderGameOverlayEvent.Post event) {
		for (int i = 0; i < overlays.size(); i++)
			overlays.get(i).doRender(event);
	}

	@SubscribeEvent
	public static void playerTick(final TickEvent.PlayerTickEvent event) {
		if (event.side == Side.SERVER || event.phase == Phase.END || Minecraft.getMinecraft().isGamePaused())
			return;

		if (event.player == null || event.player.world == null)
			return;

		if (event.player != Environment.getPlayer())
			return;

		final int tickRef = Environment.getTickCounter();
		for (int i = 0; i < overlays.size(); i++)
			overlays.get(i).doTick(tickRef);
	}
}
