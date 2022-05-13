package com.my.service.impl;

import com.my.mapper.ProductInfoMapper;
import com.my.pojo.ProductInfo;
import com.my.pojo.ProductInfoExample;
import com.my.pojo.vo.ProductInfoVo;
import com.my.service.ProductInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {

    //切记:业务逻辑层中一定有数据访问层的对象
    @Autowired
    ProductInfoMapper productInfoMapper;

    @Override
    public List<ProductInfo> getAll() {

        return productInfoMapper.selectByExample(new ProductInfoExample());
    }

    /*
    实现分页功能
    */
    //select * from product_info limit 起始记录数=((当前页-1)*每页的条数),每页取几条
    //select * from product_info limit 10,5
    @Override
    public PageInfo splitPage(int pageNum, int pageSize) {
        //分页插件使用PageHelper工具类完成分页设置
        PageHelper.startPage(pageNum, pageSize);

        /*进行PageInfo的数据封装*/
        //进行有条件的查询操作,必须要创建条件封装的ProductInfoExample对象
        ProductInfoExample example = new ProductInfoExample();
        //设置排序,按主键降序排序,增删改时，修改的数据可以显示在第一页
        //select * from product_info order by p_id desc
/*       在producionfomapper.xml中提供了拼接
    <if test="orderByClause != null" >
                order by ${orderByClause}
    </if>*/
        example.setOrderByClause("p_id desc");
        //设置完排序后,取集合,切记切记:一定在取集合之前,设置PageHelper.startPage(pageNum,pageSize);
        List<ProductInfo> list = productInfoMapper.selectByExample(example);
        //将查询到的集合封装进PageInfo对象中
        PageInfo<ProductInfo> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public int save(ProductInfo info) {
        return productInfoMapper.insert(info);
    }

    @Override
    public ProductInfo getByID(int pid) {

        return productInfoMapper.selectByPrimaryKey(pid);
    }

    @Override
    public int update(ProductInfo info) {
//按照主键更新
        return productInfoMapper.updateByPrimaryKey(info);
    }

    @Override
    public int delete(int pid) {
        return productInfoMapper.deleteByPrimaryKey(pid);
    }

    //批量删除
    @Override
    public int deleteBatch(String[] ids) {
        return productInfoMapper.deleteBatch(ids);
    }

    @Override
    public List<ProductInfo> selectCondition(ProductInfoVo vo) {
        return productInfoMapper.selectCondition(vo);
    }

    //多条件查询分页
    @Override
    public PageInfo<ProductInfo> splitPageVo(ProductInfoVo vo, int pageSize) {
        //取出集合之前,先要设置PageHelper.startPage()属性
        PageHelper.startPage(vo.getPage(), pageSize);
        List<ProductInfo> list = productInfoMapper.selectCondition(vo);
        return new PageInfo<>(list);
    }


}
