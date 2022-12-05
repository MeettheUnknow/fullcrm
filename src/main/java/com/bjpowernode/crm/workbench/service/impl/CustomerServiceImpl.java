package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.CustomerRemark;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.vo.PaginationVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranRemarkDao tranRemarkDao = SqlSessionUtil.getSqlSession().getMapper(TranRemarkDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);


    public List<String> getCustomerName(String name) {

        List<String> sList = customerDao.getCustomerName(name);

        return sList;
    }

    public Customer detail(String id) {

        Customer c  = customerDao.detail(id);

        return c;
    }

    public PaginationVO<Customer> pageList(Map<String, Object> map) {

        //取得total
        int total = customerDao.getTotalByCondition(map);

        //取得dataList
        List<Customer> dataList = customerDao.getCustomerListByCondition(map);

        //创建一个vo对象，将total和dataList封装到vo中
        PaginationVO<Customer> vo = new PaginationVO<Customer>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //将vo返回

        return vo;
    }

    public boolean save(Customer c) {

        boolean flag = true;

        int count = customerDao.save(c);
        if(count!=1){

            flag = false;

        }

        return flag;
    }

    public boolean deleteContactsByIds(String[] ids) {

        boolean flag = true;

        //查询出需要删除的备注的数量
        int count1 = contactsRemarkDao.selectCountRemarkByContactsId(ids);

        //删除备注，返回受到影响的条数（实际删除的数量）
        int count2 = contactsRemarkDao.deleteCountRemarkByContactsId(ids);

        if(count1!=count2){
            flag = false;
        }

        for(String id:ids){

            //删除联系人详细页交易信息
            String[] tranIds = tranDao.selectTranIds(id);
            for(String tranId:tranIds){

                int count11 = tranRemarkDao.selectCountRemarkByTranId(tranId);

                int count12 = tranRemarkDao.deleteCountRemarkByTranId(tranId);

                if(count11!=count12){

                    flag = false;

                }

                int count13 = tranHistoryDao.selectTranHistoryByTranId(tranId);

                int count14 = tranHistoryDao.deleteTranHistoryByTranId(tranId);

                if(count13!=count14){

                    flag = false;

                }

                int count15 = tranDao.deleteTranByTranId(tranId);

                if(count15!=1){

                    flag = false;

                }
            }

            //删除联系人详细页市场活动

            String[] activityIds = contactsActivityRelationDao.selectActivityId(id);
            for(String activityId:activityIds){

                int count21 = contactsActivityRelationDao.unboundActivityByAid(activityId);

                if(count21!=1){

                    flag = false;

                }

            }

        }

        //删除联系人
        int count3 = contactsDao.delete(ids);
        if(count3!=ids.length){

            flag = false;

        }

        return flag;
    }

    public boolean delete(String[] ids) {

        boolean flag = true;

        //查询出需要删除的备注的数量
        int count1 = customerRemarkDao.getCountByCids(ids);

        //删除备注，返回受到影响的条数（实际删除的数量）
        int count2 = customerRemarkDao.deleteByCids(ids);

        if(count1!=count2){

            flag = false;

        }

        for(String id:ids){

            //删除客户详细页交易信息
            String[] tranIds = tranDao.selectTranIds(id);
            for(String tranId:tranIds){

                int count11 = tranRemarkDao.selectCountRemarkByTranId(tranId);

                int count12 = tranRemarkDao.deleteCountRemarkByTranId(tranId);

                if(count11!=count12){

                    flag = false;

                }

                int count13 = tranHistoryDao.selectTranHistoryByTranId(tranId);

                int count14 = tranHistoryDao.deleteTranHistoryByTranId(tranId);

                if(count13!=count14){

                    flag = false;

                }

                int count15 = tranDao.deleteTranByTranId(tranId);

                if(count15!=1){

                    flag = false;

                }
            }

            //删除客户详细页联系人信息
            String[] contactsIds = contactsDao.selectContactsIdByCustomerId(id);
            if(contactsIds.length!=0){
                flag = deleteContactsByIds(contactsIds);
            }


        }

        //删除客户
        int count3 = customerDao.delete(ids);
        if(count3!=ids.length){

            flag = false;

        }

        return flag;
    }

    public Map<String, Object> getUserListAndCustomer(String id) {

        //取uList
        List<User> uList = userDao.getUserList();

        //取c
        Customer c = customerDao.getById(id);

        //取uList和a打包到map中
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uList", uList);
        map.put("c", c);

        //返回map就可以了
        return map;
    }

    public boolean update(Customer c) {

        boolean flag = true;

        int count = customerDao.update(c);
        if(count!=1){

            flag = false;

        }

        return flag;
    }

    public List<CustomerRemark> getRemarkListByCid(String customerId) {

        List<CustomerRemark> crList = customerRemarkDao.getRemarkListByCid(customerId);

        return crList;

    }

    public boolean saveRemark(CustomerRemark cr) {

        boolean flag = true;

        int count = customerRemarkDao.saveRemark(cr);

        if(count!=1){

            flag = false;

        }

        if(flag){

            //根据联系人查询出联系人对象
            Contacts c = contactsDao.getContactsByCid(cr.getCustomerId());

            if(c!=null){
                //将客户备注对象补全
                cr.setFullname(c.getFullname());
                cr.setAppellation(c.getAppellation());
            }

        }

        return flag;
    }

    public boolean deleteRemark(String id) {

        boolean flag = true;

        int count = customerRemarkDao.deleteById(id);

        if(count!=1){

            flag = false;

        }

        return flag;
    }

    public boolean updateRemark(CustomerRemark cr) {

        boolean flag = true;

        int count = customerRemarkDao.updateRemark(cr);

        if(count!=1){

            flag = false;

        }

        return flag;
    }

    public String getCustomerNameByCid(String customerId) {

        String customerName = customerDao.getCustomerNameByCid(customerId);

        return customerName;
    }

    public String getContactsNameByCid(String contactsId) {

        String contactsName = contactsDao.getContactsNameByCid(contactsId);

        return contactsName;
    }

    public Customer updateDetail(Customer c) {

        boolean flag = true;

        flag = update(c);

        if(flag){

            c = detail(c.getId());

        }

        return c;
    }


}
