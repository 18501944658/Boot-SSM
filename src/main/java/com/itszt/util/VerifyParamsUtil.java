package com.itszt.util;

import com.itszt.common.RestResponse;
import com.itszt.common.StatusCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

/*******************************************************************************
 * - Copyright (c)  2018  chinadaas.com
 * - File Name: VerifyParamsUtil 
 * - @author: lijinjian - Initial implementation
 * - Description:
 *
 * - Function List: 
 *
 * - History:
 * Date         Author          Modification
 * 2018/9/10 0010     lijinjian     Create the current class
 *******************************************************************************/
public class VerifyParamsUtil {


    public static RestResponse verifyParams(BindingResult checkParam) {

        RestResponse resultData = new RestResponse();
        List<ObjectError> errorMsgs = checkParam.getAllErrors();
        if (null != errorMsgs && errorMsgs.size() > 0) {
            resultData.setCode(400);
            /**
             * if param exception ,return first exception info
             */
            resultData.setMsg(errorMsgs.get(0).getDefaultMessage());
            return resultData;
        }
        return null;
    }

    public static RestResponse verifyUidAndId(String uid, String id) {
//        if(StringUtils.isBlank(uid)){
//            ResultData<Object> result = new ResultData<>(StatusCode.CODE_BAD_REQUEST_PARAM);
//            result.setMsg("用户ID不能为空");
//            return result;
//        }

        if (StringUtils.isBlank(id)) {
            RestResponse<Object> result = new RestResponse<>(StatusCode.ENT_NOTFOUND);
            result.setMsg("id不能为空");
            return result;
        }
        return null;
    }


}
