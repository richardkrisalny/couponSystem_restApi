package app.core.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
@Entity
@ToString(exclude = "coupons")

public class Customer{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "customers_vs_coupons",joinColumns = @JoinColumn(name = "customer_id"),inverseJoinColumns = @JoinColumn(name = "coupon_id"))
   @JsonIgnore
    private List<Coupon> coupons=new ArrayList<>();
    public void addCoupon(Coupon coupon){
        if (coupons ==null){
            coupons=new ArrayList<>();
        }
        coupons.add(coupon);
    }
}
