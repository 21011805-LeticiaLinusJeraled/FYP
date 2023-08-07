/**
 * 
 * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2023-Jan-20 12:37:47 pm 
 * 
 */
package FYP;

import java.util.List;
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
@Entity
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@NotEmpty(message = "Backend: member name cannot be empty")
	@Size(min = 3, max = 50, message = "Name length must be between 3 and 50 characters!")
	private String name;

	@NotNull
	@NotEmpty(message = "Backend: member username cannot be empty")
	@Size(min = 3, max = 50, message = "Username length must be between 3 and 50 characters!")
	private String username;

	@NotNull
	@NotEmpty(message = "Backend: member password cannot be empty")
	@Size(min = 5, max = 100, message = "Password length must be between 5 and 100 characters!")
	private String password;

	@NotNull
	@NotEmpty(message = "Backend: member email cannot be empty")
	@Size(min = 5, max = 50, message = "Email length must be between 5 and 50 characters!")
	private String email;

	private String role;

	private int banCount;

	private boolean isBanned;
	
	private double totalSpending = 0.0;
	
    @OneToMany(mappedBy = "member")
    private List<OrderItem> orderItems;
    
    @OneToMany(mappedBy = "member")
    private Set<Item> items;

    @OneToMany(mappedBy = "member") // Refers to the "member" property in the Review class
    private List<Review> reviews; // This represents the reviews written by the member
    
    private String imgName;
    
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getBanCount() {
		return banCount;
	}

	public void setBanCount(int banCount) {
		this.banCount = banCount;
	}

	public boolean isBanned() {
		return isBanned;
	}

	public void setBanned(boolean isBanned) {
		this.isBanned = isBanned;
	}

	public double getTotalSpending() {
		return totalSpending;
	}

	public void setTotalSpending(double totalSpending) {
		this.totalSpending = totalSpending;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	
}
