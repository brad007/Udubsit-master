package com.ionicframework.udubsit252887.models;

import java.security.acl.Group;

/**
 * Created by Test on 27/03/2017.
 */

public class GroupManager {
    private String groupId;
    private String groupManager;
    private String groupName;

    public GroupManager(){}
    public GroupManager(String groupId,String groupManager,String groupName)
    {
        this.groupId= groupId;
        this.groupManager = groupManager;
        this.groupName = groupName;
    }

    public String getGroupId(){
        return groupId;
    }

    public String getGroupManager(){
        return groupManager;
    }
    public String getGroupName (){return groupName; }
}
