package com.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;

import com.security.enums.Provider;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_identities")
@DynamicUpdate
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserIdentity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column(name = "provider", nullable = false)
	@Enumerated(EnumType.STRING)
	private Provider provider;

	@Column(name = "provider_id", nullable = false, unique = true)
	private String providerId;

}
