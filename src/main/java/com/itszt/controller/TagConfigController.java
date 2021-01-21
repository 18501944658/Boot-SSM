package com.itszt.controller;

import com.itszt.domain.TagConfig;
import com.itszt.repositry.TagConfigDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


@RestController
@Slf4j
public class TagConfigController {

    @Autowired
    private TagConfigDao tagConfigDao;
    @GetMapping("/tagconfigall")
    public  Integer getAll() throws IOException {
        AtomicInteger atomicInteger= new AtomicInteger();
        File file= new File("C:\\Users\\Administrator\\Desktop\\model.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line = br.readLine()) != null) {
            String[] split = line.split(",");
            TagConfig tagConfig = tagConfigDao.selectByTagKey(split[0].trim());
            if (tagConfig!=null){
                tagConfigDao.delete(tagConfig);
                log.info(split[2].trim()+"    "+split[1].trim()+"   "+split[0].trim());
                atomicInteger.incrementAndGet();
                if (!split[2].trim().equals("null")){
                    tagConfig.setThreshold(split[2].trim());
                }
                if (!split[1].trim().equals("null")) {
                    tagConfig.setSymbol(split[1].trim());
                }
                tagConfig.setId(UUID.randomUUID().toString());
                tagConfigDao.insert(tagConfig);
            }
        }
        br.close();
       return atomicInteger.get();
    }
}
