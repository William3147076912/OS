package com.scau.cfd.manage;

import lombok.NoArgsConstructor;

/**
 * 表示文件读写指针的类。
 */
//@AllArgsConstructor
@NoArgsConstructor
public class Pointer {
    int dnum;    // 磁盘盘块号
    int bnum;    // 磁盘盘块内第几个字节

    Pointer(int dnum, int bnum) {
        this.dnum = dnum;
        this.bnum = bnum;
    }
}
