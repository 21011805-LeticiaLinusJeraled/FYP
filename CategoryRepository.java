/**
 * 
 * I declare that this code was written by me, 21011805. 
 * I will not copy or allow others to copy my code. 
 * I understand that copying code is considered as plagiarism.
 * 
 * Student Name: Leticia Linus Jeraled
 * Student ID: 21011805 
 * Class: E63D
 * Date created: 2022-Nov-11 3:12:03 pm 
 * 
 */
package FYP;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author 21011805
 *
 */
//interface category repository extends to the JpaRespository to communicate to the database to the Category table, Integer is the PK
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
