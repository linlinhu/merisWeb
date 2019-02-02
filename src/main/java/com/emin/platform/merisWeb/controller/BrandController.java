package com.emin.platform.merisWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.dao.PageRequest;
import com.emin.base.exception.EminException;
import com.emin.platform.merisWeb.interfaces.BrandApiFeign;
import com.emin.platform.merisWeb.interfaces.ProductApiFeign;

/**
 * 控制层-主体信息管理
 * @author kakadanica
 *
 */
@Controller
@RequestMapping("/brand")
public class BrandController extends BaseController{
	@Autowired
	BrandApiFeign brandApiFeign;//品牌数据接口实现
	
	@Autowired
	ProductApiFeign productApiFeign;//产品数据接口实现


	@RequestMapping("/getAll")
	@ResponseBody
	public JSONObject getAll(){
		JSONObject brands = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			// 查询品牌信息，表单必选项赋值
			String res = brandApiFeign.getAll(ecmId);
			if (res != null) {
				brands = JSONObject.parseObject(res);
				if(!brands.getBoolean("success")){
					throw new EminException(brands.getString("code"));
				} else {
					JSONArray lst = brands.getJSONArray("result");
					for (int i = 0; i < lst.size(); i++) {
						JSONObject brand = lst.getJSONObject(i);
						brand.put("index", i + 1);
						Long brandId  = brand.getLong("id");
						if (brandId != null) {
							String proResStr = productApiFeign.findByBrandId(ecmId, brandId);
							if (proResStr != null) {
								JSONObject proRes = JSONObject.parseObject(proResStr);
								if(!proRes.getBoolean("success")){
									throw new EminException(proRes.getString("code"));
								} else {
									brand.put("products", proRes.getJSONArray("result"));
								}
							}
							
						}
					}
				}
			}
		}
		
		return brands;
    }
	
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(String keyword){
		ModelAndView mv = new ModelAndView("modules/brand/manage");
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			PageRequest pageRequest = getPageRequestData();
			Integer limit  = pageRequest.getLimit() < 20 ? 20 : pageRequest.getLimit();
			String brandListRes = brandApiFeign.getPages(ecmId, pageRequest.getCurrentPage(), limit, keyword);
			if (brandListRes != null) {
				JSONObject brandListResJson = JSONObject.parseObject(brandListRes);
				if (!brandListResJson.getBooleanValue("success")) {
					throw new EminException(brandListResJson.getString("code"));
				}
				mv.addObject("pageResult", brandListResJson.get("result"));
			}
			mv.addObject("keyword", keyword);
		}
		return mv;
    }
	
	
	@RequestMapping("/saveBrand")
	@ResponseBody
	public JSONObject saveBrand(String jsonStr){
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJson = brandApiFeign.save(ecmId, jsonStr);
			if(!resJson.getBoolean("success")){
				throw new EminException(resJson.getString("code"));
			}
		}
		return resJson;
    }
	
	@RequestMapping("/deleteBrand")
	@ResponseBody
	public JSONObject deleteBrand(String ids){
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			Long id = Long.valueOf(ids);
			String resJsonStr = brandApiFeign.delete(ecmId, id);
			if (resJsonStr != null) {
				resJson = JSONObject.parseObject(resJsonStr);
				if(!resJson.getBoolean("success")){
					throw new EminException(resJson.getString("code"));
				}
			}
		}
		
		return resJson;
    }
	
}
