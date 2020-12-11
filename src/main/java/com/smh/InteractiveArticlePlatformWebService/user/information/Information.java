package com.smh.InteractiveArticlePlatformWebService.user.information;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="information")
public class Information {

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

    @Column(name="created_at")
    private Date created_at;

}
