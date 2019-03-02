package com.github.ImageCompress;

import android.content.Context;

import java.util.List;

/***
 *   created by zhongrui on 2019/2/24
 */
public class MyCompress {
    private CompressManager manager;

    private MyCompress(CompressManager compressManager) {
        this.manager = compressManager;
    }

    public void startCompress() {
        manager.startCompress();
    }

    public static Builder get(Context context) {
        return new Builder(context);
    }

    public static BuilderPixel getBuilderPixel(Context context) {
        return new BuilderPixel(context);
    }

    public static BuilderQuality getBuilderQuality(Context context) {
        return new BuilderQuality(context);
    }

    /*像素+质量压缩*/
    public static class Builder {
        private CompressConfig config;
        private CompressManager manager;

        private Builder(Context context) {
            config = new CompressConfig(context);
            config.setCompressType(CompressConfig.type_pixel_quality);
            manager = new CompressManager(config);
        }

        public Builder setCompressListener(CompressListener compressListener) {
            this.manager.setCompressListener(compressListener);
            return this;
        }

        public Builder setCompressListener(CompressSingleListener compressSingleListener) {
            this.manager.setCompressSingleListener(compressSingleListener);
            return this;
        }

        public Builder setCompressListener(CompressObjListener compressObjListener) {
            this.manager.setCompressObjListener(compressObjListener);
            return this;
        }

        public Builder setPath(String path) {
            this.manager.setImagePath(path);
            return this;
        }

        public Builder setPathList(List<String> pathList) {
            this.manager.setImagePathList(pathList);
            return this;
        }

        public Builder setPhotoList(List<ThePhoto> photoList) {
            this.manager.setPhotoList(photoList);
            return this;
        }

        public Builder setMaxWidthPixel(int maxWidthPixel) {
            this.config.setMaxWidthPixel(maxWidthPixel);
            return this;
        }

        public Builder setMaxHeightPixel(int maxHeightPixel) {
            this.config.setMaxHeightPixel(maxHeightPixel);
            return this;
        }


        public Builder setQuality(int quality) {
            this.config.setQuality(quality);
            return this;
        }

        public Builder setEachCompressQuality(int eachCompressQuality) {
            this.config.setEachCompressQuality(eachCompressQuality);
            return this;
        }

        public Builder setMaxFileSize(float maxFileSize) {
            this.config.setMaxFileSize(maxFileSize);
            return this;
        }

        public Builder setIgnoreFileSize(float ignoreFileSize) {
            this.config.setIgnoreFileSize(ignoreFileSize);
            return this;
        }

        public Builder setCacheDir(String cacheDir) {
            this.config.setCacheDir(cacheDir);
            return this;
        }

      /*  public Builder setReserveRaw(boolean reserveRaw) {
            this.config.setReserveRaw(reserveRaw);
            return this;
        }*/

        public void start() {
            MyCompress compress = new MyCompress(manager);
            compress.startCompress();
        }
    }

    /*像素压缩*/
    public static class BuilderPixel {
        private CompressConfig config;
        private CompressManager manager;

        public BuilderPixel(Context context) {
            config = new CompressConfig(context);
            config.setCompressType(CompressConfig.type_pixel);
            manager = new CompressManager(config);
        }

        public BuilderPixel setCompressListener(CompressListener compressListener) {
            this.manager.setCompressListener(compressListener);
            return this;
        }

        public BuilderPixel setCompressListener(CompressSingleListener compressSingleListener) {
            this.manager.setCompressSingleListener(compressSingleListener);
            return this;
        }

        public BuilderPixel setCompressListener(CompressObjListener compressObjListener) {
            this.manager.setCompressObjListener(compressObjListener);
            return this;
        }

        public BuilderPixel setPath(String path) {
            this.manager.setImagePath(path);
            return this;
        }

        public BuilderPixel setPathList(List<String> pathList) {
            this.manager.setImagePathList(pathList);
            return this;
        }

        public BuilderPixel setPhotoList(List<ThePhoto> photoList) {
            this.manager.setPhotoList(photoList);
            return this;
        }

        public BuilderPixel setMaxWidthPixel(int maxWidthPixel) {
            this.config.setMaxWidthPixel(maxWidthPixel);
            return this;
        }

        public BuilderPixel setMaxHeightPixel(int maxHeightPixel) {
            this.config.setMaxHeightPixel(maxHeightPixel);
            return this;
        }

        public BuilderPixel setIgnoreFileSize(float ignoreFileSize) {
            this.config.setIgnoreFileSize(ignoreFileSize);
            return this;
        }

        public BuilderPixel setCacheDir(String cacheDir) {
            this.config.setCacheDir(cacheDir);
            return this;
        }
      /*  public Builder setReserveRaw(boolean reserveRaw) {
            this.config.setReserveRaw(reserveRaw);
            return this;
        }*/

        public void start() {
            MyCompress compress = new MyCompress(manager);
            compress.startCompress();
        }

    }

    /*质量压缩*/
    public static class BuilderQuality {
        private CompressConfig config;
        private CompressManager manager;

        public BuilderQuality(Context context) {
            config = new CompressConfig(context);
            config.setCompressType(CompressConfig.type_quality);
            manager = new CompressManager(config);
        }

        public BuilderQuality setCompressListener(CompressListener compressListener) {
            this.manager.setCompressListener(compressListener);
            return this;
        }

        public BuilderQuality setCompressListener(CompressSingleListener compressSingleListener) {
            this.manager.setCompressSingleListener(compressSingleListener);
            return this;
        }

        public BuilderQuality setCompressListener(CompressObjListener compressObjListener) {
            this.manager.setCompressObjListener(compressObjListener);
            return this;
        }

        public BuilderQuality setPath(String path) {
            this.manager.setImagePath(path);
            return this;
        }

        public BuilderQuality setPathList(List<String> pathList) {
            this.manager.setImagePathList(pathList);
            return this;
        }

        public BuilderQuality setPhotoList(List<ThePhoto> photoList) {
            this.manager.setPhotoList(photoList);
            return this;
        }

        public BuilderQuality setQuality(int quality) {
            this.config.setQuality(quality);
            return this;
        }

        public BuilderQuality setEachCompressQuality(int eachCompressQuality) {
            this.config.setEachCompressQuality(eachCompressQuality);
            return this;
        }

        public BuilderQuality setMaxFileSize(float maxFileSize) {
            this.config.setMaxFileSize(maxFileSize);
            return this;
        }

        public BuilderQuality setIgnoreFileSize(float ignoreFileSize) {
            this.config.setIgnoreFileSize(ignoreFileSize);
            return this;
        }

        public BuilderQuality setCacheDir(String cacheDir) {
            this.config.setCacheDir(cacheDir);
            return this;
        }
      /*  public Builder setReserveRaw(boolean reserveRaw) {
            this.config.setReserveRaw(reserveRaw);
            return this;
        }*/

        public void start() {
            MyCompress compress = new MyCompress(manager);
            compress.startCompress();
        }
    }
}
