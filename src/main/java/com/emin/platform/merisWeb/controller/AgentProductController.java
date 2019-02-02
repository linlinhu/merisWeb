package com.emin.platform.merisWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.exception.EminException;
import com.emin.platform.merisWeb.interfaces.ProductApiFeign;
import com.emin.platform.merisWeb.interfaces.PrdFeatureTplApiFeign;

/**
 * 控制层-代理产品信息管理
 * @author kakadanica
 *
 */
@Controller
@RequestMapping("/agentProduct")
public class AgentProductController extends BaseController{
	@Autowired
	ProductApiFeign productApiFeign;
	@Autowired
	PrdFeatureTplApiFeign prdFeatureTplApiFeign;
	@Autowired
	ProductController productController;

	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(String name, String sn, String brandIds){
		ModelAndView mv = new ModelAndView("modules/agentProduct/manage");
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId == null) {
			throw new EminException("BASE_0.0.001");
		} else {
			String res = productApiFeign.getAgentBrands(ecmId);
			if (res != null) {
				JSONObject json = JSONObject.parseObject(res);
				if (!json.getBooleanValue("success")) {
					throw new EminException(json.getString("code"));
				}
				mv.addObject("agentBrands", json.get("result"));
			}
		}
		return mv;
    }
	
	@RequestMapping("/getPages")
	@ResponseBody
	public JSONObject getPages(Long brandId, String name, String sn) {
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId == null) {
			throw new EminException("BASE_0.0.001");
		} else {
			String res = productApiFeign.getAgentPages(ecmId, brandId, name, sn, getPageRequestData().getCurrentPage(), getPageRequestData().getLimit());
			if (res != null) {
				json = JSONObject.parseObject(res);
				if (!json.getBooleanValue("success")) {
					throw new EminException(json.getString("code"));
				}
				JSONObject page = json.getJSONObject("result"); // 获取分页对象
				if (page != null) {
					JSONArray products = page.getJSONArray("resultList"); // 获取产品集合
					products = productController.getTplsProducts(ecmId, products);
				}
			}
		}
		return json;
	}
}
