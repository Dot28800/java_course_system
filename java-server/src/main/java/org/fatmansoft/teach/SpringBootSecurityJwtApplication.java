package org.fatmansoft.teach;

import org.fatmansoft.teach.init.MarkInit;
import org.fatmansoft.teach.repository.CourseAttendanceRepository;
import org.fatmansoft.teach.repository.CourseChooseRepository;
import org.fatmansoft.teach.util.CommonMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * SpringBootSecurityJwtApplication Web 服务 主程序
 */
@SpringBootApplication
public class SpringBootSecurityJwtApplication {
    @Autowired
    public CourseChooseRepository courseChooseRepository;
    @Autowired
    public MarkInit markInit;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootSecurityJwtApplication.class, args);
    }//创建一个自己的入口实例并运行
    @Bean//用于告诉 Spring 容器这个方法将会返回一个对象，这个对象要注册为 Spring 应用上下文中的 bean
    public CommandLineRunner commandLineRunner(){
        return (args) ->{

        };
    }

}