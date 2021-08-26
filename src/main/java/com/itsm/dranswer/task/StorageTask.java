package com.itsm.dranswer.task;

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
}
