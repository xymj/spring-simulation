package com.spring.simulation.test;

import com.spring.simulation.service.AppConfig;
import com.spring.simulation.service.OrderService;
import com.spring.simulation.service.UserService;
import com.spring.simulation.service.UserServiceInterface;
import com.spring.simulation.spring.ApplicationContext;


/**
 * <pre>
 *          // 创建启动类
 *             // 构造函数实现ComponentScan注解扫描
 *         // 创建一个业务类和测试类
 *         // 创建一个配置类
 *         // 创建一个ComponentScan扫描注解
 *         // 创建一个Compnnet注解
 *         // 配置类扫描service类
 *         // 业务类添加@Componnet注解
 *             // 测试类进行测试
 *         // 启动类添加configClass属性，并且创建一个获取Bean方法
 *             // 实现ComponentScan注解扫描
 *                 // 测试类进行测试
 *         // 创建BeanDefinition
 *         // 创建Scope注解
 *         // 启动类修改，添加bean实现（Compnnet）,识别Scope注解
 *         // 手写模拟getBean()方法的底层实现
 *         // 手写模拟Bean的创建流程
 *             // 测试
 *
 * </pre>
 */
public class MainTest {
  public static void main(String[] args) {

    ApplicationContext applicationContext = new ApplicationContext(AppConfig.class);
    UserServiceInterface userService = (UserServiceInterface) applicationContext.getBean("userService");
    System.out.println(userService);
    userService.print();
    UserServiceInterface userService2 = (UserServiceInterface) applicationContext.getBean("userService");
    System.out.println(userService2);
    userService.print();
    UserServiceInterface userService3 = (UserServiceInterface) applicationContext.getBean("userService");
    System.out.println(userService3);
    userService.print();

    OrderService orderService = (OrderService) applicationContext.getBean("orderService");
    System.out.println(orderService);
    OrderService orderService2 = (OrderService) applicationContext.getBean("orderService");
    System.out.println(orderService2);
//    OrderService orderService3 = (OrderService) applicationContext.getBean("orderService3");
//    System.out.println(orderService3);




    }
}
