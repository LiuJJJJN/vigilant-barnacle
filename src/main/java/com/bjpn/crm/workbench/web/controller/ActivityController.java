package com.bjpn.crm.workbench.web.controller;

import com.bjpn.crm.settings.domain.User;
import com.bjpn.crm.settings.service.UserService;
import com.bjpn.crm.settings.service.impl.UserServiceImpl;
import com.bjpn.crm.utils.DateTimeUtil;
import com.bjpn.crm.utils.PrintJson;
import com.bjpn.crm.utils.ServiceFactory;
import com.bjpn.crm.utils.UUIDUtil;
import com.bjpn.crm.workbench.domain.Activity;
import com.bjpn.crm.workbench.service.ActivityService;
import com.bjpn.crm.workbench.service.impl.ActivityServiceImpl;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

/**
 * Activity表对应的控制层、表示层
 * 这里使用了模板模式，让具体方法实现功能
 */
public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/workbench/activity/getUserList.do".equals(path)){
            getUserList(request, response);
        }
        if ("/workbench/activity/save.do".equals(path)){
            save(request, response);
        }
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        //String editTime;
        //String editBy;

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        ActivityService service = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag = service.save(activity);
        PrintJson.printJsonFlag(response, flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        //使用动态代理，拿张三取李四
        UserService service = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> userList = service.getUserList();

        PrintJson.printJsonObj(response, userList);
    }

}
