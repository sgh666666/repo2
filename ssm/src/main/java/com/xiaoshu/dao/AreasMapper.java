package com.xiaoshu.dao;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.Areas;
import com.xiaoshu.entity.AreasExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AreasMapper extends BaseMapper<Areas> {
        Integer queryId(String aname);
}