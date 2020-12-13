package com.smh.InteractiveArticlePlatformWebService.user.information;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name="information")
@EntityListeners(AuditingEntityListener.class)
public class Information implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="surname")
    private String username;

    @Lob
    @Column(name="image",columnDefinition = "BLOB")
    private byte[] image;

    @Column(name="company")
    private String company;

    @Column(name="phone")
    private String phone;

    @Column(name="address")
    private String address;

    @Column(name="biography")
    private String biography;

    @Column(name="birthday")
    private Date birthday;

    @CreatedDate
    @Column(name="created_at",nullable = false,updatable = false)
    private long created_at;

}
