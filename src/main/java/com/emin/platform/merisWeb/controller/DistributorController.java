package com.emin.platform.merisWeb.controller;

import java.io.IOException;
import java.io.OutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.emin.base.controller.BaseController;
import com.emin.base.dao.PageRequest;
import com.emin.base.exception.EminException;
import com.emin.platform.merisWeb.interfaces.BrandApiFeign;
import com.emin.platform.merisWeb.interfaces.DistributorApiFeign;
import com.emin.platform.merisWeb.interfaces.DistributorFileApiFeign;
import com.emin.platform.merisWeb.interfaces.PrdCategoryApiFeign;
import com.emin.platform.merisWeb.interfaces.ProductApiFeign;

/**
 * 控制层-分销商信息管理
 * @author kakadanica
 *
 */
@Controller
@RequestMapping("/distributor")
public class DistributorController extends BaseController{
	
	@Autowired
	DistributorApiFeign distributorApiFeign;
	@Autowired
	DistributorFileApiFeign distributorFileApiFeign;

	@Autowired
	PrdCategoryApiFeign categoryApiFeign;

	@Autowired
	ProductApiFeign productApiFeign;

	@Autowired
	BrandApiFeign brandApiFeign;

	@RequestMapping("/index")
	@ResponseBody
	public ModelAndView goManage(Long pid, String name, String region, String regionIds){
		ModelAndView mv = new ModelAndView("modules/distributor/manage");
		return mv;
    }
	
	@RequestMapping("/getDistributors")
	@ResponseBody
	public JSONObject getDistributors(String name, String regionIds){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			PageRequest pageRequest = getPageRequestData();
			String pageRes = distributorApiFeign.getPages(ecmId, pageRequest.getCurrentPage(), pageRequest.getLimit(), null, name, regionIds);
			if (pageRes != null) {
				json = JSONObject.parseObject(pageRes);
				if (!json.getBoolean("success")) {
					throw new EminException(json.getString("code"));
				} else {
					JSONObject page= json.getJSONObject("result");
					if (page != null) {
						page.put("limit", pageRequest.getLimit());
						JSONArray datas = page.getJSONArray("resultList");
						for (int i = 0; i < datas.size(); i++) {
							JSONObject data = datas.getJSONObject(i);
							JSONArray brands = data.getJSONArray("brands");
							if (brands == null) {
								continue;
							}
							for (int j = 0;  j < brands.size(); j++) {
								JSONObject brand = brands.getJSONObject(j);
								Long brandId = brand.getLong("brandId");
								JSONObject brandRes = brandApiFeign.detail(ecmId, brandId);
								if(!brandRes.getBoolean("success")){
									throw new EminException(brandRes.getString("code"));
								} else {
									if (brandRes.getJSONObject("result") != null) {
										brand.put("brandName", brandRes.getJSONObject("result").getString("name"));
									}
								}
								
								String proResStr = productApiFeign.findByBrandId(ecmId, brand.getLong("brandId"));
								if (proResStr != null) {
									JSONObject proRes = JSONObject.parseObject(proResStr);
									if(!proRes.getBoolean("success")){
										throw new EminException(proRes.getString("code"));
									} else {
										JSONArray products = proRes.getJSONArray("result");
										String productsInfo = "";
										if (products.size() > 0) {
											productsInfo = "<ul class=\"list-group\">";
											for (int k = 0; k < products.size(); k++) {
												productsInfo += "<li class=\"list-group-item\">" + (k+1) + "." + products.getJSONObject(k).getString("name") + "</li>";
											}
											productsInfo += "</ul>";
										} else {
											productsInfo = "<div style=\"padding: 10px;\">该品牌暂无代理产品。</div>";
										}
										brand.put("products", productsInfo);
									}
								}
							}
						}
						
					}
				}
			}
		} else {
			throw new EminException("BASE_0.0.001");
		}
		return json;
    }
	
	@RequestMapping("/getRegionDistributosAll")
	@ResponseBody
	public JSONObject getRegionDistributosAll(String regionIds){
		JSONObject json = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String res = distributorApiFeign.findByRegionIds(ecmId, regionIds);
			if (res != null) {
				json = JSONObject.parseObject(res);
				if (!json.getBoolean("success")) {
					throw new EminException(json.getString("code"));
				}
			}
		} else {
			throw new EminException("BASE_0.0.001");
		}
		return json;
    }
	
	@RequestMapping("/tree")
	@ResponseBody
	public JSONObject getTree() {
		JSONObject res = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String resStr = distributorApiFeign.getTree(ecmId);
			if (resStr != null) {
				res = JSONObject.parseObject(resStr);
				if(!res.getBoolean("success")){
					throw new EminException(res.getString("code"));
				}
			}
		}
		return res;
    }
	
	@RequestMapping("/form")
	@ResponseBody
	public ModelAndView goForm(Long id){
		ModelAndView mv = new ModelAndView("modules/distributor/form");

		if (id != null) {//根据id获取实体详情
			Long ecmId = null;
			if (getRequest().getHeader("ecmId") != null) {
				ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
			}
			if (ecmId == null) {
				mv = new ModelAndView("500");
			} else {
				JSONObject resJson = distributorApiFeign.detail(ecmId, id);
				if (!resJson.getBoolean("success")) {
					throw new EminException(resJson.getString("code"));
				}
				JSONObject distributor = resJson.getJSONObject("result");
				if (distributor != null) {
					Long parentId = distributor.getLong("parentNodeId");
					if (parentId != null) {//根据id获取实体详情
						String parentRes = distributorApiFeign.detailParent(ecmId, parentId);
						if (parentRes != null) {
							JSONObject parent = JSONObject.parseObject(parentRes);
							if (!parent.getBoolean("success")) {
								throw new EminException(parent.getString("code"));
							}
							distributor.put("parent", parent.get("result"));
						}
					}
					
					JSONArray brands = distributor.getJSONArray("brands");
					for (int j = 0;  j < brands.size(); j++) {
						JSONObject brand = brands.getJSONObject(j);
						Long brandId = brand.getLong("brandId");
						JSONObject brandRes = brandApiFeign.detail(ecmId, brandId);
						if(!brandRes.getBoolean("success")){
							throw new EminException(brandRes.getString("code"));
						} else {
							if (brandRes.getJSONObject("result") != null) {
								brand.put("brandName", brandRes.getJSONObject("result").getString("name"));
							}
						}
					} 
					
					mv.addObject("distributor", distributor);
				}
			}
		}
		return mv;
    }
	
	/**
	 * 保存分销商信息实体
	 * @param jsonStr 分销商信息实体字符串
	 * @return
	 */
	@RequestMapping("/saveDistributor")
	@ResponseBody
	public JSONObject saveDistributor(String jsonStr){
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			resJson = distributorApiFeign.save(ecmId, jsonStr);
			if(!resJson.getBoolean("success")){
				throw new EminException(resJson.getString("code"));
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "无法绑定主体信息，请重新登录！");
		}
		return resJson;
    }
	
	/**
	 * 禁用分销商
	 * @param ids 分销商id，多个用逗号分隔，必填
	 * @return
	 */
	@RequestMapping("/disableDistributor")
	@ResponseBody
	public JSONObject disableDistributor(String ids){
		
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String resJsonStr = distributorApiFeign.disable(ecmId, ids);
			if (resJsonStr != null) {
				resJson = JSONObject.parseObject(resJsonStr);
				if(!resJson.getBoolean("success")){
					throw new EminException(resJson.getString("code"));
				}
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "无法绑定主体信息，请重新登录！");
		}
		
		return resJson;
    }
	/**
	 * 启用分销商
	 * @param ids 分销商id，多个用逗号分隔，必填
	 * @return
	 */
	@RequestMapping("/enableDistributor")
	@ResponseBody
	public JSONObject enableDistributor(String ids){
		
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String resJsonStr = distributorApiFeign.activate(ecmId, ids);
			if (resJsonStr != null) {
				resJson = JSONObject.parseObject(resJsonStr);
				if(!resJson.getBoolean("success")){
					throw new EminException(resJson.getString("code"));
				}
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "无法绑定主体信息，请重新登录！");
		}
		return resJson;
    }
	
	/**
	 * 删除分销商
	 * @param ids 分销商id，多个用逗号分隔，必填
	 * @return
	 */
	@RequestMapping("/deleteDistributor")
	@ResponseBody
	public JSONObject deleteDistributor(String ids){
		
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			Long id = Long.valueOf(ids);
			String resJsonStr = distributorApiFeign.delete(ecmId, id);
			if (resJsonStr != null) {
				resJson = JSONObject.parseObject(resJsonStr);
				if(!resJson.getBoolean("success")){
					throw new EminException(resJson.getString("code"));
				}
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "无法绑定主体信息，请重新登录！");
		}
		return resJson;
    }
	
	@RequestMapping("/getDisableIds")
	@ResponseBody
	public JSONObject getDisableIds(){
		JSONObject resJson = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			String resJsonStr = distributorApiFeign.findAllDisableIds(ecmId);
			if (resJsonStr != null) {
				resJson = JSONObject.parseObject(resJsonStr);
				if(!resJson.getBoolean("success")){
					throw new EminException(resJson.getString("code"));
				}
			}
		} else {
			resJson.put("success", false);
			resJson.put("message", "无法绑定主体信息，请重新登录！");
		}
		return resJson;
    }
	
	@RequestMapping("/uploadExcel.do")
	@ResponseBody
	private JSONObject upload(MultipartFile file){
		JSONObject json = new JSONObject();
		String originalFilename = file.getOriginalFilename();
		String suffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
		boolean isLegal =  suffix.equalsIgnoreCase("xls") || suffix.equalsIgnoreCase("xlsx");
		if (isLegal) {//类型合法的情况下上传文件
			Long ecmId = null;
			if (getRequest().getHeader("ecmId") != null) {
				ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
			}
			if (ecmId != null) {
				String resStr = distributorFileApiFeign.upload(ecmId, file);
				if (resStr != null) {
					json = JSONObject.parseObject(resStr);
					if (!json.getBoolean("success")) {
						throw new EminException(json.getString("code"));
					}
				}
			} else {
				json.put("success", false);
				json.put("message", "无法绑定主体信息，请重新登录！");
			}
		} else {
			json.put("success", false);
			json.put("message", "上传文件类型与实际需求不符");
		}
		
		return json;
	}
	
	@RequestMapping("/downloadExcel.do")
	private void download() throws IOException{
		Long ecmId = null;
		if (getRequest().getParameter("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getParameter("ecmId").toString());
		}
		if (ecmId != null) {
			byte[] res = distributorFileApiFeign.downloadExcelTpl(ecmId);
			
			getResponse().setContentType("application/octet-stream;charset=UTF-8");
			getResponse().setHeader("Content-disposition",  
                    "attachment; filename=" + new String("经销商导入模板.xlsx".getBytes("utf-8"), "ISO8859-1")); 
            OutputStream stream = getResponse().getOutputStream();
            stream.write(res);      
            //InputStream in = new ByteArrayInputStream(res);
            //XSSFWorkbook wb = new XSSFWorkbook(in);
            //wb.write(stream);
            stream.flush();
            stream.close();
            //wb.close();
		}
		
	}
	
	@RequestMapping("/uploadErrorReport.do")
	private void uploadErrorReport(String filePath) throws IOException{
		Long ecmId = null;
		if (getRequest().getParameter("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getParameter("ecmId").toString());
		}
		if (ecmId != null) {
			byte[] res = distributorFileApiFeign.errorReport(ecmId, filePath);
			
			getResponse().setContentType("application/octet-stream;charset=UTF-8");
			getResponse().setHeader("Content-disposition",  
                    "attachment; filename=" + new String("批量导入经销商报错反馈.xlsx".getBytes("utf-8"), "ISO8859-1")); 
            OutputStream stream = getResponse().getOutputStream();
            stream.write(res);      
            //InputStream in = new ByteArrayInputStream(res);
            //XSSFWorkbook wb = new XSSFWorkbook(in);
            //wb.write(stream);
            stream.flush();
            stream.close();
            //wb.close();
		}
		
	}
	
	/**
	 * 根据节点编号获取代理品牌
	 * @param nodeId
	 * @return
	 */
	@RequestMapping("/getBrandsByNodeIdAndEcmId")
	@ResponseBody
	public JSONObject getBrandsByNodeIdAndEcmId(Long ecmId, Long nodeId){
		JSONObject brands = new JSONObject();
		if (ecmId != null) {
			// 查询品牌信息，表单必选项赋值
			String res = distributorApiFeign.queryBrandByNodeId(ecmId, nodeId);
			if (res != null) {
				brands = JSONObject.parseObject(res);
				if(!brands.getBoolean("success")){
					throw new EminException(brands.getString("code"));
				}
			}
		} else {
			brands.put("success", false);
			brands.put("message", "所属主体未知，查询失败");
		}
		return brands;
    }
	
	/**
	 * 根据节点编号获取代理品牌
	 * @param nodeId
	 * @return
	 */
	@RequestMapping("/getBrandsByNodeId")
	@ResponseBody
	public JSONObject getBrandsByNodeId(Long nodeId){
		JSONObject brands = new JSONObject();
		Long ecmId = null;
		if (getRequest().getHeader("ecmId") != null) {
			ecmId = Long.valueOf(getRequest().getHeader("ecmId").toString());
		}
		if (ecmId != null) {
			// 查询品牌信息，表单必选项赋值
			String res = distributorApiFeign.queryBrandByNodeId(ecmId, nodeId);
			if (res != null) {
				brands = JSONObject.parseObject(res);
				if(!brands.getBoolean("success")){
					throw new EminException(brands.getString("code"));
				}
			}
		}
		
		return brands;
    }
}
