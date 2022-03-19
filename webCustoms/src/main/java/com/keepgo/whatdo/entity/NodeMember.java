package com.keepgo.whatdo.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "node_members")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NodeMember {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="username")
	private String username;
	@Column(name="userpassword")
	private String userpassword;
	@Column(name="updatedAt")
	private Date updatedAt;
	@Column(name="createdAt")
	private Date createdAt;
	
//	@OneToOne(mappedBy = "nodeMember")
//	private Common common;
	
}
