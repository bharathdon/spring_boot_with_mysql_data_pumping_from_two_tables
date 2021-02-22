package com.abc.entity.product;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "product_tbl")
@Data
public class Product {

	@Id
	private Integer id;
	private String name;
	private Double price;
}
