package com.xiaoshu.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.*;
import com.xiaoshu.service.ContentService;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.WriterUtil;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("content")
public class ContentController {
    static Logger logger = Logger.getLogger(UserController.class);
    @Autowired
    private RoleService roleService ;
    @Autowired
    private OperationService operationService;
    @Autowired
    private ContentService contentService;
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping("contentIndex")
    public String index(HttpServletRequest request, Integer menuid) throws Exception{
        List<Style>styleList= contentService.queryStyle();
        List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
        request.setAttribute("operationList", operationList);
        request.setAttribute("styleList", styleList);
        return "content";
    }
    @RequestMapping(value="contentList",method= RequestMethod.POST)
    public void contentList(HttpServletRequest request, HttpServletResponse response, String offset, String limit) throws Exception{
        try {
            String styleid = request.getParameter("styleid");
            String title = request.getParameter("title");
            Content content = new Content();
            content.setTitle(title);
            content.setStyleid(Integer.parseInt(styleid));
            Integer pageSize = StringUtil.isEmpty(limit)? ConfigUtil.getPageSize():Integer.parseInt(limit);
            Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
            PageInfo<Content> contentList= null;
            String jsonContentList = (String) redisTemplate.boundHashOps("contentList").get(content.getTitle()+content.getStyleid()+pageNum+pageSize);
            if(jsonContentList!=null){
                System.out.println("查询缓存");
                contentList=JSON.parseObject(jsonContentList,PageInfo.class);
            }
            if(jsonContentList==null){
                System.out.println("查询数据库");
                contentList=contentService.findContentPage(content,pageNum,pageSize);
                redisTemplate.boundHashOps("contentList").put(content.getTitle()+content.getStyleid()+pageNum+pageSize,JSON.toJSONString(contentList));
            }
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("total",contentList.getTotal() );
            jsonObj.put("rows", contentList.getList());
            WriterUtil.write(response,jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户展示错误",e);
            throw e;
        }
    }
    @RequestMapping("reserveContent")
    public void reserveContent(HttpServletRequest request,Content content,HttpServletResponse response){
        Integer id = content.getId();
        JSONObject result=new JSONObject();
        try {
            if (id != null) {   // userId不为空 说明是修改
                redisTemplate.delete("contentList");
                contentService.updateContent(content);
                result.put("success", true);
            } else {   // 添加
                if(contentService.existContentWithTitle(content.getTitle())==null){  // 没有重复可以添加
                    redisTemplate.delete("contentList");
                    contentService.addContent(content);
                    redisTemplate.boundValueOps(content.getTitle()).set(String.valueOf(content.getPrice()));
                    result.put("success", true);
                } else {
                    result.put("success", true);
                    result.put("errorMsg", "该名被使用");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("保存用户信息错误",e);
            result.put("success", true);
            result.put("errorMsg", "对不起，操作失败");
        }
        WriterUtil.write(response, result.toString());
    }
    @RequestMapping("jiaoyan")
    @ResponseBody
    public String jiaoyan(String title){
        Content content = contentService.existContentWithTitle(title);
        if(content==null){
            return "";
        }else {
            return "123";
        }
    }
    @RequestMapping("deleteContent")
    public void delUser(HttpServletRequest request,HttpServletResponse response){
        JSONObject result=new JSONObject();
        try {
            String[] ids=request.getParameter("ids").split(",");
            for (String id : ids) {
                contentService.deleteContent(Integer.parseInt(id));
            }
            redisTemplate.delete("contentList");
            result.put("success", true);
            result.put("delNums", ids.length);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除用户信息错误",e);
            result.put("errorMsg", "对不起，删除失败");
        }
        WriterUtil.write(response, result.toString());
    }
    @RequestMapping("out")
    public void out(HttpServletResponse response){
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFRow titleRow = sheet.createRow(0);
        String [] str={"id","广告标题","广告分类名称","图片路径","广告链接","费用","广告状态","创建时间"};
        for (int i = 0; i <str.length ; i++) {
            titleRow.createCell(i).setCellValue(str[i]);
        }
        List<Content> contentList=contentService.queryContent();
        for (Content c: contentList) {

            int lastRowNum = sheet.getLastRowNum();
            HSSFRow dataRow = sheet.createRow(lastRowNum + 1);
            dataRow.createCell(0).setCellValue(c.getId());
            dataRow.createCell(1).setCellValue(c.getTitle());
            dataRow.createCell(2).setCellValue(c.getStyle().getSname());
            dataRow.createCell(3).setCellValue(c.getUrl());
            dataRow.createCell(4).setCellValue(c.getPath());
            dataRow.createCell(5).setCellValue(c.getPrice());
            dataRow.createCell(6).setCellValue(c.getStatus());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = dateFormat.format(c.getCreatetime());
            dataRow.createCell(7).setCellValue(format);
        }
        response.setHeader("Content-Disposition","attachment;filename=content.xls");
        response.setHeader("Content-Type","application/octet-stream");
        try {
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("in")
    public String in(MultipartFile file){

        try {
            HSSFWorkbook workbook = new HSSFWorkbook(file.getInputStream());
            HSSFSheet sheet = workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 1; i <lastRowNum; i++) {
                HSSFRow dataRow = sheet.getRow(i);
                String title = dataRow.getCell(1).getStringCellValue();
                String sname = dataRow.getCell(2).getStringCellValue();
                Integer styleid= contentService.queryIdBySname(sname);
                String url = dataRow.getCell(3).getStringCellValue();
                String path = dataRow.getCell(4).getStringCellValue();
                Integer price = (int)dataRow.getCell(5).getNumericCellValue();
                String status = dataRow.getCell(6).getStringCellValue();
                String time = dataRow.getCell(7).getStringCellValue();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date createtime = dateFormat.parse(time);
                Content content = new Content(styleid, title, url, path, price, status, createtime);
                contentService.addContent(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:contentIndex.htm?menuid=15";
    }
    @RequestMapping("bar")
    public void groupByStyle(HttpServletResponse response){
        List<GroupByStyle> styleList = contentService.groupByStyle();
        String jsonString = JSON.toJSONString(styleList);
        WriterUtil.write(response,jsonString);
    }
}
