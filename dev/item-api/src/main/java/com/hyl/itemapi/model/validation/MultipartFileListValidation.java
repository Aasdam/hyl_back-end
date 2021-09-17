package com.hyl.itemapi.model.validation;

import com.hyl.itemapi.exception.CustomBadRequestException;
import com.hyl.itemapi.exception.CustomInternalServerErrorException;
import com.hyl.itemapi.model.AuthorizedFileContentType;
import com.hyl.itemapi.model.Picture;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.util.List;

@Component
public class MultipartFileListValidation {


    //************************************************** GLOBAL VALIDATION
    public static void validMultipartFileList(List<MultipartFile> multipartFiles, List<Picture> filesAlreadySaved) {
        validContentType(multipartFiles);
        validSize(multipartFiles, filesAlreadySaved);
    }


    //************************************************** PARAM
    private static String localUrl;
    private static String sizeMax;
    private static Tika tika = new Tika();


    //************************************************** SETTER -> @Value works (doesn't work on static field)
    @Value("${hyl.url.localstorage}")
    public void setLocalUrl(String localUrl) {
        MultipartFileListValidation.localUrl = localUrl;
    }

    @Value("${hyl.constraint.subitem.image.maxsize}")
    private void setSizeMax(String sizeMax) {
        MultipartFileListValidation.sizeMax = sizeMax;
    }


    //************************************************** VALIDATIONS
    private static void validSize(List<MultipartFile> multipartFiles, List<Picture> filesAlreadySaved) {
        long size = 0;

        if (filesAlreadySaved != null) {
            for (Picture picture : filesAlreadySaved) {
                long length = new File(URI.create(localUrl + picture.getUrl())).length();
                size += length;
            }
        }

        for (MultipartFile multipartFile : multipartFiles) {
            size += multipartFile.getSize();
        }

        if (size > Long.parseLong(sizeMax)) {
            throw new CustomBadRequestException("La somme du poids des images par sous-objet " +
                                    "doit être au maximum de "+sizeMax+" octets -> poids calculé -> "+size+" octets.");
        }
    }

    private static void validContentType(List<MultipartFile> multipartFiles) {
        multipartFiles.forEach(multipartFile -> {
          String mimeType = getRealContentType(multipartFile);
            boolean isValid = false;
            for (AuthorizedFileContentType authorizedFileContentType : AuthorizedFileContentType.values()) {
                if (authorizedFileContentType.getMimeType().equals(mimeType)) {
                    isValid = true;
                    break;
                }
            }
            if ( !isValid ) {
                throw new CustomBadRequestException( "Le type '" +mimeType+ "' " +
                        "du fichier '" +multipartFile.getOriginalFilename()+ "' n'est pas autorisé.");
            }
        });
    }


    //************************************************** SHARE
    public static String getRealContentType(MultipartFile multipartFile) {
        String mimeType;
        try {
            InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(multipartFile.getBytes()));
            mimeType = tika.detect(inputStream);
            inputStream.close();
            return mimeType;
        } catch (IOException e) {
            throw new CustomInternalServerErrorException("La lecture du fichier "+multipartFile.getOriginalFilename()+" a échoué.");
        }

    }

}
