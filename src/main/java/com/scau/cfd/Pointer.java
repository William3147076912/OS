package com.scau.cfd;

/**
 * 表示文件读写指针的类。
 */
public class Pointer {
    int dnum;    // 磁盘盘块号
    int bnum;    // 磁盘盘块内第几个字节

    /**
     * 构造函数，初始化指针的磁盘盘块号和字节位置。
     *
     * @param dnum 磁盘盘块号
     * @param bnum 磁盘盘块内第几个字节
     */
    public Pointer(int dnum, int bnum) {
        this.dnum = dnum;
        this.bnum = bnum;
    }
}
