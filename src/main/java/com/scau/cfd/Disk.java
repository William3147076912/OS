package com.scau.cfd;

import lombok.Getter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Disk 类表示一个磁盘，提供磁盘的初始化、格式化和查找空闲块的功能。
 */
public class Disk {
    File file; // 磁盘文件
    @Getter
    private long volume; // 磁盘容量

    /**
     * 构造函数，初始化磁盘对象。
     *
     * @param file 磁盘文件
     */
    public Disk(File file) {
        if (file.exists()) {
            this.file = file;
            this.volume = file.length(); // 设置磁盘容量
        } else {
            System.out.println("文件不存在");
        }
    }

    /**
     * 设置磁盘容量并初始化磁盘内容。
     *
     * @param file   磁盘文件
     * @param volume 磁盘容量
     */
    public static void setVolume(File file, int volume) {
        byte[] hello = {48}; // 初始化数据
        try (
                FileOutputStream fos = new FileOutputStream(file)
        ) {
            for (int i = 0; i < volume; i++) {
                fos.write(hello); // 写入初始化数据
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 格式化磁盘。
     */
    public void format() {
        try {
            RandomAccessFile file = new RandomAccessFile(this.file, "rw"); // 打开磁盘文件进行读写

            file.seek(0);
            byte[] buffer = new byte[128];
            file.write(buffer); // 写入128字节的空缓冲区

            for (int i = 0; i < 8; i++) {
                file.seek(128 + i * 8);
                file.write('$'); // 在每个目录项位置写入'$'表示空闲
            }
            file.seek(0);
            file.write(new byte[]{(byte) 255, (byte) 255, (byte) 255}); // 写入根目录的特殊标识
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查找空闲块。
     *
     * @return 空闲块的位置，如果没有找到返回 0
     */
    public byte findEmpty() {
        byte[] one = new byte[1];
        try {
            RandomAccessFile file = new RandomAccessFile(this.file, "rw"); // 打开磁盘文件进行读写
            for (int i = 3; i < 128; i++) {
                file.seek(i);
                file.read(one, 0, 1);
                if (one[0] == (byte) 0) {
                    return (byte) i; // 返回空闲块的位置
                }
            }
            System.out.println("could not find an empty block");
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return 0; // 没有找到空闲块
    }
}
