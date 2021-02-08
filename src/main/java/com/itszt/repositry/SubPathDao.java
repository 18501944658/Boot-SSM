package com.itszt.repositry;

import com.itszt.domain.DtResultDto;
import com.itszt.domain.PathDetail;
import com.itszt.domain.SubPath;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;
import java.util.Map;

@Repository
public interface SubPathDao extends BaseMapper<SubPath> {


//    List<SubPath> selectAll();


    List<SubPath> selectById(@Param("entid") String entid);

    List<DtResultDto> selectBySrcName(@Param("srcname") String srcname);

}
