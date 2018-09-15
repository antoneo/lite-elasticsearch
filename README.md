# lite-elasticsearch
java api elasticsearch简单封装

elasticsearch 6.3.0
使用实体注解来简化对es的操作


使用时只需要在springboot

1.引入jar包lite-elasticsearch-0.0.1.jar

2.application.yml里配置

#spring
spring:
  elasticsearch:
      host: 127.0.0.1  #修改成自己elasticsearch的地址
      port: 9300

    
3.启动时配上bean
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

4.编写实体类集成Model类
		@Data
		@IndexName(index="accounts",type="person")  //使用注解配置索引和类
		public class Accounts extends Model<Accounts> implements Serializable {
    		private static final long serialVersionUID = -8809789151255187301L;
   	 		@DocumentID			//DocumentID指的是es的_id
    		private String id;
    		private String user;
   			private String title;
    		private String desc;
		}
	
	5.定义一个dao继承BaseDao即可
		@Component
		public class AccountsDao extends BaseDao<Accounts> {
		}
	
	7.具体看demo吧 
