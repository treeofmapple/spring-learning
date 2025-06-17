package com.tom.first.datajpa.model;

import java.util.List;

import com.tom.first.datajpa.model.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Course extends BaseEntity {

	private String title;

	private String description;

	@ManyToMany
	@JoinTable(
			name = "authors_courses", 
			joinColumns = @JoinColumn(name = "course_id"), 
			inverseJoinColumns = @JoinColumn(name = "author_id")
		)
	private List<Author> authors;

	@OneToMany(mappedBy = "course")
	private List<Section> section;
}
