package com.itsm.dranswer.task;

/*
 * @package : com.itsm.dranswer.task
 * @name : StorageTask.java
 * @date : 2021-10-08 오후 2:46
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.itsm.dranswer.instance.EnvInstanceService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EnvTask {

    private final EnvInstanceService envInstanceService;

    public EnvTask(EnvInstanceService envInstanceService) {
        this.envInstanceService = envInstanceService;
    }

    @Scheduled(fixedRate = 1000*60*60)
    public void expiredEnvironment() {
        envInstanceService.alarmExpiredEnvironment();
    }
}
