package com.hyl.itemapi.model;

import com.hyl.itemapi.exception.CustomInternalServerErrorException;

public enum AuthorizedFileContentType {

    JPG("image/jpg", ".jpg"),
    JPEG("image/jpeg", ".jpeg"),
    PNG("image/png", ".png");

    private String mimeType;
    private String extension;

    AuthorizedFileContentType(String mimeType, String extension) {
        this.mimeType = mimeType;
        this.extension = extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public static String getExtensionByMimeType(String mimeType) {
        for (AuthorizedFileContentType value : AuthorizedFileContentType.values()) {
           if (value.getMimeType().equals(mimeType)) return value.getExtension();
        }
        throw new CustomInternalServerErrorException("Le type '"+mimeType+"' n'est pas autorisé.");
    }
}
