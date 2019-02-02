package com.emin.platform.merisWeb.interfaces;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.alibaba.fastjson.JSONObject;

/***
 * 生态链接口桥梁定义
 * @author kakadanica
 */
@FeignClient(value = "zuul")
public interface EcoApiFeign {

	
	@RequestMapping(value = "/api-eco/eco/getJoinChainDis",method = RequestMethod.GET)
	JSONObject getJoinChainDis(@RequestParam(value="ecmId") Long ecmId);
	
	@RequestMapping(value = "/api-eco/eco/getMainChainDis",method = RequestMethod.GET)
	JSONObject getMainChainDis(@RequestParam(value="ecmId") Long ecmId);
	
}
