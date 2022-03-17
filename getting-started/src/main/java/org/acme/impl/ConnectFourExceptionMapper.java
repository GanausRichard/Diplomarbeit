package org.acme.impl;

import org.acme.model.ConnectFourException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConnectFourExceptionMapper implements ExceptionMapper<ConnectFourException> {
    @Override
    public Response toResponse(ConnectFourException connectFourException) {
        return Response.status(422)
                .type(MediaType.TEXT_PLAIN)
                .entity(connectFourException.message)
                .build();
    }
}
