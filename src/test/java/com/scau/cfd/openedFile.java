package com.scau.cfd;

public class openedFile {
    String pathAndFilename;     //已打开文件表中读、写指针的结构
    byte attribute;     //文件的属性，用1个字节表示，所以此用char类型
    int number;      //文件起始盘块号
    int length;      //文件起始盘块号
    int flag;        //操作类型，用“0”表示以读操作方式打开文件，用“1”表示以写操作方式打开文件
    //读文件的位置，文件打开时dnum为文件起始盘块号，bnum为“0”
    pointer read;
    //写文件的位置，文件刚建立时dnum为文件起始盘块号，bnum为“0 ，打开文件时dnum和bnum为文件的末尾位置
    pointer write;
    public openedFile(String pathAndFilename)
    {
        this.pathAndFilename=pathAndFilename;
    }

}