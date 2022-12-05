package com.bjpowernode.crm.workbench.web.controller;


import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.TranRemarkDao;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ContactsService;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ContactsServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.TranServiceImpl;
import com.bjpowernode.crm.workbench.vo.PaginationVO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TranController extends HttpServlet {

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到交易控制器");

        String path = request.getServletPath();

        if("/workbench/transaction/add.do".equals(path)){

            add(request, response);

        }else if("/workbench/transaction/getCustomerName.do".equals(path)){

            getCustomerName(request, response);

        }else if("/workbench/transaction/save.do".equals(path)){

            save(request, response);

        }else if("/workbench/transaction/detail.do".equals(path)){

            detail(request, response);

        }else if("/workbench/transaction/getHistoryListByTranId.do".equals(path)){

            getHistoryListByTranId(request, response);

        }else if("/workbench/transaction/changeStage.do".equals(path)){

            changeStage(request, response);

        }else if("/workbench/transaction/getCharts.do".equals(path)){

            getCharts(request, response);

        }else if("/workbench/transaction/pageList.do".equals(path)){

            pageList(request, response);

        }else if("/workbench/transaction/edit.do".equals(path)){

            edit(request, response);

        }else if("/workbench/transaction/delete.do".equals(path)){

            delete(request, response);

        }else if("/workbench/transaction/getActivityListByName.do".equals(path)){

            getActivityListByName(request, response);

        }else if("/workbench/transaction/getContactsListByName.do".equals(path)){

            getContactsListByName(request, response);

        }else if("/workbench/transaction/update.do".equals(path)){

            update(request, response);

        }else if("/workbench/transaction/getRemarkListByTranId.do".equals(path)){

            getRemarkListByTranId(request, response);

        }else if("/workbench/transaction/saveRemark.do".equals(path)){

            saveRemark(request, response);

        }else if("/workbench/transaction/deleteRemark.do".equals(path)){

            deleteRemark(request, response);

        }else if("/workbench/transaction/updateRemark.do".equals(path)){

            updateRemark(request, response);

        }else if("/workbench/transaction/detail2edit.do".equals(path)){

            detail2edit(request, response);

        }else if("/workbench/transaction/deleteDetail.do".equals(path)){

            deleteDetail(request, response);

        }




    }

    private void deleteDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("执行交易详细页的删除操作");

        String ids[] = request.getParameterValues("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.delete(ids);

        if(flag){

            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");

        }

    }


    private void detail2edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到交易详细页跳转到交易修改页的操作");

        String id = request.getParameter("id");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran t = ts.getTranListByTid(id);

        Map<String, String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(t.getStage());
        t.setPossibility(possibility);

        request.setAttribute("uList", uList);
        request.setAttribute("t", t);
        request.getRequestDispatcher("/workbench/transaction/detail2edit.jsp").forward(request, response);

    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行修改备注的操作");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        TranRemark tr = new TranRemark();

        tr.setId(id);
        tr.setNoteContent(noteContent);
        tr.setEditFlag(editFlag);
        tr.setEditBy(editBy);
        tr.setEditTime(editTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.updateRemark(tr);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", flag);
        map.put("tr", tr);

        PrintJson.printJsonObj(response, map);

    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("删除备注操作");

        String id = request.getParameter("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.deleteRemark(id);

        PrintJson.printJsonFlag(response, flag);

    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行添加备注操作");

        String tranId = request.getParameter("tranId");
        String noteContent = request.getParameter("noteContent");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        TranRemark tr = new TranRemark();
        tr.setId(id);
        tr.setCreateBy(createBy);
        tr.setCreateTime(createTime);
        tr.setTranId(tranId);
        tr.setNoteContent(noteContent);
        tr.setEditFlag(editFlag);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.saveRemark(tr);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", flag);
        map.put("tr", tr);

        PrintJson.printJsonObj(response, map);

    }

    private void getRemarkListByTranId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据交易id，取得备注信息列表");

        String tranId = request.getParameter("tranId");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<TranRemark> trList = ts.getRemarkListByTranId(tranId);

        PrintJson.printJsonObj(response, trList);

    }

    private void update(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {

        System.out.println("执行交易修改操作");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerId = request.getParameter("customerId");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String possibility = request.getParameter("possibility");
        String contactsName = request.getParameter("contactsName");
        String customerName = request.getParameter("customerName");
        String activityName = request.getParameter("activityName");

        Tran t = new Tran();
        t.setId(id);
        t.setCustomerName(customerName);
        t.setContactsName(contactsName);
        t.setActivityName(activityName);
        t.setEditBy(editBy);
        t.setEditTime(editTime);
        t.setOwner(owner);
        t.setName(name);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);
        t.setCustomerId(customerId);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setPossibility(possibility);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.update(t);

        if(flag){

            //如果添加交易成功，跳转到列表页
            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");

        }


    }

    private void getContactsListByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询联系人列表（根据名称模糊查）");

        String cname = request.getParameter("cname");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        List<Contacts> cList = cs.getContactsListByName(cname);

        PrintJson.printJsonObj(response, cList);

    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询市场活动列表（根据名称模糊查）");

        String aname = request.getParameter("aname");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByName(aname);

        PrintJson.printJsonObj(response, aList);

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行交易的删除操作");

        String ids[] = request.getParameterValues("id");
        System.out.println("=========ids:"+ids);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.delete(ids);

        PrintJson.printJsonFlag(response, flag);


    }

    private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到跳转到交易修改页的操作");

        String id = request.getParameter("id");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran t = ts.getTranListByTid(id);

        Map<String, String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(t.getStage());
        t.setPossibility(possibility);

        request.setAttribute("uList", uList);
        request.setAttribute("t", t);
        request.getRequestDispatcher("/workbench/transaction/edit.jsp").forward(request, response);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询交易信息列表的操作（结合条件查询+分页查询）");

        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String contactsName = request.getParameter("contactsName");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("owner", owner);
        map.put("customerName", customerName);
        map.put("stage", stage);
        map.put("type", type);
        map.put("source", source);
        map.put("contactsName", contactsName);
        map.put("pageNoStr", pageNoStr);
        map.put("pageSize", pageSize);
        map.put("skipCount", skipCount);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        /*

            前端要：交易信息列表
                    查询的总条数

         */
        PaginationVO<Tran> vo = ts.pageList(map);

        //vo-->{"total":100, "dataList":[{交易1},{2},{3}]}
        PrintJson.printJsonObj(response, vo);

    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得交易阶段数量统计图表的数据");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        /*

            业务层为我们返回
                total
                dataList

                通过map打包以上两项进行返回

         */
        Map<String, Object> map = ts.getCharts();

        PrintJson.printJsonObj(response, map);

    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行改变阶段的操作");

        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        Tran t = new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.changeStage(t);

        Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        t.setPossibility(pMap.get(stage));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", flag);
        map.put("t", t);

        PrintJson.printJsonObj(response, map);

    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据交易id取得相应的历史列表");

        String tranId = request.getParameter("tranId");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<TranHistory> thList = ts.getHistoryListByTranId(tranId);

        //阶段和可能性之间的对应关系
        Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");

        //将交易历史列表遍历
        for(TranHistory th : thList){

            //根据每一条交易历史，取出每一个阶段
            String stage = th.getStage();
            String possibility = pMap.get(stage);
            th.setPossibility(possibility);

        }


        PrintJson.printJsonObj(response, thList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("跳转到详细信息页");

        String id = request.getParameter("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Tran t = ts.detail(id);

        //处理可能性
        /*

            阶段 t
            阶段和可能性之间的对应关系 pMap
         */

        String stage = t.getStage();
        Map<String, String> pMap = (Map<String, String>) this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);

        /*

            t

            vo
                private Tran t;
                private String possibility;

         */

        t.setPossibility(possibility);

        request.setAttribute("t", t);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("执行添加交易的操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");//此处我们暂时只有客户的名称，还没有id
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran t =new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateBy(createBy);
        t.setCreateTime(createTime);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.save(t, customerName);

        if(flag){

            //如果添加交易成功，跳转到列表页
            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");

        }

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得 客户名称列表（按照客户名称进行模糊查询）");

        String name = request.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = cs.getCustomerName(name);

        PrintJson.printJsonObj(response, sList);

    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到跳转到交易添加页的操作");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        request.setAttribute("uList", uList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);

    }


}











