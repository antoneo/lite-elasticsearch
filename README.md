# lite-elasticsearch
java api elasticsearch简单封装

elasticsearch 6.3.0
使用实体注解来简化对es的操作


使用时只需要在springboot
1.application.yml里配置

#spring
spring:
  elasticsearch:
      host: 127.0.0.1  #修改成自己elasticsearch的地址
      port: 9300

    
2.启动时配上bean
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
    
    
