package com.scau.cfd;

import java.nio.charset.StandardCharsets;

public class OurFile {
    byte[] filename;
    byte[] type;
    byte attribute;     //文件的属性，用1个字节表示，所以此用char类型
    int number;      //文件起始盘块号
    int length;      //文件长度
    public OurFile(String filename)
    {
        this.filename = new byte[3];
        byte[] theName = filename.getBytes(StandardCharsets.US_ASCII);
        for (int i = 0; i < 3 && i < theName.length; i++) {
            this.filename[i] = theName[i];
        }
    }
}
