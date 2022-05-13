package com.my.controller;

import com.my.pojo.ProductInfo;
import com.my.pojo.vo.ProductInfoVo;
import com.my.service.ProductInfoService;
import com.my.utils.FileNameUtil;
import com.github.pagehelper.PageInfo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoAction {
    //每页显示的记录数
    public static final int PAGE_SIZE = 5;

    //异步上传的图片的名称
    String saveFileName = "";
    //切记:在界面层中,一定会有业务逻辑层的对象
    @Autowired
    ProductInfoService productInfoService;

    //显示全部商品不分页
    @RequestMapping("/getAll")
    public String getAll(HttpServletRequest request) {
        List<ProductInfo> list = productInfoService.getAll();
        //list在product.jsp页面中做显示
        request.setAttribute("list", list);
        return "product";
    }

    //打开商品管理之后，显示第1页的5条记录,没有翻页，只是显示第一页的数据
    @RequestMapping("/split")
    public String split(HttpServletRequest request) {
        PageInfo info = null;
        //把session中的条件和分页取出来
        Object vo = request.getSession().getAttribute("prodVo");
        //不为空,说明是带着条件来的
        if (vo != null) {
            info = productInfoService.splitPageVo((ProductInfoVo) vo, PAGE_SIZE);
            //从当前编辑按钮上携带页面和条件上到action中,跳回页面之前,存在session中.
            // 点击提交时,更新结束之后,在进行分页查询时,从session中取出条件进行处理.
            // 因此,该session没用了,可以移除
            request.getSession().removeAttribute("prodVo");
        } else {
            //得到第1页的数据，静态常量
            info = productInfoService.splitPage(1, PAGE_SIZE);
        }
        //product.jsp前端页面通过info得到相应数据
        request.setAttribute("info", info);
        return "product";
    }

    //ajax分页翻页处理
/*    @ResponseBody的作用其实是将java对象转为json格式的数据。
    @responseBody注解的作用是将controller的方法返回的对象通过适当的转换器转换为指定的格式之后，写入到response对象的body区，通常用来返回JSON数据或者是XML数据。
    注意：在使用此注解之后不会再走视图处理器，而是直接将数据写入到输入流中，他的效果等同于通过response对象输出指定格式的数据。*/
    @ResponseBody
    @RequestMapping("/ajaxSplit")
    //带条件分页
    public void ajaxSplit(ProductInfoVo vo, HttpSession session) {
        //取得当前page参数的页面的数据
        PageInfo info = productInfoService.splitPageVo(vo, PAGE_SIZE);
        session.setAttribute("info", info);
        //回到product.jsp中的分页的ajax实现中,即success:function中,重新加载table容器中的info.list数据
    }

    /*   多条件查询功能实现
        数据库查到的数据返回到页面,因此要加 @ResponseBody
        异步ajax请求,没有刷新视图,因此request即使设置attribute页读取不到,只能用session
        */
    @ResponseBody
    @RequestMapping("/condition")
    public void condition(ProductInfoVo vo, HttpSession session) {
        List<ProductInfo> list = productInfoService.selectCondition(vo);
        session.setAttribute("list", list);
    }


    //异步ajax文件上传处理
    //用于处理ajax请求
    @ResponseBody
    //该名称应该与addproject.jsp中url:"${pageContext.request.contextPath}/prod/ajaxImg.action",完全一致
    @RequestMapping("/ajaxImg")
    //参数为springmvc文件上传的组件的MultipartFile,其中pimage应该与addproject.jsp中图片介绍的type="file" id="pimage" name="pimage"完全一致
    //
    public Object ajaxImg(MultipartFile pimage, HttpServletRequest request) {
        //提取生成文件名UUID+上传图片的后缀.jpg  .png
        saveFileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(pimage.getOriginalFilename());
        //得到项目中图片转存的路径-服务器得到图片后转存到此处
        String path = request.getServletContext().getRealPath("/image_big");
        try {
            //将上传的文件流传到此处
            //转存  C:\idea_workspace\mimissm\image_big\23sdfasferafdafdadfasfdassf.jpg
            pimage.transferTo(new File(path + File.separator + saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //服务器端返回客户端JSON对象,在addproject.jsp中function fileChange()接收和解析
        // 在pom.xml添加json依赖,封装图片的路径,为了在页面实现立即回显
        JSONObject object = new JSONObject();
        //返回的路径src属性(图片的名称)
        object.put("imgurl", saveFileName);
        return object.toString();
    }

    //与addproduct.jsp中新增商品action="${pageContext.request.contextPath}/prod/save.action">名称一致
    @RequestMapping("/save")
    //数据封装在ProductInfo实体类中,info中已经被注入表单提交上来的数据:除图片和时间外的其他信息
    public String save(ProductInfo info, HttpServletRequest request) {
        info.setpImage(saveFileName);
        info.setpDate(new Date());
        //info对象中有表单提交上来的5个数据,有异步ajax上来的图片名称数据,有上架时间的数据
        int num = -1;
        try {
            num = productInfoService.save(info);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (num > 0) {
            //提交给product.jsp中:alert("${msg}");
            request.setAttribute("msg", "增加成功!");
        } else {
            request.setAttribute("msg", "增加失败!");
        }
        //清空saveFileName变量中的内容,为了下次增加或修改的异步ajax的上传处理
        saveFileName = "";
        //增加成功后应该重新访问数据库,所以跳转到当前分页显示的action上
        return "forward:/prod/split.action";
    }

    //根据主键查找一个数据(点击编辑功能时,数据的回显,即从数据库中根据主键查询一个数据)
    @RequestMapping("/one")
    public String one(int pid, ProductInfoVo vo, Model model, HttpSession session) {
        //该info就是数据库中根据上传的pid查询的结果,传到update页面中展示,因此放到model中
        ProductInfo info = productInfoService.getByID(pid);
        //根据id查到的商品信息放到prod中
        model.addAttribute("prod", info);
        //将多条件及页码放入session中,更新处理结束后分页时读取条件和页码进行处理
        session.setAttribute("prodVo", vo);
        //数据查询出之后,应携带数据跳转到编辑页面,即update页面
        return "update";
    }

    //与update.jsp中的action="${pageContext.request.contextPath}/prod/update.action" 一致
    @RequestMapping("/update")
    //参数:商品实体类，返回客户端的提示
    public String update(ProductInfo info, HttpServletRequest request) {
/*      因为ajax的异步图片上传,如果有上传过,则saveFileName里有上传上来的图片的名称,
        如果没有使用异步ajax上传过图片,则saveFileName="",实体类info使用隐藏表单域提供上来的pImage原始图片的名称;*/
        if (!saveFileName.equals("")) {
            info.setpImage(saveFileName);
        }
        //完成更新处理
        int num = -1;
        try {
            num = productInfoService.update(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0) {
            //此时说明更新成功，product.jsp页面接收到msg信息
            request.setAttribute("msg", "更新成功!");
        } else {
            //更新失败
            request.setAttribute("msg", "更新失败!");
        }

        //处理完更新后,saveFileName里有可能有数据,
        // 而下一次更新时要使用这个变量做为判断的依据,就会出错,所以必须清空saveFileName.
        saveFileName = "";
        //重定向到分页页面
        return "forward:/prod/split.action";
    }


    //与product.jsp中url: "${pageContext.request.contextPath}/prod/delete.action"一致
    @RequestMapping("/delete")
    //pid为product.jsp中发出ajax请求中data: {"pid": pid...（提交注入给action的pid的名称）,vo:删除的时候携带页码和条件
    public String delete(int pid, ProductInfoVo vo, HttpServletRequest request) {
        int num = -1;

        try {
            //增删改都要包上try...catch
            num = productInfoService.delete(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (num > 0) {
            //执行删除与否的提示，扔在ajax异步处理的返回值那
            request.setAttribute("msg", "删除成功!");
            //删除成功时,携带分页和多条件,跳到分页页面
            request.getSession().setAttribute("deleteProdVo", vo);
        } else {
            request.setAttribute("msg", "删除失败!");
        }
        /*   删除结束后没有返回数据，跳到ajax分页显示，再进行返回（ajax请求没有处理结束，还要跳转，因此@ResponseBody不能加在头上）
         */
        return "forward:/prod/deleteAjaxSplit.action";
    }

    //整个异步ajax请求结束之后返回，再由@ResponseBody解析返回的数据
    @ResponseBody
    //返回值带中文，因此要设置编码方式
    @RequestMapping(value = "/deleteAjaxSplit", produces = "text/html;charset=UTF-8")

    /*68行附近有一个public void ajaxSplit(ProductInfoVo vo, HttpSession session)ajax分页的方法，返回值为void
        应该将msg消息传到ajax请求处理的地方（product.jsp的success方法的alert的mag中，需要返回值（删除成功与否的语句））
        因为是局部刷新，不能重新加载jsp页面，因此无法读取product.jsp中的这段代码
         <script type="text/javascript">
        if ("${msg}" != "") {
            alert("${msg}");
        }
         </script>
         只能从服务器端扔回一个删除与否的语句，在此处进行弹框显示
         success: function (msg) {
              alert(msg);...*/

    //重写deleteAjaxSplit方法，返回值为Object
    public Object deleteAjaxSplit(HttpServletRequest request) {
        //改造——删除后携带当前页码，删除之后停在当前页
        PageInfo info = null;
        //放在session域中，才能load中 $("#table").load(...)
        Object vo = request.getSession().getAttribute("deleteProdVo");
        if (vo != null) {
            info = productInfoService.splitPageVo((ProductInfoVo) vo, PAGE_SIZE);
        } else {
            info = productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.getSession().setAttribute("info", info);
        //删除成功与否的消息，扔回到客户端alert(msg);...
        return request.getAttribute("msg");
    }

    //批量删除商品
    @RequestMapping("/deleteBatch")
    //pids="1,4,5"  ps[1,4,5]
    public String deleteBatch(String pids, HttpServletRequest request) {
        //将上传上来的字符串截开,形成商品id的字符数组
        String[] ps = pids.split(",");
        try {
            int num = productInfoService.deleteBatch(ps);
            if (num > 0) {
                request.setAttribute("msg", "批量删除成功!");
            } else {
                request.setAttribute("msg", "批量删除失败!");
            }
        } catch (Exception e) {
            //为后续订单操作做处理
            request.setAttribute("msg", "商品不可删除!");
        }
        //删除后跳转到分页的功能上，读取数据库，进行数据显示，因此不需要@ResponseBody（不会返回一些东西给请求）
        return "forward:/prod/deleteAjaxSplit.action";
/// 最终ajax从deleteAjaxSplit进行返回（写了ResponseBody），将info（PageInfo类）对象放到session域中，返回msg。
    }
}
