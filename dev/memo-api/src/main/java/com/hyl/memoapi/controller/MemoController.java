package com.hyl.memoapi.controller;

import com.hyl.memoapi.exception.CustomBadRequestException;
import com.hyl.memoapi.model.Memo;
import com.hyl.memoapi.model.ReminderByDate;
import com.hyl.memoapi.service.MemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/*

TODO
CLASS EN CONSTRUCTION

 */
@RestController
@RequestMapping(path = "memo")
public class MemoController {

    //************************************************ LOGGER
    Logger logger = LoggerFactory.getLogger(MemoController.class);

    //************************************************ BEANS
    private final MemoService memoService;

    //************************************************ CONSTRUCTOR
    @Autowired
    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }


    //************************************************ METHODES
    @GetMapping("/memos-by-id_user")
    public List<Memo> getMemosByIdUser(HttpServletRequest request) {
        return memoService.doGetMemosByIdUser(extractIdUserFromHeader(request));
    }

    @PostMapping("/add-memo")
    public Memo addMemo(@RequestBody Memo memo, HttpServletRequest request) {
        long id = Math.round(Math.random()*100);
        memo.setId(id);
        if (memo.getReminderByDate() != null) {
            memo.getReminderByDate().forEach(reminderByDate -> {
                reminderByDate.setId(id);
                reminderByDate.setMemo(memo);
            });
        }
        if (memo.getReminderByDay() != null) {
            memo.getReminderByDay().setId(id);
            memo.getReminderByDay().setMemo(memo);
        }
        logger.error(memo.toString());
        return memo;
    }

    @PatchMapping("/update-memo")
    public Memo updateMemo(@RequestBody Memo memo, HttpServletRequest request) {
        long id = Math.round(Math.random()*100);
        if (memo.getReminderByDate() != null) {
            memo.getReminderByDate().forEach(reminderByDate -> {
                reminderByDate.setMemo(memo);
            });
        }
        if (memo.getReminderByDay() != null) {
            memo.getReminderByDay().setId(id);
            memo.getReminderByDay().setMemo(memo);
        }

        if (memo.getReminderByDate() != null) {
            List<ReminderByDate> list = new ArrayList<>();
            for (int i = 0; i < memo.getReminderByDate().size(); i++) {
                if (memo.getReminderByDate().get(i).getReminderDate() == null) {
                    list.add(memo.getReminderByDate().get(i));
                }
            }
            for (ReminderByDate reminderByDate : list) {
                memo.getReminderByDate().remove(reminderByDate);
            }
        }
        logger.error(memo.toString());
        return memo;
    }

    @DeleteMapping("/delete-memo")
    public void updateMemo(@RequestParam long id, HttpServletRequest request) {
        logger.error("id->"+id);
    }

    private long extractIdUserFromHeader (HttpServletRequest request) {
        String idUserStr = request.getHeader("idUser");
        if ( idUserStr == null ) {
            throw new CustomBadRequestException("Aucun utilisateur n'est spécifié dans le header 'idUser' de la requête.");
        }

        try {
            return Long.parseLong(idUserStr);
        } catch (NumberFormatException e) {
            throw new CustomBadRequestException("L'id de l'utilisateur doit être un nombre.");
        }
    }
}
