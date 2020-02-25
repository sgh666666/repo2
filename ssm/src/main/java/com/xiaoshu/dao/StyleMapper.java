package com.xiaoshu.dao;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.Style;
import com.xiaoshu.entity.StyleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StyleMapper extends BaseMapper<Style> {

    Integer queryIdBySname(String sname);
}