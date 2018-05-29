package com.ms.consumer.kafka;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class KafkaOrder {

    //线程池
    private ExecutorService threadPool;

    //任务
    private List<KafkaOrderTask> kafkaOrderTaskList;

    //初始化线程池、order任务线程列表
    public KafkaOrder(int threadNum,String orderTopic,String servers,String groupId){
        threadPool = new ThreadPoolExecutor(threadNum,500,1000,TimeUnit.MILLISECONDS
        ,new LinkedBlockingDeque<Runnable>(1024),new ThreadPoolExecutor.AbortPolicy());
        kafkaOrderTaskList = new ArrayList<KafkaOrderTask>();
        for(int i = 0;i<threadNum;i++){
            KafkaOrderTask orderTask = new KafkaOrderTask(servers,orderTopic,groupId);
            kafkaOrderTaskList.add(orderTask);
        }
    }

    public void execute(){
        for(KafkaOrderTask orderTask : kafkaOrderTaskList){
            //开启多个消费者线程进行消费创建订单
            threadPool.submit(orderTask);
        }
    }
}
