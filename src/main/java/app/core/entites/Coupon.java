package app.core.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of="id")
@Entity
@ToString(exclude = "customers")

public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int companyID;
    @Enumerated(EnumType.STRING)
    private Category category;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private int amount;
    private double price;
    private String image;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "customers_vs_coupons",joinColumns = @JoinColumn(name = "coupon_id"),inverseJoinColumns = @JoinColumn(name = "customer_id"))
    @JsonIgnore
    private List<Customer> customers;
}
