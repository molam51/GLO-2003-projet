package ulaval.glo2003.ui.common.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import ulaval.glo2003.domain.exceptions.InvalidParamException;
import ulaval.glo2003.ui.common.errors.Error;

public class InvalidParamExceptionMapper implements ExceptionMapper<InvalidParamException> {

    @Override
    public Response toResponse(final InvalidParamException e) {
        Error error = new Error(Error.INVALID_PARAM, e.getMessage());

        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }
}
