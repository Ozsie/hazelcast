package com.github.ozsie.hazelcast

import com.hazelcast.config.Config
import com.hazelcast.config.EvictionPolicy
import com.hazelcast.config.MapConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@SpringBootApplication
@EnableAutoConfiguration
@EnableCaching
//@RestController
//@RequestMapping(value = ["/application"])
//@CacheConfig(cacheNames = ["map"])
class HazelcastApplication {

    @Bean
    fun hazelCastConfig() = Config("instance").apply {
        setProperty("hazelcast.phone.home.enabled", "false")
        addMapConfig(MapConfig("map").apply {
            evictionPolicy = EvictionPolicy.LRU
            timeToLiveSeconds = 240
        })
    }
/*
    @Autowired
    lateinit var cache: UUIDCache

    @GetMapping(path = ["{key}"])
    fun getStuff(@PathVariable("key") key: String) = cache.getUUID(key)*/
}

@RestController
@RequestMapping(value = ["/controller"])
@CacheConfig(cacheNames = ["map"])
class Controller {
    @Autowired
    lateinit var cache: UUIDCache

    @GetMapping(path = ["{key}"])
    fun getStuff(@PathVariable("key") key: String) = cache.getUUID(key)

    @Cacheable(value = ["map"])
    fun getUUID(key: String) = UUID.randomUUID().also { println("Generated $it") }
}

@Cacheable(value = ["map"])
fun getUUIDNoClass(key: String): UUID {
    return UUID.randomUUID().also { println("Generated $it") }
}

@CacheConfig(cacheNames = ["map"])
@Service
class Cache {
    @Cacheable(value = ["map"], key = "#key")
    fun getUUID(key: String): UUID? = UUID.randomUUID().also { println("Generated $it") }
}

fun main(args: Array<String>) {
    runApplication<HazelcastApplication>(*args)
}
