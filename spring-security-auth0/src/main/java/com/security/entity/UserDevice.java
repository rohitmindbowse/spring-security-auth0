package com.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_devices")
@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserDevice extends BaseEntity {

	private static final long serialVersionUID = 1L;
	@Column(name = "device_name", nullable = false)
	private String deviceName;

	@Column(name = "device_id", nullable = false)
	private String deviceId;

}
