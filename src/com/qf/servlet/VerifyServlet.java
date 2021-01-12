package com.qf.servlet;

import com.qf.util.VerifyCode;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@WebServlet("/verify")
public class VerifyServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//创建验证码VerifyCode对象
		VerifyCode vc = new VerifyCode();
		//获取图片对象
		BufferedImage bi = vc.getImage();
		//获得图片的文本内容
		String text = vc.getText();

		//将系统生成的文本内容保存到session中
		request.getSession().setAttribute("text", text);
		//向浏览器输出图片
		ImageIO.write(bi, "JPEG", response.getOutputStream());
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
