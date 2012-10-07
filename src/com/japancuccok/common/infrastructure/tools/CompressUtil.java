package com.japancuccok.common.infrastructure.tools;

import java.io.*;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Nagy Gergely
 * Date: 2012.07.10.
 * Time: 9:58
 */
public class CompressUtil {
    public static Object uncompress(byte[] bytes) {
        if (bytes==null)
            return null;

        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

            Inflater def = new Inflater();
            InflaterInputStream dis = new InflaterInputStream(bais, def, 4 * 1024);
            ObjectInputStream objectIn = new ObjectInputStream(dis);
            Object value2 = objectIn.readObject();
            objectIn.close();

            return value2;
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] compress(Object value) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Deflater def = new Deflater(Deflater.BEST_SPEED);
        DeflaterOutputStream dos = new DeflaterOutputStream(baos, def, 4 * 1024);

        try {
            ObjectOutputStream objectOut = new ObjectOutputStream(dos);

            objectOut.writeObject(value);
            objectOut.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        byte[] bytes = baos.toByteArray();

        return bytes;
    }
}
