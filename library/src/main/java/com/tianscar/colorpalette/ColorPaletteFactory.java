/*
 * Copyright (C) 2021 AnsdoShip Studio
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
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
