package com.vertx.atm8.handler;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.RoutingContext;

public class FormLoginWithTokenHandlerImpl implements FormLoginWithTokenHandler {

  private final AuthProvider authProvider;

  public FormLoginWithTokenHandlerImpl(AuthProvider authProvider) {
    this.authProvider = authProvider;
  }

  public void handle(RoutingContext context) {
    JsonObject authInfo = new JsonObject()
      .put("username", context.request().getParam("username"))
      .put("password", context.request().getParam("password"));
    authProvider.authenticate(authInfo, res -> {
      if (res.succeeded()) {

        JWTAuthOptions jwtAuthOptions = new JWTAuthOptions()
          .setKeyStore(new KeyStoreOptions()
            .setPath("jceks/keystore.jceks")
            .setType("jceks")
            .setPassword("secret"));

        JWTAuth jwt = JWTAuth.create(context.vertx(), jwtAuthOptions);

        String token = jwt.generateToken(
          new JsonObject().put("sub", context.request().getParam("username")),
          new JWTOptions().setExpiresInMinutes(60L)
        );

        context.setUser(res.result());
        context.session().put("token", token);
        context.response()
          .putHeader("location", (String) context.session().remove("return_url"))
          .setStatusCode(302)
          .end();
      } else {
        context.fail(401);
      }
    });
  }
}
