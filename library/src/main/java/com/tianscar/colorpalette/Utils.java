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
