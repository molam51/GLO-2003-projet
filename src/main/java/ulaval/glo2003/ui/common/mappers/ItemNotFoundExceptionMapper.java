package ulaval.glo2003.ui.common.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import ulaval.glo2003.domain.exceptions.ItemNotFoundException;
import ulaval.glo2003.ui.common.errors.Error;

public class ItemNotFoundExceptionMapper implements ExceptionMapper<ItemNotFoundException> {

    @Override
    public Response toResponse(final ItemNotFoundException e) {
        Error error = new Error(Error.ITEM_NOT_FOUND, e.getMessage());

        return Response.status(Response.Status.NOT_FOUND).entity(error).build();
    }
}
