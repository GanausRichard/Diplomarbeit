package org.acme.impl;

import org.acme.model.ConnectFourException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

//Provider interface is necessary for the ExceptionMapper to work correctly
@Provider
//maps a connect four exception to a response
public class ConnectFourExceptionMapper implements ExceptionMapper<ConnectFourException> {
    @Override
    public Response toResponse(ConnectFourException connectFourException) {
        //return a response status 422 with the connect four exception
        //response status 422 means that the request entity was correct but the data couldn't be processed
        return Response.status(422)
                .type(MediaType.TEXT_PLAIN)
                .entity(connectFourException.message)
                .build();
    }
}
