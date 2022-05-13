package com.my.test;

import com.my.mapper.ProductInfoMapper;
import com.my.pojo.ProductInfo;
import com.my.pojo.vo.ProductInfoVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/*Spring接管当前jUnit单元测试类,
通过List<ProductInfo> selectCondition(ProductInfoVo vo)这个接口指向动态代理出来的多条件查询的功能
因此要依赖spring的依赖注入*/
//让测试运行于Spring测试环境
@RunWith(SpringJUnit4ClassRunner.class)
//Spring整合JUnit4测试时,使用注解引入多个配置文件
@ContextConfiguration(locations = {"classpath:applicationContext_dao.xml","classpath:applicationContext_service.xml"})
public class MyTest {
    @Autowired
    ProductInfoMapper mapper;
    @Test
    public void testSelectCondition(){
        ProductInfoVo vo = new ProductInfoVo();
        //vo.setPname("4");
        //vo.setTypeid(3);
        vo.setLprice(3000);
        vo.setHprice(3999);
        //查询全部商品
        List<ProductInfo> list = mapper.selectCondition(vo);
        list.forEach(productInfo -> System.out.println(productInfo));
        /*控制台的日志输出配置在SqlMapConfig中
        <settings>
            <setting name="logImpl" value="STDOUT_LOGGING"/>
        </settings>
        * */

    }
}
