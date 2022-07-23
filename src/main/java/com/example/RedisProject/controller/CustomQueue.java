package com.example.RedisProject.controller;

import com.example.RedisProject.Service.QueueInterface;
import lombok.extern.slf4j.Slf4j;
import com.example.RedisProject.model.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component("customqueue")
public class CustomQueue implements QueueSelector {
    @Qualifier
    private QueueInterface queueInterface;

    public CustomQueue() {
    }

    //    @Autowired
//    @Qualifier("impl")
//    QueueInterface<UserRequest> queueInterface;
    @Override
    public void enque(UserRequest userRequest) {
        queueInterface.enque(userRequest);
        log.info(" customQ - Enque Method has been Accessed...");
    }

    @Override
    public Object deque() throws NullPointerException {
        Object deletedElement =  queueInterface.deque();
        if (deletedElement == null ) {
            log.info(" customQ - deque has been Accessed But Queue is Empty...");
            return "empty queue";
        } else {
            log.info(" customQ - deque has been Accessed ...");
            return deletedElement;
        }
    }

    @Override
    public int size() {
        log.info(" customQ - size has been Accessed ...");
        return queueInterface.size();
    }
}
