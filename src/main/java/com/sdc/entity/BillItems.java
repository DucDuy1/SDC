package com.sdc.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table
@Data
public class BillItems {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int billItemsId;

	private int quantity;

	private int buyPrice;

	private String name;

	@ManyToOne
	@JoinColumn(name = "idBill")
	private Bill bill;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "productId")
	private Product product;

}
