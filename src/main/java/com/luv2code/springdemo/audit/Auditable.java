package com.luv2code.springdemo.audit;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<U> {

	@CreatedBy
	@Column(name = "created_by", nullable = false, updatable = false)
	protected U createdBy;

	@CreatedDate
	@Column(name = "creation_date", nullable = false, updatable = false)
	private LocalDateTime creationDate;

	@LastModifiedBy
	@Column(name = "last_updated_by", nullable = false)
	protected U lastModifiedBy;

	@LastModifiedDate
	@Column(name = "last_update_date", nullable = false)
	private LocalDateTime lastUpdateDate;

}