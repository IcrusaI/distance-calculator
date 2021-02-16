package model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "distances")
public class DistanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_city", nullable = false)
    private CityEntity fromCity;

    @ManyToOne
    @JoinColumn(name = "to_city", nullable = false)
    private CityEntity toCity;

    @Column (name = "distance", nullable = false)
    private Double distance;

    public DistanceEntity() {}

    public DistanceEntity(CityEntity fromCity, CityEntity toCity, Double distance) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CityEntity getFromCity() {
        return fromCity;
    }

    public void setFromCity(CityEntity fromCity) {
        this.fromCity = fromCity;
    }

    public CityEntity getToCity() {
        return toCity;
    }

    public void setToCity(CityEntity toCity) {
        this.toCity = toCity;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}