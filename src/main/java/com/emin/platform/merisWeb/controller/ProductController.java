package com.emin.platform.merisWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.dao.PageRequest;
import com.emin.base.exception.EminException;
import com.emin.platform.merisWeb.interfaces.BrandApiFeign;
import com.emin.platform.merisWeb.interfaces.PrdFeatureTplApiFeign;
import com.emin.platform.merisWeb.interfaces.ProductApiFeign;

/**
 * 控制层-独立产品信息管理
 * @author kakadanica
 *
 */
@Controller
@RequestMapping("/product")
public class ProductController extends BaseController{

	@Autowired
	ProductApiFeign productApiFeign;//产品数据实现

	@Autowired
	PrdFeatureTplApiFeign prdFeatureTplApiFeign; //产品特性模板实现
	
	@Autowired
	BrandApiFeign brandApiFeign;//品牌数据接口实现
	
	/**
	 * 主页搜索查询
	 * @param name 产品名称关键字
	 * @param sn 产品编号关键字
	 * @param tags 产品标签对象数组字符串
	 * @param category 产品分类对象字符串
	 * @return
	 */
	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(String name, String sn, String tags, String category){
		ModelAndView mv = new ModelAndView("modules/product/manage");
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId == null) {
			mv = new ModelAndView("500");
		} else {
			PageRequest pageRequest = getPageRequestData();
			JSONArray selectedTags = new JSONArray();
			String tagIds = "";//标签查询条件
			if (tags != null && tags.indexOf("{") == 0) {
				JSONObject tagsObj = JSON.parseObject(tags);
				mv.addObject("tagsObj", tagsObj);
				selectedTags = JSONArray.parseArray(tagsObj.getString("objs"));
				if (selectedTags.size() > 0) {
					for (int i = 0; i < selectedTags.size(); i++) {
						tagIds += selectedTags.getJSONObject(i).getLong("id") + ",";
					}
					tagIds = tagIds.substring(0, tagIds.length() - 1);
				}
			}
			Long categoryId = null;//分类查询条件
			if (category != null && category.indexOf("{") == 0) {
				JSONObject categoryObj = JSON.parseObject(category);
				mv.addObject("categoryObj", categoryObj);
				categoryId = categoryObj.getLong("id");
			}
			String resStr = productApiFeign.getPages(ecmId, pageRequest.getCurrentPage(),pageRequest.getLimit(), name, sn, tagIds, categoryId);
			if (resStr != null) {
				JSONObject res = JSONObject.parseObject(resStr);
				if (!res.getBoolean("success")) {
					throw new EminException(res.getString("code"));
				} else {
					JSONObject page = res.getJSONObject("result"); // 获取分页对象
					if (page != null) {
						JSONArray products = page.getJSONArray("resultList"); // 获取产品集合
						products = getTplsProducts(ecmId, products);
					}
					page.put("limit", pageRequest.getLimit());
					// 将筛选条件返回到页面上
					mv.addObject("name", name);
					mv.addObject("sn", sn);
					mv.addObject("tags", tags);
					mv.addObject("category", category);
					// 将筛选结果返回到页面上
					mv.addObject("pageResult", page);
				}
				
			}
		}
		return mv;
    }
	
	public JSONArray getTplsProducts(Long ecmId, JSONArray products) {
		for (int i = 0; i < products.size(); i++) {
			//当前遍历产品
			JSONObject product = products.getJSONObject(i);
			// 获取当前产品的所属分类集合
			JSONArray cates = product.getJSONArray("prdCategories"); 
			// 拼接所属分类id，用逗号隔开
			String cateIds = "";
			for(int j = 0; j < cates.size(); j++) {
				JSONObject cate = cates.getJSONObject(j);
				cateIds += cate.getInteger("id") + ",";
			}
			if (cateIds.length() > 1) {
				cateIds = cateIds.substring(0, cateIds.length() - 1);
			}
			if (cateIds.length() > 0) {
				// 根据分类查询关联特性模板
				String featureTplsStr = prdFeatureTplApiFeign.getFeatureTpl(ecmId, cateIds);
				if (featureTplsStr != null) {
					JSONObject featureTplsRes = JSONObject.parseObject(featureTplsStr);
					if(!featureTplsRes.getBoolean("success")){
						throw new EminException(featureTplsRes.getString("code"));
					} else {
						// 获取返回的特性模板对象
						JSONObject featureTplsObj = featureTplsRes.getJSONObject("result");
		    			//获取本身包含的特性模板集合
						JSONArray selfFeatureTpls = featureTplsObj.getJSONArray("featureTemplates");
		    			// 获取父级节点包含的特性模板集合
						JSONArray parentFeatureTpls = featureTplsObj.getJSONArray("parentFeatureTemplates");
		    			// 产品包含的特性模板集合定义
						JSONArray productFeatureTpls = new JSONArray();
						
						// 遍历本身和父级的模板集合，去重后装载至productFeatureTpls
						for (int j = 0; j < selfFeatureTpls.size(); j++) {
							productFeatureTpls.add(selfFeatureTpls.get(j));
						}
						
						for (int j = 0; j < parentFeatureTpls.size(); j++) {
							boolean isRepeat = false;
							for (int k = 0; k < selfFeatureTpls.size(); k++) {
								if (selfFeatureTpls.getJSONObject(k).getInteger("id") == parentFeatureTpls.getJSONObject(j).getInteger("id")) {
									isRepeat = true;
								}
							}
							if (!isRepeat) {
								productFeatureTpls.add(parentFeatureTpls.get(j));
							}
						}
						// 当前遍历产品对象添加模板集合信息
						product.put("tpls", productFeatureTpls);
					}
				}
				
			}
		}
		return products;
	}
	
	/**
	 * 新增/编辑页面跳转
	 * @param id 产品id
	 * @return
	 */
	@RequestMapping("/form")
	@ResponseBody
	public ModelAndView goForm(Long id){
		ModelAndView mv = new ModelAndView("modules/product/form");
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId == null) {
			mv = new ModelAndView("500");
		} else {
			// 查询品牌信息，表单必选项赋值
			String brands = brandApiFeign.getAll(ecmId);
			if (brands != null) {
				JSONObject brandsJson = JSONObject.parseObject(brands);
				if(!brandsJson.getBoolean("success")){
					throw new EminException(brandsJson.getString("code"));
				} else {
					mv.addObject("brands", brandsJson.get("result"));
				}
			}
			
			if (id != null) { // 存在id，则查询产品详情，将详细信息展示在表单界面，供用户参考
				JSONObject productJson = productApiFeign.detail(ecmId, id);
				if(!productJson.getBoolean("success")){
					throw new EminException(productJson.getString("code"));
				} else {
					mv.addObject("product", productJson.get("result"));
				}
			}
		}
		
		return mv;
    }
	/**
	 * 保存产品
	 * @param jsonStr 产品信息对象字符串
	 * @return
	 */
	@RequestMapping("/saveProduct")
	@ResponseBody
	public JSONObject saveProduct(String jsonStr) {
		JSONObject res = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {

			res = productApiFeign.save(ecmId, jsonStr);
			if(!res.getBoolean("success")){
				throw new EminException(res.getString("code"));
			}		
		} else {
			res.put("success", false);
			res.put("message", "无法绑定主体信息，请重新登录！");
		}
		
		return res;
	}
	
	/**
	 * 保存产品
	 * @param jsonStr 产品信息对象字符串
	 * @return
	 */
	@RequestMapping("/savePics")
	@ResponseBody
	public JSONObject savePics(String pics) {
		JSONObject res = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			res = productApiFeign.savePics(ecmId, pics);
			if(!res.getBoolean("success")){
				throw new EminException(res.getString("code"));
			}	
		} else {
			res.put("success", false);
			res.put("message", "无法绑定主体信息，请重新登录！");
		}
		
		return res;
	}
	
	/**
	 * 删除产品信息
	 * @param ids 产品id
	 * @return
	 */
	@RequestMapping("/deleteProduct")
	@ResponseBody
	public JSONObject deleteProduct(Long ids) {
		JSONObject res = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {

			String resStr = productApiFeign.remove(ecmId, ids);
			if (resStr != null) {
				res = JSONObject.parseObject(resStr);
				if(!res.getBoolean("success")){
					throw new EminException(res.getString("code"));
				}
			}			
		} else {
			res.put("success", false);
			res.put("message", "无法绑定主体信息，请重新登录！");
		}
		
		return res;
	}
	
	/**
	 * 保存产品特性设置
	 * @param pfStr
	 * @return
	 */
	@RequestMapping("/savePfs")
	@ResponseBody
	public JSONObject savePfs(String pfStr) {
		JSONObject res = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {

			res = productApiFeign.savePf(ecmId, pfStr);
			if(!res.getBoolean("success")){
				throw new EminException(res.getString("code"));
			}	
		} else {
			res.put("success", false);
			res.put("message", "无法绑定主体信息，请重新登录！");
		}
		
		return res;
	}

	
	
}
