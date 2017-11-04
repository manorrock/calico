
Manorrock Files Webapp
======================


Quickstart
----------

The fastest way to run Manorrock Files Webapp is using the following command line:

```
java -DFILES_REST_URL=http://localhost:8081/files/rest -jar webapp-all.jar
```

This will spawn it serving on localhost:8082 with a /files/webapp path prefix,
and serving and connecting to Manorrock Files REST at 
http://localhost:8081/files/rest

How do I change the path prefix?
--------------------------------

If you want to change the path prefix you can use one of the following:

 * the PATH_PREFIX environment variable.
 * the PATH_PREFIX System property (-DPATH_PREFIX=/prefix) passed on the command line.

How do I change the Manorrock Files REST to be used?
----------------------------------------------------

Use the -e option to set it (e.g. -e "FILES_REST_URL=http://localhost:8081/files/rest").

How do I change the port number?
--------------------------------

If you want to change the port number the server is listening on you can use
one of the following:

* the SERVER_PORT environment variable.
* the SERVER_PORT System property (-DSERVER_PORT=8082) passed on the command line.

Can I run this on Docker?
-------------------------

Yes, to run it on Docker we deliver fhe following containers:

* a regular Docker container (manorrock/files-webapp)
* an ARM32v6 Docker container (manorrock/files-webapp:arm32v6)

The fastest way to run the Docker container is using the following command line:

```
docker run -d -it -e "FILES_REST_URL=http://localhost:8081/files/rest"
  -p 8082:8082 manorrock/files-webapp
```

This will spawn it serving on localhost:8082 with a path prefix of /files/webapp
and connecting to Manorrock Files REST at http://localhost:8081/files/rest

### How do I change the path prefix?

Use the -e option to set the path prefix (e.g. -e "PATH_PREFIX=/your_prefix/").

### How do I change the Manorrock Files REST to be used?

Use the -e option to set it (e.g. -e "FILES_REST_URL=http://localhost:8081/files/rest").

### How do I change the port number?

Use the -p option to set the port number (e.g. -p 8082:8082). Note the docker
image is by default setup to serve on port 8082 hence why the right-hand side
is set to 8082.

--END
