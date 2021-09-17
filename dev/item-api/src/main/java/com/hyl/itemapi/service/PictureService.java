package com.hyl.itemapi.service;

import com.hyl.itemapi.dao.PictureDao;
import com.hyl.itemapi.exception.CustomNotFoundException;
import com.hyl.itemapi.model.Picture;
import com.hyl.itemapi.model.SubItem;
import com.hyl.itemapi.model.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Optional;

@Service
@Transactional
public class PictureService {


    //************************************************** DAO
    private static PictureDao pictureDao;


    //************************************************** CONSTRUCTEUR
    @Autowired
    public PictureService(PictureDao pictureDao) {
        PictureService.pictureDao = pictureDao;
    }


    //************************************************** METHODES
    protected static Picture getPictureById(long id) {
       Optional<Picture> optPicture = pictureDao.findById(id);
       if (optPicture.isPresent()) return optPicture.get();
       else throw new CustomNotFoundException("L'objet Picture avec pour id="+id+" est introuvable");
    }

    protected static void deletePictureById(SubItem subItem, long id) {
        Picture picture = getPictureById(id);
        subItem.getUrlImages().remove(picture);
        pictureDao.delete(picture);
        FileService.deleteFile(picture);
    }

    protected static Picture buildPicture(SubItem subItem, MultipartFile multipartFile) {

        // Construction de la nouvelle Picture
        Picture picture = new Picture();
        picture.setName(FileService.defineFileName(multipartFile.getOriginalFilename(), multipartFile.getName()));
        picture.setSubItem(subItem);

        // Retour
        return picture;

    }

    /**
     * Met à jour l'url de l'objet Picture
     * @param urlTable La table avec key=idPicture et value=urlPicture
     */
    protected static void majUrlPicture(Hashtable<String, String> urlTable) {
        Enumeration<String> keys = urlTable.keys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            Picture picture = getPictureById(Long.parseLong(key));
            picture.setUrl(urlTable.get(key));
            CustomValidator.validate(picture, Picture.AfterAddFileValidation.class);
            pictureDao.save(picture);
        }
    }

    /**
     * Valide l'objet picture avant un ajout en bdd
     *
     * @param picture La picture à valider
     */
    protected static void validAddPicture(Picture picture) {
        CustomValidator.validate(picture, Picture.AddValidation.class);
    }
}
