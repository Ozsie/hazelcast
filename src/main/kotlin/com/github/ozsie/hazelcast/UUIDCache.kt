package com.github.ozsie.hazelcast

import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.util.*

@CacheConfig(cacheNames = ["map"])
@Service
class UUIDCache {
    @Cacheable(value = ["map"], key = "#key")
    fun getUUID(key: String) = UUID.randomUUID().also { println("Generated $it") }
}