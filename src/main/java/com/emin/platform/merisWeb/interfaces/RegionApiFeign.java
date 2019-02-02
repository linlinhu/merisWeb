package com.emin.platform.merisWeb.interfaces;

import com.alibaba.fastjson.JSONObject;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


/***
 * 区域管理接口桥梁定义
 * @author winnie
 */
@FeignClient(value = "zuul")
public interface RegionApiFeign {
	
	/**
	 * 区域管理分类树加载
	 * @param ecmId 主体编号
	 * @param parentId 父级id
	 * @return
	 */
	@RequestMapping(value = "api-dm/region/findAll",method = RequestMethod.GET)
	String getTree(@RequestHeader(value="ecmId") Long ecmId);
	
	/**
	 * 区域管理分类树根据名称查询
	 * @param ecmId 主体编号
	 * @param name 根据名称查询
	 * @return
	 */
	@RequestMapping(value = "api-dm/region/findByName",method = RequestMethod.GET)
	String findByName(@RequestHeader(value="ecmId") Long ecmId,@RequestParam(value="name") String name);
	
	/**
	 * 区域管理分类树根据pid才查询
	 * @param ecmId 主体编号
	 * @param  pid 区域父类id
	 * @return
	 */
	@RequestMapping(value = "api-dm/region/findByPid",method = RequestMethod.GET)
	String findByPid(@RequestHeader(value="ecmId") Long ecmId,@RequestParam(value="pid") Long pid);
	
	/**
	 * 区域管理分类保存
	 * @param ecmId 主体编号
	 * @param jsonStr 需要保存的数据
	 */
	@RequestMapping(value = "api-dm/region/saveOrUpdate",method = RequestMethod.GET)
	JSONObject save(@RequestHeader(value="ecmId") Long ecmId,@RequestParam(value="regionStr") String jsonStr);
	
	/**
	 *区域管理分类删除
	 * @param ecmId 主体编号
	 * @param regionId 区域id
	 * @return
	 */
	@RequestMapping(value = "api-dm/region/deleteById",method = RequestMethod.GET)
	String delete(@RequestHeader(value="ecmId") Long ecmId,@RequestParam(value="regionId") Long id);
}
