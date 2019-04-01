package com.wwj1.util;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
public class common {
	public static String spiltToSql(String str) {
		StringBuffer sb = new StringBuffer();
		str = str.replaceAll("\\s*", "");
		str = str.replaceAll("，", ",");
		String[] temp = str.split(",");
		for (int i = 0; i < temp.length; i++) {
			if (!"".equals(temp[i]) && temp[i] != null)
				sb.append("'" + temp[i] + "',");
		}
		String result = sb.toString();
		String tp = result.substring(result.length() - 1, result.length());
		if (",".equals(tp))
			return result.substring(0, result.length() - 1);
		else
			return result;
	}
	
	public static boolean isBFJ(String str){
		return str.matches("[BFJWXSCD0-9,_']+?");
	}

	
	public static String getAvailablePort(String ip) {
		String port="";
		if(isHostConnectable(ip,1521)){
			port="[1521]";
		}
		if(isHostConnectable(ip,8080)){
			port+="[8080]";
		}
		if(isHostConnectable(ip,8088)){
			port+="[8088]";
		}
		if(isHostConnectable(ip,7022)){
			port+="[7022]";
		}
		if(isHostConnectable(ip,7686)){
			port+="[7686]";
		}
		if(isHostConnectable(ip,7687)){
			port+="[7687]";
		}
		if(isHostConnectable(ip,7688)){
			port+="[7688]";
		}
		if("".equals(port)) {
			return "未找到映射端口号";
		}
		return port;

	}
	
	
	public static boolean isHostConnectable(String host, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port));
        }catch(ConnectException e){
            return false;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
	
	public static boolean isHostReachable(String host, Integer timeOut) {
        try {
            return InetAddress.getByName(host).isReachable(timeOut);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
public static void main(String[] args) {
 //System.out.println(isBFJ(spiltToSql("BFJ644519031300177_137809256, BFJ644519031200160_137788681")));
	//System.out.println(getAvailablePort("1.193.160.136"));
}



}
