package com.farmers.underground.ui.utils;

import android.os.Bundle;
import android.util.SparseArray;

import com.farmers.underground.FarmersApp;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ImageCacheManager {

    public interface ImageLoaderCallbacks {
        @NotNull
        ImageLoader onCreateImageLoader(int loaderId, @Nullable Bundle params);
    }

    private final ImageLoaderCallbacks imageLoaderCallbacks;

    @SuppressWarnings("UnusedDeclaration")
    private ImageCacheManager(@NotNull ImageLoaderCallbacks imageLoaderCallbacks) {
        this.imageLoaderCallbacks = imageLoaderCallbacks;
    }

    private final SparseArray<ImageLoader> loaderCache = new SparseArray<>(1);

    public ImageLoader imageLoaderGet(int loaderId, @Nullable Bundle params) {
        ImageLoader imageLoaderInstance;

        synchronized (loaderCache) {
            imageLoaderInstance = loaderCache.get(loaderId);

            if (imageLoaderInstance != null
                    && params != null && !params.isEmpty()) {
                imageLoaderInstance.destroy();

                imageLoaderInstance = null;
            }

            if (imageLoaderInstance == null) {
                loaderCache.put(loaderId, imageLoaderInstance
                        = imageLoaderCallbacks.onCreateImageLoader(loaderId, params));
            }
        }
        return imageLoaderInstance;
    }

    public ImageLoader imageLoaderGet(int loaderId) {
        return imageLoaderGet(loaderId, null);
    }

    private static final ImageCacheManager singleton = new ImageCacheManager(FarmersApp.getInstance().getImageCache());

    public static ImageCacheManager getInstance() {
        return singleton;
    }

    public static ImageLoader getImageLoader(int loaderId) {
        return getInstance().imageLoaderGet(loaderId);
    }
}
