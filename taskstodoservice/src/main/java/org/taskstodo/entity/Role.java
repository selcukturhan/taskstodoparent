package org.taskstodo.entity;


public class Role {

    //TODO: Add Permissions
	private String title;
    private Long roleId;
    
    
    public Role() {}
    
    public String getTitle() {
        return title;
    }

	public Long getRoleId() {
		return roleId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}


}