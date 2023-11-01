# Manorrock Calico

[![build](https://github.com/manorrock/calico/actions/workflows/build.yml/badge.svg)](https://github.com/manorrock/calico/actions/workflows/build.yml)

## Running using the container image from DockerHub

In an empty directory of your choice use the following command line to start 
Manorrock Calico.

```shell
  docker run --rm -d -it -p 8080:8080 -v $PWD:/mnt manorrock/calico
```

## Verify the server is up and running

To verify the server is up and running point your browser to:

```
http://localhost:8080/
```

Congratulations you are now running Manorrock Calico!

## REST API

Click [here](REST.md) for the REST API.

## How do I contribute?

See [Contributing](CONTRIBUTING.md)

## Our code of Conduct

See [Code of Conduct](CODE_OF_CONDUCT.md)

## Important notice

Note if you file issues or answer questions on the issue tracker and/or issue 
pull requests you agree that those contributions will be owned by Manorrock.com
and that Manorrock.com can use those contributions in any manner Manorrock.com
so desires.
