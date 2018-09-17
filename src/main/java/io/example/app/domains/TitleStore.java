package io.example.app.domains;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="TITLE_STORE")
@Entity
public class TitleStore {
	
	@Id
	@Column(name="ID")
	private Integer id;
	
	@Column(name="TITLE")
	private String title;

	@Column(name="TIME")
	private String time;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	
}
