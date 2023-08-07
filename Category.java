/**
 * 
 * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2022-Nov-11 2:25:58 pm 
 * 
 */
package FYP;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author 21011805
 *
 */
@Entity // helps to create a table with the name sportsCategory
public class Category {

	@Id // used to set the field id as a primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // used to auto increment the id date
	private int id;

	@NotNull
	@NotEmpty(message = "Backend: category name cannot be empty")
	@Size(min = 3, message = "Category name length must be at least 3 characters!")
	private String name;

	@OneToMany(mappedBy = "category")
	private Set<Item> items;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
