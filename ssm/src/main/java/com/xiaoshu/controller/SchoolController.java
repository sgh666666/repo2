package com.xiaoshu.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.*;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.service.SchoolService;
import com.xiaoshu.service.UserService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.WriterUtil;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("school")
public class SchoolController {
    static Logger logger = Logger.getLogger(SchoolController.class);
    @Autowired
    private SchoolService schoolService;

    @Autowired
    private RoleService roleService ;

    @Autowired
    private OperationService operationService;
    @RequestMapping("schoolIndex")
    public String index(HttpServletRequest request, Integer menuid) throws Exception{
        List<Areas> areasList=schoolService.getAreas();
        List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
        request.setAttribute("operationList", operationList);
        request.setAttribute("areasList", areasList);
        return "school";
    }
    @RequestMapping(value="schoolList",method= RequestMethod.POST)
    public void schoolList(HttpServletRequest request, HttpServletResponse response,String offset, String limit) throws Exception{
        try {
            String aname = request.getParameter("aname");
            Integer pageSize = StringUtil.isEmpty(limit)? ConfigUtil.getPageSize():Integer.parseInt(limit);
            Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
            PageInfo<School> schoolList= schoolService.findSchoolPage(aname,pageNum,pageSize);
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("total",schoolList.getTotal() );
            jsonObj.put("rows", schoolList.getList());
            WriterUtil.write(response,jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户展示错误",e);
            throw e;
        }
    }
    //添加或修改
    @RequestMapping("reserveUser")
    public void reserveUser(HttpServletRequest request,School school,HttpServletResponse response){
        Integer id = school.getId();
        JSONObject result=new JSONObject();
        try {
            if (id != null) {   // userId不为空 说明是修改
                    schoolService.updateSchool(school);
                    result.put("success", true);
                } else {   // 添加
                if(schoolService.existSchoolWithSname(school.getSname())==null){  // 没有重复可以添加
                    schoolService.addSchool(school);
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
    @RequestMapping("out")
    public void out(HttpServletResponse response){
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        HSSFRow titleRow = sheet.createRow(0);
        String [] title ={"id","分校名称","所在城市","联系方式","详细地址","分校状态","建校时间"};
        for (int i = 0; i <title.length ; i++) {
            titleRow.createCell(i).setCellValue(title[i]);
        }
		/*titleRow.createCell(0).setCellValue("id");
		titleRow.createCell(1).setCellValue("分校名称");
		titleRow.createCell(2).setCellValue("所在城市");
		titleRow.createCell(3).setCellValue("联系方式");
		titleRow.createCell(4).setCellValue("详细地址");
		titleRow.createCell(5).setCellValue("分校状态");
		titleRow.createCell(6).setCellValue("建校时间");*/
        List<School> schoolList = schoolService.querySchool();
        for (School s: schoolList) {
            int lastRowNum = sheet.getLastRowNum();
            HSSFRow dataRow = sheet.createRow(lastRowNum + 1);
            dataRow.createCell(0).setCellValue(s.getId());
            dataRow.createCell(1).setCellValue(s.getSname());
            dataRow.createCell(2).setCellValue(s.getAreas().getAname());
            dataRow.createCell(3).setCellValue(s.getPhone());
            dataRow.createCell(4).setCellValue(s.getAddress());
            dataRow.createCell(5).setCellValue(s.getStatus());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String result = dateFormat.format(s.getCreatetime());
            dataRow.createCell(6).setCellValue(result);
        }
        response.setHeader("Content-Disposition","attachment;filename=school.xls");
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
            for (int i = 1; i <=lastRowNum ; i++) {
                HSSFRow dataRow = sheet.getRow(i);
                String sname = dataRow.getCell(1).getStringCellValue();
                String aname = dataRow.getCell(2).getStringCellValue();
                Integer areaid = schoolService.queryAreaidByName(aname);
                String phone= dataRow.getCell(3).getStringCellValue();
                String address= dataRow.getCell(4).getStringCellValue();
                String status= dataRow.getCell(5).getStringCellValue();
                String createtime= dataRow.getCell(6).getStringCellValue();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(createtime);
                School school = new School(sname,areaid,phone,address,status,date);
                System.out.println(school.toString());
                schoolService.addSchool(school);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:schoolIndex.htm?menuid=13";
    }
}
