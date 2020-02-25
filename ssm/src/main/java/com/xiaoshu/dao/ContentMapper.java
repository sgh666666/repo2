package com.xiaoshu.dao;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.Content;
import com.xiaoshu.entity.ContentExample;
import java.util.List;

import com.xiaoshu.entity.GroupByStyle;
import org.apache.ibatis.annotations.Param;

public interface ContentMapper extends BaseMapper<Content> {

    List<Content> queryContent(Content content);

    List<GroupByStyle> groupByStyle();
}