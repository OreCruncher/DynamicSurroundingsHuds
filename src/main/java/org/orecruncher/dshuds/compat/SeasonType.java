/*
 * This file is part of Dynamic Surroundings, licensed under the MIT License (MIT).
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

import javax.annotation.Nonnull;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sereneseasons.api.season.ISeasonState;
import sereneseasons.api.season.Season;

@SideOnly(Side.CLIENT)
public enum SeasonType {

	//@formatter:off
	NONE("noseason"),
	SPRING("spring"),
	SUMMER("summer"),
	AUTUMN("autumn"),
	WINTER("winter");
	//@formatter:on

	public static enum SubType {

		//@formatter:off
		NONE("nosubtype"),
		EARLY("early"),
		MID("mid"),
		LATE("late");
		//@formatter:on

		private final String val;

		SubType(@Nonnull final String val) {
			this.val = val;
		}

		@Nonnull
		public String getValue() {
			return this.val;
		}
	}

	private final String val;

	SeasonType(@Nonnull final String val) {
		this.val = val;
	}

	@Nonnull
	public String getValue() {
		return this.val;
	}
	
	@Nonnull
	public static SeasonType getSeasonType(@Nonnull final ISeasonState state) {
		if (state == null || state.getSeason() == null)
			return SeasonType.NONE;
		
		final Season season = state.getSeason();
		switch (season) {
		case SUMMER:
			return SeasonType.SUMMER;
		case AUTUMN:
			return SeasonType.AUTUMN;
		case WINTER:
			return SeasonType.WINTER;
		case SPRING:
			return SeasonType.SPRING;
		default:
			return SeasonType.NONE;
		}
	}

	@Nonnull
	public static SeasonType.SubType getSeasonSubType(@Nonnull final ISeasonState state) {
		final Season.SubSeason sub = state.getSubSeason();
		switch (sub) {
		case EARLY_SUMMER:
		case EARLY_AUTUMN:
		case EARLY_WINTER:
		case EARLY_SPRING:
			return SeasonType.SubType.EARLY;
		case MID_SUMMER:
		case MID_AUTUMN:
		case MID_WINTER:
		case MID_SPRING:
			return SeasonType.SubType.MID;
		case LATE_SUMMER:
		case LATE_AUTUMN:
		case LATE_WINTER:
		case LATE_SPRING:
			return SeasonType.SubType.LATE;
		default:
			return SeasonType.SubType.NONE;
		}
	}
}