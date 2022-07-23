package com.example.RedisProject;

import com.example.RedisProject.Redis.FactoryInterface;
import com.example.RedisProject.Redis.FilterSelector;
import com.example.RedisProject.Service.QueueInterface;
import com.example.RedisProject.controller.CustomQueue;
import com.example.RedisProject.controller.QueueClass;
import com.example.RedisProject.controller.QueueSelector;
import com.example.RedisProject.controller.RabbitQ;
import com.example.RedisProject.model.Converter;
import com.example.RedisProject.model.UserRequest;
import com.example.RedisProject.model.UserRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

//@RunWith(MockitoJUnitRunner.class)
@WebMvcTest(QueueClass.class)
@Slf4j
@AutoConfigureMockMvc
@ContextConfiguration
//@Configuration
public class TestQueueController {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean(name = "customqueue")
    private QueueSelector customQueue;
    @MockBean(name = "rabbitq")
    private QueueSelector rabbitQ;
  @MockBean
    Converter converter;
    @MockBean
    QueueClass queueSelector;
    private UserRequest userRequest;
    ObjectMapper om = new ObjectMapper();
    @MockBean
    FilterSelector filterSelector;

    @MockBean(name = "custom")
    FactoryInterface factoryInterface;
    @MockBean(name = "cache")
    FactoryInterface factoryInterfaceRedis;
    @MockBean
    QueueInterface<UserRequest> queueInterface;
//    @MockBean(name = "redis1")
//    RedisTemplate<String, Integer> redis1redisTemplate;
//    @MockBean(name = "redis2")
//    RedisTemplate<String, Integer> redis2redisTemplate;
    @MockBean
    RedisTemplate<String, Integer> redisTemplate;


//    @Before
//    public void init() {
//        MockitoAnnotations.initMocks(this);
//        mvc = MockMvcBuilders.webAppContextSetup(context).build();
//    }

//    @Bean
//    @Qualifier("rabbitq")
//    public QueueSelector rabbitQ() {
//        return Mockito.mock(QueueSelector.class);
//    }
//    @Bean @Qualifier("customqueue")
//    public QueueSelector customQueue() {
//        return Mockito.mock(QueueSelector.class);
//    }

    @BeforeEach
    public void beforeEach() throws IOException {
        userRequest = new UserRequest(1L, "user1", 26, "01/01/1995", "chennai");
    }

    @Test
    public void testEnque() throws Exception {

        String uri = "/queue/enque";
        when(queueSelector.enque(converter.entityToDto(userRequest))).thenReturn(ResponseEntity.accepted().body(userRequest.getId() + "is inserted"));
        String jsonRequest = om.writeValueAsString(userRequest);

        this.mvc.perform(get("/queue/enque")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(userRequest.getId() + "is inserted")));
//        int status;
//        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
//                        .content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andExpect(status().isOk()).andReturn();
//        status = mvcResult.getResponse().getStatus();
//        assertEquals(202, status);
    }

    @TestConfiguration
    static class MyTestConfig {
        @Bean
        public JedisConnectionFactory jedisConnectionFactory() {
            RedisStandaloneConfiguration configuration=new RedisStandaloneConfiguration("redis",6379);
            JedisClientConfiguration jedisClientConfiguration= JedisClientConfiguration.builder().build();
            JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(configuration,jedisClientConfiguration);
            return jedisConnectionFactory;
        }
        @Bean("redis1")
        public RedisTemplate<String, Integer> redis1redisTemplate() {
            RedisTemplate<String, Integer> redis1redisTemplate = new RedisTemplate<>();
            redis1redisTemplate.setKeySerializer(new StringRedisSerializer());
            redis1redisTemplate.setHashKeySerializer(new GenericToStringSerializer<String>(String.class));
            redis1redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
            redis1redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
            redis1redisTemplate.setConnectionFactory(jedisConnectionFactory());
            return redis1redisTemplate;
        }
        @Bean("redis2")
        public RedisTemplate<String, Integer> redis2redisTemplate() {
            RedisTemplate<String, Integer> redis2redisTemplate = new RedisTemplate<>();
            redis2redisTemplate.setKeySerializer(new StringRedisSerializer());
            redis2redisTemplate.setHashKeySerializer(new GenericToStringSerializer<String>(String.class));
            redis2redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
            redis2redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
            redis2redisTemplate.setConnectionFactory(jedisConnectionFactory());
            return redis2redisTemplate;
        }
        @Bean
        public RedisTemplate<String, Integer> redisTemplate() {
            RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setHashKeySerializer(new GenericToStringSerializer<String>(String.class));
            redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
            redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
            redisTemplate.setConnectionFactory(jedisConnectionFactory());
            return redisTemplate;
        }
        }


    }
