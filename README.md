# mqchannel
> 一个消息传输的小工具，仿照花生壳的功能，将proxy部署到受控机器，worker部署到云环境，实现内网穿透功能，不过功能不太完善。后续有时间再补充。

消息传输采用RabbitMQ, 可以用docker快速的搭建。
```
    docker run -d -t --name rabbitmq -p 9900:5672 -e RABBITMQ_DEFAULT_USER=rabbitadmin -e RABBITMQ_DEFAULT_PASS=admin rabbitmq:management
```



## 现有问题
HTTP转发基本上没什么问题，但是TCP由于时序问题，连接失败率很高，而且长时间未响应会自动断开。
