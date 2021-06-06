/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.posbo.entity.admin;

import com.dimata.qdep.entity.Entity;

public class MappingUserGroup extends Entity {

  private long userId = 0;
  private long groupUserId = 0;
  private long companyId = 0;

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public long getGroupUserId() {
    return groupUserId;
  }

  public void setGroupUserId(long groupUserId) {
    this.groupUserId = groupUserId;
  }

  public long getCompanyId() {
    return companyId;
  }

  public void setCompanyId(long companyId) {
    this.companyId = companyId;
  }

}
