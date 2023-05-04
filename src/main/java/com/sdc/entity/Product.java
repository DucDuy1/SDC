package com.sdc.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Table
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int price;

	private String imageURL;

	private int quantity;

	private String name;

	private String description;

	@ManyToOne
	@JoinColumn(name = "categoryId")
	private Category category;
}
