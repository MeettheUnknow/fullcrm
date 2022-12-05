package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Contacts;

import java.util.List;
import java.util.Map;

public interface ContactsDao {

    int save(Contacts con);

    Contacts getContactsByCid(String customerId);

    List<Contacts> getContactsListByName(String cname);

    int getTotalByCondition(Map<String, Object> map);

    List<Contacts> getContactsListByCondition(Map<String, Object> map);

    int delete(String[] ids);

    Contacts getById(String id);

    int update(Contacts c);

    Contacts detail(String id);

    List<Contacts> getContactsListByCid(String customerId);

    int deleteDetailContactByCid(String id);

    String getContactsNameByCid(String contactsId);

    String[] selectContactsIdByCustomerId(String id);

}
