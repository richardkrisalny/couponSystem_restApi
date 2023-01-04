package app.core.entites;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
@Entity
@ToString(exclude = "coupons")
public class Company {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String name;
    private String email;
    private String password;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
   private List<Coupon> coupons=new ArrayList<>();
}
