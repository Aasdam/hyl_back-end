package com.hyl.itemapi.service;

import com.hyl.itemapi.dao.TrackingSheetDao;
import com.hyl.itemapi.exception.CustomBadRequestException;
import com.hyl.itemapi.exception.CustomNotFoundException;
import com.hyl.itemapi.model.SubItem;
import com.hyl.itemapi.model.TrackingSheet;
import com.hyl.itemapi.model.validation.CustomValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TrackingSheetService {


    //************************************************** DAO
    private static TrackingSheetDao trackingSheetDao;


    //************************************************** CONSTRUCTEUR
    @Autowired
    public TrackingSheetService(TrackingSheetDao trackingSheetDao) {
        TrackingSheetService.trackingSheetDao = trackingSheetDao;
    }


    //************************************************** METHODE
    public static String getCommentByIdLoan(Long idLoan) {
        if (idLoan == null) return  null;
        Optional<TrackingSheet> optSheet=  trackingSheetDao.findByIdLoan(idLoan);
        return optSheet.map(TrackingSheet::getComment).orElse(null);
    }

    public static void addTrackingSheet(String comment, Long idSubItem, String reference, Long idLoan, long idUser) {
        TrackingSheet trackingSheet = new TrackingSheet();
        trackingSheet.setDate(new Date());
        trackingSheet.setComment(comment);
        trackingSheet.setIdLoan(idLoan);
        trackingSheet.setSubItem(
                idSubItem != null ? SubItemService.getSubItemById(idSubItem) : SubItemService.getSubItemByRef(reference, idUser));
        CustomValidator.validate(trackingSheet, TrackingSheet.AddValidation.class);
        trackingSheetDao.save(trackingSheet);
    }

    public static SubItem deleteTrackingSheetsByIds(List<String> ids, long idUser) {
        List<Long> idList = new ArrayList<>();
        ids.forEach(id -> idList.add(Long.parseLong(id)));
        List<TrackingSheet> trackingSheets = trackingSheetDao.findAllById(idList);
        checkTrackingSheetIntegrityBeforeDeletion(trackingSheets, idUser);
        trackingSheetDao.deleteAll(trackingSheets);
        return SubItemService.getSubItemById( (!trackingSheets.isEmpty() ? trackingSheets.get(0).getSubItem().getId() : 0) );
    }

    public static SubItem deleteTrackingSheetsByIdSubItem(long idSubItem, long idUser) {
        SubItem subItem = SubItemService.getSubItemById(idSubItem);
        checkTrackingSheetIntegrityBeforeDeletion(subItem.getTrackingSheets(), idUser);
        subItem.getTrackingSheets().clear();
        return SubItemService.save(subItem);
    }

    public static void deleteTrackingSheetsByIdsLoan(List<String> idsLoan, long idUser) {
        List<Long> ids = new ArrayList<>();
        idsLoan.forEach(id -> ids.add(Long.parseLong(id)));
        List<TrackingSheet> trackingSheets = trackingSheetDao.findAllByIdLoanIn(ids);
        checkTrackingSheetIntegrityBeforeDeletion(trackingSheets, idUser);
        trackingSheetDao.deleteAll(trackingSheets);
    }

    public static void deleteTrackingSheet(TrackingSheet trackingSheet) {
        trackingSheetDao.delete(trackingSheet);
    }

    private static void checkTrackingSheetIntegrityBeforeDeletion(List<TrackingSheet> trackingSheets, long idUser) {
        for (TrackingSheet trackingSheet : trackingSheets) {
            if (trackingSheet.getSubItem().getItem().getIdUser() != idUser) {
                throw new CustomBadRequestException("L'id renseigné dans le header ne correspond pas à l'id du propriétaire de l'objet.");
            }
        }
    }
}
