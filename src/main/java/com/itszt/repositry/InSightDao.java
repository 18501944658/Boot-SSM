package com.itszt.repositry;

import com.itszt.domain.Insight;
import com.itszt.domain.PathDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

@Repository
public interface InSightDao extends BaseMapper<Insight> {


//    List<Insight> selectAll();
    Insight selectByEntid(@Param("entid") String entid);

    List<Insight> selectInEntid(@Param("entids") List<String> entids);
    List<Insight> selectInEntname(@Param("entnames") List<String> entName);

}
