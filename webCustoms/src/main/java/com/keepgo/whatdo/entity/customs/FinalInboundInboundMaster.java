package com.keepgo.whatdo.entity.customs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "web_final_inbound_inboundmst")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class FinalInboundInboundMaster {

	@Id
	@Column(name = "id")
//	@ExcelColumn(headerName="id",order = 1)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JoinColumn(name = "finalInboundId")
	@ManyToOne(fetch = FetchType.LAZY)
	private FinalInbound finalInbound;
	
	@JoinColumn(name = "inboundMasterId")
	@ManyToOne(fetch = FetchType.LAZY)
	private InboundMaster inboundMaster;
	
	@Column(name = "preperOrder")
	private int preperOrder;
}
