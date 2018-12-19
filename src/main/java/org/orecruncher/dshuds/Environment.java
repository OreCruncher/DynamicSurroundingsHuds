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
package org.orecruncher.dshuds;

import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.orecruncher.dshuds.compat.SereneSeasonHandler;
import org.orecruncher.lib.MinecraftClock;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

@EventBusSubscriber(modid = ModInfo.MOD_ID)
public final class Environment {

	private static final MinecraftClock clock = new MinecraftClock();
	private static final BlockPos.MutableBlockPos playerPosition = new BlockPos.MutableBlockPos();
	private static final Function<World, String> seasonStringHandler;

	private static int tickCount = 0;
	private static String biomeName = StringUtils.EMPTY;
	private static String seasonString = StringUtils.EMPTY;

	static {
		if (Loader.isModLoaded("sereneseasons"))
			seasonStringHandler = new SereneSeasonHandler();
		else
			seasonStringHandler = (world) -> StringUtils.EMPTY;
	}

	public static int getTickCounter() {
		return tickCount;
	}

	@Nullable
	public static World getWorld() {
		return Minecraft.getMinecraft().world;
	}

	@Nullable
	public static EntityPlayer getPlayer() {
		return Minecraft.getMinecraft().player;
	}

	@Nonnull
	public static MinecraftClock getClock() {
		return clock;
	}

	@Nonnull
	public static BlockPos getPlayerPosition() {
		return playerPosition;
	}

	@Nonnull
	public static String getBiomeName() {
		return biomeName;
	}

	@Nonnull
	public static String getSeasonString() {
		return seasonString;
	}

	@SubscribeEvent
	public static void clientTick(@Nonnull final TickEvent.ClientTickEvent event) {
		if (event.phase == Phase.START && getPlayer() != null && getWorld() != null) {
			tickCount++;
			clock.update(getWorld());
			playerPosition.setPos(getPlayer());
			biomeName = getWorld().getBiome(playerPosition).getBiomeName();
			seasonString = seasonStringHandler.apply(getWorld());
		}
	}
}
