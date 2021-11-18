package com.itszt.util;

import com.itszt.config.ApplicationContextAccessor;
import com.itszt.domain.UserDemo;

public class TestApplicationContext {

  private static UserDemo userDemo = ApplicationContextAccessor.getBean(UserDemo.class);

   public static UserDemo getDemo(){
       System.out.println(ApplicationContextAccessor.getApplicationContext());
       System.out.println("userDemo = " + userDemo);
       return userDemo;
   }



}
