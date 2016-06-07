# Play-slick-oauth2 template
This is a play framework 2.5 template with fully integration of scala-oauth2-provider, using slick 3 as backend.

##Running

```
$ sbt -Dplay.evolutions.db.default.autoApply=true run
```

##Testing

```
$ sbt test
```

##Simple example

```
$ curl http://localhost:9000/oauth/access_token -X POST -d "client_id=bob_client_id" -d "client_secret=bob_client_secret" -d "grant_type=client_credentials"
```

```
{ "token_type":"Bearer",
  "access_token":"sXX7cCHBsk5Qdyh2lbGfduKutgeCJb8lxZZltfpT",
  "expires_in":3599,
  "refresh_token":"hUbAKiQEN95CNtRTLHmAanqf5bZoLRkVSqctjW6m"
}
```

##Another examples

There are examples in tests for getting tokens with authorization code and user password.

##Credits

This template was generated from a base fork of the example [scala-oauth2-provider-example-skinny-orm](https://github.com/tsuyoshizawa/scala-oauth2-provider-example-skinny-orm/blob/master/README.md),
So thanks for all the work of @tsuyoshizawa, it was a great base. 
