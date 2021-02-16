package XML;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "root")
public class CitiesXML {

    private List<CityXML> city;

    @XmlElement(name = "city")
    public List<CityXML> getCity() {
        return city;
    }

    public void setCity(List<CityXML> city) {
        this.city = city;
    }
}
