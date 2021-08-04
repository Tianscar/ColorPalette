/*
 * MIT License
 *
 * Copyright (c) 2021 Tianscar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.tianscar.colorpalette;

import androidx.annotation.NonNull;
import androidx.core.math.MathUtils;

import java.util.Arrays;

public final class ColorPalette {

    private final int[] mColors;

    private int index;

    private ColorPalette(@NonNull int[] colors) {
        this(colors, 0);
    }

    private ColorPalette(@NonNull int[] colors, int index) {
        if (colors.length < 1) {
            throw new IllegalArgumentException("Color array length cannot be < 1");
        }
        mColors = colors.clone();
        setIndex(index);
    }

    private ColorPalette(int size, int color) {
        this(size, color, 0);
    }

    private ColorPalette(int size, int color, int index) {
        if (size < 1) {
            throw new IllegalArgumentException("Size cannot be < 1");
        }
        mColors = new int[size];
        for (int i = 0; i < size; i++) {
            mColors[i] = color;
        }
        setIndex(index);
    }

    public static @NonNull
    ColorPalette createColorPalette (int size) {
        return createColorPalette (size, 0);
    }

    public static @NonNull
    ColorPalette createColorPalette (int size, int index) {
        return createColorPalette (size, 0, index);
    }

    public static @NonNull
    ColorPalette createColorPalette (int size, int color, int index) {
        return new ColorPalette(size, color, index);
    }

    public static @NonNull
    ColorPalette createColorPalette (@NonNull ColorPalette src) {
        return createColorPalette(src, src.size(), src.getIndex());
    }

    public static @NonNull
    ColorPalette createColorPalette (@NonNull ColorPalette src, int index) {
        return createColorPalette(src, src.size(), index);
    }

    public static @NonNull
    ColorPalette createColorPalette (@NonNull ColorPalette src, int size, int index) {
        int[] colors = new int[size];
        for (int i = 0; i < Math.min(size, src.size()); i ++) {
            colors[i] = src.getColor(i);
        }
        return createColorPalette(colors, index);
    }

    public static @NonNull
    ColorPalette createColorPalette (@NonNull int[] colors) {
        return createColorPalette(colors, 0);
    }

    public static @NonNull
    ColorPalette createColorPalette (@NonNull int[] colors, int index) {
        return new ColorPalette(colors, index);
    }

    public int size () {
        return mColors.length;
    }

    public int getColor (int index) {
        return mColors[index];
    }

    public void setColor (int index, int color) {
        mColors[index] = color;
    }

    public void setAllColors (int color) {
        for (int i = 0; i < mColors.length; i++) {
            setColor(i, color);
        }
    }

    public void resetColor (int index) {
        setColor(index, 0);
    }

    public void clearAllColors () {
        setAllColors(0);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = MathUtils.clamp(index, 0, mColors.length - 1);
    }

    public int getCurrentColor() {
        return getColor(getIndex());
    }

    public void setCurrentColor(int color) {
        setColor(getIndex(), color);
    }

    public int[] getColors() {
        return mColors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColorPalette colorPalette = (ColorPalette) o;
        return index == colorPalette.index && Arrays.equals(mColors, colorPalette.mColors);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(new int[]{index});
        result = 31 * result + Arrays.hashCode(mColors);
        return result;
    }

}
