package top.coolbreeze4j.mybatisbatch;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author CoolBreeze
 * @date 2023/7/24 11:37.
 */
@SpringBootApplication
@MapperScan(basePackages = {"top.coolbreeze4j.mybatisbatch.mapper"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
