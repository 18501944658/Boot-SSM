package com.itszt.service;


import com.itszt.common.BaseParmerters;
import com.itszt.domain.User;

import java.util.List;

public interface UserService {

    List<User> selecctAll(BaseParmerters baseParmerters);

    User selectByPrimaryKey(Integer id);
}
