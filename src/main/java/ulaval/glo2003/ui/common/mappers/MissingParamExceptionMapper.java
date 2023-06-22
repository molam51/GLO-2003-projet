package ulaval.glo2003.ui.common.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import ulaval.glo2003.domain.exceptions.MissingParamException;
import ulaval.glo2003.ui.common.errors.Error;

public class MissingParamExceptionMapper implements ExceptionMapper<MissingParamException> {

    @Override
    public Response toResponse(final MissingParamException e) {
        Error error = new Error(Error.MISSING_PARAM, e.getMessage());

        return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
    }
}
