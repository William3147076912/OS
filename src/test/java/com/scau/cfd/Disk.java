package com.scau.cfd;

import jdk.jfr.Unsigned;

import java.io.*;

public class Disk {
    File file;
    private long volume;

    public Disk(File file) {
        if (file.exists()) {
            this.file = file;
            this.volume = file.length();
        } else {
            System.out.println("文件不存在");
        }
    }

    public static void setVolume(File file, int volume) {

        byte[] hello = {48};
        try (
                FileOutputStream fos = new FileOutputStream(file)) {
            for (int i = 0; i < volume; i++) {
                fos.write(hello);
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public long getVolume() {
        return volume;
    }

    public void format() {
        try {
            RandomAccessFile file = new RandomAccessFile(this.file, "rw");

            file.seek(0);
            byte[] buffer = new byte[128];
            file.write(buffer);
            file.seek(128);

            for (int i = 0; i < 9; i++) {
                file.write('$');
                file.seek(128 + i * 8);
            }
            file.seek(0);
            file.write(new byte[]{(byte) 255, (byte) 255, (byte) 255});
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte findEmpty() {
        byte[] one = new byte[1];
        try {
            RandomAccessFile file = new RandomAccessFile(this.file, "rw");
            for (int i = 3; i < 128; i++) {
                file.seek(i);
                file.read(one, 0, 1);
                if (one[0] == (byte) 0) {
                    return (byte) i;
                }
            }
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }
}
