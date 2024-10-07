package com.davidfunes.test.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    private Long id;
    private String name;
    private String email;
    private Date createdAt;
    private Date updatedAt;
}
