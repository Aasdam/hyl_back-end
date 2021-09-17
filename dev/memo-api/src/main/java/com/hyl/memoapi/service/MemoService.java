package com.hyl.memoapi.service;

import com.hyl.memoapi.dao.MemoDao;
import com.hyl.memoapi.model.Memo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemoService {

    //************************************************ LOGGER
    Logger logger = LoggerFactory.getLogger(MemoService.class);

    //************************************************ BEANS
    private final MemoDao memoDao;

    //************************************************ CONSTRUCTOR
    @Autowired
    public MemoService(MemoDao memoDao) {
        this.memoDao = memoDao;
    }


    //************************************************ METHODES
    public List<Memo> doGetMemosByIdUser(Long idUser) {
        return memoDao.findAllByIdUser(idUser);
    }

}
