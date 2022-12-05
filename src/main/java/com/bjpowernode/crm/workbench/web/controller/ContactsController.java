package com.bjpowernode.crm.workbench.web.controller;


import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ContactsController extends HttpServlet {

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到联系人控制器");

        String path = request.getServletPath();

        if("/workbench/contacts/pageList.do".equals(path)){

            pageList(request, response);

        }else if("/workbench/contacts/getCustomerName.do".equals(path)){

            getCustomerName(request, response);

        }else if("/workbench/contacts/getUserList.do".equals(path)){

            getUserList(request, response);

        }else if("/workbench/contacts/save.do".equals(path)){

            save(request, response);

        }else if("/workbench/contacts/delete.do".equals(path)){

            delete(request, response);

        }else if("/workbench/contacts/getUserListAndContacts.do".equals(path)){

            getUserListAndContacts(request, response);

        }else if("/workbench/contacts/update.do".equals(path)){

            update(request, response);

        }else if("/workbench/contacts/detail.do".equals(path)){

            detail(request, response);

        }else if("/workbench/contacts/getRemarkListByCid.do".equals(path)){

            getRemarkListByCid(request, response);

        }else if("/workbench/contacts/saveRemark.do".equals(path)){

            saveRemark(request, response);

        }else if("/workbench/contacts/deleteRemark.do".equals(path)){

            deleteRemark(request, response);

        }else if("/workbench/contacts/updateRemark.do".equals(path)){

            updateRemark(request, response);

        }else if("/workbench/contacts/getActivityListByName.do".equals(path)){

            getActivityListByName(request, response);

        }else if("/workbench/contacts/getContactsListByName.do".equals(path)){

            getContactsListByName(request, response);

        }else if("/workbench/contacts/saveTran.do".equals(path)){

            saveTran(request, response);

        }else if("/workbench/contacts/add.do".equals(path)){

            add(request, response);

        }else if("/workbench/contacts/getTranListByContactsId.do".equals(path)){

            getTranListByContactsId(request, response);

        }else if("/workbench/contacts/deleteTranByTranId.do".equals(path)){

            deleteTranByTranId(request, response);

        }else if("/workbench/contacts/getActivityListByContactsId.do".equals(path)){

            getActivityListByContactsId(request, response);

        }else if("/workbench/contacts/getActivityListByNameAndNotByContactsId.do".equals(path)){

            getActivityListByNameAndNotByContactsId(request, response);

        }else if("/workbench/contacts/bound.do".equals(path)){

            bound(request, response);

        }else if("/workbench/contacts/unbound.do".equals(path)){

            unbound(request, response);

        }else if("/workbench/contacts/detailActivity.do".equals(path)){

            detailActivity(request, response);

        }else if("/workbench/contacts/updateDetail.do".equals(path)){

            updateDetail(request, response);

        }else if("/workbench/contacts/deleteDetail.do".equals(path)){

            deleteDetail(request, response);

        }



    }

    private void deleteDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("执行联系人详细页的删除操作再跳到联系人页");

        String ids[] = request.getParameterValues("id");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.delete(ids);

        if(flag){

            response.sendRedirect(request.getContextPath() + "/workbench/contacts/index.jsp");
            //request.getRequestDispatcher("/workbench/contacts/index.jsp").forward(request, response);

        }

    }

    private void updateDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("执行联系人详细页修改操作再跳回到详细页");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String source = request.getParameter("source");
        String customerId = request.getParameter("customerId");
        String customerName = request.getParameter("customerName");
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String email = request.getParameter("email");
        String mphone = request.getParameter("mphone");
        String job = request.getParameter("job");
        String birth = request.getParameter("birth");
        //修改人：当前登录用户
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        //修改时间：当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Contacts c = new Contacts();
        c.setId(id);
        c.setEditTime(editTime);
        c.setEditBy(editBy);
        c.setCustomerName(customerName);
        c.setSource(source);
        c.setOwner(owner);
        c.setNextContactTime(nextContactTime);
        c.setMphone(mphone);
        c.setJob(job);
        c.setFullname(fullname);
        c.setEmail(email);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setBirth(birth);
        c.setAppellation(appellation);
        c.setAddress(address);
        c.setCustomerId(customerId);

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        c = cs.updateDetail(c);

        request.setAttribute("c", c);

        request.getRequestDispatcher("/workbench/contacts/detail.jsp").forward(request, response);

    }

    private void detailActivity(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到跳转到详细信息页的操作");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity a = as.detail(id);

        request.setAttribute("a", a);

        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request, response);

    }

    private void unbound(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行解除关联操作");

        String id = request.getParameter("id");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.unbound(id);

        PrintJson.printJsonFlag(response, flag);

    }

    private void bound(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行关联市场活动的操作");

        String contactsId = request.getParameter("contactsId");
        String aids[] = request.getParameterValues("aid");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.bound(contactsId, aids);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getActivityListByNameAndNotByContactsId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询市场活动列表（根据名称模糊查+排除掉已经关联指定联系人的列表）");

        String aname = request.getParameter("aname");
        String contactsId = request.getParameter("contactsId");

        Map<String, String> map = new HashMap<String, String>();
        map.put("aname", aname);
        map.put("contactsId", contactsId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByNameAndNotByContactsId(map);

        PrintJson.printJsonObj(response, aList);

    }

    private void getActivityListByContactsId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据联系人id查询关联的市场活动列表");

        String contactsId = request.getParameter("contactsId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByContactsId(contactsId);

        PrintJson.printJsonObj(response, aList);

    }

    private void deleteTranByTranId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行删除交易操作");

        String id = request.getParameter("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.deleteTranByTranId(id);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getTranListByContactsId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据联系人id查询关联的市场活动列表");

        String contactsId = request.getParameter("contactsId");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<Tran> tList = ts.getTranListByContactsId(contactsId);

        PrintJson.printJsonObj(response, tList);


    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到跳转到交易添加页的操作");

        String contactsId = request.getParameter("contactsId");

        UserService us = (UserService)ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        String contactsName = cs.getContactsNameByCid(contactsId);

        request.setAttribute("uList", uList);
        request.setAttribute("contactsId", contactsId);
        request.setAttribute("contactsName", contactsName);
        request.getRequestDispatcher("/workbench/contacts/save.jsp").forward(request, response);

    }

    private void saveTran(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException  {

        System.out.println("执行新建交易的操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");
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

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setName(name);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateBy(createBy);
        t.setCreateTime(createTime);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.save(t, customerName);

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());
        Contacts c = cs.detail(t.getContactsId());

        if(flag){

            request.setAttribute("c", c);
            request.getRequestDispatcher("/workbench/contacts/detail.jsp").forward(request, response);

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

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行修改备注的操作");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ContactsRemark cr = new ContactsRemark();

        cr.setId(id);
        cr.setNoteContent(noteContent);
        cr.setEditFlag(editFlag);
        cr.setEditBy(editBy);
        cr.setEditTime(editTime);

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.updateRemark(cr);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", flag);
        map.put("cr", cr);

        PrintJson.printJsonObj(response, map);

    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("删除备注操作");

        String id = request.getParameter("id");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.deleteRemark(id);

        PrintJson.printJsonFlag(response, flag);

    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行添加备注操作");

        String noteContent = request.getParameter("noteContent");
        String contactsId = request.getParameter("contactsId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ContactsRemark cr = new ContactsRemark();
        cr.setId(id);
        cr.setNoteContent(noteContent);
        cr.setEditFlag(editFlag);
        cr.setCreateTime(createTime);
        cr.setCreateBy(createBy);
        cr.setContactsId(contactsId);

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.saveRemark(cr);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", flag);
        map.put("cr", cr);

        PrintJson.printJsonObj(response, map);

    }

    private void getRemarkListByCid(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据联系人id，取得备注信息列表");

        String contactsId = request.getParameter("contactsId");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        List<ContactsRemark> crList = cs.getRemarkListByCid(contactsId);

        PrintJson.printJsonObj(response, crList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到跳转到详细信息页的操作");

        String id = request.getParameter("id");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        Contacts c = cs.detail(id);

        request.setAttribute("c", c);

        request.getRequestDispatcher("/workbench/contacts/detail.jsp").forward(request, response);

    }

    private void update(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行联系人修改操作");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String source = request.getParameter("source");
        String customerId = request.getParameter("customerId");
        String customerName = request.getParameter("customerName");
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String email = request.getParameter("email");
        String mphone = request.getParameter("mphone");
        String job = request.getParameter("job");
        String birth = request.getParameter("birth");
        //修改人：当前登录用户
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        //修改时间：当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Contacts c = new Contacts();
        c.setId(id);
        c.setEditTime(editTime);
        c.setEditBy(editBy);
        c.setCustomerName(customerName);
        c.setSource(source);
        c.setOwner(owner);
        c.setNextContactTime(nextContactTime);
        c.setMphone(mphone);
        c.setJob(job);
        c.setFullname(fullname);
        c.setEmail(email);
        c.setDescription(description);
        c.setContactSummary(contactSummary);
        c.setBirth(birth);
        c.setAppellation(appellation);
        c.setAddress(address);
        c.setCustomerId(customerId);

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.update(c);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getUserListAndContacts(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询用户信息列表和根据联系人id查询单条记录的操作");

        String id = request.getParameter("id");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        /*

            前端管业务层去要
            uList
            u

         */
        Map<String, Object> map = cs.getUserListAndContacts(id);

        PrintJson.printJsonObj(response, map);


    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行联系人的删除操作");

        String ids[] = request.getParameterValues("id");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.delete(ids);

        PrintJson.printJsonFlag(response, flag);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行联系人添加操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String source = request.getParameter("source");
        String customerName = request.getParameter("customerName");
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String email = request.getParameter("email");
        String mphone = request.getParameter("mphone");
        String job = request.getParameter("job");
        String birth = request.getParameter("birth");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Contacts c = new Contacts();
        c.setId(id);
        c.setSource(source);
        c.setOwner(owner);
        c.setNextContactTime(nextContactTime);
        c.setMphone(mphone);
        c.setJob(job);
        c.setFullname(fullname);
        c.setEmail(email);
        c.setDescription(description);
        c.setCreateTime(createTime);
        c.setCreateBy(createBy);
        c.setContactSummary(contactSummary);
        c.setBirth(birth);
        c.setAppellation(appellation);
        c.setAddress(address);

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.save(c, customerName);

        PrintJson.printJsonFlag(response, flag);


    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response, uList);

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得 客户名称列表（按照客户名称进行模糊查询）");

        String name = request.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = cs.getCustomerName(name);

        PrintJson.printJsonObj(response, sList);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询联系人信息列表的操作（结合条件查询+分页查询）");

        String owner = request.getParameter("owner");
        String fullname = request.getParameter("fullname");
        String customerName = request.getParameter("customerName");
        String source = request.getParameter("source");
        String birth = request.getParameter("birth");
        String pageNoStr = request.getParameter("pageNo");
        int pageNo = Integer.valueOf(pageNoStr);
        //每页展现的记录数
        String pageSizeStr = request.getParameter("pageSize");
        int pageSize = Integer.valueOf(pageSizeStr);
        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("owner", owner);
        map.put("fullname", fullname);
        map.put("customerName", customerName);
        map.put("source", source);
        map.put("birth", birth);
        map.put("pageNoStr", pageNoStr);
        map.put("pageSize", pageSize);
        map.put("skipCount", skipCount);

        System.out.println("==========map:"+map);

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        /*

            前端要：联系人信息列表
                    查询的总条数

         */
        PaginationVO<Contacts> vo = cs.pageList(map);

        //vo-->{"total":100, "dataList":[{联系人1},{2},{3}]}
        PrintJson.printJsonObj(response, vo);

    }


}











