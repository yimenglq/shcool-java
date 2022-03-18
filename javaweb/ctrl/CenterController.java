package ctrl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.*;
import util.FileUploadUtil;

/**
 * Servlet implementation class CenterController
 */
@WebServlet("*.action")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class CenterController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CenterController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		String path = request.getServletPath();
		System.out.println("path:"+path);
		path = path.substring(path.lastIndexOf('/') + 1, path.indexOf(".action"));
		System.out.println("path002:"+path);
		if (path.equals("homepage")) {
			// 首页
			FoodService food = new FoodService();
			request.setAttribute("hot", food.getHotFoods());
			request.setAttribute("special", food.getSpecialFoods());
			request.setAttribute("recomm", food.getRecommFoods());
			request.getRequestDispatcher("/pages/homepage.jsp").forward(request, response);
		} else if (path.equals("show_detail")) {
			// 菜品详细信息
			FoodService food = new FoodService();
			String id = request.getParameter("id");
			request.setAttribute("food", food.getFood(id));
			request.getRequestDispatcher("/pages/show_detail.jsp").forward(request, response);
		}
		else if(path.equals("register")){
			String un = request.getParameter("un");
			String pw = request.getParameter("pw");
			String tp = request.getParameter("tp");
			String address = request.getParameter("address");
			if (un == null || pw == null){
				request.setAttribute("msg", "用户名和密码不能为空！");
				request.setAttribute("href", request.getContextPath() + "/register.action");
				request.getRequestDispatcher("/pages/result.jsp").forward(request, response);
			}
			else{
				UserService us = new UserService();
				Map<String, String> m = us.registerName(un);
				if (m==null){
					int r=us.register(un,pw,tp,address);
					request.setAttribute("msg","注册成功！");
					request.setAttribute("href",request.getContextPath()+"/homepage.action");
					request.getRequestDispatcher("/pages/result.jsp").forward(request,response);
				}else{
					request.setAttribute("msg","用户名已被注册！");
					request.setAttribute("href",request.getContextPath()+"/homepage.action");
					request.getRequestDispatcher("/pages/result.jsp").forward(request,response);
				}
			}
		} else if (path.equals("login")) {
			// 用户登录
			// 读取请求参数un和pw
			String un = request.getParameter("un");
			String pw = request.getParameter("pw");
			// 判断用户名和密码是否正确
			if (un == null || pw == null || un.equals("") || pw.equals("")) {
				// 用户未登录
				request.setAttribute("msg", "请先登录！");
				request.setAttribute("href", request.getContextPath() + "/homepage.action");
				request.getRequestDispatcher("/pages/result.jsp").forward(request, response);
			}

			else {
				// 调用模型进行身份验证
				UserService us = new UserService();
				Map<String, String> r = us.login(un, pw);
				if (r != null) {
					// 登录成功
					String ident = r.get("ident");
					// 用session保存用户的登录信息
					session.setAttribute("loginID", r.get("id"));
					session.setAttribute("loginName", un);
					session.setAttribute("ident", ident);
					if (ident.equals("1")) {
						// 管理员
						response.sendRedirect(request.getContextPath() + "/admin/admin_index.action");
					} else {
						// 普通用户
						response.sendRedirect(request.getContextPath() + "/user/user_index.action");
					}
				} else {
					// 登录失败
					// 不合法用户
					request.setAttribute("msg", "用户名或密码错误！");
					request.setAttribute("href", request.getContextPath() + "/homepage.action");
					request.getRequestDispatcher("/pages/result.jsp").forward(request, response);
				}
			}
		} else if (path.equals("login_ident_check")) {
			response.setContentType("appLication/json;charset=utf-8");
			String un = request.getParameter("un");
			int ident = Integer.parseInt(request.getParameter("ident"));
			Map<String,Object> map = new HashMap<>();
			UserService us = new UserService();
			int checkIdent = us.checkIdent(un);
			if (checkIdent == ident){
				map.put("userIdent",true);
				map.put("msg","身份正确");
			}else {
				map.put("userIdent",false);
				map.put("msg","身份不正确");
				//request.setAttribute("href", request.getContextPath() + "/homepage.action");
			}
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getWriter(),map);

		}
		//添加喜爱
		else if(path.equals("laiket")){
			response.setContentType("application/json;charset=utf-8");
			String userid = (String) session.getAttribute("loginID");
			String foodid = request.getParameter("id");
			UserService us = new UserService();
			int s = us.laike(userid,foodid);
			System.out.print("foodid:"+foodid+"  "+"s:"+s);
			Map<String,Object> map = new HashMap<>();
			if(s != 0){
				map.put("mylaike",s);
				map.put("laike","我喜欢的");
			}
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getWriter(),map);
		}
		//查看喜爱
		else if(path.equals("laike")){
			response.setContentType("application/json;charset=utf-8");
			String userid = (String) session.getAttribute("loginID");
			UserService us = new UserService();
			
			List<Map<String,String>> s = us.kViewlaike(userid);
			Map<String,Object> map = new HashMap<>();
			if(s != null) {
				map.put("mylaike", s);
			}
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(response.getWriter(),map);
		}


		else if (path.equals("user_index")) {
			// 在进行用户其他功能之前，前提是用户已经登录，并且session中已经存储了用户信息。
			// session保存用户的登录信息的语句可以参考如下
//			session.setAttribute("loginID", **));		//存储用户id
			// session.setAttribute("loginName", **); //存储用户名
//			session.setAttribute("ident", **);		//存储用户身份

			FoodService f = new FoodService();
			String s_fn = request.getParameter("s_fn");
			String s_type = request.getParameter("s_type");
			request.setAttribute("foods", f.getFoods(s_fn, s_type));
			FoodTypeService ft = new FoodTypeService();
			request.setAttribute("types", ft.getAllTypes());
			request.getRequestDispatcher("/pages/user/user_index.jsp").forward(request, response);

		} else if (path.equals("user_add_dc")) {
			System.out.println("你好世界");
			String[] ids = request.getParameterValues("ids");
			String[] shuliang = new String[ids.length];
			for (int i = 0; i < ids.length; i++){
				System.out.println("ids"+i+":"+ids[i]);
				String shuliangid = "shuliang"+ids[i];
				System.out.println("shuliangid"+shuliangid);
				shuliang[i] = request.getParameter(shuliangid);
			}
//			String[] nums = request.getParameterValues("num");
//			String[] prices = request.getParameterValues("price");


//			System.out.println(shuliang.length);
//			System.out.println("ids:"+ids[0]+"nums:+nums[0]+prices:+prices[0]");

			String userid = (String) session.getAttribute("loginID");
			System.out.println("0");
			DiningCarService dc = new DiningCarService();
			System.out.println("1");
			int r = dc.addToDC(userid, ids, shuliang);
//			int r = dc.addToDC(userid,ids,null,null);
			System.out.println("r:" + r);
			request.setAttribute("msg", "成功将" + r + "个菜品加入点餐车！");
			request.setAttribute("href", request.getContextPath() + "/pages/user/user_show_dc.action");
			request.getRequestDispatcher("/pages/result.jsp").forward(request, response);
//			response.sendRedirect(request.getContextPath() + "/pages/user/user_show_dc.jsp");
		}
		//添加订单
		else if (path.equals("user_add_order")) {
			String[] ids = request.getParameterValues("ids");
			String userid = (String) session.getAttribute("loginID");
			//获取对应数量
			String[] shuliang = new String[ids.length];
			for (int i = 0; i < ids.length; i++){
				System.out.println("ids"+i+":"+ids[i]);
				String shuliangid = "shuliang"+ids[i];
				System.out.println("shuliangid"+shuliangid);
				shuliang[i] = request.getParameter(shuliangid);
			}
			OrderService odr = new OrderService();
			odr.Order(userid,shuliang,ids);

			request.setAttribute("odr", odr.showOrders(userid));
			request.getRequestDispatcher("/pages/user/user_order.jsp").forward(request, response);
		}
		else if (path.equals("user_show_order")) {
			String userid = (String) session.getAttribute("loginID");
			OrderService odr = new OrderService();

			request.setAttribute("odr", odr.showOrders(userid));
			request.getRequestDispatcher("/pages/user/user_order.jsp").forward(request, response);
		}
		else if (path.equals("user_add_dcOne")) {
			String[] ids = request.getParameterValues("ids");

			String userid = (String) session.getAttribute("loginID");
			DiningCarService dc = new DiningCarService();
			int r = dc.addToDCOne(userid, ids);
			request.setAttribute("msg", "成功将" + r + "个菜品加入点餐车！");
			request.setAttribute("href", request.getContextPath() + "/user/user_show_dc.action");
			request.getRequestDispatcher("/pages/result.jsp").forward(request, response);
//			request.getRequestDispatcher("/user/user_show_dc.action");
		} else if (path.equals("user_show_dc")) {
			String userid = (String) session.getAttribute("loginID");
			DiningCarService dc = new DiningCarService();
			request.setAttribute("dc", dc.showDC(userid));
			System.out.println("dc:" + dc.showDC(userid));
			request.getRequestDispatcher("/pages/user/user_show_dc.jsp").forward(request, response);
		} else if (path.equals("user_del_dc")) {
			response.setContentType("application/json;charset=utf-8");
			String[] ids = request.getParameterValues("cbox");
			DiningCarService dc = new DiningCarService();
			int r = dc.delFromDC(ids);
			request.setAttribute("msg", "成功将" + r + "个菜品从点餐车删除！");
			request.setAttribute("href", request.getContextPath() + "/user/user_show_dc.action");
			request.getRequestDispatcher("/pages/result.jsp").forward(request, response);
		} else if (path.equals("admin_index")) {
			request.getRequestDispatcher("/pages/admin/admin_index.jsp").forward(request, response);
		} else if (path.equals("admin_show_dc")) {
			// 读取所有用户的点餐情况
			DiningCarService dc = new DiningCarService();
			request.setAttribute("dcs", dc.showAllDC());
			request.getRequestDispatcher("/pages/admin/admin_show_dc.jsp").forward(request, response);
		} else if (path.equals("admin_show_jf")) {
			// 读取所有用户的积分情况
			DiningCarService dc = new DiningCarService();
			request.setAttribute("dcs", dc.showAllDC());
			request.getRequestDispatcher("/pages/admin/admin_show_jf.jsp").forward(request, response);
		} else if (path.equals("admin_add_food_do")) {
			//foodname,feature,material,price, type,picture,comment
			String foodname = request.getParameter("foodname");
			String feature = request.getParameter("feature");
			String material = request.getParameter("material");
			String price = request.getParameter("price");
			String type = request.getParameter("type");
			String picture = request.getParameter("picture");
			String comment = request.getParameter("comment");
			FoodService f = new FoodService();
			int re = f.addFood(foodname, feature, material, price, type, picture, comment);
			if (re != 0) {
				request.setAttribute("foods", f.getAllFood());
				FoodTypeService ft = new FoodTypeService();
				request.setAttribute("types", ft.getAllTypes());
				request.getRequestDispatcher("/pages/admin/admin_list_food.jsp").forward(request, response);

			}

		} else if (path.equals("admin_add_food")) {

			FoodTypeService ft = new FoodTypeService();
			request.setAttribute("types", ft.getAllTypes());


			request.getRequestDispatcher("/pages/admin/admin_add_food.jsp").forward(request, response);
		} else if (path.equals("admin_list_user")) {
			session.getAttribute("loginID");
			String userCx = request.getParameter("user_cx");
			UserService u = new UserService();
			request.setAttribute("Users", u.getUsers(userCx));
			System.out.println(u.getUsers());
			request.getRequestDispatcher("/pages/admin/admin_show_user.jsp").forward(request, response);

		}
		//删除用户
		else if (path.equals("admin_delete_user")) {
			String id = request.getParameter("id");
			System.out.println("id:" + id);
			UserService user = new UserService();
			user.deluser(id);
			request.setAttribute("Users", user.getUsers());
			System.out.println(user.getUsers());
			request.getRequestDispatcher("/pages/admin/admin_show_user.jsp").forward(request, response);
		}
		//商品分类管理
		else if (path.equals("admin_foodtyupe")) {
			FoodTypeService fdtp = new FoodTypeService();
			String tyupe = request.getParameter("tyupe");
			fdtp.getfoodTypef(tyupe);
			request.setAttribute("Types", fdtp.getfoodType());
			System.out.print("fdtp:" + fdtp.getfoodType());
			request.getRequestDispatcher("/pages/admin/admin_foodtyupe.jsp").forward(request, response);
		}
		//修改
		else if (path.equals("admin_edit_tyupe")) {
			String id = request.getParameter("id");
			String name = request.getParameter(id);
			FoodTypeService fdtp = new FoodTypeService();
			System.out.println("id:" + id);
			fdtp.updata(id, name);
			request.setAttribute("Types", fdtp.getfoodType());
			System.out.print("fdtp:" + fdtp.getfoodType());
			request.getRequestDispatcher("/pages/admin/admin_foodtyupe.jsp").forward(request, response);
		}
		//删除
		else if (path.equals("admin_delete_tyupe")) {
			String id = request.getParameter("id");
			System.out.println("id:" + id);
			FoodTypeService fdtp = new FoodTypeService();
			fdtp.deltype(id);
			request.setAttribute("Types", fdtp.getfoodType());
			System.out.print("fdtp:" + fdtp.getfoodType());
			request.getRequestDispatcher("/pages/admin/admin_foodtyupe.jsp").forward(request, response);
		}

		//商品管理
		else if (path.equals("admin_list_food")) {
			String s_fn = request.getParameter("s_fn");
			String s_type = request.getParameter("s_type");
			FoodService f = new FoodService();
			request.setAttribute("foods", f.getFoods(s_fn, s_type));
			FoodTypeService ft = new FoodTypeService();
			request.setAttribute("types", ft.getAllTypes());
			request.getRequestDispatcher("/pages/admin/admin_list_food.jsp").forward(request, response);
		}
		//
		else if (path.equals("admin_edit_food")) {
			String id = request.getParameter("id");
			FoodTypeService ft = new FoodTypeService();
			request.setAttribute("types", ft.getAllTypes());
			request.setAttribute("food", new FoodService().getFood(id));
			request.getRequestDispatcher("/pages/admin/admin_edit_food.jsp").forward(request, response);
		}
		//删除
		else if (path.equals("admin_delete_food")) {
			String id = request.getParameter("id");
			FoodService food = new FoodService();
			int result = food.deleteFood(id);
			FoodTypeService ft = new FoodTypeService();
			request.setAttribute("types", ft.getAllTypes());
			request.setAttribute("foods", new FoodService().getAllFood());
			request.getRequestDispatcher("/pages/admin/admin_list_food.jsp").forward(request, response);
		} else if (path.equals("admin_edit_food_do")) {
			try {
				String id = request.getParameter("id");
				String foodname = request.getParameter("fn");
				String feature = request.getParameter("fea");
				String material = request.getParameter("mat");
				String type = request.getParameter("type");
				String price = request.getParameter("price");
				String comment = request.getParameter("com");
				String picture = null;
				Part img = request.getPart("img");
				// 判断上传文件的扩展名是否符合要求
				String fileExtName = FileUploadUtil.getFileExtName(img);
				if (!fileExtName.equals("") && !fileExtName.equalsIgnoreCase(".jpg")
						&& !fileExtName.equalsIgnoreCase(".png") && !fileExtName.equalsIgnoreCase(".gif")) {
					request.setAttribute("msg", "上传文件的扩展名应为jpg,png或gif！");
					request.setAttribute("href", "javascript:history.back()");
					request.getRequestDispatcher("/pages/result.jsp").forward(request, response);
				} else {
					picture = FileUploadUtil.uploadSingleFile(img, request);
					// 调用模型插入数据库
					FoodService f = new FoodService();
					int r = f.editFood(foodname, feature, material, price, type, picture, comment, id);
					request.setAttribute("msg", r == 1 ? "修改菜品成功！" : "修改菜品失败！");
					request.setAttribute("href", request.getContextPath() + "/admin/admin_list_food.action");
					request.getRequestDispatcher("/pages/result.jsp").forward(request, response);
				}
			} catch (IllegalStateException e) {
				request.setAttribute("msg", "上传文件大小应小于5M！");
				request.setAttribute("href", "javascript:history.back()");
				request.getRequestDispatcher("/pages/result.jsp").forward(request, response);
			}
		}
	}

}

