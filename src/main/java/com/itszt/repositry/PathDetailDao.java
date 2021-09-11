package com.itszt.repositry;

import com.itszt.domain.PathDetail;
import com.itszt.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

@Repository
public interface PathDetailDao extends BaseMapper<PathDetail> {


    List<PathDetail> selectAllDate();


    List<PathDetail> selectByPathId(@Param("pathId") String pathId);


    List<PathDetail> selectPathAllByPathId(@Param("pathIds") List<String> pathIds);

}
