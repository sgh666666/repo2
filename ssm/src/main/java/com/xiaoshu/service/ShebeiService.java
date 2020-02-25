package com.xiaoshu.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.dao.SbtypeMapper;
import com.xiaoshu.dao.ShebeiMapper;
import com.xiaoshu.entity.GroupByStyle;
import com.xiaoshu.entity.Sbtype;
import com.xiaoshu.entity.Shebei;
import com.xiaoshu.entity.ShebeiExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ShebeiService {
    @Resource
    private SbtypeMapper sbtypeMapper;
    @Resource
    private ShebeiMapper shebeiMapper;

    public List<Sbtype> querySbtype() {
        return sbtypeMapper.selectAll();
    }

    public PageInfo<Shebei> findShebeiPage(Shebei shebei, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shebei> shebeiList= shebeiMapper.queryByShebei(shebei);
        PageInfo<Shebei> shebeiPageInfo = new PageInfo<>(shebeiList);
        return shebeiPageInfo;
    }

    public Shebei existShebeiWithSname(String sname) {
        ShebeiExample shebeiExample = new ShebeiExample();
        shebeiExample.createCriteria().andSnameEqualTo(sname);
        List<Shebei> shebeiList = shebeiMapper.selectByExample(shebeiExample);
        return shebeiList.isEmpty()? null:shebeiList.get(0);
    }

    public void addShebei(Shebei shebei) {
        shebeiMapper.insert(shebei);
    }

    public List<Shebei> queryShebei() {
        return shebeiMapper.queryByShebei(null);
    }

    public List<GroupByStyle> group() {
        return shebeiMapper.group();
    }
}
