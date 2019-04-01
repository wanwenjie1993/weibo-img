package com.wwj1.controller;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.wwj1.common.DbPoolConnection;
import com.wwj1.common.Message;
import com.wwj1.common.Response;
import com.wwj1.exception.ServiceException;
import com.wwj1.util.GeneralUtils;
import com.wwj1.util.SinaPicBedUtil;




@Controller 
public class PicController extends Response{
	private static Logger logger =LoggerFactory.getLogger(PicController.class);
	static DbPoolConnection dbp = DbPoolConnection.getInstance();
	@PostMapping("/sina/upload.php")
	@ResponseBody
	public Message<List<String>> upload(@RequestParam("file") MultipartFile[] multipartFiles, 
			@RequestParam(value="size", required=false, defaultValue="0") Integer size) throws IOException, ServiceException {
		String userName="1315131416@qq.com";
		String passWord="weilian.w";
		if(GeneralUtils.isEmpty(multipartFiles)) {
			throw new ServiceException("图片不能为空");
		}else if(multipartFiles.length > 10) {
			throw new ServiceException("一次最多只能上传10张图片");
		} 
		String base64name = GeneralUtils.base64Encode(userName);
		String cookies = SinaPicBedUtil.getSinaCookie(base64name, passWord);// 持久化起来 不用每次都登录 一般失效7天左右
		List<String> url = SinaPicBedUtil.uploadFile(multipartFiles, cookies, size);
		logger.info("multipartFiles:"+multipartFiles[0].getOriginalFilename());

		logger.info("url:"+url.get(0));
		String sql="INSERT INTO `weiboPic`.`t_pic` ( `"+multipartFiles[0].getOriginalFilename()+"`, `"+url.get(0)+"`, `"+userName+"`) VALUES ('name', 'url', 'name')";
		  try { 
			  executeUpdateBySQL(sql); 
			  }catch (SQLException e) {
				  // TODOAuto-generated catch block 
				  e.printStackTrace(); 
				  return super.ERROR(e.getMessage());
			  }
		 
		return super.SUCCESS(url);
	}
	private static void executeUpdateBySQL(String sql) throws SQLException {
		DruidPooledConnection con = dbp.getConnection();
		PreparedStatement ps = con.prepareStatement(sql);
		ps.executeUpdate();
		ps.close();
		con.close();
	}
}
