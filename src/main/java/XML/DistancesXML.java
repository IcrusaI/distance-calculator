package XML;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "root")
public class DistancesXML {

    private List<DistanceXML> distance;

    @XmlElement(name = "distance")
    public List<DistanceXML> getDistance() {
        return distance;
    }

    public void setDistance(List<DistanceXML> distance) {
        this.distance = distance;
    }
}
