package com.xh.controller;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.xh.service.ifce.ISiteInfoService;
import com.xh.util.ResultObjStr;

@RestController
@RequestMapping(value = "/siteInfo")
public class SiteInfoController {

	private static Logger logger = LoggerFactory.getLogger(SiteInfoController.class);
	
	@Autowired
	private ISiteInfoService siteInfoService;
	
	@PostMapping(value = "/importExcel")
	public String importExcel (@RequestParam("file") MultipartFile file,Integer myid) {
		try {
			return siteInfoService.importExcel(file, myid).toJson();
		} catch (Exception e) {
			logger.error("上传excel后台错误 : " + e.getMessage());
			return new ResultObjStr(ResultObjStr.ERROR, "后台错误", null).toJson();
		}
	}
	
	@GetMapping(value = "/exportExcel")
	public String exportExcel (Integer myid, String fileName, String excelFormat, HttpServletResponse response,HttpServletRequest request) {
		response.setContentType("application/force-download");// 设置下载框
		response.addHeader("Content-Disposition","attachment;filename="+fileName+"." + excelFormat);// 设置下载文件名（*+fileName这个值可以定死，下载时会引用这个名字如：”aa.xml“）
		try {
			OutputStream 	out = response.getOutputStream();//拿到用户选择的路径
			this.siteInfoService.exportExcel(myid, fileName, excelFormat, out);
		} catch (IOException e) {
			logger.error("下载excel后台错误 : " + e.getMessage());
		}
		return null;
	}
	
}
