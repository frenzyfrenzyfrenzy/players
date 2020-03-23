package com.svintsov.players;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;

/**
 * Utils.
 *
 * @author Ilya_Svintsov
 */
@UtilityClass
public class Utils {

    public String readString(InputStream inputStream) throws IOException {
        byte[] bytes = new byte[1000];
        int bytesRead = inputStream.read(bytes);
        return new String(bytes, 0, bytesRead);
    }

}
