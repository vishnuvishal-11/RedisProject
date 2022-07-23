//package Redis;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//import security.Counter;
//
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//
//@Service
//public class RedisService {
//    Logger logger = LoggerFactory.getLogger(RedisService.class);
//    Date date;
//
//
//    @Value("${Ratelimit.penalty.minutes}")
//    int penalty;
//
//    @Autowired
//    @Qualifier("redis1")
//    private RedisTemplate<String, Integer> redis1redisTemplate;
//    @Autowired
//    @Qualifier("redis2")
//    RedisTemplate<String, Integer> redis2redisTemplate;
//
//    public void save(String ip, int count) {
//        if (this.existsById(ip)) {
////            redis1redisTemplate.opsForHash().put("access", ip, count);
////            redis2redisTemplate.opsForHash().put("access2", ip, date);
//            if (count <= 10) {
//                redis1redisTemplate.opsForSet().add(ip, count);
//                if (count == 1)
//                    redis1redisTemplate.expire(ip, penalty, TimeUnit.MINUTES);
//            } else {
//                redis1redisTemplate.opsForSet().pop(ip);
//                redis2redisTemplate.opsForSet().add(ip);
//                redis2redisTemplate.expire(ip, penalty, TimeUnit.MINUTES);
//            }
//        }
//    }
//
//
//    public boolean existsById(String ip) {
//        if (redis1redisTemplate.opsForSet().isMember(ip,ip))
//            return true;
//        else
//            return false;
//    }
//
////    public int findById(String Ip) {
////        return (int) redis1redisTemplate.opsForSet().;
//
////    }
//
//    public int findById(String ip) {
//
//        return (int) redis1redisTemplate.opsForSet().getOperations().opsForHash().get(ip,ip);
//
//    }
//
//
//}
//
//
//
