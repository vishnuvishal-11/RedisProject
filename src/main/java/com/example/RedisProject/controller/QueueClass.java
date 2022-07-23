package com.example.RedisProject.controller;
import com.example.RedisProject.model.UserRequestDto;
import com.example.RedisProject.Repository.UserRequestRepository;
import com.example.RedisProject.Service.QueueInterface;
import com.example.RedisProject.model.Converter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import com.example.RedisProject.model.UserRequest;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@Component
@RestController
@RequestMapping("/queue")
@Slf4j
public class QueueClass {
    @Autowired
    private QueueInterface queueInterface;
    @Value("${dynamic.queue}")
    private String queuestring;
    @Autowired
    @Qualifier("customqueue")
    private QueueSelector queuecustom;
    @Autowired
    @Qualifier("rabbitq")
    private QueueSelector queuerabbit;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    @Qualifier("convert")
    private Converter converter;
    @Autowired
    private UserRequestRepository userRequestRepository;

    @GetMapping("/display")
    public List<UserRequestDto> display() {
        List<UserRequest> findAll = queueInterface.display();
        return converter.entityToDto(findAll);
    }

    @GetMapping("/size")
    public int size() {
        int size = 0;
        if (queuestring.equalsIgnoreCase("rabbit")) {
            size = queuerabbit.size();
        } else {
            size = queuecustom.size();
        }
        return size;
    }

    @PostMapping("/enque")
    @SneakyThrows
    @JsonIgnore(value = true)
    public ResponseEntity<String> enque(@Valid @RequestBody UserRequestDto userRequestDto) {
        if (converter.checkDateAndName(userRequestDto)) {
            UserRequest userRequest= converter.dtoToEntity(userRequestDto);
            if (queuestring.equalsIgnoreCase("rabbit")) {
                queuerabbit.enque(userRequest);
            } else {
                queuecustom.enque(userRequest);
            }
            return new ResponseEntity<>(userRequest.getId()+"is inserted",HttpStatus.ACCEPTED);
        } else {
            log.info("Enque Method has been Accessed but Error Occurred...");
            return new ResponseEntity<>("not a valid input", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deque")
    public Serializable deque() {
        Object deque = null;
        try {
            if (queuestring.equalsIgnoreCase("rabbit")) {
                deque = queuerabbit.deque();
            } else {
                deque = queuecustom.deque();
            }
            return converter.entityToDto((UserRequest) deque);
        } catch (Exception e) {
            return "empty queue";
        }

    }

}




