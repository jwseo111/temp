package com.itsm.dranswer.task;

/*
 * @package : com.itsm.dranswer.task
 * @name : StorageTask.java
 * @date : 2021-10-08 오후 2:46
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StorageTask {

    private final StorageService storageService;

    @Autowired
    public StorageTask(StorageService storageService) {
        this.storageService = storageService;
    }

    @Scheduled(fixedRate =  1000*60*60*12)
    public void setBucketSize() {
        storageService.bucketSize();
    }

    @Scheduled(fixedRate = 1000*60*60)
    public void expiredUseStorage() {
        storageService.alarmExpiredUseStorage();
    }
}
