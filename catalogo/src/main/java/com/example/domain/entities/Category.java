package com.example.domain.entities;

import java.io.Serial;
import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import com.example.core.domain.entities.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * The persistent class for the category database table.
 * 
 */
@Entity
@Table(name="category")
@NamedQuery(name="Category.findAll", query="SELECT c FROM Category c")
public class Category extends AbstractEntity<Category> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="category_id")
	@JsonProperty("id")
	private int id;

	@Column(name="last_update", insertable = false, updatable = false)
	@PastOrPresent
	@JsonIgnore
	private LocalDateTime lastUpdate;

	@NotBlank
	@Size(max=25)
	@JsonProperty("categoria")
	private String name;

	//bi-directional many-to-one association to FilmCategory
	@OneToMany(mappedBy="category")
	@JsonIgnore
	private List<FilmCategory> filmCategories;

	public Category() {
	}

	public Category(int id) {
		this.id = id;
	}

	public Category(int id, @NotBlank @Size(max = 25) String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(LocalDateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FilmCategory> getFilmCategories() {
		return Collections.unmodifiableList(this.filmCategories);
	}

	public void setFilmCategories(List<FilmCategory> filmCategories) {
		this.filmCategories = filmCategories;
	}

	public FilmCategory addFilmCategory(FilmCategory filmCategory) {
		getFilmCategories().add(filmCategory);
		filmCategory.setCategory(this);

		return filmCategory;
	}

	public FilmCategory removeFilmCategory(FilmCategory filmCategory) {
		getFilmCategories().remove(filmCategory);
		filmCategory.setCategory(null);

		return filmCategory;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof Category o)
			return id == o.id;
		else
			return false;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", lastUpdate=" + lastUpdate + "]";
	}

}