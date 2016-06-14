/**
 * Created by ricardoramos on 6/1/16.
 */
import java.sql.*;

import javax.persistence.*;

@Entity
@Table(name = "ETIQUETA")
public class Tag {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Integer id;

    @Column(name = "TAG", length = 50, unique = true)
    private String tag;

    public Tag(){

    }
    public Tag(Integer id, String tag){

        this.setId(id);
        this.setTag(tag);
    }

    public Tag(String tag) {
        this.setTag(tag);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


}
