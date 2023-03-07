package com.mk.manageuserpro.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_detail_japan")
public class UserDetailJapan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_detail_japan_id")
    private int userDetailJapanId;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "code_level")
    private String codeLevel;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "end_date")
    private Date endDate;
    @Column(name = "total")
    private Date total;
}
