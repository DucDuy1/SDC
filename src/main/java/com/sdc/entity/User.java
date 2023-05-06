package com.sdc.entity;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

//	@NotEmpty(message = "{user.name.notempty}")
//	@Size(min = 3, message = "{user.name.size}")
//	@Column(name = "name")
//	private String name;

	private String username;

	private String password;

	private String emailUser;

	@ElementCollection(fetch = FetchType.EAGER)
	@Column(name = "role")
	@CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
	private List<String> roles;
}
