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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

final class Utils {

    public static @NonNull JSONObject decodeFile (@NonNull String pathname) throws IOException, JSONException {
        return decodeFile(new File(pathname));
    }

    public static @NonNull JSONObject decodeFile (@NonNull File file) throws IOException, JSONException {
        return new JSONObject(readFileToString(file, "UTF-8"));
    }

    public static void encodeFile (@NonNull JSONObject jsonObject, @NonNull String pathname) throws IOException {
        encodeFile(jsonObject, new File(pathname));
    }

    public static void encodeFile (@NonNull JSONObject jsonObject, @NonNull File file) throws IOException {
        writeStringToFile(file, jsonObject.toString(), "UTF-8");
    }

    @NonNull
    public static String readFileToString(@NonNull File file, String charsetName) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        FileInputStream inputStream = new FileInputStream(file);
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(charsetName);
    }

    public static void writeStringToFile(@NonNull File file, @NonNull String string,
                                         @NonNull String charsetName) throws IOException {
        if (!file.exists()) {
            boolean success = file.createNewFile();
            if (!success) {
                throw new IOException("Failed to create file.");
            }
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(string.getBytes(charsetName));
        outputStream.flush();
        outputStream.close();
    }

}
