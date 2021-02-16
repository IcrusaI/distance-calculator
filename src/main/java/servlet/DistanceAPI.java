package servlet;

import XML.DistancesXML;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import service.DistanceService;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXB;
import java.io.InputStream;
import java.util.List;

@Path("/distances")
public class DistanceAPI {
    @EJB
    private DistanceService distanceService;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculateDistance(@FormParam("method") String method,
                                      @FormParam("fromCity") List<String> fromCities,
                                      @FormParam("toCity") List<String> toCities) {
        return distanceService.calculateDistance(method, fromCities, toCities);
    }

    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addDistance(MultipartFormDataInput form) {
        return distanceService.insertCities(form);
    }
}
