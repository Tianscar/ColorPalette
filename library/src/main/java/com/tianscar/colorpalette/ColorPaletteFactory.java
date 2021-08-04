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
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public final class ColorPaletteFactory {

    public interface Callback {
        void onCreateFile(boolean isSuccess);
        void onException(Exception e);
        void onFileExists(boolean isDirectory);
        void onSuccess();
    }

    public @Nullable static ColorPalette decodeFile(@NonNull String pathname) {
        return decodeFile(new File(pathname));
    }

    public @Nullable static ColorPalette decodeFile (@NonNull File file) {
        if (file.exists() && file.canRead()) {
            try {
                JSONObject jsonObject = Utils.decodeFile(file);
                return ColorPalette.createColorPalette(JSONColor2IntArray(jsonObject.getString("colors")),
                        jsonObject.getInt("index"));
            }
            catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Nullable
    public static ColorPalette decodeString (@NonNull String string) {
        try {
            return decodeJSONObject(new JSONObject(string));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static ColorPalette decodeJSONObject(@NonNull JSONObject jsonObject) {
        try {
            return ColorPalette.createColorPalette(JSONColor2IntArray(jsonObject.getString("colors")),
                    jsonObject.getInt("index"));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void encodeFile (@NonNull ColorPalette colorPalette, @NonNull File file, boolean override,
                                   @NonNull Callback callback) {
        if((!file.exists()) || (override && file.isFile())) {
            try {
                callback.onCreateFile(file.createNewFile());
                Utils.encodeFile(palette2JSON(colorPalette), file);
                callback.onSuccess();
            }
            catch (IOException | JSONException e) {
                callback.onException(e);
            }
        }
        else {
            callback.onFileExists(file.isDirectory());
        }
    }

    public static void encodeFile (@NonNull ColorPalette colorPalette, @NonNull String pathname, boolean override,
                                   @NonNull Callback callback) {
        encodeFile(colorPalette, new File(pathname), override, callback);
    }

    public static boolean encodeFile (@NonNull ColorPalette colorPalette, @NonNull File file, boolean override) {
        final boolean[] result = new boolean[1];
        encodeFile(colorPalette, file, override, new Callback() {
            @Override
            public void onCreateFile(boolean isSuccess) {
                result[0] = isSuccess;
            }
            @Override
            public void onException(Exception e) {
                e.printStackTrace();
                result[0] = false;
            }
            @Override
            public void onFileExists(boolean isDirectory) {
                result[0] = false;
            }
            @Override
            public void onSuccess() {
                result[0] = true;
            }
        });
        return result[0];
    }

    public static boolean encodeFile (@NonNull ColorPalette colorPalette, @NonNull String pathname, boolean override) {
        return encodeFile(colorPalette, new File(pathname), override);
    }

    @Nullable
    public static String encodeString(@NonNull ColorPalette colorPalette) {
        JSONObject jsonObject = encodeJSONObject(colorPalette);
        if (jsonObject == null) {
            return null;
        }
        return jsonObject.toString();
    }

    @Nullable
    public static JSONObject encodeJSONObject(@NonNull ColorPalette colorPalette) {
        try {
            return palette2JSON(colorPalette);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static @NonNull JSONObject palette2JSON (@NonNull ColorPalette colorPalette) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("index", colorPalette.getIndex());
        jsonObject.put("colors", Arrays.toString(colorPalette.getColors()));
        return jsonObject;
    }

    private static @NonNull int[] JSONColor2IntArray(@NonNull String colorStringArray) {
        String[] stringArray = colorStringArray.substring(1, colorStringArray.length() - 1).split(",");
        int[] colorArray = new int[stringArray.length];
        for (int i = 0; i < stringArray.length; i ++) {
            stringArray[i] = stringArray[i].replace(" ", "");
            if (stringArray[i].startsWith("-")) {
                colorArray[i] = - Integer.parseInt(stringArray[i].substring(1));
            }
            else {
                colorArray[i] = Integer.parseInt(stringArray[i]);
            }
        }
        return colorArray;
    }

}
