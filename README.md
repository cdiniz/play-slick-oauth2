# play-slick-rest
This is a work in progress.

The Play Slick OAuth2 is a very simple json rest api showing one way of using Play Framework 2.5 with [slick 3](https://github.com/slick/slick) library for database access with support for oauth2.


It supports the following features:

* Generic Data Access Objects, create a DAOS with crud for an entity with just one line using DI
* Models as case classes and slick models, independent from database driver and profile
* Oauth2

The project was thought to be used as an activator template.

#Running

The database pre-configured is an h2, so you just have to:


        $ sbt run

#TODO


#Credits

To make this template, I just mixed the play scala template with play slick.

