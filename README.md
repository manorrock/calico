
# Manorrock Calico

Manorrock Calico delivers you with an online file store.

Note the documentation for the master branch might show features that are NOT
yet available as it is under active development. Please see the documentation
for the release you are using in the links below.

## Documentation for older releases

1. [Version 1.6.0](https://github.com/manorrock/calico/tree/v1.6.0)
2. [Version 1.5.0](https://github.com/manorrock/calico/tree/v1.5.0)
3. [Version 1.3.0](https://github.com/manorrock/calico/tree/v1.3.0)
4. [Version 1.2.0](https://github.com/manorrock/calico/tree/v1.2.0)
5. [Version 1.1.0](https://github.com/manorrock/calico/tree/v1.1.0)
6. [Version 1.0.3](https://github.com/manorrock/calico/tree/v1.0.3)
7. [Version 1.0.2](https://github.com/manorrock/calico/tree/v1.0.2)
8. [Version 1.0.1](https://github.com/manorrock/calico/tree/v1.0.1)
9. [Version 1.0.0](https://github.com/manorrock/calico/tree/v1.0.0)

## REST API

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