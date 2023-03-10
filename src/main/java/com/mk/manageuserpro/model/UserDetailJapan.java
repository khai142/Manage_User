package com.mk.manageuserpro.model;

import com.mk.manageuserpro.validator.CompareDate;
import com.mk.manageuserpro.validator.RequiredOnInputJapanLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_detail_japan")
@CompareDate(before = "startDate", after="endDate", message = "Expire date must be greater than Qualification date")
@RequiredOnInputJapanLevel.List({
        @RequiredOnInputJapanLevel(requireField = "startDate", message = "Qualification date is required on select Qualification"),
        @RequiredOnInputJapanLevel(requireField = "endDate", message = "Expire date is required on select Qualification"),
        @RequiredOnInputJapanLevel(requireField = "score", message = "Score is required on select Qualification")
})
public class UserDetailJapan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_detail_japan_id")
    private Long userDetailJapanId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "code_level")
    private JapanLevel japanLevel;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_date")
    private Date endDate;
//    @Size(min = 80, max = 180, message = "Score in range 80 to 180")
    @Column(name = "score")
    private Integer score;
}
