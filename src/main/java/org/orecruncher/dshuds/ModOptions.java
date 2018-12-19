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

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = ModInfo.MOD_ID)
@Config(modid = ModInfo.MOD_ID, type = Type.INSTANCE, name = ModInfo.MOD_ID, category = "")
public class ModOptions {

	private static final String PREFIX = "config." + ModInfo.MOD_ID;

	@Name("Logging")
	@Comment({ "Options to control logging" })
	@LangKey(Logging.PREFIX)
	public static Logging logging = new Logging();

	public static class Logging {

		private static final String PREFIX = ModOptions.PREFIX + ".logging";

		@LangKey(PREFIX + ".enableLogging")
		@Comment({ "Enables debug logging output for diagnostics" })
		public boolean enableLogging = false;

		@LangKey(PREFIX + ".enableVersionCheck")
		@Comment({ "Enables display of chat messages related to newer versions", "of the mod being available." })
		public boolean enableVersionCheck = true;

		@LangKey(PREFIX + ".chatnotices")
		@Comment({ "Disable display of hud toggles in chat" })
		public boolean hideChatNotices = false;
	}

	@Name("PotionHud")
	@LangKey(PotionHUD.PREFIX)
	@Comment({ "Options for the Potion HUD overlay" })
	public static PotionHUD potionHUD = new PotionHUD();

	public static class PotionHUD {

		private static final String PREFIX = ModOptions.PREFIX + ".potionhud";

		@LangKey(PREFIX + ".nohud")
		@Comment({ "Disables Vanilla and Dynamic Surroundings potion HUD" })
		public boolean none = false;

		@LangKey(PREFIX + ".enable")
		@Comment({ "Enable display of potion icons in display" })
		public boolean enabled = true;

		@LangKey(PREFIX + ".transparency")
		@RangeDouble(min = 0.0F, max = 1.0F)
		@Comment({ "Transparency factor for icons (higher more solid)" })
		public float transparency = 0.75F;

		@LangKey(PREFIX + ".leftoffset")
		@RangeInt(min = 0)
		@Comment({ "Offset from left side of screen" })
		public int leftOffset = 5;

		@LangKey(PREFIX + ".topoffset")
		@RangeInt(min = 0)
		@Comment({ "Offset from top of screen" })
		public int topOffset = 5;

		@LangKey(PREFIX + ".scale")
		@RangeDouble(min = 0.0F, max = 1.0F)
		@Comment({ "Size scale of icons (lower is smaller)" })
		public float scale = 0.75F;

		@LangKey(PREFIX + ".location")
		@RangeInt(min = 0, max = 1)
		@Comment({ "Area of the display the Potion HUD is displayed (0 upper left, 1 upper right)" })
		public int anchor = 0;
	}

	@Name("LightLevelHud")
	@LangKey(LightLevelHUD.PREFIX)
	@Comment({ "Options for configuring Light Level HUD" })
	public static LightLevelHUD lightLevelHUD = new LightLevelHUD();

	public static class LightLevelHUD {

		private static final String PREFIX = ModOptions.PREFIX + ".lightlevelhud";

		@LangKey(PREFIX + ".range")
		@Comment({ "Range from player to analyze light levels" })
		@RangeInt(min = 16, max = 32)
		public int range = 24;

		@LangKey(PREFIX + ".mobspawnthreshold")
		@Comment({ "Light level at which mobs can spawn" })
		@RangeInt(min = 0, max = 15)
		public int spawnThreshold = 7;

		@LangKey(PREFIX + ".displaymode")
		@Comment({ "0: Block Light, 1: Block Light + Sky Light" })
		@RangeInt(min = 0, max = 1)
		public int displayMode = 0;

		@LangKey(PREFIX + ".hidesafe")
		@Comment("Hide light level information for blocks that are considered safe")
		public boolean hideSafe = false;

		@LangKey(PREFIX + ".indicatecaution")
		@Comment({ "Indicate current light levels that will change at night which could result in mob spawns" })
		public boolean indicateCaution = true;

		@LangKey(PREFIX + ".colors")
		@Comment({ "Color set: 0 bright, 1 dark" })
		@RangeInt(min = 0, max = 1)
		public int colors = 1;
	}

	@Name("CompassHud")
	@LangKey(CompassHUD.PREFIX)
	@Comment({ "Options for configuring compass HUD" })
	public static CompassHUD compassHUD = new CompassHUD();

	public static class CompassHUD {

		private static final String PREFIX = ModOptions.PREFIX + ".compasshud";

		@LangKey(PREFIX + ".enable")
		@Comment({ "Enable/disable compass HUD when compass is held" })
		public boolean enable = true;

		@LangKey(PREFIX + ".style")
		@Comment({ "Style of compass bar" })
		@RangeInt(min = 0, max = 6)
		public int style = 0;

		@LangKey(PREFIX + ".transparency")
		@Comment({ "Compass transparency" })
		@RangeDouble(min = 0F, max = 1.0F)
		public float transparency = 0.4F;

		@LangKey(PREFIX + ".format")
		@Comment({ "Format string for location coordinates" })
		public String coordFormat = "x: %1$d, z: %3$d";
	}
	
	@Name("ClockHud")
	@LangKey(ClockHUD.PREFIX)
	@Comment({ "Options for configuring clock HUD" })
	public static ClockHUD clockHUD = new ClockHUD();
	
	public static class ClockHUD {

		private static final String PREFIX = ModOptions.PREFIX + ".clockhud";

		@LangKey(PREFIX + ".enable")
		@Comment({ "Enable/disable clock HUD when clock is held" })
		public boolean enable = true;

	}

	@SubscribeEvent
	public static void onConfigChangedEvent(final OnConfigChangedEvent event) {
		if (event.getModID().equals(ModInfo.MOD_ID)) {
			ConfigManager.sync(ModInfo.MOD_ID, Type.INSTANCE);
			ModBase.log().setDebug(ModOptions.logging.enableLogging);
		}
	}
}
