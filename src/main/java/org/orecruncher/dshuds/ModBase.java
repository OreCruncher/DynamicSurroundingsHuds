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

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.orecruncher.dshuds.proxy.Proxy;
import org.orecruncher.lib.VersionChecker;
import org.orecruncher.lib.logging.ModLog;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

//@formatter:off
@Mod(
	modid = ModInfo.MOD_ID,
	clientSideOnly = true,
	useMetadata = true,
	dependencies = ModInfo.DEPENDENCIES,
	version = ModInfo.VERSION,
	acceptedMinecraftVersions = ModInfo.MINECRAFT_VERSIONS,
	updateJSON = ModInfo.UPDATE_URL,
	certificateFingerprint = ModInfo.FINGERPRINT
)
//@formatter:on
public class ModBase {

	@Instance(ModInfo.MOD_ID)
	protected static ModBase instance;

	@Nonnull
	public static ModBase instance() {
		return instance;
	}

	@SidedProxy(clientSide = "org.orecruncher.dshuds.proxy.ProxyClient")
	protected static Proxy proxy;
	protected static ModLog logger = ModLog.NULL_LOGGER;

	@Nonnull
	public static Proxy proxy() {
		return proxy;
	}

	@Nonnull
	public static ModLog log() {
		return logger;
	}

	public ModBase() {
		logger = ModLog.setLogger(ModInfo.MOD_ID, LogManager.getLogger(ModInfo.MOD_ID));
	}

	@EventHandler
	public void preInit(@Nonnull final FMLPreInitializationEvent event) {
		logger = ModLog.setLogger(ModInfo.MOD_ID, event.getModLog());
		logger.setDebug(ModOptions.logging.enableLogging);
		MinecraftForge.EVENT_BUS.register(this);

		proxy().preInit(event);
	}

	@EventHandler
	public void init(@Nonnull final FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(@Nonnull final FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@EventHandler
	public void loadCompleted(@Nonnull final FMLLoadCompleteEvent event) {
		proxy.loadCompleted(event);
	}

	@EventHandler
	public void onFingerprintViolation(@Nonnull final FMLFingerprintViolationEvent event) {
		log().warn("Invalid fingerprint detected!");
	}

	////////////////////////
	//
	// Client state events
	//
	////////////////////////
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void clientConnect(@Nonnull final ClientConnectedToServerEvent event) {
		proxy.clientConnect(event);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void clientDisconnect(@Nonnull final ClientDisconnectionFromServerEvent event) {
		proxy.clientDisconnect(event);
	}
	
	@SubscribeEvent
	public void playerLogin(final PlayerLoggedInEvent event) {
		if (ModOptions.logging.enableVersionCheck)
			VersionChecker.doCheck(event, ModInfo.MOD_ID);
	}
}
