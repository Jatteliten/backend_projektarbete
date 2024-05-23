package com.example.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ThymeLeafTemplates {
    @Id
    @GeneratedValue
    private long id;

    private String title;
    @Lob
    @Column(length = 10000)
    private String htmlTemplateString;

    public ThymeLeafTemplates(String title, String htmlTemplateString) {
        this.title = title;
        this.htmlTemplateString = htmlTemplateString;
    }
}
