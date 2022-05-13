package com.my.service.impl;

import com.my.mapper.ProductTypeMapper;
import com.my.pojo.ProductType;
import com.my.pojo.ProductTypeExample;
import com.my.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//交给spring框架，添加Service注解,命名为productTypeService,方便在监听器中获取
@Service(value = "ProductTypeServiceImpl")
public class ProductTypeServiceImpl implements ProductTypeService {

    //在业务逻辑层一定会有数据访问层的对象
    @Autowired
    ProductTypeMapper productTypeMapper;

    @Override
    public List<ProductType> getAll() {

        return productTypeMapper.selectByExample(new ProductTypeExample());
    }
    //添加监听器ProductTypeListener，在监听器中获取service，得到所有商品类别的列表，把它放在application的作用域中（变化较少，可以放在全局的作用域中）
    //在需要的地方直接获取全局的application，在增删改每次操作完之后，修改一下application中的商品类别即可
}
