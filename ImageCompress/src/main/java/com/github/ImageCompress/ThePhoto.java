package com.github.ImageCompress;

import java.io.Serializable;

/***
 *   created by zhongrui on 2019/2/27
 */
public class ThePhoto implements Serializable {
    public String originalPath;
    public String compressPath;

    public ThePhoto() {

    }
    public ThePhoto(String originalPath) {
        this.originalPath = originalPath;
    }
    public ThePhoto(String originalPath, String compressPath) {
        this.originalPath = originalPath;
        this.compressPath = compressPath;
    }
}
