package com.blockchain.accesscontrol.access_control_system.dto.responses;

import java.time.LocalDateTime;

import com.blockchain.accesscontrol.access_control_system.enums.Role;

public class MemberDTO 
{
	private String memberAddress;
	private String name;
	private String memberType;
	private String status;
	private LocalDateTime lastStatusUpdate;
	private Role role;
	
	public String getMemberAddress() {
		return memberAddress;
	}
	
	public void setMemberAddress(String memberAddress) {
		this.memberAddress = memberAddress;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMemberType() {
		return memberType;
	}
	
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public LocalDateTime getLastStatusUpdate() {
		return lastStatusUpdate;
	}
	
	public void setLastStatusUpdate(LocalDateTime lastStatusUpdate) {
		this.lastStatusUpdate = lastStatusUpdate;
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "MemberDTO [memberAddress=" + memberAddress + ", name=" + name + ", memberType=" + memberType
				+ ", status=" + status + ", lastStatusUpdate=" + lastStatusUpdate + ", role=" + role + "]";
	}	
}
