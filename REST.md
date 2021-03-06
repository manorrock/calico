
# Manorrock Calico REST API

* Create a file
* Delete a file
* Get a file
* Update a file

## Create a file

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

## Delete a file

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

## Get a file

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

## Update a file

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
