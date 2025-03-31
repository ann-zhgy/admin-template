package life.klstoys.admin.template.rbac;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@MapperScan(basePackages = {"life.klstoys.admin.template.rbac.dal.mapper"})
public class AdminTemplateWithRbacApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminTemplateWithRbacApplication.class, args);
        log.info("AdminTemplateWithRbacApplication started.");
    }

}
