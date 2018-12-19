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
package org.orecruncher.dshuds.compat;

import java.util.function.Function;
import javax.annotation.Nonnull;

import org.orecruncher.lib.Localization;

import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.SeasonHelper;

@SideOnly(Side.CLIENT)
public class SereneSeasonHandler implements Function<World, String> {

	protected static final String noSeason = Localization.loadString("dshuds.season.noseason");

	@Override
	public String apply(@Nonnull final World world) {
		final ISeasonState ss = SeasonHelper.getSeasonState(world);
		final SeasonType season = SeasonType.getSeasonType(ss);
		if (season == SeasonType.NONE)
			return noSeason;

		final SeasonType.SubType sub = SeasonType.getSeasonSubType(ss);
		final String seasonStr = Localization.loadString("dshuds.season." + season.getValue());
		final String subSeasonStr = Localization.loadString("dshuds.season." + sub.getValue());
		return Localization.format("dshuds.season.format", subSeasonStr, seasonStr);
	}

}
