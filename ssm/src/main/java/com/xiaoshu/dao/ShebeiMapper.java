package com.xiaoshu.dao;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.GroupByStyle;
import com.xiaoshu.entity.Shebei;
import com.xiaoshu.entity.ShebeiExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ShebeiMapper extends BaseMapper<Shebei> {

    List<Shebei> queryByShebei(Shebei shebei);

    List<GroupByStyle> group();
}