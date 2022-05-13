package com.my.pojo.vo;

/*vo（value object）：值对象，又称表现层对象，对应展示界面的数据对象。
比如一个前台展示的数据列表，这个列表中的姓名和用户名分别是存在于两个表中的数据，
此时数据显示时查询语句用到了多表查询sql，查询出来的数据包含了两个表的字段，
此时使用一个JavaBean实体类无法存储返回结果中两个表的数据字段，
因此将这两个表中使用到的字段或属性重新封装为一个新的JavaBean，
这个JavaBean就叫做vo。通俗来说，vo就是一个自定义的、多个表的属性或字段的集合。
*/
public class ProductInfoVo {
    //商品名称
    private String pname;
    //商品类型
    private Integer typeid;
    //最低价格
    private Integer lprice;
    //最高价格
    private Integer hprice;
    //设置页码
    private Integer page=1;

    @Override
    public String toString() {
        return "ProductInfoVo{" +
                "pname='" + pname + '\'' +
                ", typeid=" + typeid +
                ", lprice=" + lprice +
                ", hprice=" + hprice +
                ", page=" + page +
                '}';
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Integer getTypeid() {
        return typeid;
    }

    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    public Integer getLprice() {
        return lprice;
    }

    public void setLprice(Integer lprice) {
        this.lprice = lprice;
    }

    public Integer getHprice() {
        return hprice;
    }

    public void setHprice(Integer hprice) {
        this.hprice = hprice;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public ProductInfoVo(String pname, Integer typeid, Integer lprice, Integer hprice, Integer page) {
        this.pname = pname;
        this.typeid = typeid;
        this.lprice = lprice;
        this.hprice = hprice;
        this.page = page;
    }

    public ProductInfoVo() {
    }
}
