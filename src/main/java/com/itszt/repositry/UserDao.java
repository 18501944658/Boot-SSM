package com.itszt.repositry;

import com.itszt.domain.User;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

@Repository
public interface UserDao extends BaseMapper<User> {


    List<User> selectAll();

}
