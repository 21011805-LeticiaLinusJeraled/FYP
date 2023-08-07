/**
 * 
 * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2022-Nov-11 3:16:44 pm 
 * 
 */
package FYP;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author 21011805
 *
 */
@Entity // helps to create a table with the name sportsItem
public class Item {

	@Id // used to set the field id as a primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // used to auto increment the id date
	private int id;

	@NotNull
	@NotEmpty(message = "Backend: item name cannot be empty")
	@Size(min = 3, message = "Category length must be at least 3 characters!")
	private String name;

	@NotNull
	@NotEmpty(message = "Backend: item name cannot be empty")
	@Size(min = 5, max = 50, message = "Category length must be between 5 and 50 characters!")
	private String description;

	@Min(value = 1, message = "Only accept positive numerical values")
	private double price;

	@Min(value = 0, message = "Only accept positive whole numbers")
	private int quantity;

	private String imgName;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@OneToMany(mappedBy = "item")
	private List<Review> reviews = new ArrayList<>();

	private String reportReason;

	private int reportCount; 

	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public String getReportReason() {
		return reportReason;
	}

	public void setReportReason(String reportReason) {
		this.reportReason = reportReason;
	}

	public int getReportCount() {
		return reportCount;
	}

	public void setReportCount(int reportCount) {
		this.reportCount = reportCount;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
}
