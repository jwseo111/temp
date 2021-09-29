package com.itsm.dranswer.config;

import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {
    private net.sf.ehcache.CacheManager createCacheManager() {
        net.sf.ehcache.config.Configuration configuration = new net.sf.ehcache.config.Configuration();
        configuration.diskStore(new DiskStoreConfiguration().path("java.io.tmpdir"));
        return net.sf.ehcache.CacheManager.create(configuration);
    }

    @Bean
    public EhCacheCacheManager ehCacheCacheManager() {
        net.sf.ehcache.CacheManager manager = this.createCacheManager();

        Cache getNCloudKeys = new Cache(new CacheConfiguration()
                .maxEntriesLocalHeap(1000)
                .maxEntriesLocalDisk(10000)
                .eternal(false)
                .timeToIdleSeconds(1800)
                .timeToLiveSeconds(1800)
                .memoryStoreEvictionPolicy("LFU")
                .transactionalMode(CacheConfiguration.TransactionalMode.OFF)
                .persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP))
                .name("getNCloudKeys")
        );
        manager.addCache(getNCloudKeys);
        return new EhCacheCacheManager(manager);
    }

}
