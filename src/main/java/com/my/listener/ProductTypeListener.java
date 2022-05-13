package com.my.listener;

import com.my.pojo.ProductType;
import com.my.service.ProductTypeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;

//获取当前的商品类型列表，存入当前的全局作用域中
//注册监听器
@WebListener
//全局的监听器，故实现ServletContextListener接口
public class ProductTypeListener implements ServletContextListener {
    /*
    无法通过依赖注册的方式得到商品列表的原因：
    spring框架注册时，也是用监听器方式进行注册，继承同一个接口，因此实现了同一个监听器
    因此无法保证那个监听器先被创建，先被执行，因此不能使用Spring容器的依赖注入
    *
    * */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //手工从Spring容器中取出ProductTypeServiceImpl的对象.
        //获取Spring容器->加载Spring的配置文件(有两个,统配加载)
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext_*.xml");
        //获取当前productTypeService对象.ProductTypeServiceImpl.getBean中与ProductTypeServiceImpl名一致
        ProductTypeService productTypeService = (ProductTypeService) context.getBean("ProductTypeServiceImpl");
        List<ProductType> typeList = productTypeService.getAll();
        //放入全局应用作用域中,供新增页面,修改页面,前台的查询功能提供全部商品类别集合
        servletContextEvent.getServletContext().setAttribute("typeList",typeList);
    }
    //在addproduct.jsp页面打开时,可以直接获取typelist的所有数据
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
