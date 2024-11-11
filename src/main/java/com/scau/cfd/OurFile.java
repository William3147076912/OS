package com.scau.cfd;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.nio.charset.StandardCharsets;

/**
 * 表示一个文件的信息。
 */
@Data
@AllArgsConstructor
public class OurFile {
    byte[] filename;     // 文件名
    byte[] type;         // 文件类型
    byte attribute;      // 文件的属性，用1个字节表示
    int number;          // 文件起始盘块号
    int length;          // 文件长度

    public OurFile(String filename, String type, int attribute, int number, int length) {
        this.filename = filename.getBytes();
        this.type = type.getBytes();
        this.attribute = (byte) attribute;
        this.number = number;
        this.length = length;
    }

    /**
     * 构造函数，根据文件名初始化文件信息。
     *
     * @param filename 文件名
     */
    public OurFile(String filename) {
        this.filename = new byte[3];
        byte[] theName = filename.getBytes(StandardCharsets.US_ASCII);
        for (int i = 0; i < 3 && i < theName.length; i++) {
            this.filename[i] = theName[i];
        }
    }


    public String getName() {
        return new String(filename);
    }

    public void setName(String name) {
        filename = name.getBytes();
    }

    public String getType() {
        return new String(type);
    }

    public void setType(String type) {
        this.type = type.getBytes();
    }

    public int getAttribute() {
        return attribute;
    }

    public String getNumber() {
        return String.valueOf(number);
    }

    public String getLength() {
        return String.valueOf(length);
    }
}
