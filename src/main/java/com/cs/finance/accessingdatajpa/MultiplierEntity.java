package com.cs.finance.accessingdatajpa;

import jakarta.persistence.*;

@Entity
@Table(name = "INSTRUMENT_PRICE_MODIFIER" )
public class MultiplierEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(unique=true)
	private String name;

	private Double multiplier;

	protected MultiplierEntity() {}

	public MultiplierEntity(String pName, Double pMultiplier) {
		this.name = pName;
		this.multiplier = pMultiplier;
	}

	@Override
	public String toString() {
		return String.format(
				"MultiplierEntity[id=%d, firstName='%s', lastName='%D']",
				id, name, multiplier);
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Double getMultiplier() {
		return multiplier;
	}
}
