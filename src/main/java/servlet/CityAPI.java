package servlet;

import DTO.CityDTO;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import service.CityService;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

@Path("/cities")
public class CityAPI {

    @EJB
    private CityService cityService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CityDTO> getCities() {
        return cityService.getCities();
    }

    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_XML)
    public Response addCities(MultipartFormDataInput form) {
        return cityService.insertCities(form);
    }

}