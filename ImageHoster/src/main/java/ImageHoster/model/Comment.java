package ImageHoster.model;

import javax.persistence.*;
import java.time.LocalDate;

//@Entity annotation specifies that the corresponding class is a JPA entity
@Entity
//@Table annotation provides more options to customize the mapping.
//Here the name of the table to be created in the database is explicitly mentioned as 'comment'. Hence the table named 'comment' will be created in the database with all the columns mapped to all the attributes in 'Comment' class
@Table(name = "comment")
public class Comment {

    //@Id annotation specifies that the corresponding attribute is a primary key
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@Column annotation specifies that the attribute will be mapped to the column in the database.
    //Here the column name is explicitly mentioned as 'id'
    @Column(name = "id")
    private Integer id;

    // Text is a Postgres specific column type that allows you to save
    // text based data that will be longer than 256 characters
    // this is a base64 encoded version of the image
    @Column(name = "text", columnDefinition = "text")
    private String text;

    @Column(name = "createdDate")
    private LocalDate createdDate;

    //The 'comment' table is mapped to 'users' table with One:One mapping
    //FetchType is EAGER
    @OneToOne(fetch = FetchType.EAGER)
    //Below annotation indicates that the name of the column in 'comment' table referring the primary key in 'users' table will be 'user_id'
    @JoinColumn(name = "user_id")
    private User user;

    //The 'comment' table is mapped to 'images' table with One:One mapping
    //FetchType is LAZY
    @OneToOne(fetch = FetchType.LAZY)
    //Below annotation indicates that the name of the column in 'comment' table referring the primary key in 'images' table will be 'image_id'
    @JoinColumn(name = "image_id")
    private Image image;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
