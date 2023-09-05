package com.zid.zid.entity;



import lombok.Data;

@Data
public class LoadFile {
    private String id;
    private String filename;
    private String fileType;
    private String fileSize;
    private byte[] file;

}