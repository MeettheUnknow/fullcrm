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


public class CustomerController extends HttpServlet {

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到客户控制器");

        String path = request.getServletPath();

        if("/workbench/customer/getUserList.do".equals(path)){

            getUserList(request, response);

        }else if("/workbench/customer/pageList.do".equals(path)){

            pageList(request, response);

        }else if("/workbench/customer/detail.do".equals(path)){

            detail(request, response);

        }else if("/workbench/customer/save.do".equals(path)){

            save(request, response);

        }else if("/workbench/customer/delete.do".equals(path)){

            delete(request, response);

        }else if("/workbench/customer/getUserListAndCustomer.do".equals(path)){

            getUserListAndCustomer(request, response);

        }else if("/workbench/customer/update.do".equals(path)){

            update(request, response);

        }else if("/workbench/customer/getRemarkListByCid.do".equals(path)){

            getRemarkListByCid(request, response);

        }else if("/workbench/customer/saveRemark.do".equals(path)){

            saveRemark(request, response);

        }else if("/workbench/customer/deleteRemark.do".equals(path)){

            deleteRemark(request, response);

        }else if("/workbench/customer/updateRemark.do".equals(path)){

            updateRemark(request, response);

        }else if("/workbench/customer/add.do".equals(path)){

            add(request, response);

        }else if("/workbench/customer/getActivityListByName.do".equals(path)){

            getActivityListByName(request, response);

        }else if("/workbench/customer/getContactsListByName.do".equals(path)){

            getContactsListByName(request, response);

        }else if("/workbench/customer/saveTran.do".equals(path)){

            saveTran(request, response);

        }else if("/workbench/customer/getTranListByCid.do".equals(path)){

            getTranListByCid(request, response);

        }else if("/workbench/customer/deleteTranByTranId.do".equals(path)){

            deleteTranByTranId(request, response);

        }else if("/workbench/customer/tranDetail.do".equals(path)){

            tranDetail(request, response);

        }else if("/workbench/customer/getCustomerName.do".equals(path)){

            getCustomerName(request, response);

        }else if("/workbench/customer/getContactsListByCid.do".equals(path)){

            getContactsListByCid(request, response);

        }else if("/workbench/customer/detailContact.do".equals(path)){

            detailContact(request, response);

        }else if("/workbench/customer/saveContacts.do".equals(path)){

            saveContacts(request, response);

        }else if("/workbench/customer/deleteDetailContactByCid.do".equals(path)){

            deleteDetailContactByCid(request, response);

        }else if("/workbench/customer/updateDetail.do".equals(path)){

            updateDetail(request, response);

        }else if("/workbench/customer/deleteDetail.do".equals(path)){

            deleteDetail(request, response);

        }



    }

    private void deleteDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("执行客户详细页的删除操作再跳到客户页");

        String ids[] = request.getParameterValues("id");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = cs.delete(ids);

        if(flag){

            response.sendRedirect(request.getContextPath() + "/workbench/customer/index.jsp");
            //request.getRequestDispatcher("/workbench/customer/index.jsp").forward(request, response);

        }


    }

    private void updateDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("执行客户详细页修改操作再跳回详细页");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String website = request.getParameter("website");
        String phone = request.getParameter("phone");
        //修改人：当前登录用户
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        //修改时间：当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String description = request.getParameter("description");
        String address = request.getParameter("address");

        Customer c = new Customer();
        c.setId(id);
        c.setAddress(address);
        c.setEditTime(editTime);
        c.setEditBy(editBy);
        c.setWebsite(website);
        c.setPhone(phone);
        c.setDescription(description);
        c.setName(name);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setOwner(owner);

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        c = cs.updateDetail(c);

        request.setAttribute("c", c);

        request.getRequestDispatcher("/workbench/customer/detail.jsp").forward(request, response);

    }

    private void deleteDetailContactByCid(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行联系人的删除操作");

        String id = request.getParameter("id");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.deleteDetailContactByCid(id);

        PrintJson.printJsonFlag(response, flag);

    }

    private void saveContacts(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行联系人添加操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String source = request.getParameter("source");
        String customerName = request.getParameter("customerName");
        String customerId = request.getParameter("customerId");
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
        c.setCustomerId(customerId);
        c.setCustomerName(customerName);

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        boolean flag = cs.saveContact(c);

        PrintJson.printJsonFlag(response, flag);

    }

    private void detailContact(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到跳转到联系人详细信息页的操作");

        String id = request.getParameter("id");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        Contacts c = cs.detail(id);

        request.setAttribute("c", c);

        request.getRequestDispatcher("/workbench/contacts/detail.jsp").forward(request, response);

    }

    private void getContactsListByCid(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据客户id，取得联系人信息列表");

        String customerId = request.getParameter("customerId");

        ContactsService cs = (ContactsService) ServiceFactory.getService(new ContactsServiceImpl());

        List<Contacts> cList = cs.getContactsListByCid(customerId);

        PrintJson.printJsonObj(response, cList);

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得 客户名称列表（按照客户名称进行模糊查询）");

        String name = request.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = cs.getCustomerName(name);

        PrintJson.printJsonObj(response, sList);

    }

    private void tranDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("跳转到交易详细信息页");

        String id = request.getParameter("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Tran t = ts.detail(id);

        //处理可能性
        /*

            阶段t
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

    private void deleteTranByTranId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行删除交易操作");

        String id = request.getParameter("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.deleteTranByTranId(id);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getTranListByCid(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据客户id查询关联的市场活动列表");

        String customerId = request.getParameter("customerId");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        List<Tran> tList = ts.getTranListByCid(customerId);

        PrintJson.printJsonObj(response, tList);


    }

    private void saveTran(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException  {

        System.out.println("执行新建交易的操作");

        String id = UUIDUtil.getUUID();
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
        t.setCustomerId(customerId);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.saveTran(t);

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        Customer c = cs.detail(customerId);

        if(flag){

            request.setAttribute("c", c);
            request.getRequestDispatcher("/workbench/customer/detail.jsp").forward(request, response);

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

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到跳转到交易添加页的操作");

        String customerId = request.getParameter("customerId");

        UserService us = (UserService)ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        String customerName = cs.getCustomerNameByCid(customerId);

        request.setAttribute("uList", uList);
        request.setAttribute("customerId", customerId);
        request.setAttribute("customerName", customerName);
        request.getRequestDispatcher("/workbench/customer/save.jsp").forward(request, response);

    }


    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行修改备注的操作");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editTime = DateTimeUtil.getSysTime();
        String editFlag = "1";

        CustomerRemark cr = new CustomerRemark();

        cr.setId(id);
        cr.setNoteContent(noteContent);
        cr.setEditFlag(editFlag);
        cr.setEditBy(editBy);
        cr.setEditTime(editTime);

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = cs.updateRemark(cr);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", flag);
        map.put("cr", cr);

        PrintJson.printJsonObj(response, map);

    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("删除备注操作");

        String id = request.getParameter("id");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = cs.deleteRemark(id);

        PrintJson.printJsonFlag(response, flag);

    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行添加备注操作");

        String id = UUIDUtil.getUUID();
        String noteContent = request.getParameter("noteContent");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String editFlag = "0";
        String customerId = request.getParameter("customerId");

        CustomerRemark cr = new CustomerRemark();
        cr.setId(id);
        cr.setNoteContent(noteContent);
        cr.setCreateTime(createTime);
        cr.setCreateBy(createBy);
        cr.setEditFlag(editFlag);
        cr.setCustomerId(customerId);

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = cs.saveRemark(cr);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", flag);
        map.put("cr", cr);

        PrintJson.printJsonObj(response, map);

    }

    private void getRemarkListByCid(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据客户id，取得备注信息列表");

        String customerId = request.getParameter("customerId");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<CustomerRemark> crList = cs.getRemarkListByCid(customerId);

        PrintJson.printJsonObj(response, crList);

    }

    private void update(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行客户修改操作");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String website = request.getParameter("website");
        String phone = request.getParameter("phone");
        //修改人：当前登录用户
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        //修改时间：当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String description = request.getParameter("description");
        String address = request.getParameter("address");

        Customer c = new Customer();
        c.setId(id);
        c.setAddress(address);
        c.setEditTime(editTime);
        c.setEditBy(editBy);
        c.setWebsite(website);
        c.setPhone(phone);
        c.setDescription(description);
        c.setName(name);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setOwner(owner);

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = cs.update(c);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getUserListAndCustomer(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询用户信息列表和根据客户id查询单条记录的操作");

        String id = request.getParameter("id");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        /*

            前端需要的，管业务层要
            uList
            c

         */
        Map<String, Object> map = cs.getUserListAndCustomer(id);

        PrintJson.printJsonObj(response, map);

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行客户的删除操作");

        String ids[] = request.getParameterValues("id");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = cs.delete(ids);

        PrintJson.printJsonFlag(response, flag);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行客户添加操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String website = request.getParameter("website");
        String phone = request.getParameter("phone");
        //创建人，当前登录用户
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        //创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String description = request.getParameter("description");
        String address = request.getParameter("address");

        Customer c = new Customer();
        c.setId(id);
        c.setAddress(address);
        c.setWebsite(website);
        c.setPhone(phone);
        c.setDescription(description);
        c.setName(name);
        c.setCreateBy(createBy);
        c.setCreateTime(createTime);
        c.setContactSummary(contactSummary);
        c.setNextContactTime(nextContactTime);
        c.setOwner(owner);

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        boolean flag = cs.save(c);

        PrintJson.printJsonFlag(response, flag);

    }


    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到跳转到详细信息页的操作");

        String id = request.getParameter("id");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        Customer c = cs.detail(id);

        request.setAttribute("c", c);

        request.getRequestDispatcher("/workbench/customer/detail.jsp").forward(request, response);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询客户信息列表的操作（结合条件查询+分页查询）");

        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
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
        map.put("phone", phone);
        map.put("website", website);
        map.put("pageSize", pageSize);
        map.put("skipCount", skipCount);

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        PaginationVO<Customer> vo = cs.pageList(map);

        //vo-->{"total":100, "dataList":[{市场活动1},{2},{3}]}
        PrintJson.printJsonObj(response, vo);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("获得用户信息列表");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response, uList);

    }


}











