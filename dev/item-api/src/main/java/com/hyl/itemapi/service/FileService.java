package com.hyl.itemapi.service;

import com.hyl.itemapi.exception.CustomInternalServerErrorException;
import com.hyl.itemapi.model.AuthorizedFileContentType;
import com.hyl.itemapi.model.Item;
import com.hyl.itemapi.model.Picture;
import com.hyl.itemapi.model.SubItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

@Component
public class FileService {


    //************************************************** PARAMS
    private static int fileNameMaxLength;
    private static String localUrl;
    private static String separator = "-";


    //************************************************** SETTERS
    @Value("${hyl.constraint.file.name.maxlength}")
    public void setFileNameLength(String fileNameMaxLength) {
        FileService.fileNameMaxLength = Integer.parseInt(fileNameMaxLength);
    }

    @Value("${hyl.url.localstorage}")
    public void setLocalUrl(String localUrl) {
        FileService.localUrl = localUrl;
    }

    @Value("${hyl.url.separator}")
    public void setSeparator(String separator) {
        FileService.separator = separator;
    }

    //************************************************** METHODES
    protected static Hashtable<String, String> saveMultipartFile(SubItem subItem, List<MultipartFile> files) {
        return FileService.saveMultipartFile( FileService.convertListToTable(files, subItem),
                "user"+subItem.getItem().getIdUser()+"/item"+subItem.getItem().getId()+"/sub"+subItem.getId()+"/img");
    }

    protected static Hashtable<String, String> saveMultipartFile( Hashtable<String, MultipartFile> table, String url) {
        Hashtable<String, String> urlTable = new Hashtable<>();
        Enumeration<String> keys = table.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            MultipartFile multipartFile = table.get(key);

            String url0 = url + key.substring( key.lastIndexOf(separator) ) +
                    AuthorizedFileContentType.getExtensionByMimeType(multipartFile.getContentType());

            // Neccessaire pour mettre à jour les url de Picture en bdd
            urlTable.put(key.substring( key.lastIndexOf(separator) + 1), url0);

            File file = new File(URI.create(localUrl + url0));
            file.mkdirs();

            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                throw new CustomInternalServerErrorException(
                        "Une erreur interne a empêché la sauvegarde du fichier "+multipartFile.getOriginalFilename());
            }
        }
        return urlTable;

    }

    protected static void deleteFile(Item item) {
        String url = "user"+item.getIdUser()+"/item"+item.getId();
        new File(URI.create(localUrl + url)).delete();
    }

    protected static void deleteFile(SubItem subItem) {
        if (subItem.getUrlImages() != null) subItem.getUrlImages().forEach(FileService::deleteFile);
        String url = "user"+subItem.getItem().getIdUser()+"/item"+subItem.getItem().getId()+"/sub"+subItem.getId();
        new File(URI.create(localUrl + url)).delete();
    }

    protected static void deleteFile(Picture picture) {
        new File(URI.create(localUrl + picture.getUrl())).delete();
    }

    private static Hashtable<String, MultipartFile> convertListToTable(List<MultipartFile> files, SubItem subItem) {
        Hashtable<String, MultipartFile> table = new Hashtable<>();
        int finalLength = files.size();
        int length = 0;
        while (length != finalLength) {
            for (Picture picture : subItem.getUrlImages()) {
                MultipartFile fileToRemove = null;
                for (MultipartFile file : files) {
                    String fileName = FileService.defineFileName(file.getOriginalFilename(), file.getName());
                    if ( picture.getName().equals( fileName ) ) {
                        String key = fileName + separator + picture.getId();
                        table.put(key, file);
                        fileToRemove = file;
                        length++;
                        break;
                    }
                }
                if ( fileToRemove != null ) files.remove(fileToRemove);
                if (length == finalLength) break;
            }
        }
        return table;
    }

    protected static String defineFileName(String originalName, String basicName) {
        // Retire l'extention du nom du fichier -> ex : image.png -> image
        if ( originalName != null && !originalName.isBlank() ) {
            originalName = originalName.substring(0, originalName.lastIndexOf("."));
        }
        return (originalName != null && !originalName.isBlank()
                ? originalName.substring(0, (Math.min(originalName.length(), fileNameMaxLength)))
                : basicName.substring(0, (Math.min(basicName.length(), fileNameMaxLength))));
    }
}
