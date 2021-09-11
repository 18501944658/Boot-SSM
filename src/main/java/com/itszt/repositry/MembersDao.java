package com.itszt.repositry;

import com.itszt.domain.Members;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

@Repository
public interface MembersDao extends BaseMapper<Members> {

    List<Members> selectAllMembers();

    List<Members> selectGroupByEntId(@Param("entidlist") List<String> entidlist);


    List<Members> selectGroupByEntName(@Param("entnamelist") List<String> entnamelist);
}
