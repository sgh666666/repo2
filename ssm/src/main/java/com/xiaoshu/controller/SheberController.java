package com.xiaoshu.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.*;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.service.ShebeiService;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("shebei")
public class SheberController {
    static Logger logger = Logger.getLogger(UserController.class);
    @Autowired
    private RoleService roleService ;
    @Autowired
    private OperationService operationService;
    @Autowired
    private ShebeiService shebeiService;
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping("shebeiIndex")
    public String index(HttpServletRequest request, Integer menuid) throws Exception{
        List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);
        List<Sbtype> sbtypeList = shebeiService.querySbtype();
        request.setAttribute("operationList", operationList);
        request.setAttribute("sbtypeList", sbtypeList);
        return "shebei";
    }
    @RequestMapping(value="shebeiList",method= RequestMethod.POST)
    public void userList(HttpServletRequest request,HttpServletResponse response,String offset,String limit) throws Exception{
        try {
            String sname = request.getParameter("sname");
            String status = request.getParameter("status");
            Shebei shebei = new Shebei();
            shebei.setStatus(status);
            shebei.setSname(sname);
            Integer pageSize = StringUtil.isEmpty(limit)? ConfigUtil.getPageSize():Integer.parseInt(limit);
            Integer pageNum =  (Integer.parseInt(offset)/pageSize)+1;
            PageInfo<Shebei> shebeiList= null;
            String shebeiListJson = (String) redisTemplate.boundHashOps("shebeiList").get(shebei.getSname() + shebei.getStatus() + pageNum + pageSize);
            if(shebeiListJson!=null){
                System.out.println("查询缓存");
                shebeiList=JSON.parseObject(shebeiListJson,PageInfo.class);
            }else {
                System.out.println("查询数据库");
                shebeiList= shebeiService.findShebeiPage(shebei,pageNum,pageSize);
                redisTemplate.boundHashOps("shebeiList").put(shebei.getSname()+shebei.getStatus()+pageNum+pageSize, JSON.toJSONString(shebeiList));
            }
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("total",shebeiList.getTotal() );
            jsonObj.put("rows", shebeiList.getList());
            WriterUtil.write(response,jsonObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户展示错误",e);
            throw e;
        }
    }
    // 新增或修改
    @RequestMapping("reserveShebei")
    public void reserveUser(HttpServletRequest request,Shebei shebei,HttpServletResponse response){
        JSONObject result=new JSONObject();
        try {
                if(shebeiService.existShebeiWithSname(shebei.getSname())==null){  // 没有重复可以添加
                    redisTemplate.delete("shebeiList");
                    shebeiService.addShebei(shebei);
                    result.put("success", true);
                } else {
                    result.put("success", true);
                    result.put("errorMsg", "该用户名被使用");
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
        String [] str={"编号","设备名称","设备类型分类","内存","机身颜色","价格","设备状态","创建时间"};
        for (int i = 0; i <str.length ; i++) {
            titleRow.createCell(i).setCellValue(str[i]);
        }
        List<Shebei> shebeiList=shebeiService.queryShebei();
        for (Shebei s: shebeiList) {

            int lastRowNum = sheet.getLastRowNum();
            HSSFRow dataRow = sheet.createRow(lastRowNum + 1);
            dataRow.createCell(0).setCellValue(s.getId());
            dataRow.createCell(1).setCellValue(s.getSname());
            dataRow.createCell(2).setCellValue(s.getSbtype().getTname());
            dataRow.createCell(3).setCellValue(s.getSbram());
            dataRow.createCell(4).setCellValue(s.getColor());
            dataRow.createCell(5).setCellValue(s.getPrice());
            dataRow.createCell(6).setCellValue(s.getStatus());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String format = dateFormat.format(s.getCreatetime());
            dataRow.createCell(7).setCellValue(format);
            if(s.getPrice()>1000){
                dataRow.createCell(8).setCellValue("高端机");
            }if(s.getPrice()<1000){
                dataRow.createCell(8).setCellValue("低端机");
            }
        }
        response.setHeader("Content-Disposition","attachment;filename=shebei.xls");
        response.setHeader("Content-Type","application/octet-stream");
        try {
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("bar")
    public void groupByStyle(HttpServletResponse response){
        List<GroupByStyle> groupByStyleList=shebeiService.group();
        String jsonString = JSON.toJSONString(groupByStyleList);
        WriterUtil.write(response,jsonString);
    }
}
