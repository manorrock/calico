
# Manorrock Calico

Manorrock Calico delivers you with an online file store server.

### Using the Docker image to deploy the online file store server.

```shell
  docker run --rm -d -p 8080:8080 -v $PWD:/root/.manorrock/calico manorrock/calico:VERSION
```

Replace VERSION with the version of Manorrock Calico you want to run.

> _Note_ in the command line above we have mapped the `/root/.manorrock/calico`
> directory to point to the current directory so we can persist outside of the
> Docker container.

## REST API

Click [here](REST.md) for the REST API.

## Testing SNAPSHOT versions

Every night we push a SNAPSHOT version to Docker Hub. If you want to give the
version under development a test drive use `snapshot` as the version for the
instructions above.

## Important notice

Note if you file issues or answer questions on the issue tracker and/or issue 
pull requests you agree that those contributions will be owned by Manorrock.com
and that Manorrock.com can use those contributions in any manner Manorrock.com
so desires.
