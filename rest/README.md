
Manorrock Files REST
====================


Quickstart
----------

The fastest way to run Manorrock Files REST is using the following command line:

```
java -jar rest-all.jar
```

This will spawn it serving on localhost:8081 with no path prefix and serving
the ${user.home}/.manorrock/files/rest directory.

How do I change the path prefix?
--------------------------------

If you want to change the path prefix you can use one of the following:

 * the PATH_PREFIX environment variable.
 * the PATH_PREFIX System property (-DPATH_PREFIX=/prefix) passed on the command line.

How do I change the root directory?
-----------------------------------

If you want to change the root directory you can use one of the following:

* the ROOT_DIRECTORY environment variable.
* the ROOT_DIRECTORY System property (-DROOT_DIRECTORY=/directory) passed on the command line.

How do I change the port number?
--------------------------------

If you want to change the port number the server is listening on you can use
one of the following:

* the SERVER_PORT environment variable.
* the SERVER_PORT System property (-DSERVER_PORT=8081) passed on the command line.

Can I run this on Docker?
-------------------------

Yes, to run it on Docker we deliver fhe following containers:

* a regular Docker container (manorrock/files-rest)
* an ARM32v6 Docker container (manorrock/files-rest:arm32v6)

The fastest way to run the Docker container is using the following command line:

```
docker run -d -it -v $PWD:/mnt -p 8081:8081 manorrock/files-rest
```

This will spawn it serving on localhost:8081 with a path prefix of /files/rest/
and serving the current directory.

### How do I change the path prefix?

Use the -e option to set the path prefix (e.g. -e "PATH_PREFIX=/your_prefix/").

### How do I change the root directory?

Use the -v option to map a directoru (e.g. -v $PWD:/mnt). Note the Docker image
is internally setup to serve out of the /mnt directory so it should always be 
on the right-hand side.

### How do I change the port number?

Use the -p option to set the port number (e.g. -p 8081:8081). Note the docker
image is by default setup to serve on port 8081 hence why the right-hand side
is set to 8081.

REST API
--------

* Create a file
* Delete a file
* Get a file
* Update a file

### Create a file

<table>
  <tr>
    <td>POST</td>
    <td colspan="2">{path}</td>
  </tr>
  <tr>
    <td colspan="3">Parameters</td>
  </tr>
  <tr>
    <td>Name</td>
    <td colspan="2">Description</td>
  </tr>
  <tr>
    <td>path</td>
    <td colspan="2">The file/directory path</td>
  </tr>
  <tr>
    <td>body</td>
    <td colspan="2">The file body as an application/octect-stream</td>
  </tr>
  <tr>
    <td colspan="3">Responses</td>
  </tr>
  <tr>
    <td>Code</td>
    <td>Description</td>
    <td>Response content-type</td>
  </tr>
  <tr>
    <td>201</td>
    <td>When the file was successfully created. <br>
        <br>
        It includes the "Location" response header which contains the relative URI of the file/directory</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>400</td>
    <td>
        When the file already exists
    </td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>500</td>
    <td>
        When an I/O error occurred
    </td>
    <td>&nbsp;</td>
  </tr>
</table>

### Delete a file

<table>
  <tr>
    <td>DELETE</td>
    <td colspan="2">{path}</td>
  </tr>
  <tr>
    <td colspan="3">Parameters</td>
  </tr>
  <tr>
    <td>Name</td>
    <td colspan="2">Description</td>
  </tr>
  <tr>
    <td>path</td>
    <td colspan="2">The file/directory path</td>
  </tr>
  <tr>
    <td colspan="3">Responses</td>
  </tr>
  <tr>
    <td>Code</td>
    <td>Description</td>
    <td>Response content-type</td>
  </tr>
  <tr>
    <td>200</td>
    <td>When the file/directory was successfully deleted</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>500</td>
    <td>
        When an I/O error occurred
    </td>
    <td>&nbsp;</td>
  </tr>
</table>

### Get a file

<table>
  <tr>
    <td>GET</td>
    <td colspan="2">{path}</td>
  </tr>
  <tr>
    <td colspan="3">Parameters</td>
  </tr>
  <tr>
    <td>Name</td>
    <td colspan="2">Description</td>
  </tr>
  <tr>
    <td>path</td>
    <td colspan="2">The file/directory path</td>
  </tr>
  <tr>
    <td colspan="3">Responses</td>
  </tr>
  <tr>
    <td>Code</td>
    <td>Description</td>
    <td>Response content-type</td>
  </tr>
  <tr>
    <td>200</td>
    <td>
        When a file was requested it returns the file body
    </td>
    <td>
        application/octet-stream
    </td>
  </tr>
  <tr>
    <td>200</td>
    <td>
        When a directory was requested it returns the directory model 
        <p>
            Example
        </p>
        <pre>
  { 
    "name":"",
    "files": [
      {
        "name":"test",
        "attributes": {
          "created":1496531103000,
          "length":102,
          "symbolicLink":false,
          "lastAccessed":1498797883000,
          "lastModified":1496531727000,
          "directory":true
        }
      }
    ]
  }</pre>
    </td>
    <td>
        application/json
    </td>
  </tr>
  <tr>
    <td>404</td>
    <td>
        When the file/directory was not found
    </td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>500</td>
    <td>
        When an I/O error occurred
    </td>
    <td>&nbsp;</td>
  </tr>
</table>

### Update a file

<table>
  <tr>
    <td>PUT</td>
    <td colspan="2">{path}</td>
  </tr>
  <tr>
    <td colspan="3">Parameters</td>
  </tr>
  <tr>
    <td>Name</td>
    <td colspan="2">Description</td>
  </tr>
  <tr>
    <td>path</td>
    <td colspan="2">The file path</td>
  </tr>
  <tr>
    <td>body</td>
    <td colspan="2">The file body as an application/octect-stream</td>
  </tr>
  <tr>
    <td colspan="3">Responses</td>
  </tr>
  <tr>
    <td>Code</td>
    <td>Description</td>
    <td>Response content-type</td>
  </tr>
  <tr>
    <td>200</td>
    <td>When the file was successfully updated</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>400</td>
    <td>
        When the file does NOT exist
    </td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td>500</td>
    <td>
        When an I/O error occurred
    </td>
    <td>&nbsp;</td>
  </tr>
</table>

--END
