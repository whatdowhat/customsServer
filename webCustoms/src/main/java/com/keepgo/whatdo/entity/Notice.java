package com.keepgo.whatdo.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notices")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notice {

	@Id
	@Column(name = "Seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Seq;
	
	@Column(name = "viewCount")
	private Integer Viewcount;
	
	@Column(name = "Title")
	private String Title;
	@Column(name = "Context")
	private String Context;
	@Column(name = "Regdt")
	private LocalDateTime Regdt;
}
