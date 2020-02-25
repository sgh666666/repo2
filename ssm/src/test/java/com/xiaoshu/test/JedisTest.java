package com.xiaoshu.test;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisTest {
    @Test
    public void test01(){
        Jedis jedis = new Jedis("192.168.135.129",6379);
        jedis.set("baby","boy");
        String baby = jedis.get("baby");
        System.out.println(baby);
    }
    @Test
    public void test02(){
        JedisPool jedisPool = new JedisPool("192.168.135.129",6379);
        Jedis jedis = jedisPool.getResource();
        jedis.set("baby1","boy1");
        String baby = jedis.get("baby1");
        System.out.println(baby);
        jedis.close();
    }
}
