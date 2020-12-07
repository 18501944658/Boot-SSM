package com.itszt.repositry;

import com.itszt.domain.OrderJob;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;

@Repository
public interface JobDao extends BaseMapper<OrderJob> {


}
