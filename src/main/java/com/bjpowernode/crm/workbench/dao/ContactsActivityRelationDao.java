package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ContactsActivityRelation;

public interface ContactsActivityRelationDao {

    int save(ContactsActivityRelation contactsActivityRelation);

    int bound(ContactsActivityRelation car);

    int unbound(String id);

    String[] selectActivityId(String id);

    int unboundActivityByAid(String activityId);

}
