## 为什么有这个项目？
公司办公APP为Skype，需要实现企业微信或钉钉机器人的功能，将监控数据发送到通知群中，以便运营去处理。
但是Skype没有提供类似的接口，所以调用Skype-Web API来实现上述功能
## 各模块介绍
### authentication
认证模块，通过Skype账号密码登录获取接口访问令牌
1.通过账号密码获取SkypeToken
2.通过SkypeToken获取RegistrationToken
### common
存放公用工具类、实体对象、公用常量值
### send
发送消息，用获取的RegistrationToken调用发送消息接口
### events
通过定时任务调用接口读取未读的消息，把未读消息发送到RabbitMQ中的eventsQueue队列，以便消费者处理
