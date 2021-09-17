package com.hyl.itemapi.service;

import com.hyl.itemapi.controller.ItemController;
import com.hyl.itemapi.dao.SubItemDao;
import com.hyl.itemapi.exception.CustomNotFoundException;
import com.hyl.itemapi.model.Item;
import com.hyl.itemapi.model.SubItem;
import com.hyl.itemapi.model.TrackingSheet;
import com.hyl.itemapi.model.validation.CustomValidator;
import com.hyl.itemapi.proxy.LoanProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Transactional
public class SubItemService {


    //************************************************** DAO
    private static SubItemDao subItemDao;
    private static LoanProxy loanProxy;


    //************************************************** CONSTRUCTEUR
    @Autowired
    public SubItemService(SubItemDao subItemDao, LoanProxy loanProxy) {
        SubItemService.subItemDao = subItemDao;
        SubItemService.loanProxy = loanProxy;
    }

    //************************************************** PARAM
    private static String max;


    //************************************************** SETTER
    @Value("${hyl.constraint.subitem.max}")
    public void setMax(String max) {
        SubItemService.max = max;
    }


    //************************************************** METHODES
    public static void renameSubItem(long idSubItem, String reference, long idUser, String token) {
        SubItem subItem = getSubItemById(idSubItem);
        String oldReference = subItem.getReference();
        if (!oldReference.equals(reference)) {
            subItem.setReference(reference);
            CustomValidator.validate(subItem, SubItem.UpdateValidation.class);

            // Envoi d'une requête vers loan-api pour mettre à jour la référence dans les prêt
            sendReferenceToLoanAPI(token, idUser, oldReference, reference);

            subItemDao.save(subItem);
        }
    }

    public static SubItem addSubItem(String reference, long idItem, List<MultipartFile> files) {
        SubItem subItem = buildSubItem( ItemService.getItemById(idItem), reference, files );
        validAddSubItem(subItem);
        subItemDao.save(subItem);
        if (files != null) {
            Hashtable<String, String> urlTable =  FileService.saveMultipartFile(subItem, files);
            PictureService.majUrlPicture(urlTable);
        }
        return subItem;
    }

    public static void deleteSubItem(SubItem subItem, String token, long idUser) {

        // Si c'est le dernier subitem de l'item -> true -> l'item sera aussi supprimé
        boolean deleteItem = subItem.getItem().getSubItems().size() == 1;

        // Suppresion du subitem de la liste des subitems de l'item (pour que la suppresion en bdd fonctionne -> cascade)
        subItem.getItem().getSubItems().remove(subItem);

        // Envoi d'une requête pour supprimer les prêts associés à la référence du subitem
        HashMap<String, String> header = new HashMap<>();
        header.put(HttpHeaders.AUTHORIZATION, token);
        header.put("idUser", String.valueOf(idUser));
        loanProxy.deleteLoanByReference(header, subItem.getReference());

        // Suppression des fiches de suivi du subitem
        List<TrackingSheet> trackingSheets = new ArrayList<>(subItem.getTrackingSheets());
        trackingSheets.forEach(trackingSheet -> {
            subItem.getTrackingSheets().remove(trackingSheet);
            TrackingSheetService.deleteTrackingSheet(trackingSheet);
        });

        // Suppresion du subitem
        subItemDao.delete(subItem);

        // Suppresion du répertoire contenant les images
        FileService.deleteFile(subItem);

        // Si c'est le dernier subitem de l'item -> true -> l'item sera aussi supprimé
        if (deleteItem) ItemService.deleteItem(subItem.getItem().getId(), token, idUser);
    }

    public static SubItem editSubItem(String reference, SubItem subItem,
                                      List<Long> filesToDel, List<MultipartFile> files,
                                      String token, long idUser) {

        String oldReference = subItem.getReference();

        // Mise à jour de la référence si elle a changé
        if (!oldReference.equals(reference)) {
            subItem.setReference(reference);
            CustomValidator.validate(subItem, SubItem.UpdateValidation.class);
        }

        // Supression des images à supprimer en bdd et sur la machine
        if (filesToDel != null) {
            for (long id : filesToDel) {
                PictureService.deletePictureById(subItem, id);
            }
        }

        // Construction des 'Picture' (stockage des informations des images)
        if (files != null) {
            for (MultipartFile file : files) {
                subItem.getUrlImages().add(PictureService.buildPicture(subItem, file));
            }
        }

        // Envoi d'une requête vers loan-api pour mettre à jour la référence dans les prêt
        if (!oldReference.equals(reference)) {
            sendReferenceToLoanAPI(token, idUser, oldReference, reference);
        }

        // Mise à jour du subitem en bdd avec ses nouvelles images
        subItem = subItemDao.save(subItem);

        if (files != null) {
            // Ajout des nouvelles images sur la machine
            Hashtable<String, String> urlTable = FileService.saveMultipartFile(subItem, files);

            // Mise à jour de l'url des 'Picture' en bdd
            PictureService.majUrlPicture(urlTable);
        }

        // Renvoi du subitem mis à jour
        return subItem;
    }

    protected static SubItem save(SubItem subItem) {
        return subItemDao.save(subItem);
    }

    protected static SubItem buildSubItem(Item item, String reference, List<MultipartFile> files) {

        // Construction du subitem
        SubItem subItem = new SubItem();
        subItem.setReference(reference);
        subItem.setItem(item);
        subItem.setUrlImages(new ArrayList<>());

        if (files != null) {
            // Construction des 'Picture' (stockage des informations des images)
            files.forEach(multipartFile -> subItem.getUrlImages().add(PictureService.buildPicture(subItem, multipartFile)));
        }

        // Retour
        return subItem;
    }

    /**
     * Validation du nouvel subitem et de ses composants avant l'ajout en bdd
     * @param subItem Le nouvel subitem
     */
    protected static void validAddSubItem(SubItem subItem) {
        CustomValidator.validate(subItem, SubItem.AddValidation.class);
        if(subItem.getUrlImages() != null) {
            subItem.getUrlImages().forEach(PictureService::validAddPicture);
        }
    }

    public static boolean checkAtomicRef(String reference, long idUser) {
        List<SubItem> subItems = subItemDao.findByReference(reference);
        for (SubItem subItem : subItems) {
            if (subItem.getItem().getIdUser() == idUser) return false;
        }
        return true;
    }

    public static SubItem getSubItemById(long id) {
        Optional<SubItem> optSubItem = subItemDao.findById(id);
        if ( optSubItem.isPresent() ) return optSubItem.get();
        else throw new CustomNotFoundException("L'objet SubItem avec pour id="+id+" n'a pas été trouvé.");
    }

     public static SubItem getSubItemByRef(String reference, long idUser) {
         List<SubItem> subItems = subItemDao.findByReference(reference);
         for (SubItem subItem : subItems) {
             if (subItem.getItem().getIdUser() == idUser) return subItem;
         }
         throw new CustomNotFoundException("L'objet SubItem avec pour reference="+reference
                                                +" et pour id_utilisateur="+idUser+" n'a pas été trouvé.");
    }

    public static boolean maxSubItemByUserIsValid(long idUser) {
        int maxSubItemAuthorized = Integer.parseInt(max);
        int count = 0;
        List<Item> items = ItemService.getItemsByIdUser(idUser);
        for (Item item : items) {
            for (SubItem subItem : item.getSubItems()) {
                count++;
            }
        }
        return count < maxSubItemAuthorized;
    }

    private static void sendReferenceToLoanAPI(String token, long idUser, String oldReference, String reference) {
        // Envoi d'une requête vers loan-api pour mettre à jour la référence dans les prêt
        HashMap<String, String> header = new HashMap<>();
        header.put(HttpHeaders.AUTHORIZATION, token);
        header.put("idUser", String.valueOf(idUser));
        HashMap<String, String> body = new HashMap<>();
        body.put("oldReference", oldReference);
        body.put("newReference",reference);
        loanProxy.updateReference(header, body);
    }

}
