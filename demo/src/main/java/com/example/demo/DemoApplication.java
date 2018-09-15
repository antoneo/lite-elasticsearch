package com.example.demo;

import org.apache.log4j.Logger;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
public class DemoApplication {
    private static final Logger log=Logger.getLogger(DemoApplication.class);

    @Value("${spring.elasticsearch.host}")
    String host;
    @Value("${spring.elasticsearch.port}")
    int port;


    @Bean
    public TransportClient client(){
        TransportClient client = null;
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(host), port));
        } catch (UnknownHostException e) {
            log.error("创建elasticsearch客户端失败");
            e.printStackTrace();
        }
        log.info("创建elasticsearch客户端成功");
        return client;
    }


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
