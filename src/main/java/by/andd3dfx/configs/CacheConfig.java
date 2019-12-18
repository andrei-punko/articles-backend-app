package by.andd3dfx.configs;

import com.google.common.cache.CacheBuilder;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(final String name) {
                ConcurrentMap<Object, Object> store = CacheBuilder.newBuilder()
                    .expireAfterWrite(30, TimeUnit.MINUTES)
                    .maximumSize(100)
                    .build().asMap();
                return new ConcurrentMapCache(name, store, false);
            }
        };
    }
}
