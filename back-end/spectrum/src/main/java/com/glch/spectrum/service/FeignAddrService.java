package com.glch.spectrum.service;


import com.glch.spectrum.util.Result;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "ADDRLIST", path = "/")
public interface FeignAddrService {

    /**
     * 获取通信录装备
     * @param sn 通信地址编码
     * @return 装备信息
     */
    @GetMapping("/addr/node/{sn}")
    Result getEquipBySn(@PathVariable("sn") String sn);

}
