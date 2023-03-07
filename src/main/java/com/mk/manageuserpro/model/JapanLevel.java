package com.mk.manageuserpro.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mst_group")
public class JapanLevel {
	@Id
	private Long id;
	@Column(name = "code_level")
	private String codeLevel;

	@Column(name = "name_level")
	private String nameLevel;

}
