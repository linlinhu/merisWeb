package com.emin.platform.merisWeb.interfaces;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.emin.platform.merisWeb.config.FeignMultipartSupportConfig;


/***
 * 主体接口桥梁定义
 * @author kakadanica
 */
@FeignClient(value = "zuul",configuration = FeignMultipartSupportConfig.class)
public interface DistributorFileApiFeign {
	
	@RequestMapping(value = "/api-dm/file/uploadExcel",method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	String upload(@RequestHeader(value="ecmId") Long ecmId, @RequestPart(value="file") MultipartFile file);

	@RequestMapping(value = "/api-dm/file/downloadExcel",method = RequestMethod.GET)
	byte[] downloadExcelTpl(@RequestHeader(value="ecmId") Long ecmId);
	
	@RequestMapping(value = "/api-dm/file/downloadErrorExcel",method = RequestMethod.GET)
	byte[] errorReport(@RequestHeader(value="ecmId") Long ecmId, @RequestParam(value="filePath") String filePath);
	
}
