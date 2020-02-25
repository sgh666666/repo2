package com.xiaoshu.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.dao.ContentMapper;
import com.xiaoshu.dao.StyleMapper;
import com.xiaoshu.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentService {
    @Autowired
    private StyleMapper styleMapper;
    @Autowired
    private ContentMapper contentMapper;

    public List<Style> queryStyle() {
        return styleMapper.selectAll();
    }

    public PageInfo<Content> findContentPage(Content content, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Content> contentList= contentMapper.queryContent(content);
        PageInfo<Content> pageInfo = new PageInfo<>(contentList);
        return pageInfo;
    }
    public Content existContentWithTitle(String title) {
        ContentExample contentExample = new ContentExample();
        contentExample.createCriteria().andTitleEqualTo(title);
        List<Content> contentList = contentMapper.selectByExample(contentExample);
        return contentList.isEmpty()?null:contentList.get(0);
    }

    public void addContent(Content content) {
        contentMapper.insert(content);
    }

    public void updateContent(Content content) {
        contentMapper.updateByPrimaryKeySelective(content);
    }

    public void deleteContent(int parseInt) {
        contentMapper.deleteByPrimaryKey(parseInt);
    }

    public List<Content> queryContent() {
        return contentMapper.queryContent(null);
    }

    public Integer queryIdBySname(String sname) {
        return styleMapper.queryIdBySname(sname);
    }

    public List<GroupByStyle> groupByStyle() {
        return contentMapper.groupByStyle();
    }
}
