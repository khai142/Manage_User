package com.mk.manageuserpro.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mst_japan_level")
public class JapanLevel {
	@Id
	@Column(name = "code_level")
	private String codeLevel;

	@Column(name = "name_level")
	private String nameLevel;

	@OneToMany(mappedBy = "japanLevel", cascade = CascadeType.MERGE)
	private Collection<UserDetailJapan> userDetailJapans;

}
