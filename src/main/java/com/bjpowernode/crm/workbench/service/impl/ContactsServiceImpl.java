package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ContactsService;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.vo.PaginationVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactsServiceImpl implements ContactsService {

    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranRemarkDao tranRemarkDao = SqlSessionUtil.getSqlSession().getMapper(TranRemarkDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);


    public List<Contacts> getContactsListByName(String cname) {

        List<Contacts> cList = contactsDao.getContactsListByName(cname);

        return cList;
    }

    public PaginationVO<Contacts> pageList(Map<String, Object> map) {

        //取得total
        int total = contactsDao.getTotalByCondition(map);

        //取得dataList
        List<Contacts> dataList = contactsDao.getContactsListByCondition(map);

        //创建一个vo对象，将total和dataList封装到vo中
        PaginationVO<Contacts> vo = new PaginationVO<Contacts>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //将vo返回

        return vo;
    }

    public boolean save(Contacts c, String customerName) {

        /*

            联系人添加业务：

                在做添加之前，参数c里面就少了一项信息，就是客户的主键：customerId

                先处理客户相关的需求

                （1）判断customerName，根据客户名字在客户表进行精确查询
                        如果有这个客户，则取出这个客户的id，封装到c对象中
                        如果没有这个客户，则在客户表新建一条客户信息，然后将新建的客户的id取出，封装到t对象中

                （2）经过以上操作后，c对象中的信息就全了，需要执行添加联系人的操作

         */

        boolean flag = true;

        Customer cus = customerDao.getCustomerByName(customerName);

        //如果cus为null，需要创建客户
        if(cus==null){

            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(customerName);
            cus.setCreateBy(c.getCreateBy());
            cus.setCreateTime(DateTimeUtil.getSysTime());
            cus.setContactSummary(c.getContactSummary());
            cus.setNextContactTime(c.getNextContactTime());
            cus.setOwner(c.getOwner());
            //添加客户
            int count1 = customerDao.save(cus);
            if(count1!=1){
                flag = false;
            }

        }

        //通过以上对于客户的处理，不论是查询出来已有的客户，还是以前没有我们新增的客户，总之客户是已经有了，客户的id已有了
        //将客户id封装到c对象中
        c.setCustomerId(cus.getId());

        //添加联系人
        int count2 = contactsDao.save(c);
        if(count2!=1){
            flag = false;
        }

        return flag;
    }

    public boolean delete(String[] ids) {

        boolean flag = true;

        //查询出需要删除的备注的数量
        int count1 = contactsRemarkDao.getCountByCids(ids);

        //删除备注，返回受到影响的条数（实际删除的数量）
        int count2 = contactsRemarkDao.deleteByCids(ids);

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

    public Map<String, Object> getUserListAndContacts(String id) {

        //取uList
        List<User> uList = userDao.getUserList();

        //取c
        Contacts c = contactsDao.getById(id);

        //取uList和c打包到map中
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uList", uList);
        map.put("c", c);

        //返回map就可以了
        return map;
    }

    public boolean update(Contacts c) {

        boolean flag = true;

        int count = contactsDao.update(c);
        if(count!=1){

            flag = false;

        }

        return flag;
    }

    public Contacts detail(String id) {

        Contacts c = contactsDao.detail(id);

        return c;
    }

    public List<Contacts> getContactsListByCid(String customerId) {

        List<Contacts> cList = contactsDao.getContactsListByCid(customerId);

        return cList;
    }

    public boolean saveContact(Contacts c) {

        boolean flag = true;

        //添加联系人
        int count = contactsDao.save(c);
        if(count!=1){
            flag = false;
        }

        return flag;
    }

    public boolean deleteDetailContactByCid(String id) {

        /*boolean flag = true;

        //删除联系人
        int count = contactsDao.deleteDetailContactByCid(id);
        if(count!=1){
            flag = false;
        }*/

        String ids[] = {id};

        boolean flag = delete(ids);

        return flag;
    }

    public List<ContactsRemark> getRemarkListByCid(String contactsId) {

        List<ContactsRemark> crList = contactsRemarkDao.getRemarkListByCid(contactsId);

        return crList;
    }

    public boolean saveRemark(ContactsRemark cr) {

        boolean flag = true;

        int count = contactsRemarkDao.saveRemark(cr);

        if(count!=1){

            flag = false;

        }

        return flag;
    }

    public boolean deleteRemark(String id) {

        boolean flag = true;

        int count = contactsRemarkDao.deleteRemark(id);

        if(count!=1){

            flag = false;

        }

        return flag;
    }

    public boolean updateRemark(ContactsRemark cr) {

        boolean flag = true;

        int count = contactsRemarkDao.updateRemark(cr);

        if(count!=1){

            flag = false;

        }

        return flag;
    }

    public boolean bound(String contactsId, String[] aids) {

        boolean flag = true;

        for(String aid:aids){

            //取得每一个aid和contactsId做关联
            ContactsActivityRelation car = new ContactsActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setContactsId(contactsId);

            //添加关联关系表中的记录
            int count = contactsActivityRelationDao.bound(car);
            if(count!=1){
                flag = false;
            }

        }

        return flag;
    }

    public boolean unbound(String id) {

        boolean flag = true;

        int count = contactsActivityRelationDao.unbound(id);

        if(count!=1){

            flag = false;

        }

        return flag;
    }

    public Contacts updateDetail(Contacts c) {

        boolean flag = true;

        flag = update(c);

        if(flag){

            c = detail(c.getId());

        }

        return c;
    }

}
