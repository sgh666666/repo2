package com.xiaoshu.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.dao.AreasMapper;
import com.xiaoshu.dao.SchoolMapper;
import com.xiaoshu.entity.Areas;
import com.xiaoshu.entity.School;
import com.xiaoshu.entity.SchoolExample;
import com.xiaoshu.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolService {
    @Autowired
    SchoolMapper schoolMapper;
    @Autowired
    AreasMapper areasMapper;

    public PageInfo<School> findSchoolPage(String aname, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<School> schoolList=schoolMapper.querySchool(aname);
        PageInfo<School> pageInfo = new PageInfo<>(schoolList);
        return pageInfo;
    }

    public List<Areas> getAreas() {
        return areasMapper.selectAll();
    }

    public School existSchoolWithSname(String sname) {
        SchoolExample example = new SchoolExample();
        SchoolExample.Criteria criteria = example.createCriteria();
        criteria.andSnameEqualTo(sname);
        List<School> schoolList = schoolMapper.selectByExample(example);
        return schoolList.isEmpty()?null:schoolList.get(0);
    }

    public void updateSchool(School school) {
        schoolMapper.updateByPrimaryKeySelective(school);
    }

    public void addSchool(School school) {
        schoolMapper.insert(school);
    }

    public List<School> querySchool() {
        return schoolMapper.querySchool("");
    }

    public Integer queryAreaidByName(String aname) {
        return areasMapper.queryId(aname);
    }
}
