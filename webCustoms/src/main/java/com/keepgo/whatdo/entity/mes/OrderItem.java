package com.keepgo.whatdo.entity.mes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "web_orderItem")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderItem {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	@Column(name = "orderId")
//	private Long orderId;
//	@Column(name = "ItemId")
//	private Long ItemId;
	
	@Column(name = "count")
	private int count;
	@Column(name = "price")
	private int price;
	
	
	@JoinColumn(name = "orderId")
	@ManyToOne(fetch = FetchType.LAZY)
	private Order order;
	@JoinColumn(name = "itemId")
	@ManyToOne(fetch = FetchType.LAZY)
	private Item item;
	
}
