package com.czy.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.czy.domain.ResponseResult;
import com.czy.domain.dto.CategoryDto;
import com.czy.domain.entity.Category;
import com.czy.domain.vo.CategoryVO;
import com.czy.domain.vo.ExcelCategoryVo;
import com.czy.enums.AppHttpCodeEnum;
import com.czy.service.CategoryService;
import com.czy.utils.BeanCopyUtils;
import com.czy.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * ClassName: CategoryController
 * Package: com.czy.controller
 * Description:
 *
 * @Author Chen Ziyun
 * @Version 1.0
 */
@RestController
@RequestMapping("content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.getCategoryList();
    }

    @GetMapping("list")
    public ResponseResult listByPage(Integer pageNum, Integer pageSize){
        return categoryService.listByPage(pageNum, pageSize);
    }

    @PostMapping
    public ResponseResult updateCategory(@RequestBody CategoryDto categoryDto){
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteById(@PathVariable Long id){
        return categoryService.deleteById(id);
    }

    @GetMapping("/{id}")
    public ResponseResult getById(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }

    @PutMapping
    public ResponseResult addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }

    /**
     * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     *
     * @since 2.1.1
     */
    @PreAuthorize("@ps.hasPermission('content:category:export')")//权限控制，ps是PermissionService类的bean名称
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        try {
            //设置下载文件的请求头，下载下来的Excel文件叫'分类.xlsx'
            WebUtils.setDownLoadHeader("分类.xlsx",response);

            // 获取需要导出的数据
            List<Category> categories = categoryService.list();
            List<CategoryVO> categoryVOS = BeanCopyUtils.copyBeanList(categories, CategoryVO.class);
            //把数据写入到Excel中，也就是把ExcelCategoryVo实体类的字段作为Excel表格的列头
            //sheet方法里面的字符串是Excel表格左下角工作簿的名字
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("文章分类")
                    .doWrite(categoryVOS);
        } catch (Exception e) {
            //如果出现异常,就返回失败的json数据给前端。AppHttpCodeEnum和ResponseResult是我们在huanf-framework工程写的类
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            //WebUtils是我们在huanf-framework工程写的类，里面的renderString方法是将json字符串写入到请求体，然后返回给前端
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }
}
