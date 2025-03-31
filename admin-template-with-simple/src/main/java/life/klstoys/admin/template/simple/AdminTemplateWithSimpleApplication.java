package life.klstoys.admin.template.simple;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@MapperScan(basePackages = {"life.klstoys.admin.template.simple.dal.mapper"})
public class AdminTemplateWithSimpleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminTemplateWithSimpleApplication.class, args);
        log.info("AdminTemplateWithSimpleApplication started.");
    }

}
