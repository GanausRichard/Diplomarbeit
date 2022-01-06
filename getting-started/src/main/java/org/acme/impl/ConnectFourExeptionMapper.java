package org.acme.impl;

import org.acme.model.ConnectFourExeption;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConnectFourExeptionMapper implements ExceptionMapper<ConnectFourExeption> {
    @Override
    public Response toResponse(ConnectFourExeption connectFourExeption) {
        return Response.status(422)
                .type(MediaType.TEXT_PLAIN)
                .entity(connectFourExeption.message)
                .build();
    }
}
