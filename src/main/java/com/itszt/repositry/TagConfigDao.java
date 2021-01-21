package com.itszt.repositry;

import com.itszt.domain.TagConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

@Repository
public interface TagConfigDao extends BaseMapper<TagConfig> {

    TagConfig selectByTagKey(@Param("tagkey") String tagKey);
    void updateOne(@Param("one") TagConfig tagConfig);
}
