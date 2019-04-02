package com.wwj1.controller;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
	@RequestMapping({"/","/index"})
	public String index(){
		logger.info("go to index");
		return "index";
	}
	@RequestMapping({"/view"})
	public String view(Model model){
		try {
		List<Map<String, Object>> list=	findModeResult("select * from b3_solo_wbimg",null);
		logger.info("list:"+list);
			model.addAttribute("list", list);
		}catch (SQLException e) {
			// TODOAuto-generated catch block
			e.printStackTrace();
			logger.info(e.getMessage());
		}

		logger.info("go to view");
		return "view";
	}

	@PostMapping("/sina/upload.php")
	@ResponseBody
	public Message<List<String>> upload(@RequestParam("file") MultipartFile[] multipartFiles, 
			@RequestParam(value="size", required=false, defaultValue="0") Integer size) throws IOException, ServiceException {
		String userName="1315131416@qq.com";
		String passWord="xxxx";
		if(GeneralUtils.isEmpty(multipartFiles)) {
			throw new ServiceException("图片不能为空");
		}
		String base64name = GeneralUtils.base64Encode(userName);
		String cookies = SinaPicBedUtil.getSinaCookie(base64name, passWord);// 持久化起来 不用每次都登录 一般失效7天左右
		List<String> url = SinaPicBedUtil.uploadFile(multipartFiles, cookies, size);
		logger.info("multipartFiles:"+multipartFiles[0].getOriginalFilename());
		logger.info("url:"+url.get(0));
		String sql="INSERT INTO `solo`.`b3_solo_wbimg` (`pic_name`, `pic_url`, `create_user`) VALUES  ( '"+multipartFiles[0].getOriginalFilename()+"','"+url.get(0)+"','"+userName+"')";
		logger.info("sql:"+sql);
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


	/**查询多条记录
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> findModeResult(String sql, List<Object> params) throws SQLException{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		int index = 1;
		DruidPooledConnection con = dbp.getConnection();
		PreparedStatement pstmt = con.prepareStatement(sql);
		if(params != null && !params.isEmpty()){
			for(int i = 0; i<params.size(); i++){
				pstmt.setObject(index++, params.get(i));
			}
		}
		ResultSet resultSet = pstmt.executeQuery();
		ResultSetMetaData metaData = resultSet.getMetaData();
		int cols_len = metaData.getColumnCount();
		while(resultSet.next()){
			Map<String, Object> map = new HashMap<String, Object>();
			for(int i=0; i<cols_len; i++){
				String cols_name = metaData.getColumnName(i+1);
				Object cols_value = resultSet.getObject(cols_name);
				if(cols_value == null){
					cols_value = "";
				}
				map.put(cols_name, cols_value);
			}
			list.add(map);
		}

		return list;
	}

}
